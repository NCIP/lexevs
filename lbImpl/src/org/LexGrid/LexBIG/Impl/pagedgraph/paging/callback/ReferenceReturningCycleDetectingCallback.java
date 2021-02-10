
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