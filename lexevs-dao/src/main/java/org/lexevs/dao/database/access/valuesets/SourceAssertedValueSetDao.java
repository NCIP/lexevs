package org.lexevs.dao.database.access.valuesets;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.access.association.model.DefinedNode;

public interface SourceAssertedValueSetDao extends LexGridSchemaVersionAwareDao {
	
	/**
	 * @param matchCode - code to match for value set top node
	 * @param assertedRelation
	 * @param version
	 * @param codingSchemeURI
	 * @return
	 */
	List<Entity> getSourceAssertedValueSetEntitiesForEntityCode(String matchCode, String assertedRelation, String version, String codingSchemeURI);

	/**
	 * @param matchCode - code to match to retrieve top node entity
	 * @param codingSchemeUID - coding scheme guid
	 * @return List<Entity> - entity representation of a value set top node
	 */
	List<Entity> getSourceAssertedValueSetTopNodeForEntityCode(String matchCode, String codingSchemeUID);

	/**
	 * @param codingSchemeUid
	 * @param predUid
	 * @param start
	 * @param pageSize
	 * @return
	 */
	List<String> getValueSetEntityUids(String codingSchemeUid, String predUid, int start, int pageSize);

	/**
	 * @param codingSchemeUid
	 * @param predUid
	 * @param code
	 * @param start
	 * @param pageSize
	 * @return
	 */
	List<String> getValueSetEntityUidForTopNodeEntityCode(String codingSchemeUid, String predUid, String code, int start,
			int pageSize);

	/**
	 * @param matchCode
	 * @param csUID
	 * @param predicateUID
	 * @param start
	 * @param pageSize
	 * @return
	 */
	List<Entity> getPagedValueSetEntities(String matchCode, String csUID, String predicateUID, int start, int pageSize);

	/**
	 * @param matchCode
	 * @param csUID
	 * @param predicateUID
	 * @return
	 */
	int getValueSetEntityCount(String matchCode, String csUID, String predicateUID);

	/**
	 * @param matchCode
	 * @param assertedValueSetRelation
	 * @param predUid
	 * @param csUID
	 * @return
	 */
	List<Entity> getSourceAssertedValueSetsForVSMemberEntityCode(String matchCode, String assertedValueSetRelation,
			String predUid, String csUID);

	/**
	 * @param propertyName
	 * @param propertyValue
	 * @param predUid
	 * @param csUID
	 * @return
	 */
	List<DefinedNode> getAllValidValueSetTopNodeCodes(String propertyName, String propertyValue, String predUid, String csUID);

	/**
	 * @param entityCode
	 * @param csUid
	 * @return
	 */
	List<Property> getValueSetEntityProperties(String entityCode, String csUid);

}
