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

import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.lexgrid.loader.rxn.RxnBatchLoaderImpl;

/**
 * The Class RxnBatchLoaderLauncher.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RxnBatchLoaderLauncher {
	
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
//		RxnBatchLoader loader = (RxnBatchLoader)LexBIGServiceImpl.defaultInstance().getServiceManager(null).getLoader("RxnBatchLoader");
		RxnBatchLoaderImpl loader = new RxnBatchLoaderImpl();
				
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
}
