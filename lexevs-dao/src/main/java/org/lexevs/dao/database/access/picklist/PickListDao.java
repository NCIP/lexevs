package org.lexevs.dao.database.access.picklist;

import java.util.List;

import org.LexGrid.valueDomains.PickListDefinition;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

public interface PickListDao extends LexGridSchemaVersionAwareDao {
	
	public PickListDefinition getPickListDefinitionById(String pickListId);
	
	public String getGuidFromPickListId(String pickListId);
	
	public String insertPickListDefinition(String systemReleaseUri, PickListDefinition definition);

	public List<String> getPickListIds();
	
	public String insertPickListEntry(String pickListGuid, PickListDefinition definition);
}
