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
package org.lexgrid.loader.umls;

import java.io.File;
import java.net.URI;
import java.util.Properties;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.MedRtUmlsBatchLoader;
import org.LexGrid.LexBIG.Extensions.Load.MetaBatchLoader;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.lexevs.dao.database.spring.DynamicPropertyApplicationContext;
import org.lexgrid.loader.AbstractSpringBatchLoader;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.properties.ConnectionPropertiesFactory;
import org.lexgrid.loader.properties.impl.DefaultLexEVSPropertiesFactory;
import org.lexgrid.loader.setup.JobRepositoryManager;
import org.lexgrid.loader.staging.StagingManager;
import org.springframework.context.ApplicationContext;

import edu.mayo.informatics.lexgrid.convert.options.StringOption;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * The Class UmlsBatchLoaderImpl.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MedRtUmlsBatchLoaderImpl extends AbstractSpringBatchLoader implements MedRtUmlsBatchLoader {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/** The connection properties factory. */
private ConnectionPropertiesFactory connectionPropertiesFactory = new DefaultLexEVSPropertiesFactory();

	public static String SAB_OPTION = "SAB";
	public static final String SAB = "MED-RT";
	
	public MedRtUmlsBatchLoaderImpl(){
		super();
		super.setDoIndexing(false);
		super.setDoRegister(false);
		super.setDoComputeTransitiveClosure(false);
	}

	/** The UML s_ loade r_ config. */
	private String UMLS_LOADER_CONFIG = "medrtUmlsLoader.xml";

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.umls.UmlsBatchLoader#loadUmls(java.lang.String, java.lang.String)
	 */
	public void loadMEDRT(URI rrfDir) throws Exception {
		this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(false);
		this.getOptions().getStringOption(SAB_OPTION).setOptionValue(SAB);
		
		this.load(rrfDir);
	}	
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.umls.UmlsBatchLoader#resumeUmls(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void resumeMEDRT(URI rrfDir, String uri, String version) throws Exception {
		Properties connectionProps = connectionPropertiesFactory.getPropertiesForExistingLoad(uri, version);
		connectionProps.put("sab", SAB);
		connectionProps.put("rrfDir", rrfDir.toString());
		connectionProps.put("retry", "true");
		launchJob(connectionProps, UMLS_LOADER_CONFIG, "umlsJob");
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.umls.UmlsBatchLoader#removeLoad(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void removeLoad(String uri, String version) throws LBParameterException  {
		Properties connectionProps = connectionPropertiesFactory.getPropertiesForExistingLoad(uri, version);
		connectionProps.put("retry", "true");
		
		DynamicPropertyApplicationContext ctx = new DynamicPropertyApplicationContext("umlsLoaderStaging.xml", connectionProps);
		
		JobRepositoryManager jobRepositoryManager = (JobRepositoryManager)ctx.getBean("jobRepositoryManager");
		try {
			jobRepositoryManager.dropJobRepositoryDatabases();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		StagingManager stagingManager = (StagingManager)ctx.getBean("umlsStagingManager");
		try {
			stagingManager.dropAllStagingDatabases();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
    
    @Override
	public String getName() {
		return MedRtUmlsBatchLoader.NAME;
	}

    public static void main(String[] args) throws Exception { 
    	System.setProperty("LG_CONFIG_FILE", "src/test/resources/lbconfig.props");

    	MedRtUmlsBatchLoader ubl = new MedRtUmlsBatchLoaderImpl();
		 //ubl.loadUmls(new File("/home/LargeStorage/ontologies/rrf/snomed-ct/2009AA").toURI(), "SNOMEDCT");
		// mbl.loadMeta("/home/LargeStorage/ontologies/rrf/LNC/LNC226");
    	ubl.loadMEDRT(new File("src/test/resources/data/sample-air").toURI());
	 }

	@Override
	protected OptionHolder declareAllowedOptions(OptionHolder holder) {
		holder.setIsResourceUriFolder(true);
		holder.getStringOptions().add(new StringOption(SAB_OPTION));
		return holder;
	}

	@Override
	protected URNVersionPair[] doLoad() {
		try {
			Properties connectionProps = connectionPropertiesFactory.getPropertiesForNewLoad();	
			connectionProps.put("sab", this.getOptions().getStringOption(SAB_OPTION).getOptionValue());
			connectionProps.put("rrfDir", this.getResourceUri().toString());
			connectionProps.put("retry", "false");
				
			// Verify the SAB is valid before continuing
			UmlsBatchLoaderSabVerifier verifier = new UmlsBatchLoaderSabVerifier(connectionProps);
			
			boolean isValid = verifier.isSabValid();
			if (!isValid) {
				throw new RuntimeException("Invalid SAB: " + this.getOptions().getStringOption(SAB_OPTION).getOptionValue());
			}
			
			launchJob(connectionProps, UMLS_LOADER_CONFIG, "umlsJob");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this.getLoadedCodingSchemes();
	}

	@Override
	protected URNVersionPair[] getLoadedCodingSchemes(ApplicationContext context) {
		CodingSchemeIdSetter codingSchemeIdSetter = (CodingSchemeIdSetter)context.getBean("umlsCodingSchemeIdSetter");
		
		URNVersionPair scheme = new URNVersionPair(
				codingSchemeIdSetter.getCodingSchemeUri(), 
				codingSchemeIdSetter.getCodingSchemeVersion());
		return new URNVersionPair[]{scheme};
	}
	
	@Override
	protected ExtensionDescription buildExtensionDescription() {
		ExtensionDescription meta = new ExtensionDescription();
		meta.setExtensionBaseClass(MetaBatchLoader.class.getName());
		meta.setExtensionClass("org.lexgrid.loader.umls.MedRtUmlsBatchLoaderImpl");
		meta.setDescription(MetaBatchLoader.DESCRIPTION);
		meta.setName(MetaBatchLoader.NAME);
		meta.setVersion(MetaBatchLoader.VERSION);
		return meta;
	}
	
	@Override
    public OntologyFormat getOntologyFormat() {
        return OntologyFormat.UMLS;
    }
}