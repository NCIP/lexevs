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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;

/**
 * The Class ReferenceReturningCycleDetectingCallback.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ReferenceReturningCycleDetectingCallback extends AbstractCycleDetectingCallback {

    private static final long serialVersionUID = 8829097708376607580L;
    
    /** The associated concept map. */
    private Map<AssociatedConceptKey,AssociatedConcept> associatedConceptMap = 
        Collections.synchronizedMap(new HashMap<AssociatedConceptKey,AssociatedConcept>());

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallbackI#getAssociatedConceptInGraph(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept)
     */
    public AssociatedConcept getAssociatedConceptInGraph(String associationName, AssociatedConcept associatedConcept) {
        AssociatedConceptKey key = toAssociatedConceptKey(associationName, associatedConcept);
        
        return associatedConceptMap.get(key);
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallbackI#addAssociatedConceptToGraph(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept)
     */
    public void addAssociatedConceptToGraph(String associationName, AssociatedConcept associatedConcept) {
        AssociatedConceptKey key = toAssociatedConceptKey(associationName, associatedConcept);
        associatedConceptMap.put(key, associatedConcept);
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallbackI#isAssociatedConceptAlreadyInGraph(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept)
     */
    public boolean isAssociatedConceptAlreadyInGraph(String associationName, AssociatedConcept associatedConcept) {
        AssociatedConceptKey key = toAssociatedConceptKey(associationName, associatedConcept);
        
        return associatedConceptMap.containsKey(key);
    }

    @Override
    public void clear() {
       this.associatedConceptMap.clear();
    }
}