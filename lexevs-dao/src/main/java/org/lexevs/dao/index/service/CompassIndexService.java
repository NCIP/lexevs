package org.lexevs.dao.index.service;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.compass.core.CompassQuery;
import org.lexevs.dao.index.indexer.IndexCreator;
import org.lexevs.dao.index.model.compass.v20.IndexedProperty;
import org.lexevs.dao.index.repository.entity.EntityDao;

public class CompassIndexService implements IndexService {
	
	private EntityDao<CompassQuery,IndexedProperty> entityDao;
	private IndexCreator indexCreator;

	public void removeIndexForEntity(String codingSchemeUri,
			String codingSchemeVersion, Entity entity) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	public void updateIndexForEntity(String codingSchemeUri,
			String codingSchemeVersion, Entity entity) {
		//entityDao.insertResource(null)
	}

	public void setEntityDao(EntityDao<CompassQuery,IndexedProperty> entityDao) {
		this.entityDao = entityDao;
	}

	public EntityDao<CompassQuery,IndexedProperty> getEntityDao() {
		return entityDao;
	}

	public void createIndex(AbsoluteCodingSchemeVersionReference reference) {
		indexCreator.index(reference);
	}

	public IndexCreator getIndexCreator() {
		return indexCreator;
	}

	public void setIndexCreator(IndexCreator indexCreator) {
		this.indexCreator = indexCreator;
	}
	
	
}
