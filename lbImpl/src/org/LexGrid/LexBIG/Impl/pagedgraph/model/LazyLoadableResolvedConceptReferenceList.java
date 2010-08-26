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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.RootConceptReferenceIterator;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallback;
import org.LexGrid.LexBIG.Impl.pagedgraph.utility.PagedGraphUtils;
import org.LexGrid.LexBIG.Impl.pagedgraph.utility.ValidatedParameterResolvingCallback;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.annotations.LgProxyClass;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.paging.AbstractPageableIterator;

/**
 * The Class LazyLoadableAssociatedConceptList.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@LgProxyClass
public class LazyLoadableResolvedConceptReferenceList extends ResolvedConceptReferenceList {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -434490124369412627L;
    
    /** The graph query. */
    private GraphQuery graphQuery;
    
    /** The coding scheme uid. */
    private String codingSchemeUri; 
    
    /** The coding scheme version. */
    private String codingSchemeVersion;
    
    /** The relations container name. */
    private String relationsContainerName; 
    
    /** The page size. */
    private int maxToReturn;
    
    /** The resolve forward association depth. */
    private int resolveAssociationDepth;

    
    /** The resolve coded entry depth. */
    private int resolveCodedEntryDepth;
    
    /** The resolve forward. */
    private boolean resolveForward;
    
    /** The resolve backward. */
    private boolean resolveBackward;
    
    /** The cycle detecting callback. */
    private CycleDetectingCallback cycleDetectingCallback;
    
    private SortOptionList sortAlgorithms;
    
    private LocalNameList propertyNames;
    
    private LocalNameList filterOptions;
    
    private PropertyType[] propertyTypes;
    
    private boolean keepLastAssociationLevelUnresolved;
    
    private ValidatedParameterResolvingCallback validatedParameterResolvingCallback;

    private ResolvedConceptReference[] cache;
    
    public LazyLoadableResolvedConceptReferenceList() {
        super();
    }
    /**
     * Instantiates a new lazy loadable associated concept list.
     * 
     * @param count the count
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
     * @param filterOptions 
     * @param cycleDetectingCallback the cycle detecting callback
     */
    public LazyLoadableResolvedConceptReferenceList(
            ValidatedParameterResolvingCallback validatedParameterResolvingCallback,
            String codingSchemeUri, 
            String codingSchemeVersion, 
            String relationsContainerName,
            boolean resolveForward,
            boolean resolveBackward,
            int resolveAssociationDepth,
            int resolveCodedEntryDepth,
            boolean keepLastAssociationLevelUnresolved,
            GraphQuery graphQuery,
            LocalNameList propertyNames, 
            PropertyType[] propertyTypes, 
            SortOptionList sortAlgorithms,
            LocalNameList filterOptions, 
            CycleDetectingCallback cycleDetectingCallback,
            int maxToReturn) {
        super();
        this.codingSchemeUri = codingSchemeUri;
        this.codingSchemeVersion = codingSchemeVersion;
        this.relationsContainerName = relationsContainerName;
        this.resolveAssociationDepth = resolveAssociationDepth;
        this.resolveCodedEntryDepth = resolveCodedEntryDepth;
        this.graphQuery = graphQuery;
        this.resolveForward = resolveForward;
        this.resolveBackward = resolveBackward;
        this.cycleDetectingCallback = cycleDetectingCallback;
        this.sortAlgorithms = sortAlgorithms;
        this.propertyTypes = propertyTypes;
        this.propertyNames = propertyNames;
        this.filterOptions = filterOptions;
        this.maxToReturn = maxToReturn;
        this.keepLastAssociationLevelUnresolved = keepLastAssociationLevelUnresolved;
        this.validatedParameterResolvingCallback = validatedParameterResolvingCallback;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList#addResolvedConceptReference(org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference)
     */
    @Override
	public void addResolvedConceptReference(ResolvedConceptReference vResolvedConceptReference)
			throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList#addResolvedConceptReference(int, org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference)
	 */
	@Override
	public void addResolvedConceptReference(int index,
			ResolvedConceptReference vResolvedConceptReference)
			throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList#enumerateResolvedConceptReference()
	 */
	@Override
	public Enumeration<ResolvedConceptReference> enumerateResolvedConceptReference() {
		return new Enumeration<ResolvedConceptReference>() {

			private Iterator<ResolvedConceptReference> iterator = iterateResolvedConceptReference();
			
			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public ResolvedConceptReference nextElement() {
				return iterator.next();
			}
		};
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList#getResolvedConceptReference()
	 */
	@Override
	public ResolvedConceptReference[] getResolvedConceptReference() {
	    if(this.cache == null) {
	        List<ResolvedConceptReference> list = new ArrayList<ResolvedConceptReference>();

	        Iterator<ResolvedConceptReference> itr = iterateResolvedConceptReference();

	        while(itr.hasNext()) {
	            list.add(itr.next());
	        }

	        cache = list.toArray(new ResolvedConceptReference[list.size()]);
	    }
	    
	    return cache;
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList#getResolvedConceptReference(int)
	 */
	@Override
	public ResolvedConceptReference getResolvedConceptReference(int index)
			throws IndexOutOfBoundsException {
		return getResolvedConceptReference()[index];
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList#getResolvedConceptReferenceCount()
	 */
	@Override
	public int getResolvedConceptReferenceCount() {
	    return this.getResolvedConceptReference().length;
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList#iterateResolvedConceptReference()
	 */
	@Override
	public Iterator<ResolvedConceptReference> iterateResolvedConceptReference() {
	    if(this.cache != null) {
	        return Arrays.asList(this.cache).iterator();
	    } else {
	        return new RootResolvedConceptReferenceIterator();
	    }
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList#removeAllResolvedConceptReference()
	 */
	@Override
	public void removeAllResolvedConceptReference() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList#removeResolvedConceptReference(org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference)
	 */
	@Override
	public boolean removeResolvedConceptReference(ResolvedConceptReference vResolvedConceptReference) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList#removeResolvedConceptReferenceAt(int)
	 */
	@Override
	public ResolvedConceptReference removeResolvedConceptReferenceAt(int index) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList#setResolvedConceptReference(org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference[])
	 */
	@Override
	public void setResolvedConceptReference(ResolvedConceptReference[] arg0) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList#setResolvedConceptReference(int, org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference)
	 */
	@Override
	public void setResolvedConceptReference(int index,
			ResolvedConceptReference vResolvedConceptReference)
			throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}	
	
	private class RootResolvedConceptReferenceIterator extends AbstractPageableIterator<ResolvedConceptReference> {

        private static final long serialVersionUID = -1322750106614136398L;
        
        private RootConceptReferenceIterator rootConceptReferenceIterator;

	    private RootResolvedConceptReferenceIterator(){
	        rootConceptReferenceIterator = new RootConceptReferenceIterator(
                codingSchemeUri,
                codingSchemeVersion,
                relationsContainerName,
                PagedGraphUtils.getDirection(resolveForward, resolveBackward),
                graphQuery);
	    }

        @Override
        protected List<? extends ResolvedConceptReference> doPage(int currentPosition, int pageSize) {
            List<ResolvedConceptReference> returnList = new ArrayList<ResolvedConceptReference>();
            
            int count = 0;
            while(rootConceptReferenceIterator.hasNext() && count < pageSize) {
                ConceptReference root = rootConceptReferenceIterator.next();
                
                ResolvedConceptReferenceList list;
                try {
                    list = validatedParameterResolvingCallback.doResolveAsValidatedParameterList(
                            root, 
                            resolveForward, 
                            resolveBackward, 
                            resolveCodedEntryDepth, 
                            resolveAssociationDepth,
                            propertyNames, 
                            propertyTypes,
                            sortAlgorithms, 
                            filterOptions, 
                            maxToReturn, 
                            keepLastAssociationLevelUnresolved,
                            cycleDetectingCallback);
                } catch (Exception e) {
                   throw new RuntimeException(e);
                } 
                
                if(list != null) {

                    for(ResolvedConceptReference ref : list.getResolvedConceptReference()) {

                        returnList.add(ref);    
                        count++;
                    }
                }
            }
            return returnList;
        }
	}
}
