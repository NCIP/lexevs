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

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;

/**
 * The Class OntologyFormatAddingPostProcessor.
 */
public class OntologyFormatAddingPostProcessor extends AbstractExtendable implements LoaderPostProcessor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2828520523031693573L;
    
    /** The EXTENSION Name*/
    public static String EXTENSION_NAME = "OntologyFormatAddingPostProcessor";
    
    @Override
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
        registry.registerGenericExtension(description);
    }
 
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription("OntologyFormatAddingPostProcessor");
        ed.setName(EXTENSION_NAME);
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(this.getClass().getName());
        
        return ed;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor#runPostProcess(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, org.LexGrid.LexBIG.Extensions.Load.OntologyFormat)
     */
    public void runPostProcess(AbsoluteCodingSchemeVersionReference reference, OntologyFormat ontFormat) {
        if(ontFormat == null) {
            LoggerFactory.getLogger().warn("Skipping: " + this.getName() +
                    " -- loader is not specifying a Format type.");
            return;
        }
        
        CodingSchemeService codingSchemeService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService();
        DaoCallbackService daoCallbackService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getDaoCallbackService();
        
        final String uri = reference.getCodingSchemeURN();
        final String version = reference.getCodingSchemeVersion();
        
        
        final CodingScheme codingScheme = codingSchemeService.getCodingSchemeByUriAndVersion(uri, version);
        
        if(codingScheme.getProperties() == null) {
            Properties properties = new Properties();
            codingScheme.setProperties(properties);
        }
        final Property prop = new Property();
        prop.setPropertyName(OntologyFormat.getMetaName());
        prop.setPropertyId(OntologyFormat.getMetaName());
        Text t = new Text();
        t.setContent(ontFormat.toString());
        prop.setValue(t);
        codingScheme.getProperties().addProperty(prop);
        
        try {
            daoCallbackService.executeInDaoLayer(new DaoCallback<Void>() {

                @Override
                public Void execute(DaoManager daoManager) {
                   String codingSchemeUid = daoManager.getCodingSchemeDao(uri, version).getCodingSchemeUIdByUriAndVersion(uri, version);
                   PropertyDao dao = daoManager.getPropertyDao(uri, version);
                   dao.insertProperty(codingSchemeUid, codingSchemeUid, PropertyType.CODINGSCHEME, prop);
                   return null;
                }
            });
            
            updateRegistryForCodingSchemeMetadata(uri, version, codingScheme.getFormalName(), ontFormat);
        } catch (Exception e) {
           LoggerFactory.getLogger().warn("Post Process failed -- Load will not be rolled back.", e);
        }  
    }
    
    public void updateRegistryForCodingSchemeMetadata(String uri, String version, String designation,  OntologyFormat format) throws LBParameterException{
        Registry registry = LexEvsServiceLocator.getInstance().getRegistry();
        AbsoluteCodingSchemeVersionReference codingScheme = Constructors.
                createAbsoluteCodingSchemeVersionReference(uri, version);
        RegistryEntry entry = registry.getCodingSchemeEntry(codingScheme);
        entry.setDbName(format.name());
        entry.setDbUri(designation);
    }
}