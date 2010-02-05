package org.lexevs.dao.database.access.versions;

import org.LexGrid.versions.EntryState;

public interface VersionsDao {
	
	public EntryState getEntryStateById(String codingSchemeName, String codingSchemeVersion, String entryStateId);

	public void updateEntryState(String entryStateId, EntryState entryState);
	
	public void insertEntryState(
			String codingSchemeName,
			String codingSchemeVersion,
			String entryStateId, 
			String entryId,
			String entryType,
			String previousEntryStateId,
			EntryState entryState);
	
	public void insertEntryState(
			String codingSchemeId,
			String entryStateId, 
			String entryId,
			String entryType,
			String previousEntryStateId,
			EntryState entryState);
}
