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
package com.gemstone.gemfire.internal.shared.unsafe;

import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("serial")
public abstract class FreeMemory extends AtomicLong implements Runnable {

  protected FreeMemory(long address) {
    super(address);
  }

  protected final long getAndResetAddress() {
    // try hard to ensure freeMemory call happens only once
    final long address = get();
    return (address != 0L && compareAndSet(address, 0L)) ? address : 0L;
  }

  protected abstract String owner();

  @Override
  public void run() {
    final long address = getAndResetAddress();
    if (address != 0L) {
      UnsafeHolder.getUnsafe().freeMemory(address);
    }
  }

  public interface Factory {
    FreeMemory newFreeMemory(long address, int size);
  }
}
