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
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Impl.helpers.IteratorBackedResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;

public class MappingExtensionImpl extends AbstractExtendable implements MappingExtension {

    private static final long serialVersionUID = 6439060328876806104L;
    
    protected static int PAGE_SIZE = 1000;
    
    public MappingExtensionImpl() {
        super();
    }
    
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription("Mapping Extension for LexEVS.");
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(MappingExtensionImpl.class.getName());
        ed.setName("MappingExtension");
        ed.setVersion("1.0");
        
        return ed;
    }
    
    public void register() throws LBParameterException, LBException {
        ExtensionRegistryImpl.instance().registerGenericExtension(
                super.getExtensionDescription());
    }

    @Override
    public ResolvedConceptReferencesIterator resolveMapping(
            String codingScheme,
            CodingSchemeVersionOrTag codingSchemeVersionOrTag, 
            String relationsContainerName,
            List<MappingSortOption> sortOptionList) throws LBParameterException {
        AbsoluteCodingSchemeVersionReference ref = 
            ServiceUtility.getAbsoluteCodingSchemeVersionReference(codingScheme, codingSchemeVersionOrTag, true);
        
        String uri = ref.getCodingSchemeURN();
        String version = ref.getCodingSchemeVersion();
        
        Iterator<ResolvedConceptReference> iterator = 
            new MappingTripleIterator(
                    uri,
                    version,
                    relationsContainerName, 
                    sortOptionList);
        
        int count = LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().
                getCodedNodeGraphService().
                    getMappingTriplesCount(
                            uri, 
                            version, 
                            relationsContainerName);

        return 
            new IteratorBackedResolvedConceptReferencesIterator(iterator, count);
    }

    @Override
    public boolean isMappingCodingScheme(String codingScheme,
            CodingSchemeVersionOrTag codingSchemeVersionOrTag) throws LBParameterException {
        AbsoluteCodingSchemeVersionReference ref = 
            ServiceUtility.getAbsoluteCodingSchemeVersionReference(codingScheme, codingSchemeVersionOrTag, true);

        final String uri = ref.getCodingSchemeURN();
        final String version = ref.getCodingSchemeVersion();

        boolean isMappingCodingScheme = 
            LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().
            getDaoCallbackService().
            executeInDaoLayer(new DaoCallback<Boolean>() {

                @Override
                public Boolean execute(DaoManager daoManager) {
                    String codingSchemeUid = daoManager.getCodingSchemeDao(uri, version).getCodingSchemeUIdByUriAndVersion(uri, version);

                    AssociationDao associationDao = daoManager.getAssociationDao(uri, version);

                    List<String> relationContainerNames = 
                        associationDao.getRelationsNamesForCodingSchemeUId(codingSchemeUid);

                    for(String relationsContainerName : relationContainerNames) {
                        String relationsUid = daoManager.getAssociationDao(uri, version).getRelationUId(codingSchemeUid, relationsContainerName);

                        Relations relation = daoManager.getAssociationDao(uri, version).getRelationsByUId(
                                codingSchemeUid, 
                                relationsUid, 
                                false);

                        if(relation.getIsMapping() == null || relation.getIsMapping() == false) {
                            return false;
                        }
                    }
                    return true;
                }});
        
        return isMappingCodingScheme;
    }
    
    @Override
    public AbsoluteCodingSchemeVersionReferenceList getMappingCodingSchemesEntityParticipatesIn(
            String entityCode,
            String entityCodeNamespace) throws LBParameterException {
        
        AbsoluteCodingSchemeVersionReferenceList returnList = new AbsoluteCodingSchemeVersionReferenceList();
        
        try {
            for(CodingSchemeRendering csr : 
                LexBIGServiceImpl.defaultInstance().getSupportedCodingSchemes().getCodingSchemeRendering()) {
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                
                String uri = css.getCodingSchemeURI();
                String version = css.getRepresentsVersion();
                
                if(this.isMappingCodingScheme(uri, Constructors.createCodingSchemeVersionOrTagFromVersion(version))) {
                    if(this.doesCodeParticipateInMapping(uri, version, entityCode, entityCodeNamespace)) {
                        returnList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(css));
                    }
                }
            }
        } catch (LBInvocationException e) {
            throw new RuntimeException(e);
        } 
        
        return returnList;   
    }

    protected boolean doesCodeParticipateInMapping(
            final String uri,
            final String version,
            final String code,
            final String namespace) throws LBParameterException {
       
        boolean participates = 
            LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().
            getDaoCallbackService().
            executeInDaoLayer(new DaoCallback<Boolean>() {

                @Override
                public Boolean execute(DaoManager daoManager) {
                    String codingSchemeUid = daoManager.getCodingSchemeDao(uri, version).getCodingSchemeUIdByUriAndVersion(uri, version);

                    return daoManager.getCodedNodeGraphDao(uri, version).doesEntityParticipateInRelationships(codingSchemeUid, null, code, namespace);
                }});
        
        return participates;
    }
}