package org.lexevs.dao.index.service;

import org.lexevs.dao.index.service.entity.EntityIndexService;

public class IndexServiceManager {

	private EntityIndexService entityIndexService;

	public void setEntityIndexService(EntityIndexService entityIndexService) {
		this.entityIndexService = entityIndexService;
	}

	public EntityIndexService getEntityIndexService() {
		return entityIndexService;
	}
}
