package org.lexevs.dao.database.ibatis.ncihistory.parameter;

import java.util.Date;

import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;

public class NCIHistoryResultSetWrapper {
	
	private String editAction;

    private java.lang.String conceptcode;

    private java.lang.String conceptName;

    private java.util.Date editDate;
    
    private String releaseAgency;

    private java.lang.String referencecode;
    
    private java.lang.String referencename;
    
    private NCIChangeEvent changeEvent;

	public NCIHistoryResultSetWrapper(String conceptCode, String conceptName, String editAction, Date editDate,
			String releaseAgency, String referenceCode, String referenceName) {
		super();

		this.conceptcode = conceptCode;
		this.conceptName = conceptName;
		this.editAction = editAction;
		this.editDate = editDate;
		this.releaseAgency = releaseAgency;
		this.referencecode = referenceCode;
		this.referencename = referenceName;
	}

	public String getEditAction() {
		return editAction;
	}

	public void setEditAction(String editAction) {
		this.editAction = editAction;
	}

	public java.lang.String getConceptcode() {
		return conceptcode;
	}

	public void setConceptcode(java.lang.String conceptcode) {
		this.conceptcode = conceptcode;
	}

	public java.lang.String getConceptName() {
		return conceptName;
	}

	public void setConceptName(java.lang.String conceptName) {
		this.conceptName = conceptName;
	}

	public java.util.Date getEditDate() {
		return editDate;
	}

	public void setEditDate(java.util.Date editDate) {
		this.editDate = editDate;
	}

	public String getReleaseAgency() {
		return releaseAgency;
	}

	public void setReleaseAgency(String releaseAgency) {
		this.releaseAgency = releaseAgency;
	}

	public NCIChangeEvent getChangeEvent() {
		return changeEvent;
	}

	public void setChangeEvent(NCIChangeEvent changeEvent) {
		this.changeEvent = changeEvent;
	}

	public java.lang.String getReferencecode() {
		return referencecode;
	}

	public void setReferencecode(java.lang.String referencecode) {
		this.referencecode = referencecode;
	}

	public java.lang.String getReferencename() {
		return referencename;
	}

	public void setReferencename(java.lang.String referencename) {
		this.referencename = referencename;
	}
	
	public NCIChangeEvent buildChangeEvent()
	{ this.changeEvent = new NCIChangeEvent();
	  changeEvent.setConceptcode(getConceptcode());
	  changeEvent.setConceptName(getConceptName());
	  changeEvent.setEditaction(ChangeType.fromValue(getEditAction().toLowerCase()));
	  changeEvent.setEditDate(getEditDate());
	  changeEvent.setReferencecode(getReferencecode());
	  changeEvent.setReferencename(getReferencename());
	  return changeEvent;
	}

}
