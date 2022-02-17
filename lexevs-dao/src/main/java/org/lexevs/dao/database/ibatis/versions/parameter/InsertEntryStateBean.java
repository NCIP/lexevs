
package org.lexevs.dao.database.ibatis.versions.parameter;

import org.LexGrid.versions.EntryState;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertEntryStateBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertEntryStateBean extends IdableParameterBean {

	/** The entry id. */
	private String entryUId;
	
	/** The entry type. */
	private String entryType;
	
	/** The previous entry state id. */
	private String previousEntryStateUId;
	
	/** The entry state. */
	private EntryState entryState;
	
	/** revision guid*/
	private String revisionUId = null;
	
	/** prev revision guid*/
	private String prevRevisionUId = null;
	
	/**
	 * Gets the entry id.
	 * 
	 * @return the entry id
	 */
	public String getEntryUId() {
		return entryUId;
	}
	
	/**
	 * Sets the entry id.
	 * 
	 * @param entryUId the new entry id
	 */
	public void setEntryUId(String entryUId) {
		this.entryUId = entryUId;
	}
	
	/**
	 * Gets the entry type.
	 * 
	 * @return the entry type
	 */
	public String getEntryType() {
		return entryType;
	}
	
	/**
	 * Sets the entry type.
	 * 
	 * @param entryType the new entry type
	 */
	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}
	
	/**
	 * Gets the previous entry state id.
	 * 
	 * @return the previous entry state id
	 */
	public String getPreviousEntryStateUId() {
		return previousEntryStateUId;
	}
	
	/**
	 * Sets the previous entry state id.
	 * 
	 * @param previousEntryStateUId the new previous entry state id
	 */
	public void setPreviousEntryStateUId(String previousEntryStateUId) {
		this.previousEntryStateUId = previousEntryStateUId;
	}
	
	/**
	 * Sets the entry state.
	 * 
	 * @param entryState the new entry state
	 */
	public void setEntryState(EntryState entryState) {
		this.entryState = entryState;
	}
	
	/**
	 * Gets the entry state.
	 * 
	 * @return the entry state
	 */
	public EntryState getEntryState() {
		return entryState;
	}

	public String getRevisionUId() {
		return revisionUId;
	}

	public void setRevisionUId(String revisionUId) {
		this.revisionUId = revisionUId;
	}

	public String getPrevRevisionUId() {
		return prevRevisionUId;
	}

	public void setPrevRevisionUId(String prevRevisionUId) {
		this.prevRevisionUId = prevRevisionUId;
	}
	
	
}