
package org.lexevs.dao.database.ibatis.association.parameter;

import org.LexGrid.relations.AssociationEntity;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertAssociationPredicateBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateAssociationEntityBean extends IdableParameterBean {
	
	private String entityUId;

	private AssociationEntity associationEntity;

	public void setAssociationEntity(AssociationEntity associationEntity) {
		this.associationEntity = associationEntity;
	}

	public AssociationEntity getAssociationEntity() {
		return associationEntity;
	}

	public void setEntityUId(String entityUId) {
		this.entityUId = entityUId;
	}

	public String getEntityUId() {
		return entityUId;
	}
}