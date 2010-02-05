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
package org.lexgrid.loader.meta;

import java.io.File;
import java.net.URI;
import java.util.Properties;

import org.LexGrid.LexBIG.Extensions.Load.MetaBatchLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.lexgrid.loader.AbstractSpringBatchLoader;
import org.lexgrid.loader.properties.ConnectionPropertiesFactory;
import org.lexgrid.loader.properties.impl.DefaultLexEVSPropertiesFactory;

/**
 * The Class MetaBatchLoaderImpl.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaBatchLoaderImpl extends AbstractSpringBatchLoader implements MetaBatchLoader {
	
	/** The connection properties factory. */
	private ConnectionPropertiesFactory connectionPropertiesFactory = new DefaultLexEVSPropertiesFactory();

	/** The MET a_ loade r_ config. */
	private String META_LOADER_CONFIG = "metaLoader.xml";
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.meta.MetaBatchLoader#loadMeta(java.lang.String)
	 */
	public void loadMeta(URI rrfDir) throws Exception {
		Properties connectionProps = connectionPropertiesFactory.getPropertiesForNewLoad(true);
		connectionProps.put("rrfDir", rrfDir.toString());
		connectionProps.put("retry", "false");
		launchJob(connectionProps, META_LOADER_CONFIG, "metaJob");
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.meta.MetaBatchLoader#resumeMeta(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void resumeMeta(URI rrfDir, String uri, String version) throws Exception {
		Properties connectionProps = connectionPropertiesFactory.getPropertiesForExistingLoad(uri, version);
		connectionProps.put("rrfDir", rrfDir.toString());
		connectionProps.put("retry", "true");
		launchJob(connectionProps, META_LOADER_CONFIG, "metaJob");	
	}

	@Override
	public String getName() {
		return MetaBatchLoader.NAME;
	}
	
	
	 public static void main(String[] args) throws Exception { 
		 MetaBatchLoader mbl = (MetaBatchLoader) LexBIGServiceImpl.defaultInstance().getServiceManager(null).getLoader(
					"MetaBatchLoader");
		 mbl.loadMeta(new File("src/test/resources/data/SAMPLEMETA").toURI());
		// mbl.loadMeta("/home/LargeStorage/ontologies/rrf/LNC/LNC226");
	 }
}
