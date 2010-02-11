package org.lexevs.dao.database.access.property.batch;

import org.LexGrid.commonTypes.Property;

public class PropertyBatchInsertItem {

	private String parentId;
	private Property property;
	
	public PropertyBatchInsertItem(){
		super();
	}
	
	public PropertyBatchInsertItem(String parentId, Property property) {
		super();
		this.parentId = parentId;
		this.property = property;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
	}
}
