
package org.lexevs.dao.index.service.metadata;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.apache.lucene.search.Query;

public interface MetadataIndexService {
	
	public AbsoluteCodingSchemeVersionReferenceList listCodingSchemes();
	
	public void indexMetadata(
	    		String codingSchemeUri, 
	    		String codingSchemeVersion, 
	    		URI metaDataLocation,  
	    		boolean appendToExistingMetaData) throws Exception;
	
	public MetadataPropertyList search(Query query);

	public void removeMetadata(
			String codingSchemeUri,
			String version);
}