package org.lexevs.dao.database.service.entity;

import java.util.List;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.DatabaseService;

public interface EntityService extends DatabaseService {
	
	public void insertEntity(
			String codingSchemeUri, 
			String version, 
			Entity entity);
	
	public Entity getEntity(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace);

	public void insertBatchEntities(
			String codingSchemeUri, 
			String version,
			List<? extends Entity> entities);
	
	public void updateEntity(
			String codingSchemeName, 
			String version, 
			String enityCode,
			String entityCodeNamespace,
			Entity entity);

}
