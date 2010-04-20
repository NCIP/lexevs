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
package org.LexGrid.LexBIG.Impl.pagedgraph.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.Impl.pagedgraph.builder.AssociationListBuilder.AssociationDirection;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.AssociatedConceptIterator;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;

/**
 * The Class LazyLoadableAssociatedConceptList.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LazyLoadableAssociatedConceptList extends AssociatedConceptList {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -434490124369412627L;
    
    private GraphQuery graphQuery;
    /** The coding scheme uid. */
    private String codingSchemeUri; 
    
    private String codingSchemeVersion;
    
    private String relationsContainerName; 
    
    /** The association predicate uid. */
    private String associationPredicateName; 
    
    /** The entity code. */
    private String entityCode;
    
    /** The entity code namespace. */
    private String entityCodeNamespace;
    
    /** The direction. */
    private AssociationDirection direction;
    
    /** The page size. */
    private int pageSize;
    
    /** The count. */
    private int count;
    
    private int resolveForwardAssociationDepth;
    private int resolveBackwardAssociationDepth;
    
    private int resolveCodedEntryDepth;
    
    private boolean resolveForward;
    private boolean resolveBackward;

    /**
     * Instantiates a new lazy loadable associated concept list.
     * 
     * @param count the count
     * @param codedNodeGraphDao the coded node graph dao
     * @param codingSchemeUid the coding scheme uid
     * @param associationPredicateUid the association predicate uid
     * @param entityCode the entity code
     * @param entityCodeNamespace the entity code namespace
     * @param direction the direction
     * @param pageSize the page size
     */
    public LazyLoadableAssociatedConceptList(
            int count,
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
            AssociationDirection direction,
            int pageSize) {
        super();
        this.count = count;
        this.codingSchemeUri = codingSchemeUri;
        this.codingSchemeVersion = codingSchemeVersion;
        this.associationPredicateName = associationPredicateName;
        this.relationsContainerName = relationsContainerName;
        this.entityCode = entityCode;
        this.entityCodeNamespace = entityCodeNamespace;
        this.resolveForwardAssociationDepth = resolveForwardAssociationDepth;
        this.resolveBackwardAssociationDepth = resolveBackwardAssociationDepth;
        this.resolveCodedEntryDepth = resolveCodedEntryDepth;
        this.graphQuery = graphQuery;
        this.direction = direction;
        this.pageSize = pageSize;
        this.resolveForward = resolveForward;
        this.resolveBackward = resolveBackward;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList#addAssociatedConcept(org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept)
     */
    @Override
	public void addAssociatedConcept(AssociatedConcept vAssociatedConcept)
			throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList#addAssociatedConcept(int, org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept)
	 */
	@Override
	public void addAssociatedConcept(int index,
			AssociatedConcept vAssociatedConcept)
			throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList#enumerateAssociatedConcept()
	 */
	@Override
	public Enumeration<AssociatedConcept> enumerateAssociatedConcept() {
		return new Enumeration<AssociatedConcept>() {

			private Iterator<AssociatedConcept> iterator = iterateAssociatedConcept();
			
			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public AssociatedConcept nextElement() {
				return iterator.next();
			}
		};
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList#getAssociatedConcept()
	 */
	@Override
	public AssociatedConcept[] getAssociatedConcept() {
	    List<AssociatedConcept> list = new ArrayList<AssociatedConcept>();
	    
	    Iterator<AssociatedConcept> itr = iterateAssociatedConcept();
	    
	    while(itr.hasNext()) {
	        list.add(itr.next());
	    }
	    
	    return list.toArray(new AssociatedConcept[list.size()]);
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList#getAssociatedConcept(int)
	 */
	@Override
	public AssociatedConcept getAssociatedConcept(int index)
			throws IndexOutOfBoundsException {
		return getAssociatedConcept()[index];
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList#getAssociatedConceptCount()
	 */
	@Override
	public int getAssociatedConceptCount() {
	    return count;
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList#iterateAssociatedConcept()
	 */
	@Override
	public Iterator<AssociatedConcept> iterateAssociatedConcept() {
	    return new AssociatedConceptIterator(
	            this.codingSchemeUri,
	            this.codingSchemeVersion, 
	            this.relationsContainerName,
	            this.associationPredicateName,
	            this.entityCode,
	            this.entityCodeNamespace,
	            this.resolveForward,
	            this.resolveBackward,
	            this.resolveForwardAssociationDepth,
	            this.resolveBackwardAssociationDepth,
	            this.resolveCodedEntryDepth,
	            this.graphQuery,
	            this.direction,
	            this.pageSize);
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList#removeAllAssociatedConcept()
	 */
	@Override
	public void removeAllAssociatedConcept() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList#removeAssociatedConcept(org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept)
	 */
	@Override
	public boolean removeAssociatedConcept(AssociatedConcept vAssociatedConcept) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList#removeAssociatedConceptAt(int)
	 */
	@Override
	public AssociatedConcept removeAssociatedConceptAt(int index) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList#setAssociatedConcept(org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept[])
	 */
	@Override
	public void setAssociatedConcept(AssociatedConcept[] arg0) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList#setAssociatedConcept(int, org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept)
	 */
	@Override
	public void setAssociatedConcept(int index,
			AssociatedConcept vAssociatedConcept)
			throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}	
}
