
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.impl.AssociationSourceCacheEhcacheImpl;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.impl.AssociationSourceCacheNoopImpl;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.interfaces.AssociationSourceCache;

public class AssociationSourceCacheFactory {
    
    public static AssociationSourceCache createCache() {
        return new AssociationSourceCacheEhcacheImpl();
        // return new AssociationSourceCacheNoopImpl();
    }

}