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
package org.LexGrid.LexBIG.Impl.pagedgraph.paging;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.NullFocusRootsResolver;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.RootsResolver;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.RootsResolver.ResolveDirection;
import org.LexGrid.annotations.LgProxyClass;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.paging.AbstractPageableIterator;

/**
 * The Class AssociatedConceptIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@LgProxyClass
public class RootConceptReferenceIterator extends AbstractPageableIterator<ConceptReference> {

    private RootsResolver rootsResolver = new NullFocusRootsResolver();
    
    private String codingSchemeUri;
    private String version;
    private String containerName;
    private ResolveDirection direction;
    private GraphQuery graphQuery;
    
    public RootConceptReferenceIterator(
            String codingSchemeUri,
            String version,
            String containerName,
            ResolveDirection direction,
            GraphQuery graphQuery) {
        this.codingSchemeUri = codingSchemeUri;
        this.version = version;
        this.containerName = containerName;
        this.direction = direction;
        this.graphQuery = graphQuery;
    }
    
    @Override
    protected List<? extends ConceptReference> doPage(int currentPosition, int pageSize) {
        return  
            rootsResolver.resolveRoots(
                    codingSchemeUri, 
                    version,
                    containerName,
                    direction, 
                    graphQuery, 
                    currentPosition, 
                    pageSize);
    }
    
   
}
