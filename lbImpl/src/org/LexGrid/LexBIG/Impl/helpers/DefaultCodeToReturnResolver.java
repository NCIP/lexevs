/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Impl.helpers;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.Impl.dataAccess.SQLImplementedMethods;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Entity;
import org.lexevs.exceptions.InternalException;
import org.lexevs.system.ResourceManager;

/**
 * The Class DefaultCodeToReturnResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultCodeToReturnResolver implements CodeToReturnResolver {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2128719170709905064L;

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.CodeToReturnResolverI#buildResolvedConceptReference(org.LexGrid.LexBIG.Impl.helpers.CodeToReturn, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], boolean)
     */
    public ResolvedConceptReference buildResolvedConceptReference(CodeToReturn codeToReturn, 
            LocalNameList restrictToProperties,
            PropertyType[] restrictToPropertyTypes,
            Filter[] filters,
            boolean resolve) throws LBInvocationException {
        // Always assign the basics...
        ResolvedConceptReference rcr = new ResolvedConceptReference();
        rcr.setCodingSchemeName(
            ResourceManager.instance()
                .getExternalCodingSchemeNameForUserCodingSchemeNameOrId(codeToReturn.getUri(), codeToReturn.getVersion()));
        rcr.setCodingSchemeURI(codeToReturn.getUri());
        rcr.setCodingSchemeVersion(codeToReturn.getVersion());
        rcr.setCode(codeToReturn.getCode());
        rcr.setCodeNamespace(codeToReturn.getNamespace());
        EntityDescription ed = new EntityDescription();
        ed.setContent(codeToReturn.getEntityDescription());
        rcr.setEntityDescription(ed);
        rcr.setEntityType(codeToReturn.getEntityTypes());

        // Only attach the fully resolved object if instructed...
        try {
            if (resolve) {
                // Resolve and assign the item to the coded node reference.
                Entity resolvedEntity = buildCodedEntry(
                    ResourceManager.instance().getInternalCodingSchemeNameForUserCodingSchemeName(codeToReturn.getUri(), codeToReturn.getVersion()),
                    codeToReturn.getVersion(),
                    codeToReturn.getCode(),
                    codeToReturn.getNamespace(),
                    restrictToProperties,
                    restrictToPropertyTypes);
                rcr.setEntityDescription(resolvedEntity.getEntityDescription());
                rcr.setEntity(resolvedEntity);
            }
        } catch (LBParameterException e) {
            // this should only happen when the codedNodeSet was constructed
            // from a graph -
            // and if a source or target concept in the graph is not available
            // in the system.
            rcr.setEntity(null);
        }

        // these (two) stay null by design
        rcr.setSourceOf(null);
        rcr.setTargetOf(null);
        
        // these (two) stay null by design
        rcr.setSourceOf(null);
        rcr.setTargetOf(null);

        if (filters != null && filters.length > 0) {
            for (int i = 0; i < filters.length; i++) {
                if (!filters[i].match(rcr)) {
                    return null;
                }
            }
        }
 
        return rcr;
    }
     
    public ResolvedConceptReferenceList buildResolvedConceptReference(List<CodeToReturn> codesToReturn,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes, Filter[] filters,
            boolean resolve) throws LBInvocationException {
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        for(CodeToReturn codeToReturn : codesToReturn){
            returnList.addResolvedConceptReference(
                    buildResolvedConceptReference(
                            codeToReturn, 
                            restrictToProperties, 
                            restrictToPropertyTypes, 
                            filters, 
                            resolve));
        }
        return returnList;
    }

    /**
     * Builds the coded entry.
     * 
     * @param internalCodingSchemeName the internal coding scheme name
     * @param internalVersionString the internal version string
     * @param code the code
     * @param namespace the namespace
     * @param restrictToProperties the restrict to properties
     * @param restrictToPropertyTypes the restrict to property types
     * 
     * @return the entity
     * 
     * @throws LBInvocationException the LB invocation exception
     */
    private Entity buildCodedEntry(String internalCodingSchemeName, String internalVersionString, String code, String namespace,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes) throws LBInvocationException {
        try {
            return SQLImplementedMethods.buildCodedEntry(internalCodingSchemeName, internalVersionString, code, namespace,
                    restrictToProperties, restrictToPropertyTypes);
        } catch (InternalException e) {
            throw new LBInvocationException("Unexpected Internal Error", e.getLogId());
        }
    }
}
