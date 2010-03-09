package org.lexevs.dao.database.ibatis.picklist.parameter;

import org.LexGrid.valueDomains.PickListDefinition;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

public class InsertPickListDefinitionBean extends IdableParameterBean {

	private PickListDefinition pickListDefinition;
	private String systemReleaseId;

	public void setPickListDefinition(PickListDefinition pickListDefinition) {
		this.pickListDefinition = pickListDefinition;
	}

	public PickListDefinition getPickListDefinition() {
		return pickListDefinition;
	}

	public void setSystemReleaseId(String systemReleaseId) {
		this.systemReleaseId = systemReleaseId;
	}

	public String getSystemReleaseId() {
		return systemReleaseId;
	}
}

