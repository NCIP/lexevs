package org.lexevs.dao.database.service.entity;

import org.LexGrid.concepts.Entity;

public interface EntityService {
	
	public void insertEntity(
			String codingSchemeName, 
			String version, 
			Entity entity);
	
	public void insertEntity(
			String codingSchemeId, 
			Entity entity);
	
	public void insertEntity(
			String codingSchemeId, 
			Entity[] entities);
	
	public void updateEntity(
			String codingSchemeName, 
			String version, 
			String enityCode,
			String entityCodeNamespace,
			Entity entity);

}
