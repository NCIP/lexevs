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

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.springframework.util.Assert;

/**
 * The Class UnionGraph.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UnionGraph implements CodedNodeGraph {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8545723123536546421L;

    /** The graph1. */
    private CodedNodeGraph graph1;
    
    /** The graph2. */
    private CodedNodeGraph graph2;
    
    /**
     * Instantiates a new union graph.
     * 
     * @param graph1 the graph1
     * @param graph2 the graph2
     */
    public UnionGraph(CodedNodeGraph graph1, CodedNodeGraph graph2) {
        this.graph1 = graph1;
        this.graph2 = graph2;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#areCodesRelated(org.LexGrid.LexBIG.DataModel.Core.NameAndValue, org.LexGrid.LexBIG.DataModel.Core.ConceptReference, org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean)
     */
    @Override
    public Boolean areCodesRelated(NameAndValue association, ConceptReference sourceCode, ConceptReference targetCode,
            boolean directOnly) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#intersect(org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph)
     */
    @Override
    public CodedNodeGraph intersect(CodedNodeGraph graph) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#isCodeInGraph(org.LexGrid.LexBIG.DataModel.Core.ConceptReference)
     */
    @Override
    public Boolean isCodeInGraph(ConceptReference code) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#listCodeRelationships(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean)
     */
    @Override
    public ConceptReferenceList listCodeRelationships(ConceptReference sourceCode, ConceptReference targetCode,
            boolean directOnly) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#listCodeRelationships(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, org.LexGrid.LexBIG.DataModel.Core.ConceptReference, int)
     */
    @Override
    public ConceptReferenceList listCodeRelationships(ConceptReference sourceCode, ConceptReference targetCode,
            int distance) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#resolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean, boolean, int, int, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], org.LexGrid.LexBIG.DataModel.Collections.SortOptionList, int)
     */
    @Override
    public ResolvedConceptReferenceList resolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions, int maxToReturn)
            throws LBInvocationException, LBParameterException {
        return this.resolveAsList(
                graphFocus, 
                resolveForward, 
                resolveBackward, 
                resolveCodedEntryDepth, 
                resolveAssociationDepth, 
                propertyNames, 
                propertyTypes, 
                sortOptions, 
                null, 
                maxToReturn);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#resolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean, boolean, int, int, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], org.LexGrid.LexBIG.DataModel.Collections.SortOptionList, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int)
     */
    @Override
    public ResolvedConceptReferenceList resolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn) throws LBInvocationException, LBParameterException {
        ResolvedConceptReferenceList list1 = this.graph1.resolveAsList(
                graphFocus, resolveForward, resolveBackward, 
                resolveCodedEntryDepth, resolveAssociationDepth, propertyNames, 
                propertyTypes, sortOptions, maxToReturn);
        
        ResolvedConceptReferenceList list2 = this.graph2.resolveAsList(
                graphFocus, resolveForward, resolveBackward, 
                resolveCodedEntryDepth, resolveAssociationDepth, propertyNames, 
                propertyTypes, sortOptions, maxToReturn);
        
        Assert.state(list1.getResolvedConceptReferenceCount() == 1);
        Assert.state(list2.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        
        ResolvedConceptReference ref1 = list1.getResolvedConceptReference(0);
        ResolvedConceptReference ref2 = list2.getResolvedConceptReference(0);
        
        returnList.addResolvedConceptReference(
                this.unionReference(
                        ref1, ref2));
        
        return returnList;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#resolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean, boolean, int, int, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], org.LexGrid.LexBIG.DataModel.Collections.SortOptionList, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int, boolean)
     */
    @Override
    public ResolvedConceptReferenceList resolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn, boolean keepLastAssociationLevelUnresolved)
            throws LBInvocationException, LBParameterException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToAssociations(org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList, org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList)
     */
    @Override
    public CodedNodeGraph restrictToAssociations(NameAndValueList association, NameAndValueList associationQualifiers)
            throws LBInvocationException, LBParameterException {
       this.graph1.restrictToAssociations(association, associationQualifiers);
       this.graph2.restrictToAssociations(association, associationQualifiers);  
       
       return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToCodeSystem(java.lang.String)
     */
    @Override
    public CodedNodeGraph restrictToCodeSystem(String codingScheme) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToCodeSystem(codingScheme);
        this.graph2.restrictToCodeSystem(codingScheme);  
        
        return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToCodes(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @Override
    public CodedNodeGraph restrictToCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToCodes(codes);
        this.graph2.restrictToCodes(codes);
        
        return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToDirectionalNames(org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList, org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList)
     */
    @Override
    public CodedNodeGraph restrictToDirectionalNames(NameAndValueList directionalNames,
            NameAndValueList associationQualifiers) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToDirectionalNames(directionalNames, associationQualifiers);
        this.graph2.restrictToDirectionalNames(directionalNames, associationQualifiers);
        
        return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToSourceCodeSystem(java.lang.String)
     */
    @Override
    public CodedNodeGraph restrictToSourceCodeSystem(String codingScheme) throws LBInvocationException,
            LBParameterException {
       this.graph1.restrictToSourceCodeSystem(codingScheme);
       this.graph2.restrictToSourceCodeSystem(codingScheme);
       
       return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToSourceCodes(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @Override
    public CodedNodeGraph restrictToSourceCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToSourceCodes(codes);
        this.graph2.restrictToSourceCodes(codes);
        
        return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToTargetCodeSystem(java.lang.String)
     */
    @Override
    public CodedNodeGraph restrictToTargetCodeSystem(String codingScheme) throws LBInvocationException,
            LBParameterException {
       this.graph1.restrictToTargetCodeSystem(codingScheme);
       this.graph2.restrictToTargetCodeSystem(codingScheme);
       
       return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToTargetCodes(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @Override
    public CodedNodeGraph restrictToTargetCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToTargetCodes(codes);
        this.graph2.restrictToTargetCodes(codes);
        
        return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#toNodeList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean, boolean, int, int)
     */
    @Override
    public CodedNodeSet toNodeList(ConceptReference graphFocus, boolean resolveForward, boolean resolveBackward,
            int resolveAssociationDepth, int maxToReturn) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#union(org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph)
     */
    @Override
    public CodedNodeGraph union(CodedNodeGraph graph) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
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
        ResolvedConceptReference returnRef = ref1;
        
        if(ref2.getSourceOf() != null) {
            for(Association assoc : ref2.getSourceOf().getAssociation()) {
                returnRef.getSourceOf().addAssociation(assoc);
            }
        }
        
        if(ref2.getTargetOf() != null) {
            for(Association assoc : ref2.getTargetOf().getAssociation()) {
                returnRef.getTargetOf().addAssociation(assoc);
            }
        }

        return returnRef;
    }
}
