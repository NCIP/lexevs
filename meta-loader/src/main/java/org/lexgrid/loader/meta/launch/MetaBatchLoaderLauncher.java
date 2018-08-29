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
package org.lexgrid.loader.meta.launch;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Extensions.Load.MetaBatchLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.lexgrid.loader.AbstractSpringBatchLoader;

/**
 * The Class UmlsBatchLoaderLauncher.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaBatchLoaderLauncher {
	
	 /** The rrf dir. */
 	@Option(name="-in")   
	 private String rrfDir;
	 
	 /** The uri. */
 	@Option(name="-uri")   
	 private String uri;
	 
	 /** The version. */
 	@Option(name="-version")   
	 private String version;

 	 private AbsoluteCodingSchemeVersionReference[] codingSchemeRefs;
 	
	 /**
 	 * The main method.
 	 * 
 	 * @param args the arguments
 	 * 
 	 * @throws Exception the exception
 	 */
 	public static void main(String[] args) throws Exception{
		 MetaBatchLoaderLauncher launcher = new MetaBatchLoaderLauncher();
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
		MetaBatchLoader loader = (MetaBatchLoader)LexBIGServiceImpl.defaultInstance().getServiceManager(null).getLoader("MetaBatchLoader");
				
		if(uri == null && version == null){
			loader.loadMeta(
					AbstractSpringBatchLoader.getURIFromPath(rrfDir));
		} else {
			loader.resumeMeta(
					AbstractSpringBatchLoader.getURIFromPath(rrfDir), 
					uri, 
					version);
		}	
		setCodingSchemeRefs(loader.getCodingSchemeReferences());
	}

	/**
	 * Load and wait.
	 * 
	 * @throws Exception the exception
	 */
	public void loadAndWait() throws Exception {
		MetaBatchLoader loader = (MetaBatchLoader)LexBIGServiceImpl.defaultInstance().getServiceManager(null).getLoader("MetaBatchLoader");
				
		if(uri == null && version == null){
			loader.loadMeta(
					AbstractSpringBatchLoader.getURIFromPath(rrfDir));
		} else {
			loader.resumeMeta(
					AbstractSpringBatchLoader.getURIFromPath(rrfDir), 
					uri, 
					version);
		}	
		
		// wait for the load to complete, then return.
		while (loader.getStatus().getEndTime() == null) {
	            Thread.sleep(500);
	    }
		
		setCodingSchemeRefs(loader.getCodingSchemeReferences());
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
	 * Sets the AbsoluteCodingSchemeVersionReference.
	 * 
	 * @param refs the new AbsoluteCodingSchemeVersionReference
	 */
	private void setCodingSchemeRefs(AbsoluteCodingSchemeVersionReference[] refs ) {
		this.codingSchemeRefs = refs;
	}
	
	/**
	 * Gets the AbsoluteCodingSchemeVersionReference.
	 * 
	 * @return the AbsoluteCodingSchemeVersionReference
	 */
	public AbsoluteCodingSchemeVersionReference[] getCodingSchemeRefs() {
		return codingSchemeRefs;
	}
}