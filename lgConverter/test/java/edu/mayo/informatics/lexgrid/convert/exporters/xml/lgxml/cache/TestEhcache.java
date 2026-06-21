
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.cache;

import junit.framework.Assert;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.junit.Test;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

public class TestEhcache {
    
    public TestEhcache() {
        super();
    }
    
    @Test
    public void test1() {
        //Create a CacheManager using defaults
        CacheManager manager = CacheManager.create();

        //Create a Cache specifying its configuration.
        int maxElementsInMemory = 2;
        Cache testCache = new Cache(
          new CacheConfiguration("testCache", maxElementsInMemory)
            .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
            .overflowToDisk(true)
            .eternal(true)
            .diskPersistent(false)
            //.diskStorePath(diskStorePath)
            .diskExpiryThreadIntervalSeconds(0));
        
        manager.addCache(testCache); 
        
        Cache cache = manager.getCache("testCache");
        Assert.assertEquals(0, cache.getSize());
        System.out.println("Test Ehcache: cache size=" + cache.getSize());

        
        //--------------------------------------------------------
        // add data to cache
        //--------------------------------------------------------
        Element element = new Element("key1", "value1");
        System.out.println("TestEhcache: add element \"" + element.getKey() + "\" " + element.getValue() + " to empty cache...");
        cache.put(element);        
        System.out.println("TestEhcache: cache size=" + cache.getSize());
        Assert.assertEquals(1, cache.getSize());
        
        // add duplicate data
        System.out.println("TestEhcache: adding duplicate element...");
        cache.put(new Element("key1", "value1"));
        System.out.println("TestEhcache: cache size=" + cache.getSize());
        Assert.assertEquals(1, cache.getSize());
        

        //--------------------------------------------------------
        // get data from cache
        //--------------------------------------------------------        
        // hit=true
        element = cache.get("key1");
        String value = (String)element.getObjectValue();        
        System.out.println("TestEhcache: value of element with key1=" + value);
        Assert.assertTrue(value.equals("value1"));
        
        // hit=false
        element = cache.get("key2");
        if(element == null) {
            System.out.println("TestEhcache: an element with key value \"key2\" does not exist");
        } else {
            value = (String)element.getObjectValue();
            System.out.println("TestEhcache: value of element with key2=" + value);            
        }
        Assert.assertNull(element);
        
        //--------------------------------------------------------
        // test disk
        //--------------------------------------------------------
        System.out.println("TestEhcache: test disk...");
        String valuePrefix = "value";
        String keyPrefix = "key";
        String tempValue = null;
        String tempKey = null;
        for(int i=0; i<20; ++i) {
            tempKey = keyPrefix + i;
            tempValue = valuePrefix + i;
            System.out.println("TestEhcache: adding to cahce: \"" + tempKey + "\" \""  +  tempValue + "\"");
            cache.put(new Element(tempKey, tempValue));
            System.out.println("TestEhcache: cache size=" + cache.getSize());
        }
        
        Assert.assertEquals(20, cache.getSize());
        
        // System.out.println(cache.toString());
        cache.flush();
        
        //System.out.println("TestEhcache: DiskStorePath=" + cache.getCacheConfiguration().dis.getDiskStorePath());
        
        element = cache.get("key18");
        value = (String)element.getObjectValue();        
        System.out.println("TestEhcache: value of element with key18=" + value);
        Assert.assertTrue(value.equals("value18"));
    }
    
    @Test
    public void test2() {
        //Create a CacheManager using defaults
        CacheManager manager = CacheManager.create();

        //Create a Cache specifying its configuration.
        int maxElementsInMemory = 2;
        Cache testCache = new Cache(
          new CacheConfiguration("testCache2", maxElementsInMemory)
            .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
            .overflowToDisk(true)
            .eternal(true)
            .diskPersistent(false)
            //.diskStorePath(diskStorePath)
            .diskExpiryThreadIntervalSeconds(0));
        
        manager.addCache(testCache); 
        
        Cache cache = manager.getCache("testCache2");
        System.out.println("Test Ehcache: cache size=" + cache.getSize());
        Assert.assertEquals(0, cache.getSize());
        
        //--------------------------------------------------------
        // get data from cache
        //--------------------------------------------------------
        AssociationTarget aT = new AssociationTarget();
        aT.setTargetEntityCode("b");
        aT.setTargetEntityCodeNamespace("ns1");
        AssociationSource aS = new AssociationSource();
        aS.addTarget(aT);
        aS.setSourceEntityCode("a");
        aS.setSourceEntityCodeNamespace("ns1");
        
        String key = aS.getSourceEntityCode() + aS.getSourceEntityCodeNamespace();
        
        Element element = new Element(key, aS);
        System.out.println("TestEhcache: add element with key: " + element.getKey() + " and value: " + element.getValue() + " to empty cache...");
        cache.put(element);
        System.out.println("TestEhcache: cache size=" + cache.getSize());
        Assert.assertEquals(1, cache.getSize());
        
        element = cache.get(key);
        Assert.assertNotNull(element);
    }

    
    
    
    
    
    public static void main(String[] args) {
        TestEhcache tester = new TestEhcache();
        tester.test1();
        tester.test2();
    }

}