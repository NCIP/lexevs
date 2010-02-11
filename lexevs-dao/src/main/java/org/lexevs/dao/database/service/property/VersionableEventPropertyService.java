package org.lexevs.dao.database.service.property;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.access.property.batch.PropertyBatchInsertItem;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.springframework.transaction.annotation.Transactional;

public class VersionableEventPropertyService extends AbstractDatabaseService implements PropertyService{

	@Transactional
	public void insertBatchEntityProperties(
			String codingSchemeName, 
			String version,
			String entityCode,
			String entityCodeNamespace,
			List<Property> items) {
		String codingSchemeId = this.getCodingSchemeId(codingSchemeName, version);
		String entityId = this.getDaoManager().getEntityDao().getEntityId(codingSchemeId, entityCode, entityCodeNamespace);
		
		this.getDaoManager().getPropertyDao().
		insertBatchProperties(codingSchemeId, PropertyType.ENTITY, 
				this.propertyListToBatchInsertList(entityId, items));
	}

	public void insertEntityProperty(
			String codingSchemeName, 
			String version, 
			String entityCode, 
			String entityCodeNamespace, 
			Property property) {
		// TODO Auto-generated method stub
		
	}


	public void insertEntityProperties(String codingSchemeId,
			List<PropertyBatchInsertItem> items) {
		// TODO Auto-generated method stub
		
	}


	public void updateEntityProperty(String codingSchemeName, String version,
			String entityCode, String entityCodeNamespace, String propertyId,
			Property property) {
		// TODO Auto-generated method stub
		
	}

	protected List<PropertyBatchInsertItem> propertyListToBatchInsertList(
			String parentId, List<Property> props){
		List<PropertyBatchInsertItem> returnList = new ArrayList<PropertyBatchInsertItem>();
		for(Property prop : props){
			returnList.add(new PropertyBatchInsertItem(parentId, prop));
		}
		return returnList;
	}
}
