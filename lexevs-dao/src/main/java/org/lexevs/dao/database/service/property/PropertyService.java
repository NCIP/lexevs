package org.lexevs.dao.database.service.property;

import org.LexGrid.commonTypes.Property;

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
	
	public void updateProperty(
			String codingSchemeName, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			String propertyId,
			Property property);
}
