
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.impl.AssociationEntityCacheEhcacheImpl;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.impl.AssociationEntityCacheInMemoryImpl;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.interfaces.AssociationEntityCache;


public class AssociationEntityCacheFactory {
    
    public static AssociationEntityCache createCache() {
        // return new AssociationEntityCacheInMemoryImpl();
        return new AssociationEntityCacheEhcacheImpl();
    }

}