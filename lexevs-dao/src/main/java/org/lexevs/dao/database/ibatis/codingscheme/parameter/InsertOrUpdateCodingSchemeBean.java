
package org.lexevs.dao.database.ibatis.codingscheme.parameter;

import java.util.Date;
import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertOrUpdateCodingSchemeBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateCodingSchemeBean extends IdableParameterBean{
	
	
	private String codingSchemeGuid;
	private String codingSchemeName;
	private String codingSchemeURI;
	private String representsVersion;
	private String formalName;
	private String defaultLanguage;
	private Long approxNumConcepts;
	private String description;
	private String copyright;
	private Boolean isActive;
	private String owner;
	private String status;
	private Date effectiveDate;
	private Date expirationDate;
	private String releaseGuid;
	private String entryStateGuid;
	
	private List<InsertOrUpdateCodingSchemeMultiAttribBean> csMultiAttribList = null;
	
	/** The system release uid*/
	private String releaseUId = null;


	public String getReleaseUId() {
		return releaseUId;
	}

	public void setReleaseUId(String releaseUId) {
		this.releaseUId = releaseUId;
	}

	/**
	 * @return the csMultiAttribList
	 */
	public List<InsertOrUpdateCodingSchemeMultiAttribBean> getCsMultiAttribList() {
		return csMultiAttribList;
	}

	/**
	 * @param csMultiAttribList the csMultiAttribList to set
	 */
	public void setCsMultiAttribList(
			List<InsertOrUpdateCodingSchemeMultiAttribBean> csMultiAttribList) {
		this.csMultiAttribList = csMultiAttribList;
	}

	public String getCodingSchemeGuid() {
		return codingSchemeGuid;
	}

	public void setCodingSchemeGuid(String codingSchemeGuid) {
		this.codingSchemeGuid = codingSchemeGuid;
	}

	public String getCodingSchemeName() {
		return codingSchemeName;
	}

	public void setCodingSchemeName(String codingSchemeName) {
		this.codingSchemeName = codingSchemeName;
	}

	public String getCodingSchemeURI() {
		return codingSchemeURI;
	}

	public void setCodingSchemeURI(String codingSchemeUri) {
		this.codingSchemeURI = codingSchemeUri;
	}

	public String getRepresentsVersion() {
		return representsVersion;
	}

	public void setRepresentsVersion(String representsVersion) {
		this.representsVersion = representsVersion;
	}

	public String getFormalName() {
		return formalName;
	}

	public void setFormalName(String formalName) {
		this.formalName = formalName;
	}

	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	public Long getApproxNumConcepts() {
		return approxNumConcepts;
	}

	public void setApproxNumConcepts(Long long1) {
		this.approxNumConcepts = long1;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
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

	public String getReleaseGuid() {
		return releaseGuid;
	}

	public void setReleaseGuid(String releaseGuid) {
		this.releaseGuid = releaseGuid;
	}

	public String getEntryStateGuid() {
		return entryStateGuid;
	}

	public void setEntryStateGuid(String entryStateGuid) {
		this.entryStateGuid = entryStateGuid;
	}
}