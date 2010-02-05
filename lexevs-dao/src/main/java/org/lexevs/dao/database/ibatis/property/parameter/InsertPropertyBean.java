package org.lexevs.dao.database.ibatis.property.parameter;

import org.LexGrid.commonTypes.Property;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertPropertyBean extends IdableParameterBean {

	private Property property;
	private String entityId;
	private String referenceType;

	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	public String getReferenceType() {
		return referenceType;
	}
}

