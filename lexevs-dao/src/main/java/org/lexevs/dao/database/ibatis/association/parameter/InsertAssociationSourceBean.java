package org.lexevs.dao.database.ibatis.association.parameter;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertAssociationSourceBean extends IdableParameterBean{
	
	private String associationPredicateId;
	private AssociationSource associationSource;
	private AssociationTarget associationTarget;
	
	public String getAssociationPredicateId() {
		return associationPredicateId;
	}
	public void setAssociationPredicateId(String associationPredicateId) {
		this.associationPredicateId = associationPredicateId;
	}
	public AssociationSource getAssociationSource() {
		return associationSource;
	}
	public void setAssociationSource(AssociationSource associationSource) {
		this.associationSource = associationSource;
	}
	public AssociationTarget getAssociationTarget() {
		return associationTarget;
	}
	public void setAssociationTarget(AssociationTarget associationTarget) {
		this.associationTarget = associationTarget;
	}
}
