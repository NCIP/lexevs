package org.lexevs.dao.database.ibatis.property.parameter;

import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertPropertyLinkBean extends IdableParameterBean {

	private String entityId;
	private String link;
	private String sourcePropertyId;
	private String targetPropertyId;
	
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getSourcePropertyId() {
		return sourcePropertyId;
	}
	public void setSourcePropertyId(String sourcePropertyId) {
		this.sourcePropertyId = sourcePropertyId;
	}
	public String getTargetPropertyId() {
		return targetPropertyId;
	}
	public void setTargetPropertyId(String targetPropertyId) {
		this.targetPropertyId = targetPropertyId;
	}
}

