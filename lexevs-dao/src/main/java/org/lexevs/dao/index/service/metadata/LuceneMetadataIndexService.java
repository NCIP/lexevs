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
package org.lexevs.dao.index.service.metadata;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.apache.lucene.search.Query;
import org.lexevs.dao.index.access.IndexDaoManager;
import org.lexevs.dao.index.indexer.MetadataIndexCreator;

public class LuceneMetadataIndexService implements MetadataIndexService {

	private MetadataIndexCreator metadataIndexCreator;
	
	private IndexDaoManager indexDaoManager;
	
	public AbsoluteCodingSchemeVersionReferenceList listCodingSchemes() {
		return indexDaoManager.getMetadataDao().listCodingSchemes();
	}
	
	@Override
	public void indexMetadata(String codingSchemeUri,
			String codingSchemeVersion, URI metaDataLocation,
			boolean appendToExistingMetaData) throws Exception {
		metadataIndexCreator.indexMetadata(
				codingSchemeUri, codingSchemeVersion, metaDataLocation, appendToExistingMetaData);
	}

	@Override
	public void removeMetadata(String codingSchemeUri, String version) {
		indexDaoManager.getMetadataDao().
			removeMetadata(codingSchemeUri, version);
	}

	@Override
	public MetadataPropertyList search(Query query) {
		return indexDaoManager.getMetadataDao().search(query);
	}

	public void setMetadataIndexCreator(MetadataIndexCreator metadataIndexCreator) {
		this.metadataIndexCreator = metadataIndexCreator;
	}

	public MetadataIndexCreator getMetadataIndexCreator() {
		return metadataIndexCreator;
	}

	public void setIndexDaoManager(IndexDaoManager indexDaoManager) {
		this.indexDaoManager = indexDaoManager;
	}

	public IndexDaoManager getIndexDaoManager() {
		return indexDaoManager;
	}

}