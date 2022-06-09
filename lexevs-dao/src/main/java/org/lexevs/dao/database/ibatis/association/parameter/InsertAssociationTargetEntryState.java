package org.lexevs.dao.database.ibatis.association.parameter;

import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertAssociationTargetEntryState extends IdableParameterBean {
	
	private String codingSchemeUID;
	private String associationTargetUId;
	private EntryStateType entryStateType;
	private String previousEntryStateUId;
	private EntryState entryState; 
	
	public String getCodingSchemeUID() {
		return codingSchemeUID;
	}
	public void setCodingSchemeUID(String codingSchemeUID) {
		this.codingSchemeUID = codingSchemeUID;
	}
	public String getAssociationTargetUId() {
		return associationTargetUId;
	}
	public void setAssociationTargetUId(String associtationTargetUId) {
		this.associationTargetUId = associtationTargetUId;
	}
	public EntryStateType getEntryStateType() {
		return entryStateType;
	}
	public void setEntryStateType(EntryStateType entryStateType) {
		this.entryStateType = entryStateType;
	}
	public String getPreviousEntryStateUId() {
		return previousEntryStateUId;
	}
	public void setPreviousEntryStateUId(String previousEntryStateUId) {
		this.previousEntryStateUId = previousEntryStateUId;
	}
	public EntryState getEntryState() {
		return entryState;
	}
	public void setEntryState(EntryState entryState) {
		this.entryState = entryState;
	}

}
