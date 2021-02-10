
package org.lexevs.dao.database.ibatis.valuesets.parameter;

import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertOrUpdateMultiAttribBean.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class InsertOrUpdateValueSetsMultiAttribBean extends IdableParameterBean {

	/** The reference GUID. */
	private String referenceUId;
	
	/** The reference type. */
	private String referenceType;
	
	/** The attribute type. */
	private String attributeType;
	
	/** The attribute value. */
	private String attributeValue;
	
	/** The sub ref. */
	private String subRef;
	
	/** The role. */
	private String role;

	/**
	 * @return the referenceGuid
	 */
	public String getReferenceUId() {
		return referenceUId;
	}

	/**
	 * @param referenceUId the referenceGuid to set
	 */
	public void setReferenceUId(String referenceUId) {
		this.referenceUId = referenceUId;
	}

	/**
	 * @return the referenceType
	 */
	public String getReferenceType() {
		return referenceType;
	}

	/**
	 * @param referenceType the referenceType to set
	 */
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

	/**
	 * @return the attributeType
	 */
	public String getAttributeType() {
		return attributeType;
	}

	/**
	 * @param attributeType the attributeType to set
	 */
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	/**
	 * @return the attributeValue
	 */
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * @param attributeValue the attributeValue to set
	 */
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	/**
	 * @return the subRef
	 */
	public String getSubRef() {
		return subRef;
	}

	/**
	 * @param subRef the subRef to set
	 */
	public void setSubRef(String subRef) {
		this.subRef = subRef;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	
}