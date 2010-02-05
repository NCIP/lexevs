package org.lexevs.dao.database.ibatis.tablemetadata.parameter;

import org.lexevs.dao.database.ibatis.parameter.PrefixedTableParameterBean;

public class InsertTableMetadataBean extends PrefixedTableParameterBean {

	private String version;
	private String description;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
