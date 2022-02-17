
package org.lexevs.dao.database.ibatis.property.parameter;

import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertPropertyMultiAttribBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertPropertyMultiAttribBean extends IdableParameterBean {

	/** The property id. */
	private String propertyUId;
	
	/** The attribute type. */
	private String attributeType;
	
	/** The attribute id. */
	private String attributeId;
	
	/** The attribute value. */
	private String attributeValue;
	
	/** The sub ref. */
	private String subRef;
	
	/** The role. */
	private String role;
	
	/** qualifier type of a property */
	private String qualifierType;
	
	/**
	 * Gets the property id.
	 * 
	 * @return the property id
	 */
	public String getPropertyUId() {
		return propertyUId;
	}
	
	/**
	 * Sets the property id.
	 * 
	 * @param propertyUId the new property id
	 */
	public void setPropertyUId(String propertyUId) {
		this.propertyUId = propertyUId;
	}
	
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
	 * Sets the attribute id.
	 * 
	 * @param attributeId the new attribute id
	 */
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	
	/**
	 * Gets the attribute id.
	 * 
	 * @return the attribute id
	 */
	public String getAttributeId() {
		return attributeId;
	}

	/**
	 * @return the qualifierType
	 */
	public String getQualifierType() {
		return qualifierType;
	}

	/**
	 * @param qualifierType the qualifierType to set
	 */
	public void setQualifierType(String qualifierType) {
		this.qualifierType = qualifierType;
	}
}