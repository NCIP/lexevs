package org.lexevs.dao.database.ibatis.versions.parameter;

import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertEntryStateBean extends IdableParameterBean {

	private String entryId;
	private String entryType;
	private String previousEntryStateId;
	private EntryState entryState;
	
	public String getEntryId() {
		return entryId;
	}
	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}
	public String getEntryType() {
		return entryType;
	}
	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}
	public String getPreviousEntryStateId() {
		return previousEntryStateId;
	}
	public void setPreviousEntryStateId(String previousEntryStateId) {
		this.previousEntryStateId = previousEntryStateId;
	}
	public void setEntryState(EntryState entryState) {
		this.entryState = entryState;
	}
	public EntryState getEntryState() {
		return entryState;
	}
	
	
}

