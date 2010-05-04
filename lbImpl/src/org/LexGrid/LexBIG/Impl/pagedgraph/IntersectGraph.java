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
package org.LexGrid.LexBIG.Impl.pagedgraph;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;

/**
 * The Class IntersectGraph.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IntersectGraph extends AbstractMultiGraph {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6431881502847939549L;

    /**
     * Instantiates a new intersect graph.
     * 
     * @param graph1 the graph1
     * @param graph2 the graph2
     */
    public IntersectGraph(CodedNodeGraph graph1, CodedNodeGraph graph2) {
        super(graph1, graph2);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.AbstractCodedNodeGraph#doResolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean, boolean, int, int, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], org.LexGrid.LexBIG.DataModel.Collections.SortOptionList, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int, boolean)
     */
    @Override
    public ResolvedConceptReferenceList doResolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn, boolean keepLastAssociationLevelUnresolved)
            throws LBInvocationException, LBParameterException {
        ResolvedConceptReferenceList list1 = this.getGraph1().resolveAsList(
                graphFocus, resolveForward, resolveBackward, 
                resolveCodedEntryDepth, resolveAssociationDepth, propertyNames, 
                propertyTypes, sortOptions, maxToReturn);
        
        ResolvedConceptReferenceList list2 = this.getGraph2().resolveAsList(
                graphFocus, resolveForward, resolveBackward, 
                resolveCodedEntryDepth, resolveAssociationDepth, propertyNames, 
                propertyTypes, sortOptions, maxToReturn);


        return this.intersectReferenceList(list1, list2);
    }
    
    

    @Override
    public CodedNodeSet toNodeList(ConceptReference graphFocus, boolean resolveForward, boolean resolveBackward,
            int resolveAssociationDepth, int maxToReturn) throws LBInvocationException, LBParameterException {
        CodedNodeSet cns1 = getGraph1().toNodeList(graphFocus, resolveForward, resolveBackward, resolveAssociationDepth, maxToReturn);
        CodedNodeSet cns2 = getGraph2().toNodeList(graphFocus, resolveForward, resolveBackward, resolveAssociationDepth, maxToReturn);
        
        return cns1.intersect(cns2);    
    }
    
    protected ResolvedConceptReferenceList intersectReferenceList(
            ResolvedConceptReferenceList list1, 
            ResolvedConceptReferenceList list2) {
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        if(list1 == null) { list1 = new ResolvedConceptReferenceList(); }
        if(list2 == null) { list2 = new ResolvedConceptReferenceList(); }
        
        for(ResolvedConceptReference ref : list1.getResolvedConceptReference()) {
            ResolvedConceptReference foundRef =
                this.getAssociatedConcept(ref, list2.getResolvedConceptReference());
            if(foundRef != null) {
                returnList.addResolvedConceptReference(this.intersectReference(ref, foundRef));
            }
        }

        return returnList;
    }
    
    /**
     * Union reference.
     * 
     * @param ref1 the ref1
     * @param ref2 the ref2
     * 
     * @return the resolved concept reference
     */
    protected ResolvedConceptReference intersectReference(ResolvedConceptReference ref1, ResolvedConceptReference ref2) {
        ref1.setSourceOf(intersectAssociationList(ref1.getSourceOf(), ref2.getSourceOf()));
        ref2.setTargetOf(intersectAssociationList(ref1.getTargetOf(), ref2.getTargetOf()));
       
        return ref1;
    }
    
    protected AssociatedConcept intersectReference(AssociatedConcept ref1, AssociatedConcept ref2) {
        ref1.setSourceOf(intersectAssociationList(ref1.getSourceOf(), ref2.getSourceOf()));
        ref2.setTargetOf(intersectAssociationList(ref1.getTargetOf(), ref2.getTargetOf()));
       
        return ref1;
    }
    
    protected AssociationList intersectAssociationList(AssociationList list1, AssociationList list2) {
        AssociationList returnList = new AssociationList();
        
        if(list1 != null) {
            for(Association association : list1.getAssociation()) {
                
                Association intersectedAssociation = getAssociationForName(association.getAssociationName(), list2);
                
                if(intersectedAssociation != null) {
                    returnList.addAssociation(intersectAssociation(association, intersectedAssociation));
                } 
            }
        }
        
        if(returnList.getAssociationCount() == 0) {
            return null;
        } else {
            return returnList;
        }
    }
    
    protected Association intersectAssociation(Association assoc1, Association assoc2) {
        AssociatedConceptList list = new AssociatedConceptList();
        
        AssociatedConcept[] associatedConcepts1 = assoc1.getAssociatedConcepts().getAssociatedConcept();
        AssociatedConcept[] associatedConcepts2 = assoc1.getAssociatedConcepts().getAssociatedConcept();
        
        for(AssociatedConcept concept : associatedConcepts1) {
            AssociatedConcept foundConcept = getAssociatedConcept(concept, associatedConcepts2);
            if(foundConcept != null) {
                
                list.addAssociatedConcept(
                        intersectReference(concept, foundConcept));
            }
        }

        assoc1.setAssociatedConcepts(list);
        
        return assoc1;
    }
    
    protected <T extends ConceptReference> T getAssociatedConcept(T searchConcept, T[] list) {
        for(T concept : list) {
            if(concept.getCode().equals(searchConcept.getCode()) &&
                    concept.getCodeNamespace().equals(searchConcept.getCodeNamespace())){
                return concept;
            }
        }
        return null;
    }
    
    protected Association getAssociationForName(String associationName, AssociationList list) {
        if(list == null) {return null;}
        
        for(Association association : list.getAssociation()) {
            if(association.getAssociationName().equals(associationName)) {
                return association;
            }
        }
        return null;
    }
}
