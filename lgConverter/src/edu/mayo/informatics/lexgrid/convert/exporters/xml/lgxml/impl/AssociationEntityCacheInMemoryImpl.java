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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.LexGrid.relations.AssociationEntity;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.interfaces.AssociationEntityCache;

public class AssociationEntityCacheInMemoryImpl implements AssociationEntityCache {

    private Hashtable<String, AssociationEntity> theCache;
    
    public AssociationEntityCacheInMemoryImpl() {
        theCache = new Hashtable<String, AssociationEntity>();
        
        System.out.println("AssociationEntityCacheInMemoryImpl: debug version 1");
         
        
    }
    
    private String makeKey(AssociationEntity associationEntity) {
        String key = associationEntity.getEntityCode() + associationEntity.getEntityCodeNamespace();
        return key;
    }
    
    @Override
    public void put(AssociationEntity associationEntity) {
        String key = this.makeKey(associationEntity);
        if(this.theCache.containsKey(key) == false) {
            this.theCache.put(key, associationEntity);
        } else {
            System.out.println("AssociationEntityCacheInMemoryImpl: put: key:" + key + " already exists. Object will not be added to cache.");
        }
    }

    @Override
    public void clear() {
        this.theCache.clear();
    }

    @Override
    public void dumpCacheContentsToStdOut() {
        System.out.println("AssociationSourceCacheEhcacheImpl: dumpCacheContentsToStdOut:");
        Enumeration<String> keyList = this.theCache.keys();
        String key;
        AssociationEntity aE;
        while(keyList.hasMoreElements() == true) {
            key = keyList.nextElement();
            aE = this.theCache.get(key);
            System.out.println("key=" + key);
            System.out.println("value=" + aE.toString());
        }
    }

    @Override
    public List<String> getKeys() {
        Enumeration<String> keyList = this.theCache.keys();
        List<String> rv = new ArrayList<String>();
        String key;
        while(keyList.hasMoreElements() == true) {
            key = keyList.nextElement();
            rv.add(key);
        }
        
        return rv;
    }

    @Override
    public AssociationEntity get(String key) {
        AssociationEntity rv;
        rv = this.theCache.get(key);
        return rv;
    }

    @Override
    public void destroy() {
        theCache = null;
        
    }

}