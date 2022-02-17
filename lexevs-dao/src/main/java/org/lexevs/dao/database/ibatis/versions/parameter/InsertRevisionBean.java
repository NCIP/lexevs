
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