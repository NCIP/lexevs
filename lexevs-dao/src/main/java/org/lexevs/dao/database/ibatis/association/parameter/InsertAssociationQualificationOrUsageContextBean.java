package org.lexevs.dao.database.ibatis.association.parameter;

import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertAssociationQualificationOrUsageContextBean extends IdableParameterBean {

	private String associationTargetId;
	private String qualifierName;
	private String qualifierValue;
	
	public String getAssociationTargetId() {
		return associationTargetId;
	}
	public void setAssociationTargetId(String associationTargetId) {
		this.associationTargetId = associationTargetId;
	}
	public String getQualifierName() {
		return qualifierName;
	}
	public void setQualifierName(String qualifierName) {
		this.qualifierName = qualifierName;
	}
	public String getQualifierValue() {
		return qualifierValue;
	}
	public void setQualifierValue(String qualifierValue) {
		this.qualifierValue = qualifierValue;
	}
	
	
}
