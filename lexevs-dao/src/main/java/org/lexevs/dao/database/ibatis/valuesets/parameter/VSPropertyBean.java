
package org.lexevs.dao.database.ibatis.valuesets.parameter;

import org.LexGrid.commonTypes.Property;

public class VSPropertyBean extends org.LexGrid.commonTypes.Versionable 
implements java.io.Serializable{

/**
	 * 
	 */
private static final long serialVersionUID = 1L;
	
	private String vsPropertyGuid;
	
	private Property property;

	/**
	 * @return the vsPropertyGuid
	 */
	public String getVsPropertyGuid() {
		return vsPropertyGuid;
	}

	/**
	 * @param vsPropertyGuid the vsPropertyGuid to set
	 */
	public void setVsPropertyGuid(String vsPropertyGuid) {
		this.vsPropertyGuid = vsPropertyGuid;
	}

	/**
	 * @return the property
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(Property property) {
		this.property = property;
	}
}