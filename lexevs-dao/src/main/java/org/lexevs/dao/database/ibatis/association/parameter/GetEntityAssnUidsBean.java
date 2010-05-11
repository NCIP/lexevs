package org.lexevs.dao.database.ibatis.association.parameter;

public class GetEntityAssnUidsBean extends GetEntityAssnUidsCountBean{

	private String associationPredicateUid;

	public void setAssociationPredicateUid(String associationPredicateUid) {
		this.associationPredicateUid = associationPredicateUid;
	}

	public String getAssociationPredicateUid() {
		return associationPredicateUid;
	}
}
