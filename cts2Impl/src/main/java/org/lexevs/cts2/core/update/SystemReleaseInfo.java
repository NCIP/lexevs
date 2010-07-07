package org.lexevs.cts2.core.update;

import java.util.Date;

/**
 * System Release information of a resource.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class SystemReleaseInfo {
	private String basedOnRelease;
	private String releaseAgency;
	private Date releaseDate;
	private String releaseId;	
	private String releaseURI;
	private String description;
	
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
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getReleaseId() {
		return releaseId;
	}
	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
	}
	public String getReleaseURI() {
		return releaseURI;
	}
	public void setReleaseURI(String releaseURI) {
		this.releaseURI = releaseURI;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	} 
}
