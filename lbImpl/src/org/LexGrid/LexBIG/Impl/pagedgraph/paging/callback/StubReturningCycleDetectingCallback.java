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
package org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback;

import java.util.HashSet;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.springframework.beans.BeanUtils;

/**
 * The Class StubReturningCycleDetectingCallback.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class StubReturningCycleDetectingCallback extends AbstractCycleDetectingCallback {

    private static final long serialVersionUID = 3055869314918129134L;
    
    /** The associated concept set. */
    private Set<AssociatedConceptKey> associatedConceptSet = 
        new HashSet<AssociatedConceptKey>();

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallbackI#getAssociatedConceptInGraph(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept)
     */
    public AssociatedConcept getAssociatedConceptInGraph(String associationName, AssociatedConcept associatedConcept) {
        AssociatedConceptKey key = toAssociatedConceptKey(associationName, associatedConcept);
        
        if(! associatedConceptSet.contains(key)) {
            throw new RuntimeException("AssociatedConcept not found.");
        }
       return buildStubAssociatedConcept(associatedConcept);   
    }
    
    /**
     * Builds the stub associated concept.
     * 
     * @param associatedConcept the associated concept
     * 
     * @return the associated concept
     */
    protected AssociatedConcept buildStubAssociatedConcept(AssociatedConcept associatedConcept) {
        AssociatedConcept newConcept = new AssociatedConcept();
        BeanUtils.copyProperties(associatedConcept, newConcept);
        newConcept.setSourceOf(null);
        newConcept.setTargetOf(null);
        
        return newConcept;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallbackI#addAssociatedConceptToGraph(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept)
     */
    public void addAssociatedConceptToGraph(String associationName, AssociatedConcept associatedConcept) {
        AssociatedConceptKey key = toAssociatedConceptKey(associationName, associatedConcept);
        associatedConceptSet.add(key);
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallbackI#isAssociatedConceptAlreadyInGraph(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept)
     */
    public boolean isAssociatedConceptAlreadyInGraph(String associationName, AssociatedConcept associatedConcept) {
        AssociatedConceptKey key = toAssociatedConceptKey(associationName, associatedConcept);
        
        return associatedConceptSet.contains(key);
    }

    @Override
    public void clear() {
       this.associatedConceptSet.clear();
    }
}
