
package org.lexevs.dao.database.ibatis.association.parameter;

import java.util.Date;
import java.util.List;

import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationSource;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertOrUpdateAssociationDataBean extends IdableParameterBean{

/** The association predicate id. */
private String associationPredicateUId;
	
	/** The association source. */
	private AssociationSource associationSource;
	
	/** The association data. */
	private AssociationData associationData;
	

	/** The association qualifications and usage contexts.*/
	private List<InsertAssociationQualificationOrUsageContextBean> assnQualsAndUsageContextList = null;
	
	
	
	private String sourceEntityCode;
	private String sourceEntityCodeNamespace;
	private String associationInstanceId;
	private Boolean isDefining;
	private Boolean isInferred;
	private String dataValue;
	private Boolean isActive;
	private String owner;
	private String status;
	private Date effectiveDate;
	private Date expirationDate;
	private String entryStateGuid;
	

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

	public String getAssociationInstanceId() {
		return associationInstanceId;
	}

	public void setAssociationInstanceId(String associationInstanceId) {
		this.associationInstanceId = associationInstanceId;
	}

	public Boolean getIsDefining() {
		return isDefining;
	}

	public void setIsDefining(Boolean boolean1) {
		this.isDefining = boolean1;
	}

	public Boolean getIsInferred() {
		return isInferred;
	}

	public void setIsInferred(Boolean boolean1) {
		this.isInferred = boolean1;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean boolean1) {
		this.isActive = boolean1;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date date) {
		this.effectiveDate = date;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date date) {
		this.expirationDate = date;
	}

	public String getEntryStateGuid() {
		return entryStateGuid;
	}

	public void setEntryStateGuid(String entryStateGuid) {
		this.entryStateGuid = entryStateGuid;
	}

	/**
	 * Gets the association predicate id.
	 * 
	 * @return the association predicate id
	 */
	public String getAssociationPredicateUId() {
		return associationPredicateUId;
	}
	
	/**
	 * Sets the association predicate id.
	 * 
	 * @param associationPredicateUId the new association predicate id
	 */
	public void setAssociationPredicateUId(String associationPredicateUId) {
		this.associationPredicateUId = associationPredicateUId;
	}
	
	/**
	 * Gets the association source.
	 * 
	 * @return the association source
	 */
	public AssociationSource getAssociationSource() {
		return associationSource;
	}
	
	/**
	 * Sets the association source.
	 * 
	 * @param associationSource the new association source
	 */
	public void setAssociationSource(AssociationSource associationSource) {
		this.associationSource = associationSource;
	}
	
	/**
	 * Gets the association data.
	 * 
	 * @return the association data
	 */
	public AssociationData getAssociationData() {
		return associationData;
	}
	
	/**
	 * Sets the association data.
	 * 
	 * @param associationData the new association data
	 */
	public void setAssociationData(AssociationData associationData) {
		this.associationData = associationData;
	}

	/**
	 * @return the assnQualsAndUsageContext
	 */
	public List<InsertAssociationQualificationOrUsageContextBean> getAssnQualsAndUsageContext() {
		return assnQualsAndUsageContextList;
	}

	/**
	 * @param assnQualsAndUsageContext the assnQualsAndUsageContext to set
	 */
	public void setAssnQualsAndUsageContext(
			List<InsertAssociationQualificationOrUsageContextBean> assnQualsAndUsageContext) {
		this.assnQualsAndUsageContextList = assnQualsAndUsageContext;
	}
}