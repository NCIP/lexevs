
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