
package org.lexevs.dao.database.ibatis.valuesets.parameter;

import java.util.List;

import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertValueSetDefinitionBean.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class InsertValueSetDefinitionBean extends IdableParameterBean {

	/** The value set definition. */
	private ValueSetDefinition valueSetDefinition;
	
	/** The system release id. */
	private String systemReleaseUId;
	
	private List<InsertOrUpdateValueSetsMultiAttribBean> vsMultiAttribList = null;

	/**
	 * Sets the value set definition.
	 * 
	 * @param valueSetDefinition the new value set definition
	 */
	public void setValueSetDefinition(ValueSetDefinition valueSetDefinition) {
		this.valueSetDefinition = valueSetDefinition;
	}

	/**
	 * Gets the value Set Definition.
	 * 
	 * @return the valueSetDefinition
	 */
	public ValueSetDefinition getValueSetDefinition() {
		return valueSetDefinition;
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