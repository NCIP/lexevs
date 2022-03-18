
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.interfaces;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.relations.AssociationSource;

public interface AssociationSourceCache {
    
    void add(AssociationSource associationSource);
    void add(ResolvedConceptReference associationSource);
    boolean exists(ResolvedConceptReference rcr);
    void clear();
    public void dumpCacheContentsToStdOut();
    void destroy();

}