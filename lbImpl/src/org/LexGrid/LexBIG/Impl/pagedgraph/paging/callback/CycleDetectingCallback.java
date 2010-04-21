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

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;

/**
 * The Interface CycleDetectingCallback.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CycleDetectingCallback {

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
}