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

package com.pivotal.gemfirexd.internal.engine.access;

import com.gemstone.gemfire.internal.cache.RollBacker;
import com.gemstone.gemfire.internal.cache.TXStateInterface;

/**
 * @author kneeraj
 *
 */
public class GfxdRollBacker implements RollBacker {

  private final TXStateInterface txs;
  
  public GfxdRollBacker(TXStateInterface txstate) {
    this.txs = txstate;  
  }
  
  @Override
  public boolean rollback(int toSavePoint) {
    // TODO Auto-generated method stub
    return false;
  }

}
