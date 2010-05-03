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
import org.LexGrid.LexBIG.Impl.pagedgraph.builder.AssociationListBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.builder.AssociationListBuilder.AssociationDirection;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallback;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.paging.AbstractPageableIterator;
import org.lexevs.paging.codednodegraph.TripleUidIterator;

/**
 * The Class AssociatedConceptIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociatedConceptIterator extends AbstractPageableIterator<AssociatedConcept> {
    
    /** The association list builder. */
    private AssociationListBuilder associationListBuilder = new AssociationListBuilder();

	/** The triple uid iterator. */
	private Iterator<String> tripleUidIterator;
	
	/** The direction. */
	private AssociationDirection direction;
	
	/** The coding scheme uid. */
	private String codingSchemeUri;
	
	/** The coding scheme version. */
	private String codingSchemeVersion;
	
	/** The resolve forward association depth. */
	private int resolveForwardAssociationDepth;
	
	/** The resolve backward association depth. */
	private int resolveBackwardAssociationDepth;
	
    /** The resolve coded entry depth. */
    private int resolveCodedEntryDepth;
    
    /** The relations container name. */
    private String relationsContainerName;
    
    /** The graph query. */
    private GraphQuery graphQuery;
    
    /** The resolve forward. */
    private boolean resolveForward;
    
    /** The resolve backward. */
    private boolean resolveBackward;
    
    /** The cycle detecting callback. */
    private CycleDetectingCallback cycleDetectingCallback;
    
    /** The association predicate name. */
    private String associationPredicateName;
	
	/**
	 * Instantiates a new associated concept iterator.
	 * 
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param direction the direction
	 * @param pageSize the page size
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param relationsContainerName the relations container name
	 * @param associationPredicateName the association predicate name
	 * @param resolveForward the resolve forward
	 * @param resolveBackward the resolve backward
	 * @param resolveForwardAssociationDepth the resolve forward association depth
	 * @param resolveBackwardAssociationDepth the resolve backward association depth
	 * @param resolveCodedEntryDepth the resolve coded entry depth
	 * @param graphQuery the graph query
	 * @param cycleDetectingCallback the cycle detecting callback
	 */
	public AssociatedConceptIterator(
            String codingSchemeUri, 
            String codingSchemeVersion, 
            String relationsContainerName,
            String associationPredicateName, 
            String entityCode,
            String entityCodeNamespace,
            boolean resolveForward,
            boolean resolveBackward,
            int resolveForwardAssociationDepth,
            int resolveBackwardAssociationDepth,
            int resolveCodedEntryDepth,
            GraphQuery graphQuery,
            CycleDetectingCallback cycleDetectingCallback,
            AssociationDirection direction,
            int pageSize){
		super(pageSize);
		tripleUidIterator = new 
		    TripleUidIterator(
		    		codingSchemeUri, 
		    		codingSchemeVersion, 
		    		relationsContainerName,
		            associationPredicateName, 
		            entityCode, 
	                entityCodeNamespace, 
		    		graphQuery,
		    		getTripleUidIteratorNode(direction), 
		    		pageSize);
		this.codingSchemeUri = codingSchemeUri;
		this.codingSchemeVersion = codingSchemeVersion;
		this.relationsContainerName = relationsContainerName;
		this.direction = direction;
		this.resolveForwardAssociationDepth = resolveForwardAssociationDepth;
		this.resolveBackwardAssociationDepth = resolveBackwardAssociationDepth;
		this.resolveCodedEntryDepth = resolveCodedEntryDepth;
		this.resolveForward = resolveForward;
		this.resolveBackward = resolveBackward;
		this.graphQuery = graphQuery;
		this.cycleDetectingCallback = cycleDetectingCallback;
		this.associationPredicateName = associationPredicateName;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.paging.AbstractPageableIterator#next()
	 */
	@Override
	public AssociatedConcept next() {
	    AssociatedConcept associatedConcept = super.next();

	    if(this.cycleDetectingCallback.isAssociatedConceptAlreadyInGraph(associationPredicateName, associatedConcept)) {
	        return cycleDetectingCallback.getAssociatedConceptInGraph(associationPredicateName, associatedConcept);
	    }

	    if(shouldResolveNextLevel(resolveForwardAssociationDepth)) {
	        if(resolveForward) {
	            associatedConcept.setSourceOf(
	                    associationListBuilder.buildSourceOfAssociationList(
	                            codingSchemeUri, 
	                            codingSchemeVersion, 
	                            associatedConcept.getCode(), 
	                            associatedConcept.getCodeNamespace(), 
	                            relationsContainerName, 
	                            resolveForward,
	                            resolveBackward,
	                            resolveForwardAssociationDepth - 1,
	                            resolveBackwardAssociationDepth, 
	                            resolveCodedEntryDepth - 1, 
	                            graphQuery,
	                            cycleDetectingCallback));
	        }
	    }

	    if(shouldResolveNextLevel(resolveBackwardAssociationDepth)) {
	        if(resolveBackward) {
	            associatedConcept.setTargetOf(
	                    associationListBuilder.buildTargetOfAssociationList(
	                            codingSchemeUri, 
	                            codingSchemeVersion, 
	                            associatedConcept.getCode(), 
	                            associatedConcept.getCodeNamespace(), 
	                            relationsContainerName, 
	                            resolveForward,
	                            resolveBackward,
	                            resolveForwardAssociationDepth,
	                            resolveBackwardAssociationDepth - 1, 
	                            resolveCodedEntryDepth - 1, 
	                            graphQuery,
	                            cycleDetectingCallback));
	        }
	    }

	    cycleDetectingCallback.addAssociatedConceptToGraph(associationPredicateName, associatedConcept);
	    
	    return associatedConcept;
	}
	
	/**
	 * Should resolve next level.
	 * 
	 * @param depth the depth
	 * 
	 * @return true, if successful
	 */
	private boolean shouldResolveNextLevel(int depth) {
	    return ! (depth == 0);
	}

    /* (non-Javadoc)
     * @see org.lexevs.paging.AbstractPageableIterator#doPage(int, int)
     */
    @Override
    protected List<AssociatedConcept> doPage(int currentPosition, int pageSize) {
        CodedNodeGraphService codedNodeGraphSerivce =
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        List<AssociatedConcept> returnList = new ArrayList<AssociatedConcept>();
        
        List<String> uids = new ArrayList<String>();
        
        int count = 0;
        while(this.tripleUidIterator.hasNext() && count < pageSize) {
            String tripleUid = tripleUidIterator.next();
            uids.add(tripleUid);
            count++;
        }
        
        if(direction.equals(AssociationDirection.SOURCE_OF)) {
            returnList.addAll(
                    codedNodeGraphSerivce.
                    getAssociatedConceptsFromUidTarget(
                            codingSchemeUri, 
                            codingSchemeVersion,
                            uids));
        } else {
            returnList.addAll(
                    codedNodeGraphSerivce.
                    getAssociatedConceptsFromUidSource(
                            codingSchemeUri, 
                            codingSchemeVersion,
                            uids));
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
