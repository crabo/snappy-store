/*
 * Copyright (c) 2017-2019 TIBCO Software Inc. All rights reserved.
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
package com.gemstone.gemfire.internal.shared;

import java.io.Closeable;
import java.nio.ByteBuffer;

import com.gemstone.gemfire.internal.shared.unsafe.DirectBufferAllocator;
import com.gemstone.gemfire.internal.shared.unsafe.UnsafeHolder;

/**
 * Allocate, release and expand ByteBuffers (in-place if possible).
 */
public abstract class BufferAllocator implements Closeable {

  public static final boolean MEMORY_DEBUG_FILL_ENABLED = Boolean.parseBoolean(
      System.getProperty("spark.memory.debugFill", "false"));

  // Same as jemalloc's debug fill values.
  public static final byte MEMORY_DEBUG_FILL_CLEAN_VALUE = (byte)0xa5;
  @SuppressWarnings("WeakerAccess")
  public static final byte MEMORY_DEBUG_FILL_FREED_VALUE = (byte)0x5a;

  public static final String STORE_DATA_FRAME_OUTPUT =
      "STORE_DATA_FRAME_OUTPUT";

  /**
   * Special owner indicating execution pool memory.
   */
  public static final String EXECUTION = "EXECUTION";

  /**
   * Allocate a new ByteBuffer of given size.
   */
  public abstract ByteBuffer allocate(int size, String owner);

  /**
   * Allocate using the default allocator and fallback to base JDK one in case
   * allocation fails due to some reason (e.g. system stop).
   */
  public ByteBuffer allocateWithFallback(int size, String owner) {
    return allocate(size, owner);
  }

  /**
   * Allocate a new ByteBuffer of given size for storage in a Region.
   */
  public abstract ByteBuffer allocateForStorage(int size);

  /**
   * Clears the memory to be zeros immediately after allocation
   * from given position to end.
   */
  public abstract void clearPostAllocate(ByteBuffer buffer, int position);

  /**
   * Fill the given portion of the buffer setting it with given byte.
   */
  public final void fill(ByteBuffer buffer, byte b, int position, int numBytes) {
    UnsafeHolder.getUnsafe().setMemory(baseObject(buffer),
        baseOffset(buffer) + position, numBytes, b);
  }

  /**
   * Fill the buffer from its current position to full capacity with given byte.
   */
  public final void fill(ByteBuffer buffer, byte b) {
    final int position = buffer.position();
    fill(buffer, b, position, buffer.capacity() - position);
  }

  /**
   * Get the base object of the ByteBuffer for raw reads/writes by Unsafe API.
   */
  public abstract Object baseObject(ByteBuffer buffer);

  /**
   * Get the base offset of the ByteBuffer for raw reads/writes by Unsafe API.
   */
  public abstract long baseOffset(ByteBuffer buffer);

  /**
   * Expand given ByteBuffer to new capacity. The new buffer is positioned
   * at the start and caller has to reposition if required. The ByteOrder
   * will be copied from the source buffer.
   *
   * @return the new expanded ByteBuffer
   */
  public abstract ByteBuffer expand(ByteBuffer buffer, int required,
      String owner);

  /**
   * Return the data as a heap byte array. Use of this should be minimal
   * when no other option exists.
   */
  public byte[] toBytes(ByteBuffer buffer) {
    final int bufferSize = buffer.remaining();
    return ClientSharedUtils.toBytesCopy(buffer, bufferSize, bufferSize);
  }

  /**
   * Return a ByteBuffer either copying from, or sharing the given heap bytes.
   */
  public abstract ByteBuffer fromBytesToStorage(byte[] bytes, int offset,
      int length);

  /**
   * Return a ByteBuffer either sharing data of given ByteBuffer
   * if its type matches, or else copying from the given ByteBuffer.
   */
  public ByteBuffer transfer(ByteBuffer buffer, String owner) {
    final int position = buffer.position();
    final ByteBuffer newBuffer = allocate(buffer.limit(), owner);
    buffer.rewind();
    newBuffer.order(buffer.order());
    newBuffer.put(buffer);
    buffer.position(position);
    newBuffer.position(position);
    return newBuffer;
  }

  /**
   * For direct ByteBuffers the release method is preferred to eagerly release
   * the memory instead of depending on heap GC which can be delayed.
   */
  public final void release(ByteBuffer buffer) {
    releaseBuffer(buffer);
  }

  /**
   * For direct ByteBuffers the release method is preferred to eagerly release
   * the memory instead of depending on heap GC which can be delayed.
   */
  public static boolean releaseBuffer(ByteBuffer buffer) {
    final boolean hasArray = buffer.hasArray();
    if (MEMORY_DEBUG_FILL_ENABLED) {
      Object baseObject;
      long baseOffset;
      if (hasArray) {
        baseObject = buffer.array();
        baseOffset = UnsafeHolder.BYTE_ARRAY_OFFSET + buffer.arrayOffset();
      } else {
        baseObject = null;
        baseOffset = UnsafeHolder.getDirectBufferAddress(buffer);
      }
      UnsafeHolder.getUnsafe().setMemory(baseObject, baseOffset, buffer.capacity(),
          MEMORY_DEBUG_FILL_FREED_VALUE);
    }
    // Actual release should depend on buffer type and not allocator type.
    // Reserved off-heap space will be decremented by FreeMemory implementation.
    if (hasArray) {
      buffer.rewind().limit(0);
      return false;
    } else {
      UnsafeHolder.releaseDirectBuffer(buffer);
      return true;
    }
  }

  /**
   * Indicates if this allocator will produce direct ByteBuffers.
   */
  public abstract boolean isDirect();

  /**
   * Return true if this is a managed direct buffer allocator.
   */
  public boolean isManagedDirect() {
    return false;
  }

  /**
   * Any cleanup required at system close.
   */
  @Override
  public abstract void close();

  protected static int expandedSize(int currentUsed, int required) {
    final long minRequired = (long)currentUsed + required;
    // increase the size by 50%
    //// max word boundary is not taken as 2147483640 but
    // 2147483632, because 8 bytes of overhead is added in FreeMemory reference cleaner
    // which would otherwise make it 2147483648 , overflowing to Long
    int maxSize = ((Integer.MAX_VALUE - DirectBufferAllocator.DIRECT_OBJECT_OVERHEAD - 7) >>> 3) << 3;
    final int newLength = (int)Math.min(Math.max((currentUsed * 3) >>> 1L,
        minRequired), maxSize);
    if (newLength >= minRequired) {
      return newLength;
    } else {
      throw new IndexOutOfBoundsException("Cannot allocate more than " +
          newLength + " bytes but required " + minRequired);
    }
  }
}
