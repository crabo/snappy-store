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

package com.pivotal.gemfirexd.internal.engine.distributed.message;

import com.gemstone.gemfire.DataSerializer;
import com.gemstone.gemfire.cache.execute.ResultCollector;
import com.gemstone.gemfire.distributed.DistributedMember;
import com.gemstone.gemfire.internal.snappy.CallbackFactoryProvider;
import com.gemstone.gemfire.internal.snappy.StoreCallbacks;
import com.pivotal.gemfirexd.internal.engine.GfxdConstants;
import com.pivotal.gemfirexd.internal.engine.Misc;
import com.pivotal.gemfirexd.internal.engine.distributed.utils.GemFireXDUtils;
import com.pivotal.gemfirexd.internal.iapi.error.StandardException;
import com.pivotal.gemfirexd.internal.shared.common.sanity.SanityManager;
import com.pivotal.gemfirexd.internal.snappy.ClusterCallbacks;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Set;

public class GetLeadNodeInfoMsg extends MemberExecutorMessage<Object> {

  private Object[] additionalArgs;
  private DataReqType requestType;
  private Long connID;


  public enum DataReqType {GET_JARS, EXPORT_DATA, EXPORT_DDLS, GET_CLASS_BYTES, CHECK_EXT_TABLE_PERMISSION}

  public GetLeadNodeInfoMsg(final ResultCollector<Object, Object> rc,
      DataReqType reqType, Long connID, Object... args) {
    super(rc, null, false, true);
    this.requestType = reqType;
    this.additionalArgs = args;
    this.connID = connID;
  }

  public GetLeadNodeInfoMsg(final ResultCollector<Object, Object> rc, DataReqType reqType, Long connID, String filePath) {
    super(rc, null, false, true);
    this.requestType = reqType;
    this.additionalArgs = new Object[1];
    this.additionalArgs[0] = filePath;
    this.connID = connID;
  }

  public GetLeadNodeInfoMsg() {
    super(true);
  }

  @Override
  public Set<DistributedMember> getMembers() {
    return Misc.getLeadNodes();
  }

  @Override
  public void postExecutionCallback() {
  }

  @Override
  public boolean isHA() {
    return true;
  }

  @Override
  public boolean optimizeForWrite() {
    return false;
  }

  @Override
  protected void execute() throws Exception {
    if (GemFireXDUtils.TraceQuery) {
      SanityManager.DEBUG_PRINT(GfxdConstants.TRACE_QUERYDISTRIB,
          "GetLeadNodeInfoMsg.execute: ");
    }
    try {
      Object result = null;
      switch (this.requestType) {
        case GET_JARS:
          result = handleGetJarsRequest();
          break;
        case EXPORT_DATA:
          if (GemFireXDUtils.TraceQuery) {
            SanityManager.DEBUG_PRINT(GfxdConstants.TRACE_QUERYDISTRIB,
                "GetLeadNodeInfoMsg - case EXPORT_DATA");
          }
          result = exportData();
          break;
        case EXPORT_DDLS:
          if (GemFireXDUtils.TraceQuery) {
            SanityManager.DEBUG_PRINT(GfxdConstants.TRACE_QUERYDISTRIB,
                "GetLeadNodeInfoMsg - case EXPORT_DDLS");
          }
          result = exportDDLs();
          break;
        case GET_CLASS_BYTES:
          result = getClassBytes();
          break;
        case CHECK_EXT_TABLE_PERMISSION:
          result = checkExternalTableAuthorization();
          break;
        default:
          throw new IllegalArgumentException("GetLeadNodeInfoMsg:" +
              " Unknown data request type: " + this.requestType);

      }
      lastResult(result, false, false, true);
    } catch (Exception ex) {
      throw LeadNodeExecutorMsg.getExceptionToSendToServer(ex);
    }
  }

  private byte[] getClassBytes() throws IOException {
    String filePath = (String)this.additionalArgs[0];
    Path p = Paths.get(filePath);
    if (!Files.exists(p)) {
      throw new FileNotFoundException(filePath);
    }
    File file = new File(filePath);
    FileInputStream fip = new FileInputStream(file);
    try {
      byte fileContent[] = new byte[(int) file.length()];
      fip.read(fileContent);
      return fileContent;
    } finally {
      if (fip != null) fip.close();
    }
  }

  private String checkExternalTableAuthorization() throws IOException {
    if (!Misc.isSecurityEnabled()) return null;
    boolean checkAuthOfExternalTables = Boolean.parseBoolean(
            System.getProperty("CHECK_EXTERNAL_TABLE_AUTHZ"));
    if (!checkAuthOfExternalTables) return null;

    String user = (String)this.additionalArgs[0];
    String allTableStr = (String)this.additionalArgs[1];
    String[] allTable = allTableStr.split(",");
    ClusterCallbacks ccb = com.pivotal.gemfirexd.internal.snappy.CallbackFactoryProvider.getClusterCallbacks();
    String result = null;
    for (String t : allTable) {
      if (!ccb.isUserAuthorizedForExternalTable(user, t)) {
        result = t;
        break;
      }
    }
    return result;
  }

  private String exportData() {
    com.pivotal.gemfirexd.internal.snappy.CallbackFactoryProvider
        .getClusterCallbacks().exportData(connID, additionalArgs[0].toString(),
        additionalArgs[1].toString(), additionalArgs[2].toString(),
        Boolean.parseBoolean(additionalArgs[3].toString()));
    return "Data recovered";
  }

  private String exportDDLs() {
    com.pivotal.gemfirexd.internal.snappy.CallbackFactoryProvider
        .getClusterCallbacks().exportDDLs(connID, additionalArgs[0].toString());
    return "DDLs recovered.";
  }

  private String handleGetJarsRequest() {
    URLClassLoader ul = CallbackFactoryProvider.getStoreCallbacks().getLeadClassLoader();
    URL[] allJarUris = ul.getURLs();
    StringBuffer res = new StringBuffer();
    for (URL u : allJarUris) {
      res.append(u);
      res.append(',');
    }
    if (res.length() > 0) {
      return res.substring(0, res.length() - 1);
    }
    return null;
  }

  @Override
  protected void executeFunction(boolean enableStreaming)
      throws StandardException, SQLException {
    try {
      super.executeFunction(enableStreaming);
    } catch (RuntimeException re) {
      throw LeadNodeExecutorMsg.handleLeadNodeRuntimeException(re);
    }
  }

  @Override
  protected LeadNodeGetStatsMessage clone() {
    final LeadNodeGetStatsMessage msg = new LeadNodeGetStatsMessage(this.userCollector);
    return msg;
  }

  @Override
  public byte getGfxdID() {
    return LEAD_NODE_DATA_MSG;
  }

  @Override
  public void fromData(DataInput in) throws IOException, ClassNotFoundException {
    super.fromData(in);
    this.requestType = DataSerializer.readObject(in);
    this.additionalArgs = DataSerializer.readObjectArray(in);
    this.connID = DataSerializer.readLong(in);
  }

  @Override
  public void toData(final DataOutput out) throws IOException {
    super.toData(out);
    DataSerializer.writeObject(this.requestType, out);
    DataSerializer.writeObjectArray(this.additionalArgs, out);
    DataSerializer.writeLong(this.connID, out);
  }

  public void appendFields(final StringBuilder sb) {
  }

}

