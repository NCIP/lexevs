
package org.lexevs.dao.database.access.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.valueSets.PickListEntryNode;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

public interface PickListEntryNodeDao extends LexGridSchemaVersionAwareDao {

/**
	 * Insert pick list entry.
	 * 
	 * @param pickListGuid the pick list definition GUID
	 * @param entryNode the pick list entry node
	 * 
	 * @return the string
	 */
public String insertPickListEntry(String pickListGuid, PickListEntryNode entryNode);

	public String getPickListEntryNodeUId(String pickListId, String pickListEntryNodeId);

	public void removeAllPickListEntryNodeMultiAttributes(String pickListEntryNodeUId);

	public String insertHistoryPickListEntryNode(String pickListEntryNodeUId);

	public String updatePickListEntryNode(String pickListEntryNodeUId,
			PickListEntryNode pickListEntryNode);

	public String updateVersionableAttributes(String pickListEntryNodeUId,
			PickListEntryNode pickListEntryNode);

	public String getPickListEntryStateUId(String pickListEntryNodeUId);

	public void updateEntryStateUId(String pickListEntryNodeUId,
			String entryStateUId);

	public void createEntryStateIfAbsent(String entryStateUId, String vsPLEntryUId);

	public String getLatestRevision(String pickListEntryNodeUId);

	public void deletePLEntryNodeByUId(String pickListEntryNodeUId);

	public PickListEntryNode resolvePLEntryNodeByRevision(
			String pickListId, String plEntryId, String revisionId) throws LBRevisionException;

}