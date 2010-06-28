package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.interfaces;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util.Triple;

public interface TripleCache {
    
    void add(Triple triple);
    boolean exists(Triple triple);
    void clear();
    public void dumpCacheContentsToStdOut();

}
