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
 * Version information of a resource.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class RevisionInfo {
	private String changeAgent;
	private String changeInstruction;
	private String revisionId;
	private Long editOrder;
	private Date revisionDate;
	private String description;
	private String systemReleaseURI;
	
	/**
	 * @return the revisionId
	 */
	public String getRevisionId() {
		return revisionId;
	}
	/**
	 * @param revisionId the revisionId to set
	 */
	public void setRevisionId(String revisionId) {
		this.revisionId = revisionId;
	}
	/**
	 * @return the changeAgent
	 */
	public String getChangeAgent() {
		return changeAgent;
	}
	/**
	 * @param changeAgent the changeAgent to set
	 */
	public void setChangeAgent(String changeAgent) {
		this.changeAgent = changeAgent;
	}
	/**
	 * @return the changeInstruction
	 */
	public String getChangeInstruction() {
		return changeInstruction;
	}
	/**
	 * @param changeInstruction the changeInstruction to set
	 */
	public void setChangeInstruction(String changeInstruction) {
		this.changeInstruction = changeInstruction;
	}
	/**
	 * @return the editOrder
	 */
	public Long getEditOrder() {
		return editOrder;
	}
	/**
	 * @param editOrder the editOrder to set
	 */
	public void setEditOrder(Long editOrder) {
		this.editOrder = editOrder;
	}
	/**
	 * @return the revisionDate
	 */
	public Date getRevisionDate() {
		return revisionDate;
	}
	/**
	 * @param revisionDate the revisionDate to set
	 */
	public void setRevisionDate(Date revisionDate) {
		this.revisionDate = revisionDate;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the systemReleaseURI
	 */
	public String getSystemReleaseURI() {
		return systemReleaseURI;
	}
	/**
	 * @param systemReleaseURI the systemReleaseURI to set
	 */
	public void setSystemReleaseURI(String systemReleaseURI) {
		this.systemReleaseURI = systemReleaseURI;
	}
}