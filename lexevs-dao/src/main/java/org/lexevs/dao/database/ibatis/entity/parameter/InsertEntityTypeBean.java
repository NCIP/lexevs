package org.lexevs.dao.database.ibatis.entity.parameter;

public class InsertEntityTypeBean {

	private String id;
	private String entityType;
	
	public InsertEntityTypeBean(String id, String entityType) {
		super();
		this.id = id;
		this.entityType = entityType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
}

