package org.lexevs.dao.database.ibatis.entity.parameter;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertEntityBean extends IdableParameterBean {

	private Entity entity;
	private String codingSchemeId;
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public String getCodingSchemeId() {
		return codingSchemeId;
	}
	public void setCodingSchemeId(String codingSchemeId) {
		this.codingSchemeId = codingSchemeId;
	}
}

