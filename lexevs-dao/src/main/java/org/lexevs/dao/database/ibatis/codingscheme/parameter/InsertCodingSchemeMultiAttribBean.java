package org.lexevs.dao.database.ibatis.codingscheme.parameter;

import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertCodingSchemeMultiAttribBean extends IdableParameterBean {

	private String codingSchemeId;
	private String attributeType;
	private String attributeValue;
	private String subRef;
	private String role;
	
	public String getAttributeType() {
		return attributeType;
	}
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}
	
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	public String getSubRef() {
		return subRef;
	}
	public void setSubRef(String subRef) {
		this.subRef = subRef;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public void setCodingSchemeId(String codingSchemeId) {
		this.codingSchemeId = codingSchemeId;
	}
	public String getCodingSchemeId() {
		return codingSchemeId;
	}
}

