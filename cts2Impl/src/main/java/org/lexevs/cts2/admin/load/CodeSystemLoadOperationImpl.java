/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and Research
 * (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the triple-shield Mayo
 * logo are trademarks and service marks of MFMER.
 * 
 * Except as contained in the copyright notice above, or as used to identify
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.lexevs.cts2.admin.load;

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.BaseLoader;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.Revision;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.BaseService.LoadFormats;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.RootOrTail;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.springframework.util.Assert;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * @author m004181
 *
 */
public class CodeSystemLoadOperationImpl extends BaseLoader implements CodeSystemLoadOperation {
	
	private static final long serialVersionUID = 1L;
	public static final String name = "LexEVSCTS2CodeSystemLoader";
    public static final String description = "This loader loads Code System into the LexGrid database.";
    
//    private CodingSchemeService csServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService();
    private CodingScheme codeSystem_;
    private URI source_;
    private URI metadata_;
    private URI manifest_;
    private String versionTag_;
    private Boolean activate_;
    private Boolean overwriteMetadata_;
    private Boolean stopOnErrors_;
    private Boolean async_;
    private LoadFormats loadFormat_ = null;
    private LexBIGServiceManager lbsm_ = null;
    
	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Impl.loaders.BaseLoader#declareAllowedOptions(org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder)
	 */
	@Override
	protected OptionHolder declareAllowedOptions(OptionHolder holder) {
		return holder;
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Impl.loaders.BaseLoader#doLoad()
	 */
	@Override
	protected URNVersionPair[] doLoad() throws Exception {
		this.setCodingSchemeManifestURI(manifest_);
		this.persistCodingSchemeToDatabase(codeSystem_);
        
		URNVersionPair urnVersion = new URNVersionPair(codeSystem_.getCodingSchemeURI(), codeSystem_.getRepresentsVersion());
        
        this.buildRootNode(
                Constructors.createAbsoluteCodingSchemeVersionReference(
                		codeSystem_.getCodingSchemeURI(), codeSystem_.getRepresentsVersion()), 
                null, 
                getRelationsContainerName(codeSystem_), 
                RootOrTail.TAIL,
                TraverseAssociations.TOGETHER);
        
        return new URNVersionPair[]{urnVersion};
	}
	
	private String getRelationsContainerName(CodingScheme codingScheme) {
        Relations[] relations = codingScheme.getRelations();
        Assert.state(relations.length == 1);
        
        return relations[0].getContainerName();
    }
	
	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
	 */
	@Override
	protected ExtensionDescription buildExtensionDescription() {
		ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(CodeSystemLoadOperationImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(CodeSystemLoadOperationImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);

        return temp;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#changeCodeSystemStatus()
	 */
	@Override
	public void changeCodeSystemStatus() throws LBException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#importCodeSystem()
	 */
	@Override
	public int importCodeSystem() throws LBException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#importCodeSystemRevsion(org.LexGrid.versions.Revision)
	 */
	@Override
	public int importCodeSystemRevsion(Revision codeSystemRevision)
			throws LBException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#importCodeSystemRevsion(java.lang.String)
	 */
	@Override
	public int importCodeSystemRevsion(String xmlFileLocation)
			throws LBException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void validate(URI source, URI metaData, int validationLevel)
			throws LBException {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#load(org.LexGrid.codingSchemes.CodingScheme, java.net.URI, java.lang.Boolean, java.lang.Boolean, java.lang.Boolean, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public URI load(CodingScheme codeSystem, URI metadata, URI manifest, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata, String versionTag, Boolean activate) throws LBException {
		if (codeSystem == null)
			throw new LBException("Code System can not be empty");
		
		URI csURI = null;
		this.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        codeSystem_ = codeSystem;
		metadata_ = metadata;
		versionTag_ = versionTag;
		activate_ = activate;
		overwriteMetadata_ = overwriteMetadata;
		async_ = async;
		stopOnErrors_ = stopOnErrors;
		manifest_ = manifest;
		
		try {
			csURI = new URI(codeSystem.getCodingSchemeURI());
		} catch (URISyntaxException e) {
			throw new LBException(e.getMessage());
		}
        
		this.load(null);		
		
		while (this.getStatus().getEndTime() == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {				
			}
		}
		
		// load code system metadata if provided
		if (metadata_ != null)
		{
			
			loadCSMetaData(codeSystem_.getCodingSchemeURI(), codeSystem_.getRepresentsVersion(), metadata_, overwriteMetadata_, stopOnErrors_, async_);
		}
		
		// apply version tag if provided		
		if (StringUtils.isNotEmpty(versionTag_))
		{
			applyCSTag(codeSystem_.getCodingSchemeURI(), codeSystem_.getRepresentsVersion(), versionTag_);
		}
		
		// activate loaded code system if specified
		if (activate_)
		{
			activateCS(codeSystem_.getCodingSchemeURI(), codeSystem_.getRepresentsVersion());
		}		
        
		return csURI;
	}	
	
	@Override
	public URNVersionPair[] load(URI source, URI metadata, URI manifest, URI releaseURI, LoadFormats loadFormat, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata, String versionTag, Boolean activate) throws LBException{
		if (loadFormat == null)
			throw new LBException("Load Format is not specified");
		
		this.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        
		URNVersionPair[] urnVersions = null;
		
		urnVersions = loadOBO(source, metadata, manifest, releaseURI, loadFormat, stopOnErrors, async, overwriteMetadata, versionTag, activate);
		
		return urnVersions;
	}
	
	private URNVersionPair[] loadOBO(URI source, URI metadata, URI manifest, URI releaseURI, LoadFormats loadFormat, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata, String versionTag, Boolean activate) throws LBException{
        OBO_Loader loader = (OBO_Loader) getLexBIGServiceManager().getLoader(org.LexGrid.LexBIG.Impl.loaders.OBOLoaderImpl.name);
        loader.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
        loader.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        loader.setCodingSchemeManifestURI(manifest);
        loader.load(source, metadata, stopOnErrors, async);
        
        while (loader.getStatus().getEndTime() == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {				
			}
		}
		
        if (activate)
        {
        	activateCS(loader);
        }

        if (StringUtils.isNotEmpty(versionTag))
        {
        	applyCSTag(loader, versionTag);
        }
		
        URNVersionPair urnVersion = null;
        AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
    	for (int i = 0; i < refs.length; i++) {
    		AbsoluteCodingSchemeVersionReference ref = refs[i];
    		urnVersion = new URNVersionPair(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion());    		
    	}
        
    	return new URNVersionPair[]{urnVersion};
	}
	
	private URI loadOWL(URI source, URI metadata, URI manifest, URI releaseURI, LoadFormats loadFormat, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata, String versionTag, Boolean activate) throws LBException{
		
		return null;
	}
	
	private URI loadRRF(URI source, URI metadata, URI manifest, URI releaseURI, LoadFormats loadFormat, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata, String versionTag, Boolean activate) throws LBException{
		
		return null;
	}
	
	private URI loadLGXML(URI source, URI metadata, URI manifest, URI releaseURI, LoadFormats loadFormat, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata, String versionTag, Boolean activate) throws LBException{
		LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        LexGrid_Loader loader = (LexGrid_Loader) getLexBIGServiceManager().getLoader(org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl.name);
        
        loader.setCodingSchemeManifestURI(manifest);
        loader.load(source, stopOnErrors, async);
        
        if (metadata != null)
        {
        	AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
        	for (int i = 0; i < refs.length; i++) {
        		AbsoluteCodingSchemeVersionReference ref = refs[i];
        		loadCSMetaData(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion(), metadata, overwriteMetadata, stopOnErrors, async);
        	}
        }
        
        
		return null;
	}
	
	private URI loadClaML(URI source, URI metadata, URI manifest, URI releaseURI, LoadFormats loadFormat, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata, String versionTag, Boolean activate) throws LBException{
		
		return null;
	}
	
	private URI loadHL7(URI source, URI metadata, URI manifest, URI releaseURI, LoadFormats loadFormat, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata, String versionTag, Boolean activate) throws LBException{
		
		return null;
	}
	
	private void loadCSMetaData(String csURI, String csVersion, URI metadata, boolean overwriteMetadata, boolean stopOnErrors, boolean async) throws LBException{
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN(csURI);
		acsvr.setCodingSchemeVersion(csVersion);
        MetaData_Loader loader = (MetaData_Loader) getLexBIGServiceManager().getLoader("MetaDataLoader");
        loader.loadAuxiliaryData(metadata, acsvr, overwriteMetadata, stopOnErrors, async);
	}
	
	private void applyCSTag(String csURI, String csVersion, String versionTag) throws LBException{
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN(csURI);
		acsvr.setCodingSchemeVersion(csVersion);
		getLexBIGServiceManager().setVersionTag(acsvr, versionTag);
	}
	
	private void activateCS(String csURI, String csVersion) throws LBException{
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN(csURI);
		acsvr.setCodingSchemeVersion(csVersion);
		getLexBIGServiceManager().activateCodingSchemeVersion(acsvr);
	}
	
	private void activateCS(Loader loader) throws LBException{
		AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
        for (int i = 0; i < refs.length; i++) {
            AbsoluteCodingSchemeVersionReference ref = refs[i];
            getLexBIGServiceManager().activateCodingSchemeVersion(ref);
        }
	}
	
	private void applyCSTag(Loader loader, String versionTag) throws LBException{
		AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
        for (int i = 0; i < refs.length; i++) {
            AbsoluteCodingSchemeVersionReference ref = refs[i];
            getLexBIGServiceManager().setVersionTag(ref, versionTag);
        }
	}
	
	private LexBIGServiceManager getLexBIGServiceManager() throws LBParameterException, LBInvocationException{
		if (lbsm_ == null)
			lbsm_ = LexBIGServiceImpl.defaultInstance().getServiceManager(null);
		
		return lbsm_;
	}
}
