package org.lexevs.dao.database.access.valuesets;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.access.association.model.DefinedNode;

public interface SourceAssertedValueSetDao extends LexGridSchemaVersionAwareDao {
	
	/**
	 * Returns list of entities for top node entity code exact match.  The entity code represents
	 * the target of a set of value set source entities.  
	 * 
	 * @param matchCode - code to match for value set top node
	 * @param assertedRelation - can be null
	 * @param predicateGuid - database specific association identifier must be pulled from db 
	 * @param codingSchemeGuid - database specific coding scheme identifier must be pulled from db 
	 * @return List<Entity> 
	 */
	List<Entity> getSourceAssertedValueSetEntitiesForEntityCode(String matchCode, String assertedRelation, String predicatGuid, String codingSchemeGuid);

	/**
	 * A list of value set top nodes represented as Entity objects which are related to this the concept
	 * defined by the exact match of the unique identifier.  
	 * 
	 * @param matchCode - code to match to retrieve top node entity
	 * @param codingSchemeUID - database specific coding scheme identifier must be pulled from db 
	 * @return List<Entity> - entity representation of a value set top node
	 */
	List<Entity> getSourceAssertedValueSetTopNodeForEntityCode(String matchCode, String codingSchemeUID);

	/**
	 * Returns all value set unique identifiers defined in the database 
	 * and only used as query options for other database calls
	 * This is a utility method for other methods.
	 * 
	 * @param codingSchemeUid - database specific coding scheme identifier must be pulled from db
	 * @param predUid - database specific association identifier must be pulled from db 
	 * @param start - paging cursor
	 * @param pageSize - result set size
	 * @return List<String> - a list of identifiers
	 */
	List<String> getValueSetEntityUids(String codingSchemeUid, String predUid, int start, int pageSize);

	/**
	 * Returns all value set member unique identifiers defined in the database and 
	 * only used as query options for other database calls.  These members are identified
	 * by the top node unique identifier passed in as a parameter.
	 * This is a utility method for other methods.
	 * 
	 * @param codingSchemeUid - database specific coding scheme identifier must be pulled from db
	 * @param predUid - database specific association identifier must be pulled from db 
	 * @param code - A string representation of the top node unique identifier
	 * @param start - paging cursor
	 * @param pageSize - result set size
	 * @return
	 */
	List<String> getValueSetEntityUidForTopNodeEntityCode(String codingSchemeUid, String predUid, String code, int start,
			int pageSize);

	/**
	 * Paging capable method for value set members defined by a unique identifier passed in as the string 
	 * mathchCode parameter. Useful for paging through larger result sets.
	 * 
	 * @param matchCode - A string representation of the top node unique identifier
	 * @param csUID  - database specific coding scheme identifier must be pulled from db
	 * @param predicateUID  - database specific association identifier must be pulled from db
	 * @param start - paging cursor
	 * @param pageSize - result set size
	 * @return List<Entity> page set of value set members as entities
	 */
	List<Entity> getPagedValueSetEntities(String matchCode, String csUID, String predicateUID, int start, int pageSize);

	/**
	 * Returns the total number of values in a given value set determined by the top node
	 * unique identifier
	 * 
	 * @param matchCode - A string representation of the top node unique identifier
	 * @param csUID  - database specific coding scheme identifier must be pulled from db
	 * @param predicateUID  - database specific association identifier must be pulled from db
	 * @return int - integer representation of the how many values in a value set.
	 */
	int getValueSetEntityCount(String matchCode, String csUID, String predicateUID);

	/**
	 *A list of value set top nodes represented as Entity objects which are related to this the concept
	 * defined by the exact match of the unique identifier.  
	 * 
	 * @param matchCode - A string representation of the top node unique identifier
	 * @param assertedValueSetRelation - value set relation identifier -- can be null
	 * @param predUid - database specific association identifier must be pulled from db
	 * @param csUID - database specific coding scheme identifier must be pulled from db
	 * @return List<Entity>
	 */
	List<Entity> getSourceAssertedValueSetsForVSMemberEntityCode(String matchCode, String assertedValueSetRelation,
			String predUid, String csUID);

	/**
	 * @param propertyName - Name of property that defines value set publish-able flag
	 * @param propertyValue - Value set publish-able flag value
	 * @param predUid - database specific association identifier must be pulled from db
	 * @param csUID - database specific coding scheme identifier must be pulled from db
	 * @return List<DefinedNode> 
	 */
	List<DefinedNode> getAllValidValueSetTopNodeCodes(String propertyName, String propertyValue, String predUid, String csUID);

	/**
	 * Returns a list of entity properties useful in defining the meta data of a value set 
	 * expressed as a coding scheme
	 * 
	 * @param entityCode - Should be a top node, defining a value set
	 * @param csUID - database specific coding scheme identifier must be pulled from db
	 * @return List<Property> - entity properties
	 */
	List<Property> getValueSetEntityProperties(String entityCode, String csUid);

}
