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
package org.lexevs.dao.database.ibatis.versions.parameter;

import java.sql.Date;
import java.sql.Timestamp;

import org.LexGrid.versions.Revision;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertRevisionBean extends IdableParameterBean {

/** revision object */
private Revision revision = null;

	/** revision Guid*/
	private String revisionGuid = null;
	
	/** system release guid*/
	private String releaseGuid = null;
	
	/** revision applied date */
	private Timestamp revAppliedDate = null;
	
	public String getRevisionGuid() {
		return revisionGuid;
	}

	public void setRevisionGuid(String revisionGuid) {
		this.revisionGuid = revisionGuid;
	}

	/**
	 * get system release guid
	 * 
	 * @return systemReleaseId
	 */
	public String getReleaseGuid() {
		return releaseGuid;
	}

	/**
	 * set system release guid
	 * 
	 * @param releaseGuid
	 */
	public void setReleaseGuid(String releaseGuid) {
		this.releaseGuid = releaseGuid;
	}

	/**
	 * get systemRelease object.
	 * 
	 * @return
	 */
	public Revision getRevision() {
		return revision;
	}

	/**
	 * set systemRelease object.
	 * 
	 * @param systemRelease
	 */
	public void setRevision(Revision revision) {
		this.revision = revision;
	}

	public Timestamp getRevAppliedDate() {
		return revAppliedDate;
	}

	public void setRevAppliedDate(Timestamp revAppliedDate) {
		this.revAppliedDate = revAppliedDate;
	}
}