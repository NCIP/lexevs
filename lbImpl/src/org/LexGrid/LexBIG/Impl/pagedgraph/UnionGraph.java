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
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.springframework.util.Assert;

/**
 * The Class UnionGraph.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UnionGraph extends AbstractMultiGraph {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8545723123536546421L;
    
    /**
     * Instantiates a new union graph.
     * 
     * @param graph1 the graph1
     * @param graph2 the graph2
     */
    public UnionGraph(CodedNodeGraph graph1, CodedNodeGraph graph2) {
        super(graph1,graph2);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#resolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean, boolean, int, int, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], org.LexGrid.LexBIG.DataModel.Collections.SortOptionList, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int)
     */
    @Override
    public ResolvedConceptReferenceList doResolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn, boolean keepLastAssociationLevelUnresolved) throws LBInvocationException, LBParameterException {
        ResolvedConceptReferenceList list1 = this.getGraph1().resolveAsList(
                graphFocus, resolveForward, resolveBackward, 
                resolveCodedEntryDepth, resolveAssociationDepth, propertyNames, 
                propertyTypes, sortOptions, maxToReturn);
        
        ResolvedConceptReferenceList list2 = this.getGraph2().resolveAsList(
                graphFocus, resolveForward, resolveBackward, 
                resolveCodedEntryDepth, resolveAssociationDepth, propertyNames, 
                propertyTypes, sortOptions, maxToReturn);
        
        Assert.state(list1.getResolvedConceptReferenceCount() == 1);
        Assert.state(list2.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        
        ResolvedConceptReference ref1 = list1.getResolvedConceptReference(0);
        ResolvedConceptReference ref2 = list2.getResolvedConceptReference(0);
        
        this.unionReference(ref1, ref2);
        
        returnList.addResolvedConceptReference(ref1);
        
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
    protected ResolvedConceptReference unionReference(ResolvedConceptReference ref1, ResolvedConceptReference ref2) {
        ref1.setSourceOf(unionAssociationList(ref1.getSourceOf(), ref2.getSourceOf()));
        ref2.setTargetOf(unionAssociationList(ref1.getTargetOf(), ref2.getTargetOf()));
       
        return ref1;
    }
    
    protected AssociatedConcept unionReference(AssociatedConcept ref1, AssociatedConcept ref2) {
        ref1.setSourceOf(unionAssociationList(ref1.getSourceOf(), ref2.getSourceOf()));
        ref2.setTargetOf(unionAssociationList(ref1.getTargetOf(), ref2.getTargetOf()));
       
        return ref1;
    }
    
    protected AssociationList unionAssociationList(AssociationList list1, AssociationList list2) {
        AssociationList returnList = new AssociationList();
        
        if(list1 != null) {
            for(Association association : list1.getAssociation()) {
                
                Association unionedAssociation = getAssociationForName(association.getAssociationName(), list2);
                
                if(unionedAssociation != null) {
                    returnList.addAssociation(unionAssociation(association, unionedAssociation));
                }  else {
                    returnList.addAssociation(association);
                }
            }
        }
        if(list2 != null) {
            for(Association association : list2.getAssociation()) {
                Association unionedAssociation = getAssociationForName(association.getAssociationName(), list1);
                
                if(unionedAssociation == null) {
                    returnList.addAssociation(association);
                }
            }
        }
        
        if(returnList.getAssociationCount() == 0) {
            return null;
        } else {
            return returnList;
        }
    }
    
    protected Association unionAssociation(Association assoc1, Association assoc2) {
        AssociatedConceptList list = new AssociatedConceptList();
        
        AssociatedConcept[] associatedConcepts1 = assoc1.getAssociatedConcepts().getAssociatedConcept();
        AssociatedConcept[] associatedConcepts2 = assoc1.getAssociatedConcepts().getAssociatedConcept();
        
        for(AssociatedConcept concept : associatedConcepts1) {
            AssociatedConcept foundConcept = getAssociatedConcept(concept, associatedConcepts2);
            if(foundConcept != null) {
                
                list.addAssociatedConcept(
                        unionReference(concept, foundConcept));
            }
        }
        
        for(AssociatedConcept concept : associatedConcepts2) {
            AssociatedConcept foundConcept = getAssociatedConcept(concept, associatedConcepts1);
            if(foundConcept == null) {
                list.addAssociatedConcept(concept);
            }
        }  
        
        assoc1.setAssociatedConcepts(list);
        
        return assoc1;
    }
    
    protected AssociatedConcept getAssociatedConcept(AssociatedConcept searchConcept, AssociatedConcept[] list) {
        for(AssociatedConcept concept : list) {
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
