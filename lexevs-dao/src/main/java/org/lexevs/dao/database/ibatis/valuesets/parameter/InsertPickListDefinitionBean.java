
package org.lexevs.dao.database.ibatis.valuesets.parameter;

import java.util.List;

import org.LexGrid.valueSets.PickListDefinition;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertPickListDefinitionBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertPickListDefinitionBean extends IdableParameterBean {

	/** The pick list definition. */
	private PickListDefinition pickListDefinition;
	
	/** The system release id. */
	private String systemReleaseUId;
	
	private List<InsertOrUpdateValueSetsMultiAttribBean> vsMultiAttribList = null;

	/**
	 * Sets the pick list definition.
	 * 
	 * @param pickListDefinition the new pick list definition
	 */
	public void setPickListDefinition(PickListDefinition pickListDefinition) {
		this.pickListDefinition = pickListDefinition;
	}

	/**
	 * Gets the pick list definition.
	 * 
	 * @return the pick list definition
	 */
	public PickListDefinition getPickListDefinition() {
		return pickListDefinition;
	}

	/**
	 * Sets the system release id.
	 * 
	 * @param systemReleaseUId the new system release id
	 */
	public void setSystemReleaseUId(String systemReleaseUId) {
		this.systemReleaseUId = systemReleaseUId;
	}

	/**
	 * Gets the system release id.
	 * 
	 * @return the system release id
	 */
	public String getSystemReleaseUId() {
		return systemReleaseUId;
	}

	/**
	 * @return the vsMultiAttribList
	 */
	public List<InsertOrUpdateValueSetsMultiAttribBean> getVsMultiAttribList() {
		return vsMultiAttribList;
	}

	/**
	 * @param vsMultiAttribList the vsMultiAttribList to set
	 */
	public void setVsMultiAttribList(
			List<InsertOrUpdateValueSetsMultiAttribBean> vsMultiAttribList) {
		this.vsMultiAttribList = vsMultiAttribList;
	}
}