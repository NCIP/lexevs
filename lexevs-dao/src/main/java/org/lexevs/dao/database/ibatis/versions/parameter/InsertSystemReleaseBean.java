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

import org.LexGrid.versions.SystemRelease;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertSystemReleaseBean extends IdableParameterBean {

/** system release guid*/
private String releaseUId = null;

	/** systemRelease object */
	private SystemRelease systemRelease = null;
	
	public String getReleaseUId() {
		return releaseUId;
	}

	public void setReleaseUId(String releaseUId) {
		this.releaseUId = releaseUId;
	}

	/**
	 * get systemRelease object.
	 * 
	 * @return
	 */
	public SystemRelease getSystemRelease() {
		return systemRelease;
	}

	/**
	 * set systemRelease object.
	 * 
	 * @param systemRelease
	 */
	public void setSystemRelease(SystemRelease systemRelease) {
		this.systemRelease = systemRelease;
	}
}