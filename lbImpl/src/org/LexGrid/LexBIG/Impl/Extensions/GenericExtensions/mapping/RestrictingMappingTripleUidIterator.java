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
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.paging.AbstractRefereshingPageableIterator;

/**
 * The Class RestrictingMappingTripleUidIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RestrictingMappingTripleUidIterator extends AbstractRefereshingPageableIterator<ResolvedConceptReferencesIterator,String> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5709428653655124881L;

    /** The uri. */
    private String uri;
    
    /** The version. */
    private String version;
    
    /** The relations container name. */
    private String relationsContainerName;
    
    /** The resolved concept references iterator. */
    private ResolvedConceptReferencesIterator resolvedConceptReferencesIterator;

     
    /**
     * Instantiates a new restricting mapping triple uid iterator.
     */
    public RestrictingMappingTripleUidIterator() {
        super();
    }
    
    /**
     * Instantiates a new restricting mapping triple uid iterator.
     * 
     * @param uri the uri
     * @param version the version
     * @param relationsContainerName the relations container name
     * @param codedNodeSet the coded node set
     * 
     * @throws LBException the LB exception
     */
    public RestrictingMappingTripleUidIterator(
            String uri, 
            String version,
            String relationsContainerName, 
            CodedNodeSet codedNodeSet) throws LBException {
      
        this.uri = uri;
        this.version = version;
        this.relationsContainerName = relationsContainerName;
        
        this.resolvedConceptReferencesIterator = codedNodeSet.resolve(null, null, null, null, false);
    } 
  
    /* (non-Javadoc)
     * @see org.lexevs.paging.AbstractPageableIterator#doPage(int, int)
     */
    @Override
    protected List<? extends String> doPage(int currentPosition, int pageSize) {
        try {
            if(!resolvedConceptReferencesIterator.hasNext()){
                return null;
            }
        } catch (LBResourceUnavailableException e) {
            throw new RuntimeException(e);
        }

        return LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService().
            getTripleUidsForMappingRelationsContainerForCodes(
                    uri, 
                    version, 
                    relationsContainerName, 
                    getConceptReferencesForPage(pageSize));
    }
    
    /**
     * Gets the concept references for page.
     * 
     * @param pageSize the page size
     * 
     * @return the concept references for page
     */
    private List<ConceptReference> getConceptReferencesForPage(int pageSize){
        try {
            ResolvedConceptReferenceList list = resolvedConceptReferencesIterator.next(pageSize);
            return DaoUtility.createList(ConceptReference.class, list.getResolvedConceptReference());
        } catch (Exception e) {
           throw new RuntimeException(e);
        } 
    }

    /* (non-Javadoc)
     * @see org.lexevs.paging.AbstractRefereshingPageableIterator#doGetRefresh()
     */
    @Override
    protected ResolvedConceptReferencesIterator doGetRefresh() {
        return this.resolvedConceptReferencesIterator;
    }

    /* (non-Javadoc)
     * @see org.lexevs.paging.AbstractRefereshingPageableIterator#doRefresh(java.lang.Object)
     */
    @Override
    protected void doRefresh(ResolvedConceptReferencesIterator refresh) {
        this.resolvedConceptReferencesIterator = refresh;
    } 
}