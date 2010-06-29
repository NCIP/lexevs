package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.impl;

/*
 * code based on example from: http://www.hsqldb.org/doc/guide/apb.html
 */

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.relations.AssociationSource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.interfaces.AssociationSourceCache;

public class AssociationSourceCacheEhcacheImpl implements AssociationSourceCache {

    private Cache theCache;
    
    public AssociationSourceCacheEhcacheImpl() {
        //Create a CacheManager using defaults
        CacheManager manager = CacheManager.create();

        //Create a Cache specifying its configuration.
        int maxElementsInMemory = 10000;
        this.theCache = new Cache(
          new CacheConfiguration("lg60XmlExporterAssocSrcCache", maxElementsInMemory)
            .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
            .overflowToDisk(true)
            .eternal(true)
            .diskPersistent(false)
            //.diskStorePath(diskStorePath)
            .diskExpiryThreadIntervalSeconds(0));
        
        manager.addCache(theCache); 
        
    }
    
    private String makeKey(AssociationSource associationSource) {
        String key = associationSource.getSourceEntityCode() + associationSource.getSourceEntityCodeNamespace();
        return key;
    }
    
    private String makeKey(ResolvedConceptReference rcr) {
        String key = rcr.getCode() + rcr.getCodeNamespace();
        return key;
    }
    
    
    @Override
    public void add(AssociationSource associationSource) {
        String key = this.makeKey(associationSource);
        
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
        System.out.println("AssociationSourceCacheEhcacheImpl: dumpCacheContentsToStdOut:");
        List keyList = this.theCache.getKeys();
        for(int i=0; i<keyList.size(); ++i) {
            Element e = this.theCache.get(keyList.get(i));
            System.out.println("  element[" + i + "] key:" + e.getKey() + " value:" + e.getValue());
        }
        
    }

    @Override
    public boolean exists(ResolvedConceptReference rcr) {
        String key = this.makeKey(rcr);
        Element element = this.theCache.get(key);
        if(element == null) {
            return false;
        } else {
            return true;
        }
    }

}
