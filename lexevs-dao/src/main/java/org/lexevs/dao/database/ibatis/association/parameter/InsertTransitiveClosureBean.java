package org.lexevs.dao.database.ibatis.association.parameter;

import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertTransitiveClosureBean extends IdableParameterBean {
	
	private String associationPredicateId;

	private String sourceEntityCode;
	
	private String sourceEntityCodeNamespace;
	
	private String targetEntityCode;
	
	private String targetEntityCodeNamespace;

	public InsertTransitiveClosureBean() {
		super();
	}

	public String getAssociationPredicateId() {
		return associationPredicateId;
	}

	public void setAssociationPredicateId(String associationPredicateId) {
		this.associationPredicateId = associationPredicateId;
	}

	public String getSourceEntityCode() {
		return sourceEntityCode;
	}

	public void setSourceEntityCode(String sourceEntityCode) {
		this.sourceEntityCode = sourceEntityCode;
	}

	public String getSourceEntityCodeNamespace() {
		return sourceEntityCodeNamespace;
	}

	public void setSourceEntityCodeNamespace(String sourceEntityCodeNamespace) {
		this.sourceEntityCodeNamespace = sourceEntityCodeNamespace;
	}

	public String getTargetEntityCode() {
		return targetEntityCode;
	}

	public void setTargetEntityCode(String targetEntityCode) {
		this.targetEntityCode = targetEntityCode;
	}

	public String getTargetEntityCodeNamespace() {
		return targetEntityCodeNamespace;
	}

	public void setTargetEntityCodeNamespace(String targetEntityCodeNamespace) {
		this.targetEntityCodeNamespace = targetEntityCodeNamespace;
	}
}
