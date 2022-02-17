
package org.lexevs.dao.database.ibatis.valuesets.parameter;

import org.LexGrid.valueSets.PickListEntry;
import org.LexGrid.valueSets.PickListEntryExclusion;

public class PickListEntryNodeBean extends org.LexGrid.commonTypes.Versionable 
implements java.io.Serializable{

/**
	 * 
	 */
private static final long serialVersionUID = 1L;
	
	private String vsPLEntryGuid;
	
	private String pickListEntryId;
	
	private Boolean include;
	
	private PickListEntry pickListEntry;
	
	private PickListEntryExclusion pickListEntryExclusion;
	
	private String entryStateUId = null;

	/**
	 * @return the pickListEntryId
	 */
	public String getPickListEntryId() {
		return pickListEntryId;
	}

	/**
	 * @param pickListEntryId the _pickListEntryId to set
	 */
	public void setPickListEntryId(String pickListEntryId) {
		this.pickListEntryId = pickListEntryId;
	}

	/**
	 * @return the include
	 */
	public Boolean getInclude() {
		return include;
	}

	/**
	 * @param include the include to set
	 */
	public void setInclude(Boolean include) {
		this.include = include;
	}

	/**
	 * @return the pickListEntry
	 */
	public PickListEntry getPickListEntry() {
		return pickListEntry;
	}

	/**
	 * @param pickListEntry the pickListEntry to set
	 */
	public void setPickListEntry(PickListEntry pickListEntry) {
		this.pickListEntry = pickListEntry;
	}

	/**
	 * @return the pickListEntryExclusion
	 */
	public PickListEntryExclusion getPickListEntryExclusion() {
		return pickListEntryExclusion;
	}

	/**
	 * @param pickListEntryExclusion the pickListEntryExclusion to set
	 */
	public void setPickListEntryExclusion(
			PickListEntryExclusion pickListEntryExclusion) {
		this.pickListEntryExclusion = pickListEntryExclusion;
	}

	/**
	 * @return the vsPLEntryGuid
	 */
	public String getVsPLEntryGuid() {
		return vsPLEntryGuid;
	}

	/**
	 * @param vsPLEntryGuid the vsPLEntryGuid to set
	 */
	public void setVsPLEntryGuid(String vsPLEntryGuid) {
		this.vsPLEntryGuid = vsPLEntryGuid;
	}

	/**
	 * @return the entryStateUId
	 */
	public String getEntryStateUId() {
		return entryStateUId;
	}

	/**
	 * @param entryStateUId the entryStateUId to set
	 */
	public void setEntryStateUId(String entryStateUId) {
		this.entryStateUId = entryStateUId;
	}
	
}