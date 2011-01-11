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

import java.util.UUID;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The Class ApproxNumOfConceptsPostProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ApproxNumOfConceptsPostProcessor extends AbstractExtendable implements LoaderPostProcessor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2828520523031693573L;
    
    /** The EXTENSIO n_ name. */
    public static String EXTENSION_NAME = "ApproxNumOfConceptsPostProcessor";

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
        ed.setDescription("ApproxNumOfConceptsPostProcessor");
        ed.setName(EXTENSION_NAME);
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(this.getClass().getName());
        
        return ed;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.postprocessor.LoaderPostProcessor#runPostProcess(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, org.LexGrid.LexBIG.Extensions.Load.OntologyFormat)
     */
    public void runPostProcess(AbsoluteCodingSchemeVersionReference reference, OntologyFormat ontFormat) {
        EntityService entityService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getEntityService();
        CodingSchemeService codingSchemeService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService();
        
        final String uri = reference.getCodingSchemeURN();
        final String version = reference.getCodingSchemeVersion();
        
        long entities = entityService.getEntityCount(uri, version);
        
        final CodingScheme codingScheme = codingSchemeService.getCodingSchemeByUriAndVersion(uri, version);
        
        codingScheme.setApproxNumConcepts(entities);
        
        CodingScheme csToUpdate = new CodingScheme();
        csToUpdate.setApproxNumConcepts(entities);
        csToUpdate.setCodingSchemeURI(codingScheme.getCodingSchemeURI());
        csToUpdate.setRepresentsVersion(codingScheme.getRepresentsVersion());
        
        String revisionId = UUID.randomUUID().toString();
        Revision revision = new Revision();
        revision.setRevisionId(revisionId);

        EntryState es = new EntryState();
        es.setContainingRevision(revisionId);
        es.setChangeType(ChangeType.MODIFY);
        
        csToUpdate.setEntryState(es);
        
        ChangedEntry ce = new ChangedEntry();
        ce.setChangedCodingSchemeEntry(csToUpdate);
        
        revision.addChangedEntry(ce);
        
        try {
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService().loadRevision(revision, null, false);
        } catch (LBRevisionException e) {
            throw new RuntimeException(e);
        }
    }
}