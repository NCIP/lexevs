package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.impl;

/*
 * code based on example from: http://www.hsqldb.org/doc/guide/apb.html
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.interfaces.TripleCache;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util.Triple;

public class TripleCacheEhcacheImpl implements TripleCache {

    private Cache theCache;
    
    public TripleCacheEhcacheImpl() {
        //Create a CacheManager using defaults
        CacheManager manager = CacheManager.create();

        //Create a Cache specifying its configuration.
        int maxElementsInMemory = 10000;
        this.theCache = new Cache(
          new CacheConfiguration("lg60XmlExporterTripleCache", maxElementsInMemory)
            .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
            .overflowToDisk(true)
            .eternal(true)
            .diskPersistent(false)
            //.diskStorePath(diskStorePath)
            .diskExpiryThreadIntervalSeconds(0));
        
        manager.addCache(theCache); 
        
    }
    
    private String makeKeyFromTriple(Triple triple) {
        String rv = triple.getSourceEntityCode() + triple.getTargetEntityCode() + triple.getAssociationName();
        return rv;
    }
    
    @Override
    public void add(Triple triple) {
        String key = this.makeKeyFromTriple(triple);
        
        // note, we don't really care about the values in the cache, just the keys.
        Element element = new Element(key, "<blank>");
        this.theCache.put(element);
    }

    @Override
    public void clear() {
        this.theCache.removeAll();
    }

    @Override
    public void dumpCacheContentsToStdOut() {
        System.out.println("TripleCacheEhcacheImpl: dumpCacheContentsToStdOut:");
        List keyList = this.theCache.getKeys();
        for(int i=0; i<keyList.size(); ++i) {
            Element e = this.theCache.get(keyList.get(i));
            System.out.println("  element[" + i + "] key:" + e.getKey() + " value:" + e.getValue());
        }
        
    }

    @Override
    public boolean exists(Triple triple) {
        String key = this.makeKeyFromTriple(triple);
        Element element = this.theCache.get(key);
        if(element == null) {
            return false;
        } else {
            return true;
        }
    }

}
