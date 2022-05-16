
package org.lexevs.dao.database.ibatis.association.parameter;

import java.util.Date;
import java.util.List;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertAssociationSourceBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateAssociationTargetBean extends IdableParameterBean{
	
	/** The association predicate id. */
	private String associationPredicateUId;
	
	/** The association source. */
	private AssociationSource associationSource;
	
	/** The association target. */
	private AssociationTarget associationTarget;
	
	/** The association qualifications and usage contexts.*/
	private List<InsertAssociationQualificationOrUsageContextBean> assnQualsAndUsageContextList = null;
	
	private String entityAssnsGuid;
	private String sourceEntityCode;
	private String sourceEntityCodeNamespace;
	private String targetEntityCode;
	private String targetEntityCodeNamespace;
	private String associationInstanceId;
	private Boolean isDefining;
	private Boolean isInferred;
	private Boolean isActive;
	private String owner;
	private String status;
	private Date effectiveDate;
	private Date expirationDate;
	private String entryStateGuid;
	
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
	 * Gets the association target.
	 * 
	 * @return the association target
	 */
	public AssociationTarget getAssociationTarget() {
		return associationTarget;
	}
	
	/**
	 * Sets the association target.
	 * 
	 * @param associationTarget the new association target
	 */
	public void setAssociationTarget(AssociationTarget associationTarget) {
		this.associationTarget = associationTarget;
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

	public List<InsertAssociationQualificationOrUsageContextBean> getAssnQualsAndUsageContextList() {
		return assnQualsAndUsageContextList;
	}

	public void setAssnQualsAndUsageContextList(
			List<InsertAssociationQualificationOrUsageContextBean> assnQualsAndUsageContextList) {
		this.assnQualsAndUsageContextList = assnQualsAndUsageContextList;
	}

	public String getEntityAssnsGuid() {
		return entityAssnsGuid;
	}

	public void setEntityAssnsGuid(String entityAssnsGuid) {
		this.entityAssnsGuid = entityAssnsGuid;
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

	public String getAssociationInstanceId() {
		return associationInstanceId;
	}

	public void setAssociationInstanceId(String associationInstanceId) {
		this.associationInstanceId = associationInstanceId;
	}

	public Boolean getIsDefining() {
		return isDefining;
	}

	public void setIsDefining(Boolean isDefining) {
		this.isDefining = isDefining;
	}

	public Boolean getIsInferred() {
		return isInferred;
	}

	public void setIsInferred(Boolean isInferred) {
		this.isInferred = isInferred;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getEntryStateGuid() {
		return entryStateGuid;
	}

	public void setEntryStateGuid(String entryStateGuid) {
		this.entryStateGuid = entryStateGuid;
	}
}