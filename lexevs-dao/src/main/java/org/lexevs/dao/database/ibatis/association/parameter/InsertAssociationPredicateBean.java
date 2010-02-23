package org.lexevs.dao.database.ibatis.association.parameter;

import org.LexGrid.relations.AssociationPredicate;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertAssociationPredicateBean extends IdableParameterBean {
	
	private AssociationPredicate associationPredicate;
	private String relationId;
	
	public AssociationPredicate getAssociationPredicate() {
		return associationPredicate;
	}
	public void setAssociationPredicate(AssociationPredicate associationPredicate) {
		this.associationPredicate = associationPredicate;
	}
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
}
