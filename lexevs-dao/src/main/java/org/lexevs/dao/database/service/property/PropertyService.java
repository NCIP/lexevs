package org.lexevs.dao.database.service.property;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.lexevs.dao.database.service.DatabaseService;

public interface PropertyService extends DatabaseService {
	
	public void insertEntityProperty(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			Property property);
	
	public void insertBatchEntityProperties(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			List<Property> batch);

	
	public void updateEntityProperty(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			String propertyId,
			Property property);
}
