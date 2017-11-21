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
package org.lexevs.dao.index.lucenesupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.dao.indexer.utility.Utility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author <A HREF="mailto:bauer.scott@mayo.edu">Scott Bauer </A>
 *
 */
public class LazyLoadMetaData implements
		ApplicationListener<ContextRefreshedEvent> {

	private LexEvsServiceLocator locator;
	private SystemVariables systemVariables;
	private LuceneDirectoryCreator directoryCreator;
	private LuceneMultiDirectoryFactory multiDirectoryFactory;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		this.locator = LexEvsServiceLocator.getInstance();
		try {
			lazyLoadMetadata();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}



	public void lazyLoadMetadata() throws LBInvocationException, IOException, LBParameterException {
		ConcurrentMetaData concurrentMetaData = ConcurrentMetaData
				.getInstance();
		List<RegistryEntry> registeredSchemes = locator.getRegistry()
				.getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME);
		List<NamedDirectory> namedDirectories = new ArrayList<NamedDirectory>();
		Map<String, File> orphanedIndexCache = new HashMap<String, File>();
		File indexDir = new File(systemVariables.getAutoLoadIndexLocation());
		for (File f : indexDir.listFiles()) {
			if (f.exists() && f.isDirectory()) {
				orphanedIndexCache.put(f.getName(), f);
				for (RegistryEntry re : registeredSchemes) {
					if (fileMatch(re, f, orphanedIndexCache)) {
						concurrentMetaData.add(reconciliateDbToIndex(re, f,
								namedDirectories, directoryCreator));
						break;
					}
				}
			}
		}
		for (String key : orphanedIndexCache.keySet()) {

			if (!key.equals("MetaDataIndex")) {
				try{
				LoggerFactory.getLogger().fatalAndThrowException("Indexes seem to be created in another context "
						+ "as they do not match database registrations. "
						+ "If these indexes were copied from another service then "
						+ "please edit the config.props file to match the source service. "
						+ "Otherwise delete them and rebuild them from scratch");
				}
				catch(Exception e){
					throw new LBInvocationException(e.getMessage(), "IndexException");
				}
			}
		}
	}

	private CodingSchemeMetaData reconciliateDbToIndex(RegistryEntry re,
			File f, List<NamedDirectory> namedDirectories,
			LuceneDirectoryCreator directoryCreator)
			throws LBParameterException, IOException {
		//Only gets a directory if it exists.   We don't get here unless there is 
		//a matching index.
		CodingSchemeMetaData csMetaData = new CodingSchemeMetaData(re.getResourceUri(),
				re.getResourceVersion(), locator.getSystemResourceService()
						.getInternalCodingSchemeNameForUserCodingSchemeName(
								re.getResourceUri(), re.getResourceVersion()),
				makeNewDirectoryIfNone(re));

		return csMetaData;
	}

	private NamedDirectory makeNewDirectoryIfNone(RegistryEntry re)
			throws IOException, LBParameterException {
		AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();
		reference.setCodingSchemeURN(re.getResourceUri());
		reference.setCodingSchemeVersion(re.getResourceVersion());
		return multiDirectoryFactory.getNamedDirectory(Utility
				.getIndexName(reference));
	}

	private boolean fileMatch(RegistryEntry re, File f, Map<String, File> orphanedIndexCache) throws LBParameterException {
		AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();
		reference.setCodingSchemeURN(re.getResourceUri());
		reference.setCodingSchemeVersion(re.getResourceVersion());
		if (Utility.getIndexName(reference).equals(f.getName())) {
			orphanedIndexCache.remove(f.getName());
			return true;
		}
		return false;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public LuceneDirectoryCreator getDirectoryCreator() {
		return directoryCreator;
	}

	public void setDirectoryCreator(LuceneDirectoryCreator directoryCreator) {
		this.directoryCreator = directoryCreator;
	}

	public LuceneMultiDirectoryFactory getMultiDirectoryFactory() {
		return multiDirectoryFactory;
	}

	public void setMultiDirectoryFactory(
			LuceneMultiDirectoryFactory multiDirectoryFactory) {
		this.multiDirectoryFactory = multiDirectoryFactory;
	}
	
	public LexEvsServiceLocator getLocator() {
		return locator;
	}

	public void setLocator(LexEvsServiceLocator locator) {
		this.locator = locator;
	}

}
