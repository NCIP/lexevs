package org.LexGrid.LexBIG.Impl.pagedgraph.paging;

import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.Impl.pagedgraph.builder.AssociationListBuilder.AssociationDirection;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallback;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;

public class CycleDetectingIteratorDecorator implements Iterator<AssociatedConcept>{

    private AssociatedConceptIterator associatedConceptIterator;
    private CycleDetectingCallback cycleDetectingCallback;
    private String associationPredicateName;
    
    public CycleDetectingIteratorDecorator(
            CycleDetectingCallback cycleDetectingCallback,
            String codingSchemeUri, 
            String codingSchemeVersion, 
            String relationsContainerName,
            String associationPredicateName, 
            String entityCode,
            String entityCodeNamespace,
            boolean resolveForward,
            boolean resolveBackward,
            int resolveAssociationDepth,
            int resolveCodedEntryDepth,
            GraphQuery graphQuery,
            AssociationDirection direction,
            int pageSize) {
        
        this.cycleDetectingCallback = cycleDetectingCallback;
        this.associationPredicateName = associationPredicateName;

        associatedConceptIterator = new AssociatedConceptIterator(
                codingSchemeUri, 
                codingSchemeVersion, 
                relationsContainerName,
                associationPredicateName, 
                entityCode,
                entityCodeNamespace,
                resolveForward,
                resolveBackward,
                resolveAssociationDepth,
                resolveCodedEntryDepth,
                resolveCodedEntryDepth,
                graphQuery,
                direction,
                pageSize);
    }
    @Override
    public boolean hasNext() {
       return associatedConceptIterator.hasNext();
    }

    @Override
    public AssociatedConcept next() {
        return cycleDetectingCallback.onNextAssociatedConcept(associationPredicateName, associatedConceptIterator.next());
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
