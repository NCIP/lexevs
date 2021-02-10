
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.interfaces;

import java.util.List;

import org.LexGrid.relations.AssociationEntity;

public interface AssociationEntityCache {
    
    void put(AssociationEntity associationEntity);
    AssociationEntity get(String key);
    List<String> getKeys();
    void clear();
    public void dumpCacheContentsToStdOut();
    void destroy();

}