package org.lexevs.dao.database.ibatis.association.parameter;

import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertRelationsBean extends IdableParameterBean {
	
	private Relations relations;
	private String codingSchemeId;

	public void setRelations(Relations relations) {
		this.relations = relations;
	}

	public Relations getRelations() {
		return relations;
	}

	public void setCodingSchemeId(String codingSchemeId) {
		this.codingSchemeId = codingSchemeId;
	}

	public String getCodingSchemeId() {
		return codingSchemeId;
	}
}
