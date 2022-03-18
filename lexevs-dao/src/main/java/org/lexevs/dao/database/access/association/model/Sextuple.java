
package org.lexevs.dao.database.access.association.model;

public class Sextuple {
	private String sourceEntityCode;
	private String sourceEntityNamespace;
	private String sourceEntityDescription;
	private String targetEntityCode;
	private String targetEntityNamespace;
	private String targetEntityDescription;
	private String associationPredicateId;
	
	public String getSourceEntityCode() {
		return sourceEntityCode;
	}
	public void setSourceEntityCode(String sourceEntityCode) {
		this.sourceEntityCode = sourceEntityCode;
	}
	public String getSourceEntityNamespace() {
		return sourceEntityNamespace;
	}
	public void setSourceEntityNamespace(String sourceEntityNamespace) {
		this.sourceEntityNamespace = sourceEntityNamespace;
	}
	public String getTargetEntityCode() {
		return targetEntityCode;
	}
	public void setTargetEntityCode(String targetEntityCode) {
		this.targetEntityCode = targetEntityCode;
	}
	public String getTargetEntityNamespace() {
		return targetEntityNamespace;
	}
	public void setTargetEntityNamespace(String targetEntityNamespace) {
		this.targetEntityNamespace = targetEntityNamespace;
	}
	public String getAssociationPredicateId() {
		return associationPredicateId;
	}
	public void setAssociationPredicateId(String associationPredicateId) {
		this.associationPredicateId = associationPredicateId;
	}

/**
	 * @return the sourceEntityDescription
	 */
public String getSourceEntityDescription() {
		return sourceEntityDescription;
	}
	/**
	 * @param sourceEntityDescription the sourceEntityDescription to set
	 */
	public void setSourceEntityDescription(String sourceEntityDescription) {
		this.sourceEntityDescription = sourceEntityDescription;
	}
	/**
	 * @return the targetEntityDescription
	 */
	public String getTargetEntityDescription() {
		return targetEntityDescription;
	}
	/**
	 * @param targetEntityDescription the targetEntityDescription to set
	 */
	public void setTargetEntityDescription(String targetEntityDescription) {
		this.targetEntityDescription = targetEntityDescription;
	}

}