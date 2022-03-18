
package org.lexevs.dao.database.ibatis.association.parameter;

import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertAssociationQualificationOrUsageContextBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertAssociationQualificationOrUsageContextBean extends IdableParameterBean {

	/** The association target id. */
	private String referenceUId;
	
	/** The qualifier name. */
	private String qualifierName;
	
	/** The qualifier value. */
	private String qualifierValue;
	
	/**
	 * Gets the qualifier name.
	 * 
	 * @return the qualifier name
	 */
	public String getQualifierName() {
		return qualifierName;
	}
	
	/**
	 * Sets the qualifier name.
	 * 
	 * @param qualifierName the new qualifier name
	 */
	public void setQualifierName(String qualifierName) {
		this.qualifierName = qualifierName;
	}
	
	/**
	 * Gets the qualifier value.
	 * 
	 * @return the qualifier value
	 */
	public String getQualifierValue() {
		return qualifierValue;
	}
	
	/**
	 * Sets the qualifier value.
	 * 
	 * @param qualifierValue the new qualifier value
	 */
	public void setQualifierValue(String qualifierValue) {
		this.qualifierValue = qualifierValue;
	}

	/**
	 * @return the referenceUId
	 */
	public String getReferenceUId() {
		return referenceUId;
	}

	/**
	 * @param referenceUId the referenceUId to set
	 */
	public void setReferenceUId(String referenceUId) {
		this.referenceUId = referenceUId;
	}
	
	
}