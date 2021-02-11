
package org.lexevs.dao.database.ibatis.tablemetadata.parameter;

import org.lexevs.dao.database.ibatis.parameter.PrefixedTableParameterBean;

/**
 * The Class InsertTableMetadataBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertTableMetadataBean extends PrefixedTableParameterBean {

	/** The version. */
	private String version;
	
	/** The description. */
	private String description;
	
	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Sets the version.
	 * 
	 * @param version the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 * 
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}