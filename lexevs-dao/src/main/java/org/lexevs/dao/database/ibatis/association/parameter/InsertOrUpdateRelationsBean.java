
package org.lexevs.dao.database.ibatis.association.parameter;

import java.util.Date;

import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertRelationsBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateRelationsBean extends IdableParameterBean {
	
	/** The relations. */
	private Relations relations;
	
	/** The coding scheme id. */
	private String codingSchemeUId;
	private String relationGuid;
	private String codingSchemeGuid;
	private String containerName;
	private Boolean isMapping;
	private String sourceCodingScheme;
	private String sourceCodingSchemeVersion;
	private String targetCodingScheme;
	private String targetCodingSchemeVersion;
	private String description;
	private Boolean isActive;
	private String owner;
	private String status;
	private Date effectiveDate;
	private Date expirationDate;
	private String entryStateGuid;

	/**
	 * Sets the relations.
	 * 
	 * @param relations the new relations
	 */
	public void setRelations(Relations relations) {
		this.relations = relations;
	}

	/**
	 * Gets the relations.
	 * 
	 * @return the relations
	 */
	public Relations getRelations() {
		return relations;
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

	public String getRelationGuid() {
		return relationGuid;
	}

	public void setRelationGuid(String relationGuid) {
		this.relationGuid = relationGuid;
	}

	public String getCodingSchemeGuid() {
		return codingSchemeGuid;
	}

	public void setCodingSchemeGuid(String codingSchemeGuid) {
		this.codingSchemeGuid = codingSchemeGuid;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public Boolean getIsMapping() {
		return isMapping;
	}

	public void setIsMapping(Boolean boolean1) {
		this.isMapping = boolean1;
	}

	public String getSourceCodingScheme() {
		return sourceCodingScheme;
	}

	public void setSourceCodingScheme(String sourceCodingScheme) {
		this.sourceCodingScheme = sourceCodingScheme;
	}

	public String getSourceCodingSchemeVersion() {
		return sourceCodingSchemeVersion;
	}

	public void setSourceCodingSchemeVersion(String sourceCodingSchemeVersion) {
		this.sourceCodingSchemeVersion = sourceCodingSchemeVersion;
	}

	public String getTargetCodingScheme() {
		return targetCodingScheme;
	}

	public void setTargetCodingScheme(String targetCodingScheme) {
		this.targetCodingScheme = targetCodingScheme;
	}

	public String getTargetCodingSchemeVersion() {
		return targetCodingSchemeVersion;
	}

	public void setTargetCodingSchemeVersion(String targetCodingSchemeVersion) {
		this.targetCodingSchemeVersion = targetCodingSchemeVersion;
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