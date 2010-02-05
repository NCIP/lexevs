package org.lexevs.dao.database.ibatis.property.parameter;

import org.LexGrid.concepts.PropertyLink;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertPropertyLinkBean extends IdableParameterBean {

	private String entityId;
	private PropertyLink propertyLink;
	
	public PropertyLink getPropertyLink() {
		return propertyLink;
	}
	public void setPropertyLink(PropertyLink propertyLink) {
		this.propertyLink = propertyLink;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getEntityId() {
		return entityId;
	}

}

