
package org.lexevs.dao.index.lucene;

import org.lexevs.dao.index.access.AbstractBaseIndexDao;
import org.lexevs.dao.index.indexregistry.IndexRegistry;

public abstract class AbstractFilteringLuceneIndexTemplateDao extends AbstractBaseIndexDao {
	private IndexRegistry indexRegistry;
	
	public IndexRegistry getIndexRegistry() {
		return indexRegistry;
	}

	public void setIndexRegistry(IndexRegistry indexRegistry) {
		this.indexRegistry = indexRegistry;
	}

}