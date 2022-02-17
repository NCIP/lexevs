
package org.lexevs.dao.database.ibatis.codingscheme.parameter;

import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertCodingSchemeMultiAttribBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateCodingSchemeMultiAttribBean extends IdableParameterBean {

	/** The coding scheme id. */
	private String codingSchemeUId;
	
	/** The attribute type. */
	private String attributeType;
	
	/** The attribute value. */
	private String attributeValue;
	
	/** The sub ref. */
	private String subRef;
	
	/** The role. */
	private String role;
	
	/**
	 * Gets the attribute type.
	 * 
	 * @return the attribute type
	 */
	public String getAttributeType() {
		return attributeType;
	}
	
	/**
	 * Sets the attribute type.
	 * 
	 * @param attributeType the new attribute type
	 */
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}
	
	/**
	 * Gets the attribute value.
	 * 
	 * @return the attribute value
	 */
	public String getAttributeValue() {
		return attributeValue;
	}
	
	/**
	 * Sets the attribute value.
	 * 
	 * @param attributeValue the new attribute value
	 */
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	/**
	 * Gets the sub ref.
	 * 
	 * @return the sub ref
	 */
	public String getSubRef() {
		return subRef;
	}
	
	/**
	 * Sets the sub ref.
	 * 
	 * @param subRef the new sub ref
	 */
	public void setSubRef(String subRef) {
		this.subRef = subRef;
	}
	
	/**
	 * Gets the role.
	 * 
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	
	/**
	 * Sets the role.
	 * 
	 * @param role the new role
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * Sets the coding scheme id.
	 * 
	 * @param codingSchemeUId the new coding scheme id
	 */
	public void setCodingSchemeUId(String codingSchemeUId) {
		this.codingSchemeUId = codingSchemeUId;
	}
	
	/**
	 * Gets the coding scheme id.
	 * 
	 * @return the coding scheme id
	 */
	public String getCodingSchemeUId() {
		return codingSchemeUId;
	}
}