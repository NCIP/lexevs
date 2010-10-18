/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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