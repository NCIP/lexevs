package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.impl;

import java.util.Vector;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.interfaces.TripleCache;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util.Triple;

public class TripleCacheInMemImpl implements TripleCache {

    private Vector<Triple> tripleCache;
    
    public TripleCacheInMemImpl() {
        super();
        this.tripleCache = new Vector<Triple>();
    }
        
    public static void main(String[] args) {
        TripleCache cache = new TripleCacheInMemImpl();
        
        Triple[] tripleAr = {
                new Triple("s1", "t1", "rel1"),
                new Triple("s1", "t2", "rel1"),
                new Triple("s2", "t1", "rel1")
        };
        
        cache.add(tripleAr[0]);
        cache.add(tripleAr[1]);
        
        boolean exists = cache.exists(tripleAr[0]);
        System.out.println("TripleCacheInMemImpl: main: does triple " + tripleAr[0] + " exist in cache? " + exists);
        
        exists = cache.exists(tripleAr[2]);
        System.out.println("TripleCacheInMemImpl: main: does triple " + tripleAr[2] + " exist in cache? " + exists);
        
    }

    @Override
    public void add(Triple triple) {
        boolean exists = this.exists(triple);
        if(exists == false) {
           this. tripleCache.add(triple);
        }
    }

    @Override
    public void clear() {
        this.tripleCache.clear();
    }

    @Override
    public void dumpCacheContentsToStdOut() {
        for(int i=0; i<this.tripleCache.size(); ++i) {
            Triple temp = this.tripleCache.elementAt(i);
            System.out.println("tripleCache[" + i + "]=" + temp);
        }
    }

    @Override
    public boolean exists(Triple triple) {
        if(this.tripleCache.size() == 0) {
            return false;
        }
        Triple temp = null;
        boolean done = false;
        boolean found = false;
        int i=0;
        while(!done)
        {
            temp = this.tripleCache.elementAt(i);
            if(triple.equals(temp) == true) {
                done = true;
                found = true;
            }
            ++i;
            if(i == this.tripleCache.size()) {
                done = true;
            }
        }
        
        return found;
    }

}
