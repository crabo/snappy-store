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
package com.gemstone.gemfire.pdx;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import io.snappydata.test.dunit.AvailablePortHelper;
import junit.framework.TestCase;

import com.gemstone.gemfire.DataSerializer;
import com.gemstone.gemfire.ToDataException;
import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.DataPolicy;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionShortcut;
import com.gemstone.gemfire.cache.client.PoolManager;
import com.gemstone.gemfire.internal.FileUtil;
import com.gemstone.gemfire.internal.HeapDataOutputStream;
import com.gemstone.gemfire.internal.cache.GemFireCacheImpl;
import com.gemstone.gemfire.internal.shared.Version;
import com.gemstone.gemfire.pdx.internal.PeerTypeRegistration;

/**
 * @author dsmith
 *
 */
public class PdxAttributesJUnitTest extends TestCase {
  
  private File diskDir;

  public void setUp() {
    diskDir = new File("PdxAttributesJUnitTest");
    GemFireCacheImpl.setDefaultDiskStoreName("PDXAttributesDefault");
    diskDir.mkdirs();
  }
  
  public void tearDown() throws Exception {
    GemFireCacheImpl instance = GemFireCacheImpl.getInstance();
    if(instance != null) {
      instance.close();
    }
    FileUtil.delete(diskDir);
    File[] defaultStoreFiles = new File(".").listFiles(new FilenameFilter() {
      
      public boolean accept(File dir, String name) {
        return name.startsWith("BACKUPPDXAttributes");
      }
    });
    
    for(File file: defaultStoreFiles) {
      FileUtil.delete(file);
    }
  }
  
  public void testPdxPersistent() throws Exception {
    {
      CacheFactory cf = new CacheFactory();
      cf.set("mcast-port", "0");
      Cache cache = cf.create();

      //define a type
      defineAType();
      Region pdxRegion = cache.getRegion(PeerTypeRegistration.REGION_NAME);
      assertEquals(DataPolicy.REPLICATE, pdxRegion.getAttributes().getDataPolicy());
    }
    
    tearDown();
    setUp();

    {
      CacheFactory cf = new CacheFactory();
      cf.set("mcast-port", "0");
      cf.setPdxPersistent(true);
      Cache cache = cf.create();

      //define a type
      defineAType();
      Region pdxRegion = cache.getRegion(PeerTypeRegistration.REGION_NAME);
      assertEquals(DataPolicy.PERSISTENT_REPLICATE, pdxRegion.getAttributes().getDataPolicy());
      cache.close();
    }
  }
  
  public void testPdxDiskStore() throws Exception {
    {
      CacheFactory cf = new CacheFactory();
      cf.set("mcast-port", "0");
      cf.setPdxPersistent(true);
      cf.setPdxDiskStore("diskstore1");
      Cache cache = cf.create();
      cache.createDiskStoreFactory().setDiskDirs(new File[] {diskDir}).setMaxOplogSize(1).create("diskstore1");

      //define a type.
      defineAType();
      Region pdxRegion = cache.getRegion(PeerTypeRegistration.REGION_NAME);
      assertEquals("diskstore1", pdxRegion.getAttributes().getDiskStoreName());
      cache.close();
    }

    tearDown();
    setUp();

    {
      CacheFactory cf = new CacheFactory();
      cf.set("mcast-port", "0");
      cf.setPdxPersistent(true);
      Cache cache = cf.create();

      //define a type
      defineAType();
      Region pdxRegion = cache.getRegion(PeerTypeRegistration.REGION_NAME);
      assertEquals(DataPolicy.PERSISTENT_REPLICATE, pdxRegion.getAttributes().getDataPolicy());
      cache.close();
    }
  }
  
  public void testNonPersistentRegistryWithOverflowRegion() throws Exception {
    {
      CacheFactory cf = new CacheFactory();
      cf.set("mcast-port", "0");
      Cache cache = cf.create();
      cache.createDiskStoreFactory().setDiskDirs(new File[] {diskDir}).setMaxOplogSize(1).create("diskstore1");
      cache.createRegionFactory(RegionShortcut.LOCAL_OVERFLOW).setDiskStoreName("diskstore1").create("region");
      defineAType();
    }
    tearDown();
    setUp();
    
    {
      CacheFactory cf = new CacheFactory();
      cf.set("mcast-port", "0");
      Cache cache = cf.create();
      defineAType();
      cache.createDiskStoreFactory().setDiskDirs(new File[] {diskDir}).setMaxOplogSize(1).create("diskstore1");
      cache.createRegionFactory(RegionShortcut.LOCAL_OVERFLOW).setDiskStoreName("diskstore1").create("region");
    }
  }
  
  public void testNonPersistentRegistryWithPersistentRegion() throws Exception {
    {
      CacheFactory cf = new CacheFactory();
      cf.set("mcast-port", "0");
      Cache cache = cf.create();
      cache.createDiskStoreFactory().setDiskDirs(new File[] {diskDir}).setMaxOplogSize(1).create("diskstore1");
      cache.createRegionFactory(RegionShortcut.LOCAL_PERSISTENT).setDiskStoreName("diskstore1").create("region");

      try {
        defineATypeNoEnum();
        fail("Should have received an exception");
      } catch(PdxInitializationException expected) {

      }
    }
    tearDown();
    setUp();
    
    {
      CacheFactory cf = new CacheFactory();
      cf.set("mcast-port", "0");
      Cache cache = cf.create();
      defineATypeNoEnum();
      cache.createDiskStoreFactory().setDiskDirs(new File[] {diskDir}).setMaxOplogSize(1).create("diskstore1");
      try {
        cache.createRegionFactory(RegionShortcut.LOCAL_PERSISTENT).setDiskStoreName("diskStore1").create("region");
        fail("Should have received an exception");
      } catch(PdxInitializationException expected) {
        
      }

    }
  }
  
  public void testNonPersistentRegistryWithGateway() throws Exception {
    {
      CacheFactory cf = new CacheFactory();
      cf.set("mcast-port", "0");
      Cache cache = cf.create();
      int port = AvailablePortHelper.getRandomAvailableTCPPort();
      cache.addGatewayHub("hubid", port).start();

      try {
        defineATypeNoEnum();
        fail("Should have received an exception");
      } catch(PdxInitializationException expected) {

      }
    }
    tearDown();
    setUp();
    
    {
      CacheFactory cf = new CacheFactory();
      cf.set("mcast-port", "0");
      Cache cache = cf.create();
      int port = AvailablePortHelper.getRandomAvailableTCPPort();
      defineATypeNoEnum();
      try {
        cache.addGatewayHub("hubid", port).start();
        fail("Should have received an exception");
      } catch(PdxInitializationException expected) {
        
      }

    }
  }
  
  /**
   * Test that loner VMs lazily determine if they
   * are a client or a peer.
   * @throws Exception
   */
  public void testLazyLoner() throws Exception {
    //Test that we can become a peer registry
    {
      CacheFactory cf = new CacheFactory();
      cf.set("mcast-port", "0");
      Cache cache = cf.create();
      //This should work, because this is a peer.
      defineAType();
    }
    tearDown();
    setUp();
    
    //Test that we can become a client registry.
    {
      CacheFactory cf = new CacheFactory();
      cf.set("mcast-port", "0");
      Cache cache = cf.create();
      int port = AvailablePortHelper.getRandomAvailableTCPPort();
      PoolManager.createFactory().addServer("localhost", port).create("pool");
      
      
      try {
        defineAType();
        fail("Should have failed, this is a client that can't connect to a server");
      } catch(ToDataException expected) {
        //do nothing.
      }
    }
  }

  private void defineAType() throws IOException {
    SimpleClass sc = new SimpleClass(1, (byte) 2);
    HeapDataOutputStream out = new HeapDataOutputStream(Version.CURRENT);
    DataSerializer.writeObject(sc, out);
  }
  private void defineATypeNoEnum() throws IOException {
    SimpleClass sc = new SimpleClass(1, (byte) 2, null);
    HeapDataOutputStream out = new HeapDataOutputStream(Version.CURRENT);
    DataSerializer.writeObject(sc, out);
  }

}
