/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.impl;

/*
 * code based on example from: http://www.hsqldb.org/doc/guide/apb.html
 */

import java.util.List;

import org.LexGrid.relations.AssociationEntity;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.interfaces.AssociationEntityCache;

public class AssociationEntityCacheEhcacheImpl implements AssociationEntityCache {

    private Cache theCache;
    private CacheManager theCacheManager;
    
    public AssociationEntityCacheEhcacheImpl() {
        //Create a CacheManager using defaults
        this.theCacheManager = CacheManager.create();

        //Create a Cache specifying its configuration.
        int maxElementsInMemory = 100;
        this.theCache = new Cache(
          new CacheConfiguration("AssociationEntityCacheEhcacheImpl", maxElementsInMemory)
            .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
            .overflowToDisk(true)
            .eternal(true)
            .diskPersistent(false)
            //.diskStorePath(diskStorePath)
            .diskExpiryThreadIntervalSeconds(0));
        try {
            this.theCacheManager .addCache(this.theCache);
        } catch (net.sf.ehcache.ObjectExistsException e) {
            
//            System.out.println("AssociationEntityCacheEhcacheImpl: " + e.getMessage());
//            System.out.println("AssociationEntityCacheEhcacheImpl: remove and re-add the cache");
            this.theCacheManager .removalAll();
            this.theCacheManager .addCache(this.theCache);
        }
//        System.out.println("AssociationEntityCacheEhcacheImpl: DiskStorePath=" + this.theCache.getCacheConfiguration().getDiskStorePath());
//        System.out.println("AssociationEntityCacheEhcacheImpl: debug version 2");
         
        
    }
    
    private String makeKey(AssociationEntity associationEntity) {
        String key = associationEntity.getEntityCode() + associationEntity.getEntityCodeNamespace();
        return key;
    }
    
    @Override
    public void put(AssociationEntity associationEntity) {
        String key = this.makeKey(associationEntity);
        Element element = new Element(key, associationEntity);
        this.theCache.put(element);
    }

    @Override
    public void clear() {
        this.theCache.removeAll();
    }

    @Override
    public void dumpCacheContentsToStdOut() {
        System.out.println("AssociationEntityCacheEhcacheImpl: dumpCacheContentsToStdOut:");
        List<String> keyList = this.theCache.getKeys();
        for(int i=0; i<keyList.size(); ++i) {
            Element e = this.theCache.get(keyList.get(i));
            System.out.println("  element[" + i + "] key:" + e.getKey() + " value:" + e.getValue());
        }
        
    }
    
    @Override
    public AssociationEntity get(String key) {
        Element e = this.theCache.get(key);
        if(e == null) {
            return null;
        }
        AssociationEntity rv = (AssociationEntity)e.getValue();
        return rv;
    }

    @Override
    public List<String> getKeys() {
        List<String> keyList = this.theCache.getKeys();
        return keyList;
    }

    @Override
    public void destroy() {
        this.theCacheManager.shutdown();
    }

}