package org.lexevs.dao.database.access.entity;

import java.util.List;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;


public interface EntityDao extends LexGridSchemaVersionAwareDao {
	
	public void insertBatchEntities(String codingSchemeId, List<? extends Entity> entities);
	
	public Entity getEntityByCodeAndNamespace(String codingSchemeId, String entityCode, String entityCodeNamespace);
	
	public String getEntityId(String codingSchemeId, String entityCode, String entityCodeNamespace);
	
	public String insertEntity(String codingSchemeId, Entity entity);
	
	public String insertHistoryEntity(String codingSchemeId, Entity entity);
	
	public void updateEntity(String codingSchemeId, Entity entity);
	
	public int getEntityCount(String codingSchemeId);
	
	public List<? extends Entity> getAllEntitiesOfCodingScheme(String codingSchemeId, int start, int pageSize);

}
