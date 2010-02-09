package org.lexevs.dao.database.service.property;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.lexevs.dao.database.service.property.batch.PropertyBatchInsertItem;

public interface PropertyService {
	
	public void insertProperty(
			String codingSchemeName, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			Property property);
	
	public void insertProperty(
			String entityCodeId,
			Property property);
	
	public void insertProperty(String codingSchemeId, 
			List<PropertyBatchInsertItem> items);

	
	public void updateProperty(
			String codingSchemeName, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			String propertyId,
			Property property);
}
