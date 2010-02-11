package org.lexevs.dao.database.service.entity;

import java.util.List;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.DatabaseService;

public interface EntityService extends DatabaseService {
	
	public void insertEntity(
			String codingSchemeName, 
			String version, 
			Entity entity);

	public void insertBatchEntities(
			String codingSchemeName, 
			String version,
			List<? extends Entity> entities);
	
	public void updateEntity(
			String codingSchemeName, 
			String version, 
			String enityCode,
			String entityCodeNamespace,
			Entity entity);

}
