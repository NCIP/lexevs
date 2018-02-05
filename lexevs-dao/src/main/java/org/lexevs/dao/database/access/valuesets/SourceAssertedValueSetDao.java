package org.lexevs.dao.database.access.valuesets;

import java.util.List;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

public interface SourceAssertedValueSetDao extends LexGridSchemaVersionAwareDao {
	
	List<Entity> getSourceAssertedValueSetEntitiesForEntityCode(String matchCode, String assertedRelation, String version, String codingSchemeURI);

	List<Entity> getSourceAssertedValueSetTopNodeForEntityCode(String matchCode, String codingSchemeUID);

	List<String> getValueSetEntityUids(String codingSchemeUid, String predUid, int start, int pageSize);

	List<String> getValueSetEntityUidForTopNodeEntityCode(String codingSchemeUid, String predUid, String code, int start,
			int pageSize);

	List<Entity> getPagedValueSetEntities(String matchCode, String csUID, String predicateUID, int start, int pageSize);

	int getValueSetEntityCount(String matchCode, String csUID, String predicateUID);

}
