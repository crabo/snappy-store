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
package com.gemstone.gemfire.internal.cache.xmlcache;

import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXParseException;

import com.gemstone.gemfire.cache.CacheXmlException;
import com.gemstone.gemfire.internal.ClassPathLoader;
import com.gemstone.gemfire.internal.i18n.LocalizedStrings;

/**
 * The abstract superclass of classes that convert XML into a {@link
 * com.gemstone.gemfire.cache.Cache} and vice versa. It provides helper methods
 * and constants.
 *
 * @author David Whitlock
 * @since 3.0
 */
public abstract class CacheXml implements EntityResolver, ErrorHandler {

  /**
   * This always refers to the latest GemFire version, in those cases where
   * we default to the current released version of GemFire.
   *
   * WHenever you upgrade the DTD, you will need to search for occurrences of
   * the previous version strings and upgrade them as well.
   *
   * @since 5.5
   */
  protected static final String VERSION_LATEST = CacheXml.VERSION_7_5;
  public static final String LATEST_SYSTEM_ID = CacheXml.SYSTEM_ID_7_5;
  public static final String LATEST_PUBLIC_ID = CacheXml.PUBLIC_ID_7_5;

//---------------------------------
  /** Version string for GemFire 7.5 */
  public static final String VERSION_7_5 = "7_5";
  /** The location of the GemFire 7.5 DTD file */
  protected static final String DTD_7_5_LOCATION =
    "/com/gemstone/gemfire/cache/doc-files/cache7_5.dtd";
  /** The URL for the 7.5 DTD */
  protected static final String SYSTEM_ID_7_5 =
    "http://www.gemstone.com/dtd/cache7_5.dtd";
  /** The public ID for the 7.5 DTD */
  protected static final String PUBLIC_ID_7_5 =
    "-//GemStone Systems, Inc.//GemFire Declarative Cache 7.5//EN";

//---------------------------------
  /** Version string for GemFire 7.0 */
  public static final String VERSION_7_0 = "7_0";
  /** The location of the GemFire 7.0 DTD file */
  protected static final String DTD_7_0_LOCATION =
    "/com/gemstone/gemfire/cache/doc-files/cache7_0.dtd";
  /** The URL for the 7.0 DTD */
  protected static final String SYSTEM_ID_7_0 =
    "http://www.gemstone.com/dtd/cache7_0.dtd";
  /** The public ID for the 7.0 DTD */
  protected static final String PUBLIC_ID_7_0 =
    "-//GemStone Systems, Inc.//GemFire Declarative Cache 7.0//EN";
  
  //---------------------------------
  /** Version string for GemFire 6.6 */
  public static final String VERSION_6_6 = "6_6";
  /** The location of the GemFire 6.6 DTD file */
  protected static final String DTD_6_6_LOCATION =
    "/com/gemstone/gemfire/cache/doc-files/cache6_6.dtd";
  /** The URL for the 6.6 DTD */
  protected static final String SYSTEM_ID_6_6 =
    "http://www.gemstone.com/dtd/cache6_6.dtd";
  /** The public ID for the 6.6 DTD */
  protected static final String PUBLIC_ID_6_6 =
    "-//GemStone Systems, Inc.//GemFire Declarative Cache 6.6//EN";
  
  //---------------------------------
  /** Version string for GemFire 6.5 */
  public static final String VERSION_6_5 = "6_5";
  /** The location of the GemFire 6.5 DTD file */
  protected static final String DTD_6_5_LOCATION =
    "/com/gemstone/gemfire/cache/doc-files/cache6_5.dtd";
  /** The URL for the 6.5 DTD */
  protected static final String SYSTEM_ID_6_5 =
    "http://www.gemstone.com/dtd/cache6_5.dtd";
  /** The public ID for the 6.5 DTD */
  protected static final String PUBLIC_ID_6_5 =
    "-//GemStone Systems, Inc.//GemFire Declarative Cache 6.5//EN";

  //---------------------------------
  /** Version string for GemFire 6.1 */
  public static final String VERSION_6_1 = "6_1";
  /** The location of the GemFire 6.1 DTD file */
  protected static final String DTD_6_1_LOCATION =
    "/com/gemstone/gemfire/cache/doc-files/cache6_1.dtd";
  /** The URL for the 6.1 DTD */
  protected static final String SYSTEM_ID_6_1 =
    "http://www.gemstone.com/dtd/cache6_1.dtd";
  /** The public ID for the 6.1 DTD */
  protected static final String PUBLIC_ID_6_1 =
    "-//GemStone Systems, Inc.//GemFire Declarative Cache 6.1//EN";

  //---------------------------------
  /** Version string for GemFire 6.0 */
  public static final String VERSION_6_0 = "6_0";
  /** The location of the GemFire 6.0 DTD file */
  protected static final String DTD_6_0_LOCATION =
    "/com/gemstone/gemfire/cache/doc-files/cache6_0.dtd";
  /** The URL for the 6.0 DTD */
  protected static final String SYSTEM_ID_6_0 =
    "http://www.gemstone.com/dtd/cache6_0.dtd";
  /** The public ID for the 6.0 DTD */
  protected static final String PUBLIC_ID_6_0 =
    "-//GemStone Systems, Inc.//GemFire Declarative Cache 6.0//EN";

  // ---------------------------------
  /** Version string for GemFire 5.8 */
  public static final String VERSION_5_8 = "5_8";
  /** The location of the GemFire 5.8 DTD file */
  protected static final String DTD_5_8_LOCATION =
    "/com/gemstone/gemfire/cache/doc-files/cache5_8.dtd";
  /** The URL for the 5.8 DTD */
  protected static final String SYSTEM_ID_5_8 =
    "http://www.gemstone.com/dtd/cache5_8.dtd";
  /** The public ID for the 5.8 DTD */
  protected static final String PUBLIC_ID_5_8 =
    "-//GemStone Systems, Inc.//GemFire Declarative Cache 5.8//EN";

  // ---------------------------------
  /** Version string for GemFire 5.7 */
  public static final String VERSION_5_7 = "5_7";
  /** The location of the GemFire 5.7 DTD file */
  protected static final String DTD_5_7_LOCATION =
    "/com/gemstone/gemfire/cache/doc-files/cache5_7.dtd";
  /** The URL for the 5.7 DTD */
  protected static final String SYSTEM_ID_5_7 =
    "http://www.gemstone.com/dtd/cache5_7.dtd";
  /** The public ID for the 5.7 DTD */
  protected static final String PUBLIC_ID_5_7 =
    "-//GemStone Systems, Inc.//GemFire Declarative Cache 5.7//EN";

  // ---------------------------------
  /** Version string for GemFire 5.5 */
  public static final String VERSION_5_5 = "5_5";
  /** The location of the GemFire 5.5 DTD file */
  protected static final String DTD_5_5_LOCATION =
    "/com/gemstone/gemfire/cache/doc-files/cache5_5.dtd";
  /** The URL for the 5.5 DTD */
  protected static final String SYSTEM_ID_5_5 =
    "http://www.gemstone.com/dtd/cache5_5.dtd";
  /** The public ID for the 5.5 DTD */
  protected static final String PUBLIC_ID_5_5 =
    "-//GemStone Systems, Inc.//GemFire Declarative Cache 5.5//EN";

  // ---------------------------------
  /** Version string for GemFire 5.1 */
  public static final String VERSION_5_1 = "5_1";
  /** The location of the GemFire 5.1 DTD file */
  protected static final String DTD_5_1_LOCATION =
    "/com/gemstone/gemfire/cache/doc-files/cache5_1.dtd";
  /** The URL for the 5.1 DTD */
  protected static final String SYSTEM_ID_5_1 =
    "http://www.gemstone.com/dtd/cache5_1.dtd";
  /** The public ID for the 5.1 DTD */
  protected static final String PUBLIC_ID_5_1 =
    "-//GemStone Systems, Inc.//GemFire Declarative Cache 5.1//EN";

  // ---------------------------------
  /** Version string for GemFire 5.0 */
  public static final String VERSION_5_0 = "5_0";
  /** The location of the GemFire 5.0 DTD file */
  protected static final String DTD_5_0_LOCATION =
    "/com/gemstone/gemfire/cache/doc-files/cache5_0.dtd";
  /** The URL for the 5.0 DTD */
  protected static final String SYSTEM_ID_5_0 =
    "http://www.gemstone.com/dtd/cache5_0.dtd";
  /** The public ID for the 5.0 DTD */
  protected static final String PUBLIC_ID_5_0 =
    "-//GemStone Systems, Inc.//GemFire Declarative Caching 5.0//EN";

  // ---------------------------------
  /** Version string for GemFire 4.1 */
  public static final String VERSION_4_1 = "4_1";
  /** The location of the GemFire 4.1 DTD file */
  protected static final String DTD_4_1_LOCATION = "/com/gemstone/gemfire/cache/doc-files/cache4_1.dtd";
  /** The URL for the 4.1 DTD */
  protected static final String SYSTEM_ID_4_1 = "http://www.gemstone.com/dtd/cache4_1.dtd";
  /** The public ID for the 4.1 DTD */
  protected static final String PUBLIC_ID_4_1 = "-//GemStone Systems, Inc.//GemFire Declarative Caching 4.1//EN";

  /** Version string for GemFire 4.0 */
  public static final String VERSION_4_0 = "4_0";
  /** The location of the GemFire 4.0 DTD file */
  protected static final String DTD_4_0_LOCATION = "/com/gemstone/gemfire/cache/doc-files/cache4_0.dtd";
  /** The URL for the 4.0 DTD */
  protected static final String SYSTEM_ID_4_0 = "http://www.gemstone.com/dtd/cache4_0.dtd";
  /** The public ID for the 4.0 DTD */
  protected static final String PUBLIC_ID_4_0 = "-//GemStone Systems, Inc.//GemFire Declarative Caching 4.0//EN";

  // ---------------------------------
  /** Version string for GemFire 3.0 */
  public static final String VERSION_3_0 = "3_0";
  /** The location of the GemFire 3.0 DTD file */
  protected static final String DTD_3_0_LOCATION = "/com/gemstone/gemfire/cache/doc-files/cache3_0.dtd";
  /** The URL for the 3.0 DTD */
  protected static final String SYSTEM_ID_3_0 = "http://www.gemstone.com/dtd/cache3_0.dtd";
  /** The public ID for the 3.0 DTD */
  protected static final String PUBLIC_ID_3_0 = "-//GemStone Systems, Inc.//GemFire Declarative Caching 3.0//EN";

  // ---------------------------------
  protected static final String DTD_TYPE_CDATA = "CDATA";

  /** The name of the <code>cache</code> element */
  protected static final String CACHE = "cache";
  /** The name of the <code>client-cache</code> element */
  protected static final String CLIENT_CACHE = "client-cache";
  /** The name of the <code>region</code> element */
  public static final String REGION = "region";
  /** The name of the <code>vm-root-region</code> element */
  protected static final String VM_ROOT_REGION = "vm-root-region";
  /** The name of the <code>region-attributes</code> element */
  protected static final String REGION_ATTRIBUTES = "region-attributes";
  /** The name of the <code>key-constraint</code> element */
  protected static final String KEY_CONSTRAINT = "key-constraint";
  /** The name of the <code>value-constraint</code> element */
  protected static final String VALUE_CONSTRAINT = "value-constraint";
  /** The name of the <code>region-time-to-live</code> element */
  protected static final String REGION_TIME_TO_LIVE = "region-time-to-live";
  /** The name of the <code>region-idle-time</code> element */
  protected static final String REGION_IDLE_TIME = "region-idle-time";
  /** The name of the <code>entry-time-to-live</code> element */
  protected static final String ENTRY_TIME_TO_LIVE = "entry-time-to-live";
  /** The name of the <code>entry-idle-time</code> element */
  protected static final String ENTRY_IDLE_TIME = "entry-idle-time";
  /** The name of the <code>expiration-attributes</code> element */
  protected static final String EXPIRATION_ATTRIBUTES = "expiration-attributes";
  /** The name of the <code>custom-expiry</code> element */
  protected static final String CUSTOM_EXPIRY = "custom-expiry";
  /** The name of the <code>entry</code> element */
  protected static final String ENTRY = "entry";
  /** The name of the <code>class-name</code> element */
  protected static final String CLASS_NAME = "class-name";
  /** The name of the <code>parameter</code> element */
  protected static final String PARAMETER = "parameter";
  /** The name of the <code>cache-loader</code> element */
  protected static final String CACHE_LOADER = "cache-loader";
  /** The name of the <code>cache-writer</code> element */
  protected static final String CACHE_WRITER = "cache-writer";
  /** The name of the <code>eviction-attributes</code> element */
  protected static final String EVICTION_ATTRIBUTES = "eviction-attributes";
  /** The name of the <code>cache-listener</code> element */
  protected static final String CACHE_LISTENER = "cache-listener";
  protected static final String PDX = "pdx";
  protected static final String PERSISTENT = "persistent";
  protected static final String READ_SERIALIZED = "read-serialized";
  protected static final String IGNORE_UNREAD_FIELDS = "ignore-unread-fields";
  protected static final String PDX_SERIALIZER = "pdx-serializer";

  /** The name of the <code>key</code> element */
  protected static final String KEY = "key";
  /** The name of the <code>value</code> element */
  protected static final String VALUE = "value";
  /** The name of the <code>string</code> element */
  protected static final String STRING = "string";
  /** The name of the <code>declarable</code> element */
  protected static final String DECLARABLE = "declarable";
  /** The name of the <code>lock-timeout</code> attribute */
  protected static final String LOCK_TIMEOUT = "lock-timeout";
  /** The name of the <code>lock-lease</code> attribute */
  protected static final String LOCK_LEASE = "lock-lease";
  /** The name of the <code>search-timeout</code> attribute */
  protected static final String SEARCH_TIMEOUT = "search-timeout";
  /** The name of the <code>message-synch-interval</code> attribute */
  protected static final String MESSAGE_SYNC_INTERVAL = "message-sync-interval";
  /** The name of the <code>dynamic-region-factory</code> element */
  protected static final String DYNAMIC_REGION_FACTORY = "dynamic-region-factory";
  /** The name of the <code>disable-persist-backup</code> attribute */
  protected static final String DISABLE_PERSIST_BACKUP = "disable-persist-backup";
  /** The name of the <code>disable-register-interest</code> attribute */
  protected static final String DISABLE_REGISTER_INTEREST = "disable-register-interest";
  /** The name of the <code>is-server</code> attribute */
  protected static final String IS_SERVER = "is-server";
  /** The name of the <code>partition-attributes</code> attribute */
  protected static final String PARTITION_ATTRIBUTES = "partition-attributes";
  /** The name of the <code>partition-attributes</code> <code>localMaxMemory</code> attribute */
  protected static final String LOCAL_MAX_MEMORY = "local-max-memory";
  /** The name of the <code>local properties</code> associated with a partitioned region */
  protected static final String LOCAL_PROPERTIES = "local-properties";
  /** The name of the <code>total-max-memory</code> attribute */
  protected static final String TOTAL_MAX_MEMORY = "total-max-memory";
  /** The name of the <code>total-num-buckets</code> property */
  protected static final String TOTAL_NUM_BUCKETS = "total-num-buckets";
  /** The name of the <code>global properties</code> associated with a partitioned region */
  protected static final String GLOBAL_PROPERTIES = "global-properties";
  /** The name of the partitioning <code>redundancy</code> attribute */
  protected static final String PARTITION_REDUNDANT_COPIES = "redundant-copies";
  /** The name of the partitioned region's <code>colocated-with</code> attribute */
  protected static final String PARTITION_COLOCATED_WITH = "colocated-with";
  /** The name of the partitioned region's <code>recovery-delay</code> attribute */
  protected static final String RECOVERY_DELAY = "recovery-delay";
  /** The name of the partitioned region's <code>startup-recovery-delay</code> attribute */
  protected static final String STARTUP_RECOVERY_DELAY = "startup-recovery-delay";
  /** The name of the <code>fixed-partition-attributes</code> attribute */
  protected static final String FIXED_PARTITION_ATTRIBUTES = "fixed-partition-attributes";
  /** The name of the fixed partition's<code>partition-name</code> attribute */
  protected static final String PARTITION_NAME = "partition-name";
  /** The name of the fixed partition's <code>is-primary</code> attribute */
  protected static final String IS_PRIMARY = "is-primary";
  /** The name of the fixed partition's <code>num-buckets</code> attribute */
  protected static final String NUM_BUCKETS = "num-buckets";
  
/** The name of the load probe element */
  protected static final String LOAD_PROBE="custom-load-probe";
  /** The name of the load poll interval element */
  protected static final String LOAD_POLL_INTERVAL="load-poll-interval";
  /** The name of the <code>bridge-server</code> element */
  protected static final String BRIDGE_SERVER = "bridge-server";
  /** The name of the <code>cache-server</code> element */
  protected static final String CACHE_SERVER = "cache-server";
  protected static final String HOSTNAME_FOR_CLIENTS = "hostname-for-clients";
  /** The name of the <code>group</code> element */
  protected static final String GROUP = "group";
  /** The name of the <code>gateway-hub</code> element */
  protected static final String GATEWAY_HUB = "gateway-hub";

  public static final String GATEWAY_SENDER = "gateway-sender";
  public static final String GATEWAY_RECEIVER = "gateway-receiver";
  
  protected static final String GATEWAY_EVENT_FILTER = "gateway-event-filter";
  protected static final String GATEWAY_TRANSPORT_FILTER = "gateway-transport-filter";
  protected static final String GATEWAY_EVENT_LISTENER = "gateway-event-listener";
  protected static final String GATEWAY_SENDER_IDS = "gateway-sender-ids";
  protected static final String HOSTNAME_FOR_SENDERS = "hostname-for-senders";

  /** The name of the <code>gateway</code> attribute */
  protected static final String GATEWAY = "gateway";
  /** The name of the <code>gateway-endpoint</code> attribute */
  protected static final String GATEWAY_ENDPOINT = "gateway-endpoint";
  /** The name of the <code>gateway-listener</code> attribute */
  protected static final String GATEWAY_LISTENER = "gateway-listener";
  /** The name of the <code>gateway-queue</code> attribute */
  protected static final String GATEWAY_QUEUE = "gateway-queue";
  /** The name of the <code>gateway-conflict-resolver</code> */ // added in 7.0
  protected static final String GATEWAY_CONFLICT_RESOLVER = "gateway-conflict-resolver";
  /** The name of the <code>host</code> attribute */
  protected static final String HOST = "host";
  /** The name of the <code>port</code> attribute */
  protected static final String PORT = "port";
  /** The name of the <code>start-port</code> attribute */
  protected static final String START_PORT = "start-port";
  /** The name of the <code>end-port</code> attribute */
  protected static final String END_PORT = "end-port";
  /** The name of the <code>startup-policy</code> attribute */
  protected static final String STARTUP_POLICY = "startup-policy";
  /** The name of the <code>parallel</code> attribute */
  protected static final String PARALLEL = "parallel";
  /** The name of the <code>manual-start</code> attribute */
  protected static final String MANUAL_START = "manual-start";
  /** The name of the <code>order-policy</code> attribute */
  protected static final String ORDER_POLICY = "order-policy";
  /** The name of the <code>remote-distributed-system</code> attribute */
  protected static final String REMOTE_DISTRIBUTED_SYSTEM_ID = "remote-distributed-system-id";

  /** The name of the <code>bind-address</code> attribute */
  protected static final String BIND_ADDRESS = "bind-address";
  /** The name of the <code>max-connections</code> attribute */
  protected static final String MAX_CONNECTIONS = "max-connections";
  /** The name of the <code>max-threads</code> attribute */
  protected static final String MAX_THREADS = "max-threads";
  /** The name of the <code>notify-by-subscription</code> attribute */
  protected static final String NOTIFY_BY_SUBSCRIPTION = "notify-by-subscription";
  /** The name of the <code>copy-on-read</code> attribute */
  protected static final String COPY_ON_READ = "copy-on-read";
  /** The name of the <code>name</code> attribute */
  protected static final String NAME = "name";
  /** The name of the <code>scope</code> attribute */
  protected static final String SCOPE = "scope";
  /** The name of the <code>mirror-type</code> attribute */
  protected static final String MIRROR_TYPE = "mirror-type";
  /** The name of the <code>eviction-policy</code> attribute */
  protected static final String CLIENT_SUBSCRIPTION_EVICTION_POLICY = "eviction-policy";
  /** The name of the <code>capacity</code> attribute */
  protected static final String CLIENT_SUBSCRIPTION_CAPACITY = "capacity";
  /** The name of the <code>eviction-policy</code> attribute */
  protected static final String CLIENT_SUBSCRIPTION = "client-subscription";
  
  /** The name of the <code>data-policy</code> attribute */
  protected static final String DATA_POLICY = "data-policy";
  protected static final String EMPTY_DP = "empty";
  protected static final String NORMAL_DP = "normal";
  protected static final String PRELOADED_DP = "preloaded";
  protected static final String REPLICATE_DP = "replicate";
  protected static final String PERSISTENT_REPLICATE_DP = "persistent-replicate";
  protected static final String PARTITION_DP = "partition";
  protected static final String PERSISTENT_PARTITION_DP = "persistent-partition";
  protected static final String HDFS_PARTITION_DP = "hdfs-partition";
  protected static final String HDFS_PERSISTENT_PARTITION_DP = "hdfs-persistent-partition";

  /** The name of the <code>keep-alive-timeout</code> attribute */
  protected static final String KEEP_ALIVE_TIMEOUT = "keep-alive-timeout";
  /** The name of the <code>initial-capacity</code> attribute */
  protected static final String INITIAL_CAPACITY = "initial-capacity";
  protected static final String CONCURRENCY_LEVEL = "concurrency-level";
  /** The name of the <code>concurrency-checks-enabled</code> attribute */
  protected static final String CONCURRENCY_CHECKS_ENABLED = "concurrency-checks-enabled";
  /** The name of the <code>serialize-values</code> attribute */
  protected static final String SERIALIZE_VALUES = "serialize-values";
  /** The name of the <code>load-factor</code> attribute */
  protected static final String LOAD_FACTOR = "load-factor";
  /** The name of the <code>statistics-enabled</code> attribute */
  protected static final String STATISTICS_ENABLED = "statistics-enabled";
  /** The name of the <code>ignore-jta</code> attribute */
  protected static final String IGNORE_JTA = "ignore-jta";
  /** The name of the <code>is-lock-grantor</code> attribute */
  protected static final String IS_LOCK_GRANTOR = "is-lock-grantor";
  /** The name of the <code>timeout</code> attribute */
  protected static final String TIMEOUT = "timeout";
  /** The name of the <code>action</code> attribute */
  protected static final String ACTION = "action";
  /** The name of the <code>local</code> value */
  protected static final String LOCAL = "local";
  /** The name of the <code>distributed-no-ack</code> value */
  protected static final String DISTRIBUTED_NO_ACK = "distributed-no-ack";
  /** The name of the <code>distributed-ack</code> value */
  protected static final String DISTRIBUTED_ACK = "distributed-ack";
  /** The name of the <code>global</code> value */
  protected static final String GLOBAL = "global";
  /** The name of the <code>none</code> value */
  protected static final String NONE = "none";
  /** The name of the <code>keys</code> value */
  protected static final String KEYS = "keys";
  /** The name of the <code>key-values</code> value */
  protected static final String KEYS_VALUES = "keys-values";
  /** The name of the <code>host-own</code> value */
  protected static final String PARTITION_HOST_OWN = "host-own";

  /** The name of the <code>host-other</code> value */
  protected static final String PARTITION_HOST_OTHER = "host-other";

  /** The name of the <code>true</code> value */
  protected static final String TRUE = "true";
  /** The name of the <code>false</code> value */
  protected static final String FALSE = "false";
  /** The name of the <code>invalidate</code> value */
  protected static final String INVALIDATE = "invalidate";
  /** The name of the <code>destroy</code> value */
  protected static final String DESTROY = "destroy";
  /** The name of the <code>local-invalidate</code> value */
  protected static final String LOCAL_INVALIDATE = "local-invalidate";
  /** The name of the <code>local-destroy</code> value */
  protected static final String LOCAL_DESTROY = "local-destroy";
  /** The name of the <code>persist-backup</code> value */
  protected static final String PERSIST_BACKUP = "persist-backup";
  /** The name of the <code>early-ack</code> value */
  protected static final String EARLY_ACK = "early-ack";
  /** The name of the <code>disk-dir</code> value */
  protected static final String DISK_DIR = "disk-dir";
  /** The name of the <code>disk-dirs</code> value */
  protected static final String DISK_DIRS = "disk-dirs";
  /** The name of the <code>disk-write-attributes</code> value */
  protected static final String DISK_WRITE_ATTRIBUTES = "disk-write-attributes";
  /** The name of the <code>synchronous-writes</code> value */
  protected static final String SYNCHRONOUS_WRITES = "synchronous-writes";
  /** The name of the <code>asynchronous-writes</code> value */
  protected static final String ASYNCHRONOUS_WRITES = "asynchronous-writes";
  /** The name of the <code>time-interval</code> value */
  public static final String TIME_INTERVAL = "time-interval";
  /** The name of the <code>bytes-threshold</code> value */
  public static final String BYTES_THRESHOLD = "bytes-threshold";
  /** The name of the <code>index</code> element */
  protected static final String INDEX = "index";
  /** The name of the <code>key-index</code> index attribute */
  protected static final String KEY_INDEX = "key-index";
  /** The name of the index type attribute */
  protected static final String INDEX_TYPE = "type";
  /** The name of the <code>hash-index</code> index type attribute */
  protected static final String HASH_INDEX_TYPE = "hash";
  /** The name of the <code>range-index</code> index type attribute */
  protected static final String RANGE_INDEX_TYPE = "range";
  /** The name of the <code>functional-sorted</code> element */
  protected static final String FUNCTIONAL = "functional";
  /** The name of the <code>primary-key</code> element */
  protected static final String PRIMARY_KEY = "primary-key";
  /** The name of the <code>index-update-type</code> element */
  protected static final String INDEX_UPDATE_TYPE = "index-update-type";
  /** The name of the <code>update-type</code> value */
  protected static final String INDEX_UPDATE_TYPE_ASYNCH = "asynchronous";
  /** The name of the <code>update-type</code> value */
  protected static final String INDEX_UPDATE_TYPE_SYNCH = "synchronous";
  /** The name of the <code>from-clause</code> attribute */
  protected static final String FROM_CLAUSE = "from-clause";
  /** The name of the <code>expression</code> attribute */
  protected static final String EXPRESSION = "expression";
  /** The name of the <code>import</code> attribute */
  protected static final String IMPORTS = "imports";
  /** The name of the <code>field</code> attribute */
  protected static final String FIELD = "field";
  /** The name of the <code>cache-transaction-manager</code> element */
  protected static final String TRANSACTION_MANAGER = "cache-transaction-manager";
  /** The name of the <code>transaction-listener</code> element */
  protected static final String TRANSACTION_LISTENER = "transaction-listener";
  /** The name of the <code>transaction-writer</code> element */
  protected static final String TRANSACTION_WRITER = "transaction-writer";
  
  /** The name of the multicast-enabled element */
  protected static final String MULTICAST_ENABLED = "multicast-enabled";
  protected static final String JNDI_BINDINGS = "jndi-bindings";
  protected static final String JNDI_BINDING = "jndi-binding";
  /** The name of the <code>config-property</code> value */
  protected static final String CONFIG_PROPERTY_BINDING = "config-property";
  /** The name of the <code>config-property-name</code> value */
  protected static final String CONFIG_PROPERTY_NAME = "config-property-name";
  /** The name of the <code>config-property-value</code> value */
  protected static final String CONFIG_PROPERTY_VALUE = "config-property-value";
  /** The name of the <code>config-property-type</code> value */
  protected static final String CONFIG_PROPERTY_TYPE = "config-property-type";
   /** Name of disk region property specifying whether to roll oplog or no **/
  public  static final String ROLL_OPLOG = "roll-oplogs";
   /** Name of disk region property specifying whether to automatically compact disk files **/
  public static final String AUTO_COMPACT = "auto-compact";
  public static final String ALLOW_FORCE_COMPACTION = "allow-force-compaction";
  public static final String COMPACTION_THRESHOLD = "compaction-threshold";
  /** Name of disk region property specifying the max oplog size in megabytes **/
  
  public static final String MAX_OPLOG_SIZE = "max-oplog-size";
  /** Name of region property specifying the cloning **/
  public static final String CLONING_ENABLED = "cloning-enabled";

  // begin constants for connection pool
  public static final String CONNECTION_POOL = "pool";
  public static final String POOL_NAME = "pool-name";
  public static final String SERVER = "server";
  public static final String LOCATOR = "locator";
  public static final String FREE_CONNECTION_TIMEOUT = "free-connection-timeout";
  public static final String LOAD_CONDITIONING_INTERVAL = "load-conditioning-interval";
  public static final String MIN_CONNECTIONS = "min-connections";
  public static final String RETRY_ATTEMPTS = "retry-attempts";
  public static final String IDLE_TIMEOUT = "idle-timeout";
  public static final String PING_INTERVAL = "ping-interval";
  public static final String STATISTIC_INTERVAL = "statistic-interval";
  public static final String SUBSCRIPTION_ENABLED = "subscription-enabled";
  public static final String PR_SINGLE_HOP_ENABLED = "pr-single-hop-enabled";
  public static final String SUBSCRIPTION_MESSAGE_TRACKING_TIMEOUT = "subscription-message-tracking-timeout";
  public static final String SUBSCRIPTION_ACK_INTERVAL = "subscription-ack-interval";
  public static final String SUBSCRIPTION_REDUNDANCY = "subscription-redundancy";
  public static final String READ_TIMEOUT = "read-timeout";
  public static final String SERVER_GROUP = "server-group";
  public static final String THREAD_LOCAL_CONNECTIONS = "thread-local-connections";
  public static final String DISK_STORE_NAME = "disk-store-name";
  public static final String DISK_STORE = "disk-store";
  public static final String DISK_SYNCHRONOUS = "disk-synchronous";
  public static final String WRITE_BUFFER_SIZE = "write-buffer-size";
  public static final String QUEUE_SIZE = "queue-size";

  public static final String MULTIUSER_SECURE_MODE_ENABLED = "multiuser-authentication";
  // end constants for connection pool

 /** Size of the disk dir in megabytes **/
   protected static final String DIR_SIZE = "dir-size";

  /** The name of the <code>id</code> attribute */
  protected static final String ID = "id";
  /** The name of the <code>refid</code> attribute */
  protected static final String REFID = "refid";

  /** The name of the <code>membership-attributes</code> element */
  protected static final String MEMBERSHIP_ATTRIBUTES = "membership-attributes";
  /** The name of the <code>loss-action</code> attribute */
  protected static final String LOSS_ACTION = "loss-action";
  /** The name of the <code>resumption-action</code> attribute */
  protected static final String RESUMPTION_ACTION = "resumption-action";
  /** The name of the <code>required-role</code> element */
  protected static final String REQUIRED_ROLE = "required-role";

  /** The name of the <code>subscription-attributes</code> element */
  protected static final String SUBSCRIPTION_ATTRIBUTES = "subscription-attributes";
  protected static final String INTEREST_POLICY = "interest-policy";
  protected static final String ALL = "all";
  protected static final String CACHE_CONTENT = "cache-content";

  /** Eviction Controller eviction on a per Entry basis */
  protected static final String LRU_ENTRY_COUNT = "lru-entry-count";
  /** Eviction Controller eviction on a per Entry size basis */
  protected static final String LRU_MEMORY_SIZE = "lru-memory-size";
  /** Eviction Controller eviction based on used heap */
  protected static final String LRU_HEAP_PERCENTAGE = "lru-heap-percentage";
  /** Eviction Controller maximum allowed value for the enclosing Eviction Controller */
  protected static final String MAXIMUM = "maximum";

  /** The name of the <code>directory</code> attribute */
  protected static final String DIRECTORY = "directory";
  /** The name of the <code>max-disk-usage</code> attribute */
  protected static final String MAX_DISK_USAGE = "max-disk-usage";

  /** The name of the <code>enable-wan</code> attribute */
  protected static final String ENABLE_WAN = "enable-wan";
  /** The name of the <code>enable-gateway</code> attribute */
  protected static final String ENABLE_GATEWAY = "enable-gateway";
  /** The name of the <code>hub-id</code> attribute */
  protected static final String GATEWAY_HUB_ID = "hub-id";

  /** The name of the <code>publisher</code> attribute */
  protected static final String PUBLISHER = "publisher";

  /** The name of the <code>overflow-directory</code> attribute */
  protected static final String OVERFLOW_DIRECTORY = "overflow-directory";
  /** The name of the <code>socket-buffer-size</code> attribute */
  protected static final String SOCKET_BUFFER_SIZE = "socket-buffer-size";
  /** The name of the <code>socket-read-timeout</code> attribute */
  protected static final String SOCKET_READ_TIMEOUT = "socket-read-timeout";
  /** The name of the <code>maximum-queue-memory</code> attribute */
  protected static final String MAXIMUM_QUEUE_MEMORY = "maximum-queue-memory";
  /** The name of the <code>batch-size</code> attribute */
  protected static final String BATCH_SIZE = "batch-size";
  /** The name of the <code>batch-time-interval</code> attribute */
  protected static final String BATCH_TIME_INTERVAL = "batch-time-interval";
  /** The name of the <code>enable-bridge-conflation</code> attribute */
  protected static final String ENABLE_BRIDGE_CONFLATION = "enable-bridge-conflation";
  /** The name of the <code>enable-subscription-conflation</code> attribute */
  protected static final String ENABLE_SUBSCRIPTION_CONFLATION = "enable-subscription-conflation";
  /** The name of the <code>enable-conflation</code> attribute */
  protected static final String ENABLE_CONFLATION = "enable-conflation";
  /** The name of the <code>batch-conflation</code> attribute */
  protected static final String BATCH_CONFLATION = "batch-conflation";
  protected static final String ENABLE_BATCH_CONFLATION = "enable-batch-conflation";
  /** The name of the <code>enable-conflation</code> attribute */
  protected static final String ENABLE_PERSISTENCE = "enable-persistence";
  
  protected static final String DISPATCHER_THREADS = "dispatcher-threads";
  
  /** The name of the <code>alert-threshold</code> attribute */
  protected static final String ALERT_THRESHOLD = "alert-threshold";
  /** The name of the <code>enable-async-conflation</code> attribute */
  protected static final String ENABLE_ASYNC_CONFLATION = "enable-async-conflation";
  /** The name of the <code>maximum-time-between-pings</code> attribute */
  protected static final String MAXIMUM_TIME_BETWEEN_PINGS = "maximum-time-between-pings";
  /** The name of the <code>maximum-message-count</code> attribute */
  protected static final String MAXIMUM_MESSAGE_COUNT = "maximum-message-count";
  /** The name of the <code>message-time-to-live</code> attribute */
  protected static final String MESSAGE_TIME_TO_LIVE = "message-time-to-live";
  /** The name of the <code>partition-resolver</code> element */
  protected static final String PARTITION_RESOLVER = "partition-resolver";
  /** The name of the <code>partition-listener</code> element */
  protected static final String PARTITION_LISTENER = "partition-listener";
  /** The name of the <code>function-service</code> element */
  protected static final String FUNCTION_SERVICE = "function-service";
  /** The name of the <code>function-name</code> element */
  protected static final String FUNCTION = "function";
  
  /** The name of the top level <code>serialization-registration></code> element */
  protected static final String TOP_SERIALIZER_REGISTRATION = "serialization-registration";
  /** The name of the initializer element */
  protected static final String INITIALIZER = "initializer";
  /** The name of the <code>serializer</code> element */
  protected static final String SERIALIZER_REGISTRATION = "serializer";
  /** The name of the <code>instantiator</code> element */
  protected static final String INSTANTIATOR_REGISTRATION = "instantiator";

  /** The name of the <code>resource-manager</code> element */
  protected static final String RESOURCE_MANAGER = "resource-manager";
  protected static final String BACKUP = "backup";
  /** The name of the <code>critical-heap-percentage</code> attribute of the resource-manager*/
  protected static final String CRITICAL_HEAP_PERCENTAGE = "critical-heap-percentage";
  /** The name of the <code>eviction-heap-percentage</code> attribute of the resource-manager*/
  protected static final String EVICTION_HEAP_PERCENTAGE = "eviction-heap-percentage";
  /** The name of the <code>critical-off-heap-percentage</code> attribute of the resource-manager*/
  protected static final String CRITICAL_OFF_HEAP_PERCENTAGE = "critical-off-heap-percentage";
  /** The name of the <code>eviction-off-heap-percentage</code> attribute of the resource-manager*/
  protected static final String EVICTION_OFF_HEAP_PERCENTAGE = "eviction-off-heap-percentage";
  
  protected static final String ASYNC_EVENT_LISTENER = "async-event-listener";
  public static final String ASYNC_EVENT_QUEUE = "async-event-queue";
  protected static final String ASYNC_EVENT_QUEUE_IDS = "async-event-queue-ids";
  
  protected static final String HDFS_EVENT_QUEUE = "hdfs-event-queue";
  protected static final String HDFS_STORE_NAME = "hdfs-store-name";
  protected static final String HDFS_STORE = "hdfs-store";
  protected static final String HDFS_HOME_DIR = "home-dir";
  protected static final String HDFS_BLOCK_CACHE_SIZE = "block-cache-size";
  protected static final String HDFS_NAMENODE_URL = "namenode-url";
  protected static final String HDFS_CLIENT_CONFIG_FILE = "hdfs-client-config-file";
  protected static final String HDFS_COMPACTION = "hdfs-compaction";
  protected static final String HDFS_COMPACTION_STRATEGY = "compaction-strategy";
  protected static final String HDFS_COMPACTION_SIZE_STRATEGY = "size-oriented";
  public static final String HDFS_COMPACTION_MAX_INPUT_FILE_SIZE_MB = "max-input-file-size-mb";
  public static final String HDFS_COMPACTION_MIN_INPUT_FILE_COUNT = "min-input-file-count";
  public static final String HDFS_COMPACTION_MAX_INPUT_FILE_COUNT = "max-input-file-count";
  public static final String HDFS_COMPACTION_MAX_CONCURRENCY = "max-threads";
  protected static final String HDFS_COMPACTION_ENABLE_COMPACTION = "auto-compaction";
  protected static final String HDFS_COMPACTION_ENABLE_MAJOR_COMPACTION = "auto-major-compaction";
  public static final String HDFS_COMPACTION_MAJOR_COMPACTION_CONCURRENCY = "major-compaction-max-threads";
  public static final String HDFS_COMPACTION_MAJOR_COMPACTION_INTERVAL_MINS = "major-compaction-interval-mins";
  public static final String HDFS_COMPACTION_OLD_FILES_CLEANUP_INTERVAL_MINS = "old-files-cleanup-interval-mins";
  public static final String HDFS_MAX_FILE_SIZE = "max-file-size-mb";
  public static final String HDFS_TIME_FOR_FILE_ROLLOVER = "file-rollover-time-secs";
  
  
  
  protected static final String HDFS_WRITE_ONLY = "hdfs-write-only";

  protected static final String HDFS_QUEUE_BATCH_SIZE = "batch-size-mb";
  
  /** The name of the <code>compressor</code> attribute */
  protected static final String COMPRESSOR = "compressor";
  /** The name of the <code>enable-off-heap-memory</code> attribute */
  protected static final String ENABLE_OFF_HEAP_MEMORY = "enable-off-heap-memory";
 
  protected static final String TCP_NO_DELAY = "tcp-no-delay";
  
  /** the version of the DTD being used by the document being parsed */
  String version;

  ///////////////////// Instance Methods /////////////////////
  /**
   * Given a public id, attempt to resolve it to a DTD. Returns an
   * <code>InputSoure</code> for the DTD.
   */
  public InputSource resolveEntity(String publicId, String systemId)
      throws SAXException {
    if (publicId == null || systemId == null) {
      throw new SAXException(LocalizedStrings.CacheXml_PUBLIC_ID_0_SYSTEM_ID_1.toLocalizedString(new Object[] {publicId, systemId}));
    }
    // Figure out the location for the publicId.
    String location;
    if (SYSTEM_ID_3_0.equals(systemId) || PUBLIC_ID_3_0.equals(publicId)) {
      location = DTD_3_0_LOCATION;
      version = VERSION_3_0;
    }
    else if (SYSTEM_ID_4_0.equals(systemId) || PUBLIC_ID_4_0.equals(publicId)) {
      location = DTD_4_0_LOCATION;
      version = VERSION_4_0;
    }
    else if (SYSTEM_ID_4_1.equals(systemId) || PUBLIC_ID_4_1.equals(publicId)) {
      location = DTD_4_1_LOCATION;
      version = VERSION_4_1;
    }
    else if (SYSTEM_ID_5_0.equals(systemId) || PUBLIC_ID_5_0.equals(publicId)) {
      location = DTD_5_0_LOCATION;
      version = VERSION_5_0;
    }
    else if (SYSTEM_ID_5_1.equals(systemId) || PUBLIC_ID_5_1.equals(publicId)) {
      location = DTD_5_1_LOCATION;
      version = VERSION_5_1;
    }
    else if (SYSTEM_ID_5_5.equals(systemId) || PUBLIC_ID_5_5.equals(publicId)) {
      location = DTD_5_5_LOCATION;
      version = VERSION_5_5;
    }
    else if (SYSTEM_ID_5_7.equals(systemId) || PUBLIC_ID_5_7.equals(publicId)) {
      location = DTD_5_7_LOCATION;
      version = VERSION_5_7;
    }
    else if (SYSTEM_ID_5_8.equals(systemId) || PUBLIC_ID_5_8.equals(publicId)) {
      location = DTD_5_8_LOCATION;
      version = VERSION_5_8;
    }
    else if (SYSTEM_ID_6_0.equals(systemId) || PUBLIC_ID_6_0.equals(publicId)) {
      location = DTD_6_0_LOCATION;
      version = VERSION_6_0;
    }
    else if (SYSTEM_ID_6_1.equals(systemId) || PUBLIC_ID_6_1.equals(publicId)) {
      location = DTD_6_1_LOCATION;
      version = VERSION_6_1;
    }
    else if (SYSTEM_ID_6_5.equals(systemId) || PUBLIC_ID_6_5.equals(publicId)) {
      location = DTD_6_5_LOCATION;
      version = VERSION_6_5;
    }
    else if (SYSTEM_ID_6_6.equals(systemId) || PUBLIC_ID_6_6.equals(publicId)) {
      location = DTD_6_6_LOCATION;
      version = VERSION_6_6;
    }
    else if (SYSTEM_ID_7_0.equals(systemId) || PUBLIC_ID_7_0.equals(publicId)) {
      location = DTD_7_0_LOCATION;
      version = VERSION_7_0;
    }
    else if (SYSTEM_ID_7_5.equals(systemId) || PUBLIC_ID_7_5.equals(publicId)) {
      location = DTD_7_5_LOCATION;
      version = VERSION_7_5;
    }
    else {
      // Instruct the XML parser to open a URI connection to the
      // system id.
      version = VERSION_LATEST; // we won't know the version, so assume the latest
      return null;
    }
    InputSource result;
    InputStream stream = ClassPathLoader.getLatest().getResourceAsStream(getClass(), location);
    if (stream != null) {
      result = new InputSource(stream);
    }
    else {
      throw new SAXNotRecognizedException(LocalizedStrings.CacheXml_DTD_NOT_FOUND_0.toLocalizedString(location));
    }
    return result;
  }

  /**
   * Warnings are ignored
   */
  public void warning(SAXParseException ex) throws SAXException {
  }

  /**
   * Throws a {@link CacheXmlException}
   */
  public void error(SAXParseException ex) throws SAXException {
    throw new CacheXmlException(LocalizedStrings.CacheXml_ERROR_WHILE_PARSING_XML.toLocalizedString(), ex);
  }

  /**
   * Throws a {@link CacheXmlException}
   */
  public void fatalError(SAXParseException ex) throws SAXException {
    throw new CacheXmlException(LocalizedStrings.CacheXml_FATAL_ERROR_WHILE_PARSING_XML.toLocalizedString(), ex);
  }
}
