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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.pagedgraph.utility.KeyedGraph;
import org.LexGrid.LexBIG.Impl.pagedgraph.utility.MultiGraphUtility;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;

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
    
    @Override
    public List<String> listCodeRelationships(ConceptReference sourceCode, ConceptReference targetCode,
            boolean directOnly) throws LBInvocationException, LBParameterException {
       List<String> assocs1 = this.getGraph1().listCodeRelationships(sourceCode, targetCode, directOnly);
       List<String> assocs2 = this.getGraph2().listCodeRelationships(sourceCode, targetCode, directOnly);
       
       Set<String> returnSet = new HashSet<String>(assocs1);
       
       returnSet.addAll(assocs2);
       
       return new ArrayList<String>(returnSet);
    }
    
    @Override
    public Boolean areCodesRelated(NameAndValue association, ConceptReference sourceCode, ConceptReference targetCode,
            boolean directOnly) throws LBInvocationException, LBParameterException {
        boolean relatedGraph1 = this.getGraph1().areCodesRelated(association, sourceCode, targetCode, directOnly);
        boolean relatedGraph2 = this.getGraph2().areCodesRelated(association, sourceCode, targetCode, directOnly);
    
        return relatedGraph1 || relatedGraph2;
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


        return this.unionReferenceList(graphFocus == null, list1, list2);
    }
    
    @Override
    public CodedNodeSet toNodeList(ConceptReference graphFocus, boolean resolveForward, boolean resolveBackward,
            int resolveAssociationDepth, int maxToReturn) throws LBInvocationException, LBParameterException {
        CodedNodeSet cns1 = getGraph1().toNodeList(graphFocus, resolveForward, resolveBackward, resolveAssociationDepth, maxToReturn);
        CodedNodeSet cns2 = getGraph2().toNodeList(graphFocus, resolveForward, resolveBackward, resolveAssociationDepth, maxToReturn);
        
        return cns1.union(cns2);    
    }
    
    protected ResolvedConceptReferenceList unionReferenceList(
            boolean nullFocus,
            ResolvedConceptReferenceList list1, 
            ResolvedConceptReferenceList list2) {
        
        if(nullFocus) {
            KeyedGraph graph1 = new KeyedGraph(list1);
            KeyedGraph unionedGraph = graph1.union(new KeyedGraph(list2));
            
            return unionedGraph.toResolvedConceptReferenceList();
        } else {
            return MultiGraphUtility.unionReferenceList(list1, list2);
        }
    }
}
