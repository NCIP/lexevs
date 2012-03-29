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
package org.lexgrid.loader.rxn.launch;

import java.io.File;
import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.types.LogLevel;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.RxNormBatchLoader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.lexgrid.loader.rxn.RxnBatchLoaderImpl;
import org.springframework.batch.core.JobExecution;

/**
 * The Class RxnBatchLoaderLauncher.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RxnBatchLoaderLauncher{
	
	 /** The rrf dir. */
 	@Option(name="-in")   
	 private String rrfDir;
	 
	 /** The sab. */
 	@Option(name="-s")   
	 private String sab;
	 
	 /** The uri. */
 	@Option(name="-uri")   
	 private String uri;
	 
	 /** The version. */
 	@Option(name="-version")   
	 private String version;

	 /**
 	 * The main method.
 	 * 
 	 * @param args the arguments
 	 * 
 	 * @throws Exception the exception
 	 */
 	public static void main(String[] args) throws Exception{
		 RxnBatchLoaderLauncher launcher = new RxnBatchLoaderLauncher();
		 CmdLineParser parser = new CmdLineParser(launcher);
		 parser.parseArgument(args);	
		 
		 launcher.load();
	 }
	 
	/**
	 * Load.
	 * 
	 * @throws Exception the exception
	 */
	private void load() throws Exception {
		RxnBatchLoaderImpl loader = (RxnBatchLoaderImpl)LexBIGServiceImpl.defaultInstance().getServiceManager(null).getLoader("RxNormBatchLoader");
//		RxnBatchLoaderImpl loader = new RxnBatchLoaderImpl();
				
		if(uri == null && version == null){
			loader.loadRxn(new File(rrfDir).toURI(), sab);
		} else {
			loader.resumeRxn(new File(rrfDir).toURI(), sab,  uri, version);
		}	
	}


	/**
	 * Gets the rrf dir.
	 * 
	 * @return the rrf dir
	 */
	public String getRrfDir() {
		return rrfDir;
	}


	/**
	 * Sets the rrf dir.
	 * 
	 * @param rrfDir the new rrf dir
	 */
	public void setRrfDir(String rrfDir) {
		this.rrfDir = rrfDir;
	}


	/**
	 * Gets the sab.
	 * 
	 * @return the sab
	 */
	public String getSab() {
		return sab;
	}


	/**
	 * Sets the sab.
	 * 
	 * @param sab the new sab
	 */
	public void setSab(String sab) {
		this.sab = sab;
	}

//	public JobExecution getJobExecution() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public void load(URI resource) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public OptionHolder getOptions() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public void clearLog() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public AbsoluteCodingSchemeVersionReference[] getCodingSchemeReferences() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public LogEntry[] getLog(LogLevel level) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public LoadStatus getStatus() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public void setCodingSchemeManifest(
//			CodingSchemeManifest codingSchemeManifest) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public CodingSchemeManifest getCodingSchemeManifest() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public void setCodingSchemeManifestURI(URI codingSchemeManifestUri)
//			throws LBException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public URI getCodingSchemeManifestURI() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public LoaderPreferences getLoaderPreferences() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public void setLoaderPreferences(LoaderPreferences loaderPreferences)
//			throws LBParameterException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public void setLoaderPreferences(URI loaderPreferencesURI)
//			throws LBParameterException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public OntologyFormat getOntologyFormat() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public String getName() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public String getDescription() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public String getProvider() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public String getVersion() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	public void removeLoad(String uri, String version)
//			throws LBParameterException {
//		// TODO Auto-generated method stub
//		
//	}
}
