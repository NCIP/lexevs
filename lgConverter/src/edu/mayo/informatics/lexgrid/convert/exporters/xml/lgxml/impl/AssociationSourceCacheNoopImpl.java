
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.impl;

/*
 * code based on example from: http://www.hsqldb.org/doc/guide/apb.html
 */

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.relations.AssociationSource;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.interfaces.AssociationSourceCache;

public class AssociationSourceCacheNoopImpl implements AssociationSourceCache {

    public AssociationSourceCacheNoopImpl() {
        super();
        System.out.println("AssociationSourceCacheNoopImpl: debug version 3");
    }
    
    @Override
    public void add(AssociationSource associationSource) {
        //
    }

    @Override
    public void clear() {
        //
    }

    @Override
    public void dumpCacheContentsToStdOut() {
        //
    }

    @Override
    public boolean exists(ResolvedConceptReference rcr) {
        return false;
    }

    @Override
    public void destroy() {
        // 
        
    }

    @Override
    public void add(ResolvedConceptReference associationSource) {
        //
    }

}