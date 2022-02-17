
package org.lexevs.dao.database.access.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.valueSets.DefinitionEntry;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

public interface VSDefinitionEntryDao extends LexGridSchemaVersionAwareDao {

/**
	 * Insert value set definition entry.
	 * 
	 * @param valueSetDefinitionGuid the value set definition GUID
	 * @param definitionEntry the Value Set definitionEntry
	 * 
	 * @return the string
	 */
public String insertDefinitionEntry(String valueSetDefinitionUId,
			DefinitionEntry vsdEntry);
	
	public void deleteDefinitionEntry(String vsDefinitionEntryUId);

	public String getDefinitionEntryUId(String valueSetDefinitionURI,
			String ruleOrder);

	public String insertHistoryDefinitionEntry(String valueSetDefUId,
			String vsDefinitionUId, DefinitionEntry defEntry);

	public String updateDefinitionEntry(String vsDefinitionUId,
			DefinitionEntry defEntry);

	public String updateDefinitionEntryVersionableAttrib(
			String vsDefinitionUId, DefinitionEntry defEntry);

	public String getLatestRevision(String vsDefEntryUId);

	public DefinitionEntry resolveDefinitionEntryByRevision(
			String valueSetDefURI, String ruleOrder, String revisionId)
			throws LBRevisionException;

}