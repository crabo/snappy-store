/*

   Derby - Class com.pivotal.gemfirexd.internal.impl.jdbc.EmbedCallableStatement30

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

/*
 * Changes for GemFireXD distributed data platform (some marked by "GemStone changes")
 *
 * Portions Copyright (c) 2010-2015 Pivotal Software, Inc. All rights reserved.
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

package com.pivotal.gemfirexd.internal.impl.jdbc;

import java.math.BigDecimal;

import java.sql.ParameterMetaData;
import java.sql.SQLException;


import com.pivotal.gemfirexd.internal.impl.jdbc.EmbedConnection;
import com.pivotal.gemfirexd.internal.impl.jdbc.Util;


/**
 * This class extends the EmbedCallableStatement20
 * in order to support new methods and classes that come with JDBC 3.0.

  <P><B>Supports</B>
   <UL>
   <LI> JDBC 3.0 - dependency on java.sql.ParameterMetaData introduced in JDBC 3.0 
   </UL>

  *
 * @see com.pivotal.gemfirexd.internal.impl.jdbc.EmbedCallableStatement
 *
 */
public class EmbedCallableStatement30 extends EmbedCallableStatement20
{

	//////////////////////////////////////////////////////////////
	//
	// CONSTRUCTORS
	//
	//////////////////////////////////////////////////////////////
//GemStone changes BEGIN
	public EmbedCallableStatement30 (EmbedConnection conn, String sql,
								   int resultSetType,
								   int resultSetConcurrency,
								   int resultSetHoldability, 
								   long id, short execFlags)
		throws SQLException
	{
		super(conn, sql, resultSetType, resultSetConcurrency, resultSetHoldability,id,execFlags);
	}
//      GemStone changes END
	/*
	 * Note: all the JDBC 3.0 Prepared statement methods are duplicated
	 * in here because this class inherits from Local20/EmbedCallableStatement, which
	 * inherits from Local/EmbedCallableStatement.  This class should inherit from a
	 * local30/PreparedStatement.  Since java does not allow multiple inheritance,
	 * duplicate the code here.
	 */

	/**
    * JDBC 3.0
    *
    * Retrieves the number, types and properties of this PreparedStatement
    * object's parameters.
    *
    * @return a ParameterMetaData object that contains information about the
    * number, types and properties of this PreparedStatement object's parameters.
    * @exception SQLException if a database access error occurs
	*/
	public ParameterMetaData getParameterMetaData()
    throws SQLException
	{
		checkStatus();
		if (preparedStatement == null)
			return null;
		
		return new EmbedParameterMetaData30(
				getParms(), preparedStatement.getParameterTypes());
	}

}











