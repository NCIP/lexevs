
package org.lexevs.dao.database.ibatis.entity.parameter;

import java.util.Date;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertEntityBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateEntityBean extends IdableParameterBean {

	/** The entity. */
	private Entity entity;
	
	/** The coding scheme uid. */
	private String codingSchemeUId;
	
	private String entityTypeTablePrefix;
	
	private String forwardName = null;
	
	private String reverseName = null;
	
	private Boolean isNavigable = null;
	
	private Boolean isTransitive = null;
	
	private String entityGuid;
	private String codingSchemeGuid;
	private String entityCode;
	private String entityCodeNamespace;
	private Boolean isDefined;
	private Boolean isAnonymous;
	private String description;
	private Boolean isActive;
	private String owner;
	private String status;
	private Date effectiveDate;
	private Date expirationDate;
	private String entryStateGuid;
	
	/**
	 * Gets the entity.
	 * 
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * Sets the entity.
	 * 
	 * @param entity the new entity
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	/**
	 * Gets the coding scheme id.
	 * 
	 * @return the coding scheme id
	 */
	public String getCodingSchemeUId() {
		return codingSchemeUId;
	}
	
	/**
	 * Sets the coding scheme id.
	 * 
	 * @param codingSchemeId the new coding scheme id
	 */
	public void setCodingSchemeUId(String codingSchemeUId) {
		this.codingSchemeUId = codingSchemeUId;
	}

	public void setEntityTypeTablePrefix(String entityTypeTablePrefix) {
		this.entityTypeTablePrefix = entityTypeTablePrefix;
	}

	public String getEntityTypeTablePrefix() {
		return entityTypeTablePrefix;
	}

	/**
	 * @return the forwardName
	 */
	public String getForwardName() {
		return forwardName;
	}

	/**
	 * @param forwardName the forwardName to set
	 */
	public void setForwardName(String forwardName) {
		this.forwardName = forwardName;
	}

	/**
	 * @return the reverseName
	 */
	public String getReverseName() {
		return reverseName;
	}

	/**
	 * @param reverseName the reverseName to set
	 */
	public void setReverseName(String reverseName) {
		this.reverseName = reverseName;
	}

	/**
	 * @return the isNavigable
	 */
	public Boolean getIsNavigable() {
		return isNavigable;
	}

	/**
	 * @param isNavigable the isNavigable to set
	 */
	public void setIsNavigable(Boolean isNavigable) {
		this.isNavigable = isNavigable;
	}

	/**
	 * @return the isTransitive
	 */
	public Boolean getIsTransitive() {
		return isTransitive;
	}

	/**
	 * @param isTransitive the isTransitive to set
	 */
	public void setIsTransitive(Boolean isTransitive) {
		this.isTransitive = isTransitive;
	}

	public String getEntityGuid() {
		return entityGuid;
	}

	public void setEntityGuid(String entityGuid) {
		this.entityGuid = entityGuid;
	}

	public String getCodingSchemeGuid() {
		return codingSchemeGuid;
	}

	public void setCodingSchemeGuid(String codingSchemeGuid) {
		this.codingSchemeGuid = codingSchemeGuid;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getEntityCodeNamespace() {
		return entityCodeNamespace;
	}

	public void setEntityCodeNamespace(String entityCodeNamespace) {
		this.entityCodeNamespace = entityCodeNamespace;
	}

	public Boolean getIsDefined() {
		return isDefined;
	}

	public void setIsDefined(Boolean boolean1) {
		this.isDefined = boolean1;
	}

	public Boolean getIsAnonymous() {
		return isAnonymous;
	}

	public void setIsAnonymous(Boolean boolean1) {
		this.isAnonymous = boolean1;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
}