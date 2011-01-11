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
package org.LexGrid.LexBIG.Impl.loaders.postprocessor;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.SupportedEntityType;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.URIMap;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The Class SupportedAttributePostProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SupportedAttributePostProcessor extends AbstractExtendable implements LoaderPostProcessor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2828520523031693573L;
    
    /** The EXTENSION Name. */
    public static String EXTENSION_NAME = "SupportedAttributePostProcessor";

    /**
     * Register.
     * 
     * @throws LBParameterException the LB parameter exception
     * @throws LBException the LB exception
     */
    public void register() throws LBParameterException, LBException {
        ExtensionRegistryImpl.instance().registerGenericExtension(
                super.getExtensionDescription());
    }
 
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription("SupportedAttributePostProcessor");
        ed.setName(EXTENSION_NAME);
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(this.getClass().getName());
        
        return ed;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor#runPostProcess(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, org.LexGrid.LexBIG.Extensions.Load.OntologyFormat)
     */
    public void runPostProcess(AbsoluteCodingSchemeVersionReference reference, OntologyFormat ontFormat) {
        CodingSchemeService codingSchemeService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService();
        DaoCallbackService daoCallbackService  = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getDaoCallbackService();
        
        final String uri = reference.getCodingSchemeURN();
        final String version = reference.getCodingSchemeVersion();
        
        CodingScheme codingScheme = 
            codingSchemeService.
                getCodingSchemeByUriAndVersion(uri, version);
        
        SupportedCodingScheme scs = new SupportedCodingScheme();
        scs.setLocalId(codingScheme.getCodingSchemeName());
        scs.setUri(codingScheme.getCodingSchemeURI());
        
        insertURIMap(uri, version, scs);
        
        this.addSupportedFormats(uri, version, daoCallbackService);
        this.addSupportedLanguages(uri, version, daoCallbackService);
        this.addSupportedEntityTypes(uri, version, daoCallbackService);
        this.addSupportedProperties(uri, version, daoCallbackService); 
        this.addSupportedAssociations(uri, version, daoCallbackService); 
    }
    
    /**
     * Adds the supported associations.
     * 
     * @param uri the uri
     * @param version the version
     * @param daoCallbackService the dao callback service
     */
    protected void addSupportedAssociations(final String uri, final String version, DaoCallbackService daoCallbackService) {
        List<String> items = daoCallbackService.executeInDaoLayer(new DaoCallback<List<String>>(){

            public List<String> execute(DaoManager daoManager) {
                CodingSchemeDao csDao = daoManager.getCodingSchemeDao(uri, version);
                String csId = csDao.getCodingSchemeUIdByUriAndVersion(uri, version);

                AssociationDao associationDao = daoManager.getAssociationDao(uri, version);

                List<String> associationNames = new ArrayList<String>();

                for(String relationsId :
                    associationDao.getRelationsUIdsForCodingSchemeUId(csId)){
                    for(String associationPredicateId :
                        associationDao.getAssociationPredicateUIdsForRelationsUId(csId, relationsId)) {
                        associationNames.add(
                                associationDao.getAssociationPredicateNameForUId(csId, associationPredicateId));
                    }
                }
                return associationNames;
            }  
        });

        for(String item : items) {
            SupportedAssociation supportedAssociation = new SupportedAssociation();
            supportedAssociation.setContent(item);
            supportedAssociation.setLocalId(item);
            insertURIMap(uri, version, supportedAssociation);
        }
    }
    
    /**
     * Adds the supported formats.
     * 
     * @param uri the uri
     * @param version the version
     * @param daoCallbackService the dao callback service
     */
    protected void addSupportedFormats(final String uri, final String version, DaoCallbackService daoCallbackService) {
        List<String> items = daoCallbackService.executeInDaoLayer(new DaoCallback<List<String>>(){

            public List<String> execute(DaoManager daoManager) {
               CodingSchemeDao csDao = daoManager.getCodingSchemeDao(uri, version);
               String csId = csDao.getCodingSchemeUIdByUriAndVersion(uri, version);
               
               return daoManager.getCodingSchemeDao(uri, version).getDistinctFormatsOfCodingScheme(csId);
            }  
        });
        
        for(String item : items) {
            SupportedDataType supportedFormat = new SupportedDataType();
            supportedFormat.setContent(item);
            supportedFormat.setLocalId(item);
            insertURIMap(uri, version, supportedFormat);
        }
    }
    
    /**
     * Adds the supported languages.
     * 
     * @param uri the uri
     * @param version the version
     * @param daoCallbackService the dao callback service
     */
    protected void addSupportedLanguages(final String uri, final String version, DaoCallbackService daoCallbackService) {
        List<String> items = daoCallbackService.executeInDaoLayer(new DaoCallback<List<String>>(){

            public List<String> execute(DaoManager daoManager) {
               CodingSchemeDao csDao = daoManager.getCodingSchemeDao(uri, version);
               String csId = csDao.getCodingSchemeUIdByUriAndVersion(uri, version);
               
               return daoManager.getCodingSchemeDao(uri, version).getDistinctLanguagesOfCodingScheme(csId);
            }  
        });
        
        for(String item : items) {
            SupportedLanguage supportedLanguage = new SupportedLanguage();
            supportedLanguage.setContent(item);
            supportedLanguage.setLocalId(item);
            insertURIMap(uri, version, supportedLanguage);
        }
    }
    
    /**
     * Adds the supported entity types.
     * 
     * @param uri the uri
     * @param version the version
     * @param daoCallbackService the dao callback service
     */
    protected void addSupportedEntityTypes(final String uri, final String version, DaoCallbackService daoCallbackService) {
        List<String> items = daoCallbackService.executeInDaoLayer(new DaoCallback<List<String>>(){

            public List<String> execute(DaoManager daoManager) {
               CodingSchemeDao csDao = daoManager.getCodingSchemeDao(uri, version);
               String csId = csDao.getCodingSchemeUIdByUriAndVersion(uri, version);
               
               return daoManager.getCodingSchemeDao(uri, version).getDistinctEntityTypesOfCodingScheme(csId);
            }  
        });
        
        for(String item : items) {
            SupportedEntityType supportedEntityType = new SupportedEntityType();
            supportedEntityType.setContent(item);
            supportedEntityType.setLocalId(item);
            insertURIMap(uri, version, supportedEntityType);
        }
    }
    
    /**
     * Adds the supported properties.
     * 
     * @param uri the uri
     * @param version the version
     * @param daoCallbackService the dao callback service
     */
    protected void addSupportedProperties(final String uri, final String version, DaoCallbackService daoCallbackService) {
        List<NameAndValue> items = daoCallbackService.executeInDaoLayer(new DaoCallback<List<NameAndValue>>(){

            public List<NameAndValue> execute(DaoManager daoManager) {
               CodingSchemeDao csDao = daoManager.getCodingSchemeDao(uri, version);
               String csId = csDao.getCodingSchemeUIdByUriAndVersion(uri, version);
               
               return daoManager.getCodingSchemeDao(uri, version).getDistinctPropertyNameAndType(csId);
            }  
        });
        
        for(NameAndValue item : items) {
            SupportedProperty uriMap = new SupportedProperty();
            uriMap.setContent(item.getName());
            uriMap.setLocalId(item.getName());
            uriMap.setPropertyType(PropertyTypes.fromValue(item.getContent().toLowerCase()));
            insertURIMap(uri, version, uriMap);
        }
    }
    
    /**
     * Insert uri map.
     * 
     * @param uri the uri
     * @param version the version
     * @param uriMap the uri map
     */
    protected void insertURIMap(String uri, String version, URIMap uriMap) {
        LexEvsServiceLocator.getInstance().getDatabaseServiceManager().
            getCodingSchemeService().
                updateURIMap(uri, version, uriMap);
    }
}