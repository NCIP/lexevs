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
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Impl.helpers.IteratorBackedResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.custom.relations.TerminologyMapBean;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The Class MappingExtensionImpl.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MappingExtensionImpl extends AbstractExtendable implements MappingExtension {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6439060328876806104L;
    
    /** The PAG e_ size. */
    protected static int PAGE_SIZE = 1000;
    

    private String rankScoreName = "score";
    
    /**
     * Instantiates a new mapping extension impl.
     */
    public MappingExtensionImpl() {
        super();
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
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
    
    @Override
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
        registry.registerGenericExtension(description);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Generic.MappingExtension#resolveMapping(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String, java.util.List)
     */
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
        
        if(relationsContainerName == null){
            relationsContainerName = this.getDefaultMappingRelationsContainer(ref);
        }
        
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
    
    public String getDefaultMappingRelationsContainer(AbsoluteCodingSchemeVersionReference ref) throws LBParameterException {
        CodingScheme cs = LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().
                getCodingSchemeService().
                getCodingSchemeByUriAndVersion(
                        ref.getCodingSchemeURN(), 
                        ref.getCodingSchemeVersion());
        
        String mappingRelationsName = null;
        
        if(cs.getRelationsCount() > 0){
            for(Relations relations : cs.getRelations()){
                if(relations.isIsMapping()){
                    if(mappingRelationsName == null){
                        mappingRelationsName = relations.getContainerName();
                    } else {
                        throw new LBParameterException("Cannot determine a default RelationsContainer name -" +
                        		" more than one were found. Please specifiy a 'relationsContainer parameter.");
                    }
                }
            }
        }
        
        if(mappingRelationsName == null){
            throw new LBParameterException("Cannot determine a default RelationsContainer name -" +
                    " no mapping RelationsContainers were found.");
        }
        return mappingRelationsName;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Generic.MappingExtension#getMapping(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String)
     */
    public Mapping getMapping(
            String codingScheme,
            CodingSchemeVersionOrTag codingSchemeVersionOrTag, 
            String relationsContainerName) throws LBException{
        
        AbsoluteCodingSchemeVersionReference ref = 
                ServiceUtility.getAbsoluteCodingSchemeVersionReference(codingScheme, codingSchemeVersionOrTag, true);
        
        if(relationsContainerName == null){
            relationsContainerName = this.getDefaultMappingRelationsContainer(ref);
        }
        
        return new CodedNodeSetBackedMapping(ref, relationsContainerName);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Generic.MappingExtension#isMappingCodingScheme(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
     */
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

                        if(relation.getIsMapping() != null && relation.getIsMapping() == true) {
                            return true;
                        }
                    }
                    return false;
                }});
        
        return isMappingCodingScheme;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Generic.MappingExtension#getMappingCodingSchemesEntityParticipatesIn(java.lang.String, java.lang.String)
     */
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

    /**
     * Does code participate in mapping.
     * 
     * @param uri the uri
     * @param version the version
     * @param code the code
     * @param namespace the namespace
     * 
     * @return true, if successful
     * 
     * @throws LBParameterException the LB parameter exception
     */
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

    @Override
    public List<TerminologyMapBean> resolveBulkMapping(final String mappingName, String mappingVersion) {

        List<TerminologyMapBean> beanList = null;
        AbsoluteCodingSchemeVersionReference ref = null;

        if (mappingName == null) {
            throw new RuntimeException("Mapping Name or URI cannot be null when returning bulk mapping");
        }
        CodingSchemeVersionOrTag version = null;
        if (mappingVersion == null) {
            try {
                mappingVersion = ServiceUtility.getVersion(mappingName, null);
                version = Constructors.createCodingSchemeVersionOrTagFromVersion(mappingVersion);
                ref = ServiceUtility.getAbsoluteCodingSchemeVersionReference(mappingName, version, true);
            } catch (LBParameterException e) {
                throw new RuntimeException("Mapping Version Could not be resolved.", e);
            }
        } else {
            version = Constructors.createCodingSchemeVersionOrTagFromVersion(mappingVersion);
            try {
                ref = ServiceUtility.getAbsoluteCodingSchemeVersionReference(mappingName, version, true);
            } catch (LBParameterException e) {
                throw new RuntimeException(
                        "Mapping Scheme" + mappingName + " : " + version.getVersion() + " Could not be resolved.", e);
            }
        }
        try {
            if (isMappingCodingScheme(ref.getCodingSchemeURN(),
                    Constructors.createCodingSchemeVersionOrTagFromVersion(ref.getCodingSchemeVersion()))) {
                // return full set of mapped metadata
                final String mappingUid = getCodingSchemeUid(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion());
                final Relations rels = ServiceUtility.getRelationsForMappingScheme(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion(),
                        this.getDefaultMappingRelationsContainer(
                                Constructors.createAbsoluteCodingSchemeVersionReference(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion())));
                //Insure you have corrected target and source uri and version
                String sourceUri = ServiceUtility.getUriForCodingSchemeName(rels.getSourceCodingScheme());
                String sourceVersion = ServiceUtility.getVersion(sourceUri, null);
                
                String targetUri = ServiceUtility.getUriForCodingSchemeName(rels.getTargetCodingScheme());
                String targetVersion = ServiceUtility.getVersion(targetUri, null);
                
                final String sourceUid = getCodingSchemeUid(sourceUri,
                        sourceVersion);
                final String targetUid = getCodingSchemeUid(targetUri,
                        targetVersion);
                final String finalName = ref.getCodingSchemeURN();
                final String finalVersion = mappingVersion;
                beanList = (List<TerminologyMapBean>) LexEvsServiceLocator.getInstance().getDatabaseServiceManager()
                        .getDaoCallbackService().executeInDaoLayer(new DaoCallback<List<TerminologyMapBean>>() {

                            @Override
                            public List<TerminologyMapBean> execute(DaoManager daoManager) {

                                return daoManager.getCodedNodeGraphDao(finalName, finalVersion)
                                        .getMapAndTermsForMappingAndReferences(mappingUid, sourceUid, targetUid, rels,
                                                rankScoreName);
                            }
                        });

            } // if
        } catch (LBParameterException e) {
            throw new RuntimeException("Mapping scheme is not present or in error", e);
        }
        return beanList;
    }

    private String getCodingSchemeUid(String uri, String mappingVersion) {
        String Uid = LexEvsServiceLocator.getInstance().
        getDatabaseServiceManager().
        getDaoCallbackService().
        executeInDaoLayer(new DaoCallback<String>() {

            @Override
            public String execute(DaoManager daoManager) {
                return daoManager.getCodingSchemeDao(uri, mappingVersion).
                        getCodingSchemeUIdByUriAndVersion(uri, mappingVersion);
            }});
        return Uid;
    }
}