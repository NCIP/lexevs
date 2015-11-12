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
package org.lexevs.dao.index.operation;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.dao.index.access.IndexDaoManager;
import org.lexevs.dao.index.indexer.IndexCreator;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.indexregistry.IndexRegistry;
import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.dao.indexer.utility.Utility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.AbstractLoggingBean;
import org.lexevs.system.model.LocalCodingScheme;

public class DefaultLexEvsIndexOperations extends AbstractLoggingBean implements LexEvsIndexOperations {
	
	private IndexCreator indexCreator;
	private IndexRegistry indexRegistry;
	private IndexDaoManager indexDaoManager;
	private ConcurrentMetaData concurrentMetaData;
	
	@Override
	public void registerCodingSchemeEntityIndex(String codingSchemeUri,
			String version) {
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(codingSchemeUri);
		ref.setCodingSchemeVersion(version);
		
		indexCreator.index(ref, null, true);
	}
	
	@Override
	public void cleanUp(
			final List<AbsoluteCodingSchemeVersionReference> expectedCodingSchemes,
			boolean reindexMissing) {
		getLogger().warn("Starting Cleanup of Entity Lucene Index.");
		File[] indexes = getIndexes();
		if (reindexMissing) {
			for (AbsoluteCodingSchemeVersionReference ref : expectedCodingSchemes) {
				try {
					if (!doesIndexExist(ref)) {
						this.registerCodingSchemeEntityIndex(
								ref.getCodingSchemeURN(),
								ref.getCodingSchemeVersion());
					}

				} catch (LBParameterException e) {
					throw new RuntimeException(
							"Removal or Re-Indexing of coding scheme "
									+ ref.getCodingSchemeURN() + " version "
									+ ref.getCodingSchemeVersion() + " failed",e);
				}
			}
		}
		for (File index : indexes) {
			AbsoluteCodingSchemeVersionReference reference = doesIndexHaveMatchingRegistryEntry(
					index, expectedCodingSchemes);

			if (reference == null) {
				//MetaData Index is a special case.  Not registered with the system in the same way.
				if(index.getName().equals("MetaDataIndex")){return;}
				CodingSchemeMetaData metaData = this.isIndexNameRegisteredWithTheSystem(index.getName());
				if(metaData == null){
					//Not registered with the system, safe to drop it now.
					//This may occur when starting up the system against an unstable index/database
					indexRegistry.destroyIndex(index.getName());
				}
				else{
					//Unregister it with the system first, then drop it
					AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
					ref.setCodingSchemeURN(metaData.getCodingSchemeUri());
					ref.setCodingSchemeVersion(metaData.getCodingSchemeVersion());
				this.dropIndex(metaData.getCodingSchemeName(), ref);
				}
			}
		}
	}

	protected Map<String, AbsoluteCodingSchemeVersionReference> getExpectedMap(
			List<AbsoluteCodingSchemeVersionReference> expectedCodingSchemes) {
		final Map<String,AbsoluteCodingSchemeVersionReference> expectedMap = 
			new HashMap<String,AbsoluteCodingSchemeVersionReference>();
		for(AbsoluteCodingSchemeVersionReference ref : expectedCodingSchemes) {
			String key = LuceneLoaderCode.createCodingSchemeUriVersionKey(
					ref.getCodingSchemeURN(), 
					ref.getCodingSchemeVersion());
			expectedMap.put(key, ref);
		}
		return expectedMap;
	}
	
	public String getLexEVSIndexLocation(){
        return LexEvsServiceLocator.getInstance().getSystemResourceService().
                getSystemVariables().getAutoLoadIndexLocation();
	}
	
	
	
	protected void dropIndex(String codingSchemeName,
			AbsoluteCodingSchemeVersionReference reference) {

		// If it's registered with the system then unregister it before deletion
		if (reference != null) {
			this.indexRegistry.unRegisterCodingSchemeIndex(
					reference.getCodingSchemeURN(),
					reference.getCodingSchemeVersion());

			String key = LocalCodingScheme.getLocalCodingScheme(
					codingSchemeName, reference.getCodingSchemeVersion())
					.getKey();
			try {
				concurrentMetaData.removeIndexMetaDataValue(key);
			} catch (RuntimeException e) {
				throw new RuntimeException(e);
			}
		}
		try {
			// If the reference is still null then delete this anyway
			this.indexRegistry.destroyIndex(reference != null ? Utility
					.getIndexName(reference) : codingSchemeName);
		} catch (LBParameterException e) {
			throw new RuntimeException("Problem deleting index from disk", e);
	}
	}

	public IndexDaoManager getIndexDaoManager() {
		return indexDaoManager;
	}

	public void setIndexDaoManager(IndexDaoManager indexDaoManager) {
		this.indexDaoManager = indexDaoManager;
	}
	
	public void setIndexCreator(IndexCreator indexCreator) {
		this.indexCreator = indexCreator;
	}
	public ConcurrentMetaData getConcurrentMetaData() {
		return concurrentMetaData;
	}

	public void setConcurrentMetaData(ConcurrentMetaData concurrentMetaData) {
		this.concurrentMetaData = concurrentMetaData;
	}

	public IndexCreator getIndexCreator() {
		return indexCreator;
	}

	public IndexRegistry getIndexRegistry() {
		return indexRegistry;
	}

	public void setIndexRegistry(IndexRegistry indexRegistry) {
		this.indexRegistry = indexRegistry;
	}

	@Override
	public boolean doesIndexExist(AbsoluteCodingSchemeVersionReference ref)
			throws LBParameterException {
		String indexLocation = getLexEVSIndexLocation();
		File indexParentFolder = new File(indexLocation);
		File[] indexes = indexParentFolder.listFiles();
		for (File index : indexes) {
			if (index.getName().equals(Utility.getIndexName(ref))) {
				return true;
			}
		}
		return false;
	}

	public File[] getIndexes() {
		String indexLocation = getLexEVSIndexLocation();
		File indexParentFolder = new File(indexLocation);
		return indexParentFolder.listFiles();
	}

	public AbsoluteCodingSchemeVersionReference doesIndexHaveMatchingRegistryEntry(
			File file,
			List<AbsoluteCodingSchemeVersionReference> expectedCodingSchemes) {
		for (AbsoluteCodingSchemeVersionReference ref : expectedCodingSchemes) {
			try {
				if (file.getName().equals(Utility.getIndexName(ref))) {
					return ref;
				}
			} catch (LBParameterException e) {
				throw new RuntimeException(
						"Problem matching registry file entry", e);
			}
		}
		return null;
	}

	public CodingSchemeMetaData isIndexNameRegisteredWithTheSystem(
			String indexName) {

		for (CodingSchemeMetaData csmd : concurrentMetaData
				.getCodingSchemeList()) {
			if (csmd.getDirectory().getIndexName().equals(indexName)) {
				return csmd;
			}
		}
		return null;
	}
}