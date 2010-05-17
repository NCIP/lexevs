package org.lexevs.dao.index.service.metadata;

import java.net.URI;

import org.apache.lucene.search.Query;

public interface MetadataIndexService {

	public void indexMetadata(URI metadata);
	
	public void search(Query query);
}
