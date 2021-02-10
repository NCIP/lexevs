
package org.lexevs.dao.database.ibatis.association.parameter;

import org.LexGrid.relations.AssociationPredicate;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertAssociationPredicateBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertAssociationPredicateBean extends IdableParameterBean {
	
	/** The association predicate. */
	private AssociationPredicate associationPredicate;
	
	/** The relation id. */
	private String relationUId;
	
	/**
	 * Gets the association predicate.
	 * 
	 * @return the association predicate
	 */
	public AssociationPredicate getAssociationPredicate() {
		return associationPredicate;
	}
	
	/**
	 * Sets the association predicate.
	 * 
	 * @param associationPredicate the new association predicate
	 */
	public void setAssociationPredicate(AssociationPredicate associationPredicate) {
		this.associationPredicate = associationPredicate;
	}
	
	/**
	 * Gets the relation id.
	 * 
	 * @return the relation id
	 */
	public String getRelationUId() {
		return relationUId;
	}
	
	/**
	 * Sets the relation id.
	 * 
	 * @param relationUId the new relation id
	 */
	public void setRelationUId(String relationUId) {
		this.relationUId = relationUId;
	}
}