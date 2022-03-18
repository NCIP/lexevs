
package org.lexevs.dao.database.ibatis.association.parameter;

import java.util.List;

import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService.Sort;

public class GetEntityAssnUidsBean extends GetEntityAssnUidsCountBean{

	private String associationPredicateUid;
	
	private List<Sort> sorts;

	public void setAssociationPredicateUid(String associationPredicateUid) {
		this.associationPredicateUid = associationPredicateUid;
	}

	public String getAssociationPredicateUid() {
		return associationPredicateUid;
	}

	public void setSorts(List<Sort> sorts) {
		this.sorts = sorts;
	}

	public List<Sort> getSorts() {
		return sorts;
	}
}