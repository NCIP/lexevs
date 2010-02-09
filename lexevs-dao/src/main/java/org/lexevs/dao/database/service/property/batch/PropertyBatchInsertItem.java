package org.lexevs.dao.database.service.property.batch;

import org.LexGrid.commonTypes.Property;

public class PropertyBatchInsertItem {

	private String entityId;
	private Property property;
	
	public PropertyBatchInsertItem(){
		super();
	}
	
	public PropertyBatchInsertItem(String entityId, Property property) {
		super();
		this.entityId = entityId;
		this.property = property;
	}
	
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
	}
}
