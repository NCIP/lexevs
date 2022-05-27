
package org.lexevs.dao.database.ibatis.ncihistory.parameter;

import java.util.Date;

import org.LexGrid.concepts.Entity;
import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertEntityBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateNciHistoryBean extends IdableParameterBean {

	String defaultPrefix;
	String codingSchemeUri;
	String releaseGuid;
	
	String releaseURI;
	String releaseId;
	Date releaseDate;
	String basedOnRelease;
	String releaseAgency;
	String description;
	
	SystemRelease release;

	public String getDefaultPrefix() {
		return defaultPrefix;
	}

	public void setDefaultPrefix(String defaultPrefix) {
		this.defaultPrefix = defaultPrefix;
	}

	public String getCodingSchemeUri() {
		return codingSchemeUri;
	}

	public void setCodingSchemeUri(String codingSchemeUri) {
		this.codingSchemeUri = codingSchemeUri;
	}

	public String getReleaseGuid() {
		return releaseGuid;
	}

	public void setReleaseGuid(String releaseGuid) {
		this.releaseGuid = releaseGuid;
	}

	public String getReleaseURI() {
		return releaseURI;
	}

	public void setReleaseURI(String releaseURI) {
		this.releaseURI = releaseURI;
	}

	public String getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getBasedOnRelease() {
		return basedOnRelease;
	}

	public void setBasedOnRelease(String basedOnRelease) {
		this.basedOnRelease = basedOnRelease;
	}

	public String getReleaseAgency() {
		return releaseAgency;
	}

	public void setReleaseAgency(String releaseAgency) {
		this.releaseAgency = releaseAgency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SystemRelease getRelease() {
		return release;
	}

	public void setRelease(SystemRelease release) {
		this.release = release;
	}
	
	
}