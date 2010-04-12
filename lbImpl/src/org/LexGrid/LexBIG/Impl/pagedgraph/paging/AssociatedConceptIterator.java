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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.Impl.pagedgraph.builder.AssociationListBuilder.AssociationDirection;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.paging.AbstractPageableIterator;
import org.lexevs.paging.codednodegraph.TripleUidIterator;

/**
 * The Class AssociatedConceptIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociatedConceptIterator extends AbstractPageableIterator<AssociatedConcept> {

	/** The triple uid iterator. */
	private Iterator<String> tripleUidIterator;
	
	/** The coded node graph dao. */
	private CodedNodeGraphDao codedNodeGraphDao;
	
	/** The direction. */
	private AssociationDirection direction;
	
	/** The coding scheme uid. */
	private String codingSchemeUid;
	
	/**
	 * Instantiates a new associated concept iterator.
	 * 
	 * @param codedNodeGraphDao the coded node graph dao
	 * @param codingSchemeUid the coding scheme uid
	 * @param associationPredicateUid the association predicate uid
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param direction the direction
	 * @param pageSize the page size
	 */
	public AssociatedConceptIterator(
	        CodedNodeGraphDao codedNodeGraphDao, 
            String codingSchemeUid, 
            String associationPredicateUid, 
            String entityCode,
            String entityCodeNamespace,
            AssociationDirection direction,
            int pageSize){
		super(pageSize);
		tripleUidIterator = new 
		    TripleUidIterator(
		    		codedNodeGraphDao, 
		    		codingSchemeUid, 
		    		associationPredicateUid, 
		    		entityCode, 
		    		entityCodeNamespace, 
		    		getTripleUidIteratorNode(direction), 
		    		pageSize);
		this.codedNodeGraphDao = codedNodeGraphDao;
		this.direction = direction;
		this.codingSchemeUid = codingSchemeUid;
	}

    /* (non-Javadoc)
     * @see org.lexevs.paging.AbstractPageableIterator#doPage(int, int)
     */
    @Override
    protected List<AssociatedConcept> doPage(int currentPosition, int pageSize) {
        List<AssociatedConcept> returnList = new ArrayList<AssociatedConcept>();
        
        int count = 0;
        while(this.tripleUidIterator.hasNext() && count < pageSize) {
            String tripleUid = tripleUidIterator.next();
            returnList.add(
                    this.codedNodeGraphDao.
                        getAssociatedConceptFromUid(
                                codingSchemeUid, 
                                tripleUid, 
                                getAssociatedConceptNode(direction)));
            count++;
        }
        
        return returnList;
    }
	
	/**
	 * Gets the triple uid iterator node.
	 * 
	 * @param direction the direction
	 * 
	 * @return the triple uid iterator node
	 */
	protected TripleNode getTripleUidIteratorNode(AssociationDirection direction) {
	    if(direction.equals(AssociationDirection.SOURCE_OF)) {
            return TripleNode.SUBJECT;
        }
        
        if(direction.equals(AssociationDirection.TARGET_OF)) {
            return TripleNode.OBJECT;
        }
        
        throw new RuntimeException("Could not assign Triple Node.");
    }
	
	/**
	 * Gets the associated concept node.
	 * 
	 * @param direction the direction
	 * 
	 * @return the associated concept node
	 */
	protected TripleNode getAssociatedConceptNode(AssociationDirection direction) {
        if(direction.equals(AssociationDirection.SOURCE_OF)) {
            return TripleNode.OBJECT;
        }
        
        if(direction.equals(AssociationDirection.TARGET_OF)) {
            return TripleNode.SUBJECT;
        }
        
        throw new RuntimeException("Could not assign Triple Node.");
    }
}
