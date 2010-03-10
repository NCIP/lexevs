package org.lexevs.dao.database.service.picklist;

import java.util.List;
import java.util.Map;

import org.LexGrid.valueDomains.PickListDefinition;

public interface PickListService {
	
	public PickListDefinition getPickListDefinitionByPickListId(String pickListId);
	
	public List<PickListDefinition> getPickListDefinitionsByValueDomainUri(String valueDomainUri);
	
	public void removePickListDefinitionByPickListId(String pickListId);

	public void insertPickListDefinition(PickListDefinition definition);

	public List<String> listPickListIds() ;
	
	public Map<String, String> getReferencedPLDefinitions(String entityCode,
			String entityCodeNameSpace, String propertyId,
			Boolean extractPickListName);
	
	public Map<String, String> getReferencedPLDefinitions(
			String valueSet, Boolean extractPickListName);	
}
