
package org.lexevs.dao.database.access.valuesets;

import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

/**
 * The Interface VSEntryStateDao.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public interface VSEntryStateDao extends LexGridSchemaVersionAwareDao {

	/**
	 * Gets the entry state by UID.
	 * 
	 * @param entryStateId the entry state UID
	 * 
	 * @return the entry state
	 */
	public EntryState getEntryStateByUId(String entryStateUId);

	/**
	 * Update entry state.
	 * 
	 * @param entryStateId the entry state UID
	 * @param entryState the entry state
	 */
	public void updateEntryState(String entryStateUId, EntryState entryState);

	/**
	 * Insert entry state.
	 * 
	 * @param entryStateUId
	 * @param entryUId the entry id
	 * @param entryType the entry type
	 * @param previousEntryStateUId the previous entry state id
	 * @param entryState the entry state
	 */
	public void insertEntryState( String entryStateUId,
			String entryUId, String entryType, String previousEntryStateUId,
			EntryState entryState);	
	
	/**
	 * Insert entry state.
	 * 
	 * @param entryUId the entry resource UID
	 * @param entryType the entry type
	 * @param previousEntryStateUId the previous entry state UID
	 * @param entryState the entry state
	 */
	public String insertEntryState(
			String entryUId,
			String entryType,
			String previousEntryStateUId,
			EntryState entryState);
			
	public void deleteAllEntryStatesOfVsPropertiesByParentUId(
			String parentUId, String parentType);

	public void deleteAllEntryStatesOfValueSetDefinitionByUId(
			String valueSetDefGuid);	
	
	public void deleteAllEntryStateEntriesByEntryUId(String entryUId);

	public void deleteAllEntryStatesOfPickListDefinitionByUId(
			String pickListUId);

	public void deleteAllEntryStatesOfPLEntryNodeByUId(
			String pickListEntryNodeUId);
	
	public void deleteAllEntryStateByEntryUIdAndType(
			String entryGuid, String entryType);
}