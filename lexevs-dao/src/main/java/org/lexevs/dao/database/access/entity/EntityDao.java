package org.lexevs.dao.database.access.entity;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;


public interface EntityDao extends LexGridSchemaVersionAwareDao {

	public String insertEntity(String codingSchemeName, String version, Entity entity);
	
	public String insertEntity(String codingSchemeId, Entity entity);
	
	public String insertHistoryEntity(String codingSchemeId, Entity entity);
	
	public void updateEntity(String codingSchemeName, String version, Entity entity);
	
	
}
