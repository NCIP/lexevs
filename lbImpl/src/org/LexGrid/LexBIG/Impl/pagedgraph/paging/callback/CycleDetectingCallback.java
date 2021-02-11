
package org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;

/**
 * The Interface CycleDetectingCallback.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CycleDetectingCallback extends Serializable {

    /**
     * Gets the associated concept in graph.
     * 
     * @param associationName the association name
     * @param associatedConcept the associated concept
     * 
     * @return the associated concept in graph
     */
    public AssociatedConcept getAssociatedConceptInGraph(String associationName,
            AssociatedConcept associatedConcept);

    /**
     * Adds the associated concept to graph.
     * 
     * @param associationName the association name
     * @param associatedConcept the associated concept
     */
    public void addAssociatedConceptToGraph(String associationName, AssociatedConcept associatedConcept);

    /**
     * Checks if is associated concept already in graph.
     * 
     * @param associationName the association name
     * @param associatedConcept the associated concept
     * 
     * @return true, if is associated concept already in graph
     */
    public boolean isAssociatedConceptAlreadyInGraph(String associationName,
            AssociatedConcept associatedConcept);
    
    public void clear();
}