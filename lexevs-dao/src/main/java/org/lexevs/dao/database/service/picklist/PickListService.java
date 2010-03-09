package org.lexevs.dao.database.service.picklist;

import java.util.List;
import java.util.Map;

import org.LexGrid.valueDomains.PickListDefinition;
import org.LexGrid.valueDomains.PickListEntryNode;

public interface PickListService {

	public void insertPickListDefinition(PickListDefinition definition);
	
	public void insertPickListEntryNode(PickListEntryNode node);
	
	public List<String> listPickListIds() ;
	
	public Map<String, String> getReferencedPLDefinitions(String entityCode,
			String entityCodeNameSpace, String propertyId,
			Boolean extractPickListName);
	
	public Map<String, String> getReferencedPLDefinitions(
			String valueSet, Boolean extractPickListName);
	
	public PickListDefinition getPickListDefinitionById(String pickListId);
	
}
