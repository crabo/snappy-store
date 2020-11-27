/*
 * Copyright (c) 2010-2015 Pivotal Software, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License. See accompanying
 * LICENSE file.
 */
/*
 * Changes for SnappyData distributed computational and data platform.
 *
 * Portions Copyright (c) 2017-2019 TIBCO Software Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License. See accompanying
 * LICENSE file.
 */
/*
 * Portions adapted from Apache Spark having license below.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gemstone.gemfire.internal.shared.unsafe;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiConsumer;

import com.gemstone.gemfire.internal.shared.ChannelBufferFramedInputStream;
import com.gemstone.gemfire.internal.shared.ChannelBufferFramedOutputStream;
import com.gemstone.gemfire.internal.shared.ChannelBufferInputStream;
import com.gemstone.gemfire.internal.shared.ChannelBufferOutputStream;
import com.gemstone.gemfire.internal.shared.InputStreamChannel;
import com.gemstone.gemfire.internal.shared.OutputStreamChannel;

/**
 * Holder for static sun.misc.Unsafe instance and some convenience methods. Use
 * other methods only if {@link UnsafeHolder#hasUnsafe()} returns true;
 *
 * @author swale
 * @since gfxd 1.1
 */
public abstract class UnsafeHolder {

  private static final class Wrapper {

    static final sun.misc.Unsafe unsafe;
    static final boolean unaligned;
    // reserved memory by ByteBuffer.allocateDirect in java.nio.Bits
    static final AtomicLong directReservedMemory;
    static final Constructor<?> directBufferConstructor;
    static final Field cleanerField;
    static final Field cleanerRunnableField;
    static final Object javaLangRefAccess;
    static final Method handlePendingRefs;

    static {
      sun.misc.Unsafe v;
      Constructor<?> dbConstructor;
      Field cleaner;
      Field runnableField = null;
      try {
        final ClassLoader systemLoader = ClassLoader.getSystemClassLoader();
        // try using "theUnsafe" field
        Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        v = (sun.misc.Unsafe)field.get(null);

        // get the constructor of DirectByteBuffer that accepts a Runnable
        Class<?> cls = Class.forName("java.nio.DirectByteBuffer",
            false, systemLoader);
        dbConstructor = cls.getDeclaredConstructor(Long.TYPE, Integer.TYPE);
        dbConstructor.setAccessible(true);

        cleaner = cls.getDeclaredField("cleaner");
        cleaner.setAccessible(true);

        // search for the Runnable field in Cleaner
        Class<?> runnableClass = Runnable.class;
        Field[] fields = sun.misc.Cleaner.class.getDeclaredFields();
        for (Field f : fields) {
          if (runnableClass.isAssignableFrom(f.getType())) {
            if (runnableField == null || f.getName().contains("thunk")) {
              f.setAccessible(true);
              runnableField = f;
            }
          }
        }

        Class<?> bitsClass = Class.forName("java.nio.Bits",
            false, systemLoader);
        Method m = bitsClass.getDeclaredMethod("unaligned");
        m.setAccessible(true);
        unaligned = Boolean.TRUE.equals(m.invoke(null));

        AtomicLong reserved = null;
        try {
          Field f = bitsClass.getDeclaredField("reservedMemory");
          f.setAccessible(true);
          reserved = (AtomicLong)f.get(null);
        } catch (Throwable ignored) {
        }
        directReservedMemory = reserved;

      } catch (LinkageError le) {
        throw le;
      } catch (Throwable t) {
        throw new ExceptionInInitializerError(t);
      }
      if (v == null) {
        throw new ExceptionInInitializerError("theUnsafe not found");
      }
      if (runnableField == null) {
        throw new ExceptionInInitializerError(
            "DirectByteBuffer cleaner thunk runnable field not found");
      }
      unsafe = v;
      directBufferConstructor = dbConstructor;
      cleanerField = cleaner;
      cleanerRunnableField = runnableField;

      Method m;
      Object langRefAccess;
      try {
        m = sun.misc.SharedSecrets.class.getMethod("getJavaLangRefAccess");
        m.setAccessible(true);
        langRefAccess = m.invoke(null);
        m = langRefAccess.getClass().getMethod("tryHandlePendingReference");
        m.setAccessible(true);
        m.invoke(langRefAccess);
      } catch (Throwable ignored) {
        langRefAccess = null;
        m = null;
      }
      javaLangRefAccess = langRefAccess;
      handlePendingRefs = m;
    }

    static void init() {
    }
  }

  /**
   * Limits the number of bytes to copy per sun.misc.Unsafe.copyMemory to
   * allow safepoint polling during a large copy.
   */
  private static final long UNSAFE_COPY_THRESHOLD = 1024L * 1024L;

  public static final int BYTE_ARRAY_OFFSET;
  public static final int LONG_ARRAY_OFFSET;

  private static final boolean hasUnsafe;
  // Limit to the chunk copied per Unsafe.copyMemory call to allow for
  // safepoint polling by JVM.
  public static final boolean littleEndian =
      ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN;

  static {
    boolean v;
    try {
      Wrapper.init();
      v = true;
    } catch (LinkageError le) {
      System.out.println(le.getMessage());
      v = false;
    }
    hasUnsafe = v;
    if (hasUnsafe) {
      BYTE_ARRAY_OFFSET = getUnsafe().arrayBaseOffset(byte[].class);
      LONG_ARRAY_OFFSET = getUnsafe().arrayBaseOffset(long[].class);
    } else {
      BYTE_ARRAY_OFFSET = 0;
      LONG_ARRAY_OFFSET = 0;
    }
  }

  private UnsafeHolder() {
    // no instance
  }

  public static boolean hasUnsafe() {
    return hasUnsafe;
  }

  public static int getAllocationSize(int size) {
    // round to word size
    size = ((size + 7) >>> 3) << 3;
    if (size > 0) return size;
    else throw new BufferOverflowException();
  }

  private static long allocateMemoryUnsafe(long size) throws OutOfMemoryError {
    try {
      return getUnsafe().allocateMemory(size);
    } catch (OutOfMemoryError oome) {
      if (oome.getMessage().contains("Direct buffer")) {
        throw oome;
      } else {
        throw new OutOfMemoryError("Direct buffer allocation of size = " + size + " failed");
      }
    }
  }

  public static ByteBuffer allocateDirectBuffer(int size,
      FreeMemory.Factory factory) {
    final int allocSize = getAllocationSize(size);
    final ByteBuffer buffer = allocateDirectBuffer(
        allocateMemoryUnsafe(allocSize), allocSize, factory);
    buffer.limit(size);
    return buffer;
  }

  public static ByteBuffer allocateDirectBuffer(long address, int size,
      FreeMemory.Factory factory) {
    if (!hasUnsafe) {
      throw new IllegalStateException("allocateDirectBuffer: Unsafe API unavailable");
    }
    try {
      ByteBuffer buffer = (ByteBuffer)Wrapper.directBufferConstructor
          .newInstance(address, size);
      if (factory != null) {
        sun.misc.Cleaner cleaner = sun.misc.Cleaner.create(buffer,
            factory.newFreeMemory(address, size));
        Wrapper.cleanerField.set(buffer, cleaner);
      }
      return buffer;
    } catch (Exception e) {
      getUnsafe().throwException(e);
      throw new IllegalStateException("unreachable");
    }
  }

  public static long getDirectBufferAddress(ByteBuffer buffer) {
    if (!hasUnsafe) {
      throw new IllegalStateException("getDirectBufferAddress: Unsafe API unavailable");
    }
    return ((sun.nio.ch.DirectBuffer)buffer).address();
  }

  public static ByteBuffer reallocateDirectBuffer(ByteBuffer buffer,
      final int newLength, Class<?> expectedClass, FreeMemory.Factory factory) {
    if (!hasUnsafe) {
      throw new IllegalStateException("reallocateDirectBuffer: Unsafe API unavailable");
    }
    sun.nio.ch.DirectBuffer directBuffer = (sun.nio.ch.DirectBuffer)buffer;
    long newAddress = 0L;

    final int newSize = getAllocationSize(newLength);
    final sun.misc.Cleaner cleaner = directBuffer.cleaner();
    if (cleaner != null) {
      // reset the runnable to not free the memory and clean it up
      try {
        Object freeMemory = Wrapper.cleanerRunnableField.get(cleaner);
        long address;
        if (expectedClass != null && (freeMemory == null ||
            !expectedClass.isInstance(freeMemory))) {
          throw new IllegalStateException("Expected class to be " +
              expectedClass.getName() + " in reallocate but was " +
              (freeMemory != null ? freeMemory.getClass().getName() : "null"));
        }
        // use the efficient realloc call if possible
        // and clear address so that cleaner.clean() below does nothing
        if ((freeMemory instanceof FreeMemory) &&
            (address = ((FreeMemory)freeMemory).getAndResetAddress()) != 0L) {
          newAddress = getUnsafe().reallocateMemory(address, newSize);
        }
      } catch (IllegalAccessException e) {
        // fallback to full copy
      }
    }
    if (newAddress == 0L) {
      if (expectedClass != null) {
        throw new IllegalStateException("Expected class to be " +
            expectedClass.getName() + " in reallocate but was non-runnable");
      }
      newAddress = allocateMemoryUnsafe(newSize);
      copyMemory(null, directBuffer.address(), null, newAddress,
          Math.min(newLength, buffer.limit()));
    }
    // clean only after copying is done
    if (cleaner != null) {
      cleaner.clean();
      cleaner.clear();
    }
    ByteBuffer newBuffer = allocateDirectBuffer(newAddress, newSize, factory)
        .order(buffer.order());
    newBuffer.limit(newLength);
    return newBuffer;
  }

  /**
   * Change the runnable field of Cleaner using given factory. The "to"
   * argument specifies that target Runnable type that factory will produce.
   * If the existing Runnable already matches "to" then its a no-op.
   * <p>
   * The provided {@link BiConsumer} is used to apply any action before actually
   * changing the runnable field with the boolean argument indicating whether
   * the current field matches "from" or if it is something else.
   */
  public static void changeDirectBufferCleaner(
      ByteBuffer buffer, int size, Class<? extends FreeMemory> from,
      Class<? extends FreeMemory> to, FreeMemory.Factory factory,
      final BiConsumer<String, Object> changeOwner) throws IllegalAccessException {
    if (!hasUnsafe) {
      throw new IllegalStateException("changeDirectBufferCleaner: Unsafe API unavailable");
    }
    sun.nio.ch.DirectBuffer directBuffer = (sun.nio.ch.DirectBuffer)buffer;
    final sun.misc.Cleaner cleaner = directBuffer.cleaner();
    if (cleaner != null) {
      // change the runnable
      final Field runnableField = Wrapper.cleanerRunnableField;
      Object runnable = runnableField.get(cleaner);
      // skip if it already matches the target Runnable type
      if (!to.isInstance(runnable)) {
        if (changeOwner != null) {
          if (from.isInstance(runnable)) {
            changeOwner.accept(((FreeMemory)runnable).owner(), runnable);
          } else {
            changeOwner.accept(null, runnable);
          }
        }
        Runnable newFree = factory.newFreeMemory(directBuffer.address(), size);
        runnableField.set(cleaner, newFree);
      }
    } else {
      throw new IllegalAccessException(
          "ByteBuffer without a Cleaner cannot be marked for storage");
    }
  }

  /**
   * Release explicitly assuming passed ByteBuffer is a direct one. Avoid using
   * this directly rather use BufferAllocator.allocate/release where possible.
   */
  public static void releaseDirectBuffer(ByteBuffer buffer) {
    if (!hasUnsafe) return;
    sun.misc.Cleaner cleaner = ((sun.nio.ch.DirectBuffer)buffer).cleaner();
    if (cleaner != null) {
      cleaner.clean();
      cleaner.clear();
    }
    buffer.rewind().limit(0);
  }

  public static void releasePendingReferences() {
    // commented code intended to be invoked by reflection for platforms
    // that may not have the requisite classes (e.g. Mac default JDK)
    /*
    final sun.misc.JavaLangRefAccess refAccess =
        sun.misc.SharedSecrets.getJavaLangRefAccess();
    while (refAccess.tryHandlePendingReference()) ;
    */
    if (!hasUnsafe) return;
    final Method handlePendingRefs = Wrapper.handlePendingRefs;
    if (handlePendingRefs != null) {
      try {
        // retry while helping enqueue pending Cleaner Reference objects
        // noinspection StatementWithEmptyBody
        while ((Boolean)handlePendingRefs.invoke(Wrapper.javaLangRefAccess)) ;
      } catch (Exception ignored) {
        // ignore any exceptions in releasing pending references
      }
    }
  }

  public static long getDirectReservedMemory() {
    return hasUnsafe && Wrapper.directReservedMemory != null
        ? Wrapper.directReservedMemory.get() : 0L;
  }

  public static sun.misc.Unsafe getUnsafe() {
    if (hasUnsafe) return Wrapper.unsafe;
    else throw new IllegalStateException("getUnsafe: Unsafe API unavailable");
  }

  public static boolean tryMonitorEnter(Object obj, boolean checkSelf) {
    if (checkSelf && Thread.holdsLock(obj)) {
      return false;
    } else if (!getUnsafe().tryMonitorEnter(obj)) {
      // try once more after a small wait
      LockSupport.parkNanos(100L);
      return getUnsafe().tryMonitorEnter(obj);
    }
    return true;
  }

  public static void monitorEnter(Object obj) {
    getUnsafe().monitorEnter(obj);
  }

  public static void monitorExit(Object obj) {
    getUnsafe().monitorExit(obj);
  }

  @SuppressWarnings("resource")
  public static InputStreamChannel newChannelBufferInputStream(
      ReadableByteChannel channel, int bufferSize) throws IOException {
    return (hasUnsafe
        ? new ChannelBufferUnsafeInputStream(channel, bufferSize)
        : new ChannelBufferInputStream(channel, bufferSize));
  }

  @SuppressWarnings("resource")
  public static OutputStreamChannel newChannelBufferOutputStream(
      WritableByteChannel channel, int bufferSize) throws IOException {
    return (hasUnsafe
        ? new ChannelBufferUnsafeOutputStream(channel, bufferSize)
        : new ChannelBufferOutputStream(channel, bufferSize));
  }

  @SuppressWarnings("resource")
  public static InputStreamChannel newChannelBufferFramedInputStream(
      ReadableByteChannel channel, int bufferSize) throws IOException {
    return (hasUnsafe
        ? new ChannelBufferUnsafeFramedInputStream(channel, bufferSize)
        : new ChannelBufferFramedInputStream(channel, bufferSize));
  }

  @SuppressWarnings("resource")
  public static OutputStreamChannel newChannelBufferFramedOutputStream(
      WritableByteChannel channel, int bufferSize) throws IOException {
    return (hasUnsafe
        ? new ChannelBufferUnsafeFramedOutputStream(channel, bufferSize)
        : new ChannelBufferFramedOutputStream(channel, bufferSize));
  }

  /**
   * Checks that the range described by {@code offset} and {@code size}
   * doesn't exceed {@code arrayLength}.
   */
  public static void checkBounds(int arrayLength, int offset, int len) {
    if ((offset | len) < 0 || offset > arrayLength ||
        arrayLength - offset < len) {
      throw new ArrayIndexOutOfBoundsException("Array index out of range: " +
          "length=" + arrayLength + " offset=" + offset + " length=" + len);
    }
  }

  /**
   * Taken from Apache Spark's org.apache.spark.unsafe.Platform.copyMemory
   */
  public static void copyMemory(Object src, long srcOffset, Object dst,
      long dstOffset, long length) {
    // Check if dstOffset is before or after srcOffset to determine if we should copy
    // forward or backwards. This is necessary in case src and dst overlap.
    if (dstOffset < srcOffset) {
      while (length > 0) {
        long size = Math.min(length, UNSAFE_COPY_THRESHOLD);
        getUnsafe().copyMemory(src, srcOffset, dst, dstOffset, size);
        length -= size;
        srcOffset += size;
        dstOffset += size;
      }
    } else {
      srcOffset += length;
      dstOffset += length;
      while (length > 0) {
        long size = Math.min(length, UNSAFE_COPY_THRESHOLD);
        srcOffset -= size;
        dstOffset -= size;
        getUnsafe().copyMemory(src, srcOffset, dst, dstOffset, size);
        length -= size;
      }
    }
  }
}
