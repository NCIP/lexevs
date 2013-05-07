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