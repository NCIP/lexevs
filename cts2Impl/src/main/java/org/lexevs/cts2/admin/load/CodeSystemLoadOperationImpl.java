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
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.loaders.BaseLoader;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.Revision;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.author.AuthoringCore;
import org.lexevs.cts2.core.update.RevisionInfo;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Implementation of LexEVS CTS 2 Code System Load Operation.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class CodeSystemLoadOperationImpl extends AuthoringCore implements CodeSystemLoadOperation {
	
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#loadCodeSystemRevsion()
	 */
	@Override
	public void loadCodeSystemRevsion(
			CodingScheme codingScheme, 
			RevisionInfo revisionInfo)
			throws LBException {
		Revision revision = this.getLexGridRevisionObject(revisionInfo);
		
		ChangedEntry entry = new ChangedEntry();
		entry.setChangedCodingSchemeEntry(codingScheme);
		
		revision.addChangedEntry(entry);
		
		this.getDatabaseServiceManager().getAuthoringService().loadRevision(
				revision, 
				revisionInfo.getSystemReleaseURI(), null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#load(org.LexGrid.codingSchemes.CodingScheme, java.net.URI, java.lang.Boolean, java.lang.Boolean, java.lang.Boolean, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public URNVersionPair[] load(
			CodingScheme codeSystem, 
			URI metadata, 
			Boolean stopOnErrors, 
			Boolean async, 
			Boolean overwriteMetadata, 
			String versionTag, 
			Boolean activate) throws LBException {
		if (codeSystem == null)
			throw new LBException("Code System can not be empty");
		
		CodingSchemeLoader codingSchemeLoader = new CodingSchemeLoader(codeSystem);
		codingSchemeLoader.load(stopOnErrors, async);

		// load code system meta data if provided
		if (metadata != null)
		{
			
			loadCSMetaData(codeSystem.getCodingSchemeURI(), codeSystem.getRepresentsVersion(), metadata, overwriteMetadata, stopOnErrors, async);
		}
		
		// apply version tag if provided		
		if (StringUtils.isNotEmpty(versionTag))
		{
			applyCSTag(codeSystem.getCodingSchemeURI(), codeSystem.getRepresentsVersion(), versionTag);
		}
		
		// activate loaded code system if specified
		if (activate)
		{
			activateCodeSystem(codeSystem.getCodingSchemeURI(), codeSystem.getRepresentsVersion());
		}		
        
		URNVersionPair urnVersionPair = new URNVersionPair(codeSystem.getCodingSchemeURI(), codeSystem.getRepresentsVersion());
		return new URNVersionPair[] {urnVersionPair};
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#applyMetadataToCodeSystem(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.net.URI, java.lang.Boolean, java.lang.Boolean, java.lang.Boolean)
	 */
	@Override
	public URNVersionPair applyMetadataToCodeSystem(String codeSystemNameOrURI, CodingSchemeVersionOrTag codeSystemVersionOrTag, URI metadata, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata) throws LBException
	{
		CodingScheme codeSystem = getLexBIGService().resolveCodingScheme(codeSystemNameOrURI, codeSystemVersionOrTag);
		
		URNVersionPair urnVersionPair = null;
		if (codeSystem != null)
		{
			loadCSMetaData(codeSystem.getCodingSchemeURI(), codeSystem.getRepresentsVersion(), metadata, overwriteMetadata, stopOnErrors, async);
			
			urnVersionPair = new URNVersionPair(codeSystem.getCodingSchemeURI(), codeSystem.getRepresentsVersion());
		}
		
		return urnVersionPair;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#load(java.net.URI, java.net.URI, java.net.URI, java.lang.String, java.lang.Boolean, java.lang.Boolean, java.lang.Boolean, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public URNVersionPair[] load(URI source, URI metadata, URI manifest, String loaderName, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata, String versionTag, Boolean activate) throws LBException{
		if (loaderName == null)
			throw new LBException("Code System loader must be specified. Use LexEVSCTS2.getSupportedCodeSystemLoaders for supported list of loaders in the service.");
		
		if (!this.getLexEvsCTS2().getSupportedLoaderNames().contains(loaderName))
		{
			throw new LBException("Provided Code System loader not supported. Use LexEVSCTS2.getSupportedCodeSystemLoaders/LoaderNames for supported list of loaders in the service.");
		}
		
		return loadSource(source, loaderName, metadata, manifest, stopOnErrors, async, overwriteMetadata, versionTag, activate);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#activateCodeSystem(java.lang.String, java.lang.String)
	 */
	public boolean activateCodeSystem(String codeSystemURI, String codeSyatemVersion) throws LBException{
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN(codeSystemURI);
		acsvr.setCodingSchemeVersion(codeSyatemVersion);
		getLexBIGServiceManager().activateCodingSchemeVersion(acsvr);
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#deactivateCodeSystem(java.lang.String, java.lang.String)
	 */
	public boolean deactivateCodeSystem(String codeSystemURI, String codeSyatemVersion) throws LBException{
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN(codeSystemURI);
		acsvr.setCodingSchemeVersion(codeSyatemVersion);
		getLexBIGServiceManager().deactivateCodingSchemeVersion(acsvr, new Date());
		return true;
	}
	
	
	private URNVersionPair[] loadSource(URI source, String loaderName, URI metadata, URI manifest, Boolean stopOnErrors, Boolean async, Boolean overwriteMetadata, String versionTag, Boolean activate) throws LBException{
		Loader loader = getLexBIGServiceManager().getLoader(loaderName);
        loader.getOptions().getBooleanOption(BaseLoader.FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
        loader.getOptions().getBooleanOption(BaseLoader.ASYNC_OPTION).setOptionValue(async);
        loader.setCodingSchemeManifestURI(manifest);
        loader.load(source);
        
        while (loader.getStatus().getEndTime() == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {				
			}
		}
		
        if (metadata != null)
        {
        	loadCSMetaData(loader, metadata, overwriteMetadata, stopOnErrors, async);
        }
        
        if (BooleanUtils.isTrue(activate))
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
	
	private void loadCSMetaData(String csURI, String csVersion, URI metadata, boolean overwriteMetadata, boolean stopOnErrors, boolean async) throws LBException{
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN(csURI);
		acsvr.setCodingSchemeVersion(csVersion);
        MetaData_Loader loader = (MetaData_Loader) getLexBIGServiceManager().getLoader("MetaDataLoader");
        loader.loadAuxiliaryData(metadata, acsvr, overwriteMetadata, stopOnErrors, async);
	}
	
	private void loadCSMetaData(Loader loader, URI metadata, boolean overwriteMetadata, boolean stopOnErrors, boolean async) throws LBException{
		AbsoluteCodingSchemeVersionReference[] refs = loader.getCodingSchemeReferences();
        for (int i = 0; i < refs.length; i++) {
            AbsoluteCodingSchemeVersionReference ref = refs[i];
            MetaData_Loader metaLoader = (MetaData_Loader) getLexBIGServiceManager().getLoader("MetaDataLoader");
            metaLoader.loadAuxiliaryData(metadata, ref, overwriteMetadata, stopOnErrors, async);
		}
	}
	
	private void applyCSTag(String csURI, String csVersion, String versionTag) throws LBException{
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN(csURI);
		acsvr.setCodingSchemeVersion(csVersion);
		getLexBIGServiceManager().setVersionTag(acsvr, versionTag);
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
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.CodeSystemLoadOperation#getSupportedLoaderNames()
	 */
	@Override
	public List<String> getSupportedLoaderNames() throws LBException {
		return this.getLexEvsCTS2().getSupportedLoaderNames();
	}

	private class CodingSchemeLoader extends BaseLoader {

		private static final long serialVersionUID = 2623487391846644470L;
		
		private CodingScheme codingScheme;
		
		private CodingSchemeLoader(CodingScheme codingScheme){
			this.codingScheme = codingScheme;
			this.setDoApplyPostLoadManifest(false);
		}
		
		public void load(boolean stopOnErrors, boolean async) {
			this.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
	        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
			this.load(null);
		}
		
		@Override
		protected OptionHolder declareAllowedOptions(OptionHolder holder) {
			return holder;
		}

		@Override
		protected URNVersionPair[] doLoad() throws Exception {
			this.persistCodingSchemeToDatabase(this.codingScheme);
			
			return this.constructVersionPairsFromCodingSchemes(codingScheme);
		}

		@Override
		protected ExtensionDescription buildExtensionDescription() {
			return null;
		}
	}
	
	public static void main(String[] args){
//		LexEvsCTS2 lexevsCTS2 = LexEvsCTS2Impl.defaultInstance();
//		CodeSystemLoadOperation csLoad = lexevsCTS2.getAdminOperation().getCodeSystemLoadOperation();
		
	}
}
