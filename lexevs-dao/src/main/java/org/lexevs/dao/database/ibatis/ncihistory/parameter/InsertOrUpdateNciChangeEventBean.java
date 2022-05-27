package org.lexevs.dao.database.ibatis.ncihistory.parameter;

import java.util.Date;

import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;

public class InsertOrUpdateNciChangeEventBean {
	
	String defaultPrefix;
	String ncitHistGuid;
	String releaseGuid;
	String entityCode;
	String conceptName;
	ChangeType editAction;
	Date editDate;
	String referenceCode;
	String referenceName;
	
	public String getDefaultPrefix() {
		return defaultPrefix;
	}
	public void setDefaultPrefix(String defaultPrefix) {
		this.defaultPrefix = defaultPrefix;
	}
	public String getNcitHistGuid() {
		return ncitHistGuid;
	}
	public void setNcitHistGuid(String ncitHistGuid) {
		this.ncitHistGuid = ncitHistGuid;
	}
	public String getReleaseGuid() {
		return releaseGuid;
	}
	public void setReleaseGuid(String releaseGuid) {
		this.releaseGuid = releaseGuid;
	}
	public String getEntityCode() {
		return entityCode;
	}
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}
	public String getConceptName() {
		return conceptName;
	}
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	public ChangeType getEditAction() {
		return editAction;
	}
	public void setEditAction(ChangeType changeType) {
		this.editAction = changeType;
	}
	public Date getEditDate() {
		return editDate;
	}
	public void setEditDate(Date date) {
		this.editDate = date;
	}
	public String getReferenceCode() {
		return referenceCode;
	}
	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}
	public String getReferenceName() {
		return referenceName;
	}
	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}
	
	

}
