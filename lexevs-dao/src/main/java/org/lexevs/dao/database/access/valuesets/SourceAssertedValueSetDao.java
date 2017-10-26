package org.lexevs.dao.database.access.valuesets;

import java.util.List;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

public interface SourceAssertedValueSetDao extends LexGridSchemaVersionAwareDao {
	
	List<Entity> getSourceAssertedValueSetEntitiesForEntityCode(String matchCode, String assertedRelation);
	
	List<Entity> getSourceAssertedValueSetTopNodeForEntityCode(String matchCode, String valueSetDesignation, String designationValue);

}
