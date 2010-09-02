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
package org.LexGrid.LexBIG.Impl.pagedgraph.model;

import java.util.Enumeration;
import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.Impl.pagedgraph.builder.AssociationListBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.builder.AssociationListBuilder.AssociationDirection;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallback;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.StubReturningCycleDetectingCallback;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.annotations.LgProxyClass;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;

/**
 * The Class LazyLoadableAssociontList.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@LgProxyClass
public class LazyLoadableAssociontList extends AssociationList {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -434490124369412627L;
    
    private AssociationListBuilder associationListBuilder = new AssociationListBuilder();
    
    private AssociationList lazyLoadedAssociationList;
 
    /** The graph query. */
    private GraphQuery graphQuery;
    
    /** The coding scheme uid. */
    private String codingSchemeUri; 
    
    /** The coding scheme version. */
    private String codingSchemeVersion;
    
    /** The relations container name. */
    private String relationsContainerName;  
    
    /** The entity code. */
    private String entityCode;
    
    /** The entity code namespace. */
    private String entityCodeNamespace;
    
    /** The direction. */
    private AssociationDirection direction;
    
    /** The resolve forward association depth. */
    private int resolveForwardAssociationDepth;
    
    /** The resolve backward association depth. */
    private int resolveBackwardAssociationDepth;
    
    /** The resolve coded entry depth. */
    private int resolveCodedEntryDepth;
    
    /** The resolve forward. */
    private boolean resolveForward;
    
    /** The resolve backward. */
    private boolean resolveBackward;
    
    /** The cycle detecting callback. */
    private CycleDetectingCallback cycleDetectingCallback = new StubReturningCycleDetectingCallback();
    
    private SortOptionList sortAlgorithms;
    
    private LocalNameList propertyNames;
    
    private LocalNameList filterOptions;
    
    private PropertyType[] propertyTypes;
    
    public LazyLoadableAssociontList() {
        super();
    }

    public LazyLoadableAssociontList(
            GraphQuery graphQuery,
            String codingSchemeUri, 
            String codingSchemeVersion, 
            String relationsContainerName,
            String associationPredicateName, 
            String entityCode, 
            String entityCodeNamespace,
            AssociationDirection direction, 
            int resolveForwardAssociationDepth,
            int resolveBackwardAssociationDepth,
            int resolveCodedEntryDepth, 
            boolean resolveForward,
            boolean resolveBackward, 
            SortOptionList sortAlgorithms,
            LocalNameList propertyNames, 
            LocalNameList filterOptions, 
            PropertyType[] propertyTypes) {
        super();
        this.graphQuery = graphQuery;
        this.codingSchemeUri = codingSchemeUri;
        this.codingSchemeVersion = codingSchemeVersion;
        this.relationsContainerName = relationsContainerName;
        this.entityCode = entityCode;
        this.entityCodeNamespace = entityCodeNamespace;
        this.direction = direction;
        this.resolveForwardAssociationDepth = resolveForwardAssociationDepth;
        this.resolveBackwardAssociationDepth = resolveBackwardAssociationDepth;
        this.resolveCodedEntryDepth = resolveCodedEntryDepth;
        this.resolveForward = resolveForward;
        this.resolveBackward = resolveBackward;
        this.sortAlgorithms = sortAlgorithms;
        this.propertyNames = propertyNames;
        this.filterOptions = filterOptions;
        this.propertyTypes = propertyTypes;
    }
    
    private AssociationList getLazyLoadedAssociationList() {
        if(this.lazyLoadedAssociationList == null) {
            switch (this.direction) {
                case SOURCE_OF: {
                    this.lazyLoadedAssociationList = this.associationListBuilder.buildSourceOfAssociationList(
                            codingSchemeUri, 
                            codingSchemeVersion, 
                            entityCode, 
                            entityCodeNamespace, 
                            relationsContainerName, 
                            resolveForward, 
                            resolveBackward, 
                            resolveForwardAssociationDepth, 
                            resolveBackwardAssociationDepth, 
                            resolveCodedEntryDepth, 
                            graphQuery, 
                            propertyNames, 
                            propertyTypes, 
                            sortAlgorithms, 
                            filterOptions, 
                            cycleDetectingCallback);
                    break;
                } 
                case TARGET_OF: {
                    this.lazyLoadedAssociationList = this.associationListBuilder.buildTargetOfAssociationList(
                            codingSchemeUri, 
                            codingSchemeVersion, 
                            entityCode, 
                            entityCodeNamespace, 
                            relationsContainerName, 
                            resolveForward, 
                            resolveBackward, 
                            resolveForwardAssociationDepth, 
                            resolveBackwardAssociationDepth, 
                            resolveCodedEntryDepth, 
                            graphQuery, 
                            propertyNames, 
                            propertyTypes, 
                            sortAlgorithms, 
                            filterOptions, 
                            cycleDetectingCallback);
                    
                    break;
                }
            }
        }
        
        if(this.lazyLoadedAssociationList == null) {
            this.lazyLoadedAssociationList = new AssociationList();
        }
        
        return this.lazyLoadedAssociationList;
    }

    @Override
    public void addAssociation(Association vAssociation) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException();
    }
    @Override
    public void addAssociation(int index, Association vAssociation) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException();
    }
    @Override
    public Enumeration<? extends Association> enumerateAssociation() {
        return getLazyLoadedAssociationList().enumerateAssociation();
    }
    @Override
    public Association[] getAssociation() {
        return getLazyLoadedAssociationList().getAssociation();
    }
    @Override
    public Association getAssociation(int index) throws IndexOutOfBoundsException {
        return getLazyLoadedAssociationList().getAssociation(index);
    }
    @Override
    public int getAssociationCount() {
        return getLazyLoadedAssociationList().getAssociationCount();
    }
    @Override
    public Iterator<? extends Association> iterateAssociation() {
        return getLazyLoadedAssociationList().iterateAssociation();
    }
    @Override
    public void removeAllAssociation() {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean removeAssociation(Association vAssociation) {
        throw new UnsupportedOperationException();
    }
    @Override
    public Association removeAssociationAt(int index) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void setAssociation(Association[] arg0) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void setAssociation(int index, Association vAssociation) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException();
    }  
}
