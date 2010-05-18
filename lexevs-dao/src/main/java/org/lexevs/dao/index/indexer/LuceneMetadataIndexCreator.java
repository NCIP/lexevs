package org.lexevs.dao.index.indexer;

import java.net.URI;

import org.lexevs.dao.index.metadata.OBOMetaDataLoader;

public class LuceneMetadataIndexCreator implements MetadataIndexCreator {

	private OBOMetaDataLoader oboMetaDataLoader;
	
	@Override
	public void indexMetadata(String codingSchemeUri,
			String codingSchemeVersion, URI metaDataLocation,
			boolean appendToExistingMetaData) throws Exception {
		oboMetaDataLoader.loadMetadata(
				codingSchemeUri, 
				codingSchemeVersion, 
				metaDataLocation, 
				appendToExistingMetaData);
	}

	public void setOboMetaDataLoader(OBOMetaDataLoader oboMetaDataLoader) {
		this.oboMetaDataLoader = oboMetaDataLoader;
	}

	public OBOMetaDataLoader getOboMetaDataLoader() {
		return oboMetaDataLoader;
	}

}
