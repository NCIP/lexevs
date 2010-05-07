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
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.lexevs.logging.LoggerFactory;

/**
 * The Class AbstractCodedNodeGraph.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractCodedNodeGraph implements CodedNodeGraph {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7955494879137282711L;
    
    /** The logger. */
    private LgLoggerIF logger = LoggerFactory.getLogger();
    
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
    public ResolvedConceptReferenceList resolveAsList(
            ConceptReference graphFocus, 
            boolean resolveForward,
            boolean resolveBackward, 
            int resolveCodedEntryDepth, 
            int resolveAssociationDepth,
            LocalNameList propertyNames, 
            PropertyType[] propertyTypes, 
            SortOptionList sortOptions,
            LocalNameList filterOptions, 
            int maxToReturn) throws LBInvocationException, LBParameterException {
        return this.doResolveAsList(graphFocus, resolveForward, resolveBackward, resolveCodedEntryDepth, resolveAssociationDepth, propertyNames, propertyTypes, sortOptions, filterOptions, maxToReturn, false);
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#resolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean, boolean, int, int, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], org.LexGrid.LexBIG.DataModel.Collections.SortOptionList, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int, boolean)
     */
    public ResolvedConceptReferenceList resolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn, boolean keepLastAssociationLevelUnresolved)
            throws LBInvocationException, LBParameterException{
 
        return this.doResolveAsList(graphFocus, resolveForward, resolveBackward, resolveCodedEntryDepth, resolveAssociationDepth, propertyNames, propertyTypes, sortOptions, filterOptions, maxToReturn, keepLastAssociationLevelUnresolved);   
    }

    /**
     * Do resolve as list.
     * 
     * @param graphFocus the graph focus
     * @param resolveForward the resolve forward
     * @param resolveBackward the resolve backward
     * @param resolveCodedEntryDepth the resolve coded entry depth
     * @param resolveAssociationDepth the resolve association depth
     * @param propertyNames the property names
     * @param propertyTypes the property types
     * @param sortOptions the sort options
     * @param filterOptions the filter options
     * @param maxToReturn the max to return
     * @param keepLastAssociationLevelUnresolved the keep last association level unresolved
     * 
     * @return the resolved concept reference list
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws LBParameterException the LB parameter exception
     */
    public abstract ResolvedConceptReferenceList doResolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn, boolean keepLastAssociationLevelUnresolved)
            throws LBInvocationException, LBParameterException;

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#intersect(org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph)
     */
    @Override
    public CodedNodeGraph intersect(CodedNodeGraph graph) throws LBInvocationException, LBParameterException {
       return new IntersectGraph(this, graph);
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#union(org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph)
     */
    @Override
    public CodedNodeGraph union(CodedNodeGraph graph) throws LBInvocationException, LBParameterException {
        return new UnionGraph(this, graph);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#isCodeInGraph(org.LexGrid.LexBIG.DataModel.Core.ConceptReference)
     */
    @Override
    public Boolean isCodeInGraph(ConceptReference code) throws LBInvocationException, LBParameterException {
        ResolvedConceptReferenceList list = 
            this.doResolveAsList(code, true, true, 0, 0, null, null, null, null, 1, false);
        return (list != null && list.getResolvedConceptReferenceCount() > 0);
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

    /**
     * Gets the logger.
     * 
     * @return the logger
     */
    public LgLoggerIF getLogger() {
        return logger;
    }

    /**
     * Sets the logger.
     * 
     * @param logger the new logger
     */
    public void setLogger(LgLoggerIF logger) {
        this.logger = logger;
    }
}