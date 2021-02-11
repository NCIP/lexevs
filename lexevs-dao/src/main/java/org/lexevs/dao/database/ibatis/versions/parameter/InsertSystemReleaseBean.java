
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