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
package com.gemstone.gemfire.cache.client.internal;

import com.gemstone.gemfire.i18n.LogWriterI18n;
import com.gemstone.gemfire.internal.cache.tier.MessageType;
import com.gemstone.gemfire.internal.cache.tier.sockets.Message;
import com.gemstone.gemfire.pdx.internal.EnumInfo;

/**
 * Retrieve the PDXType, given an integer PDX id, from a server.
 * @author darrel
 * @since 6.6.2
 */
public class GetPDXEnumByIdOp {
  /**
   * Get a enum from the given pool.
   * @param pool the pool to use to communicate with the server.
   */
  public static EnumInfo execute(ExecutablePool pool,
                             int enumId)
  {
    AbstractOp op = new GetPDXEnumByIdOpImpl(pool.getLoggerI18n(), enumId);
    return (EnumInfo) pool.execute(op);
  }
                                                               
  private GetPDXEnumByIdOp() {
    // no instances allowed
  }
  
  private static class GetPDXEnumByIdOpImpl extends AbstractOp {
    /**
     * @throws com.gemstone.gemfire.SerializationException if serialization fails
     */
    public GetPDXEnumByIdOpImpl(LogWriterI18n lw,
                                       int enumId) {
      super(lw, MessageType.GET_PDX_ENUM_BY_ID, 1);
      getMessage().addIntPart(enumId);
    }
    @Override
    protected Object processResponse(Message msg) throws Exception {
      return processObjResponse(msg, "getPDXEnumById");
    }
    @Override
    protected boolean isErrorResponse(int msgType) {
      return false;
    }
    @Override
    protected long startAttempt(ConnectionStats stats) {
      return stats.startGetPDXTypeById(); // reuse PDXType stats instead of adding new enum ones
    }
    @Override
    protected void endSendAttempt(ConnectionStats stats, long start) {
      stats.endGetPDXTypeByIdSend(start, hasFailed());
    }
    @Override
    protected void endAttempt(ConnectionStats stats, long start) {
      stats.endGetPDXTypeById(start, hasTimedOut(), hasFailed());
    }
    @Override
    protected void processSecureBytes(Connection cnx, Message message)
        throws Exception {
    }
    @Override
    protected boolean needsUserId() {
      return false;
    }
    //Don't send the transaction id for this message type.
    @Override
    protected boolean participateInTransaction() {
      return false;
    }
    
    //TODO - no idea what this mumbo jumbo means, but it's on
    //most of the other messages like this.
    @Override
    protected void sendMessage(Connection cnx) throws Exception {
      getMessage().setEarlyAck((byte)(getMessage().getEarlyAckByte() & Message.MESSAGE_HAS_SECURE_PART));
      getMessage().send(false);
    }
  }
}
