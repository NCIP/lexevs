package org.lexevs.dao.database.access.entity;

import org.LexGrid.concepts.Entity;


public interface EntityDao {

	public String insertEntity(String codingSchemeName, String version, Entity entity);
	
	public String insertEntity(String codingSchemeId, Entity entity);
	
	public String insertHistoryEntity(String codingSchemeId, Entity entity);
	
	public void updateEntity(String codingSchemeName, String version, Entity entity);
	
	
}
