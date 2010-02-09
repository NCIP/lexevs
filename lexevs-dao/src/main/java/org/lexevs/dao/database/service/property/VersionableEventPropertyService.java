package org.lexevs.dao.database.service.property;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.property.batch.PropertyBatchInsertItem;

public class VersionableEventPropertyService extends AbstractDatabaseService implements PropertyService{

	public void insertProperty(String entityCodeId, Property property) {
		// TODO Auto-generated method stub
		
	}

	public void insertProperty(String codingSchemeName, String version,
			String entityCode, String entityCodeNamespace, Property property) {
		// TODO Auto-generated method stub
		
	}

	public void updateProperty(String codingSchemeName, String version,
			String entityCode, String entityCodeNamespace, String propertyId,
			Property property) {
		// TODO Auto-generated method stub
		
	}

	public void insertProperty(String codingSchemeId,
			List<PropertyBatchInsertItem> items) {
		// TODO Auto-generated method stub
		
	}

}
