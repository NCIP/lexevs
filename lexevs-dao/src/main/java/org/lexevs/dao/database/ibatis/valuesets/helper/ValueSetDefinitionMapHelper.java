
package org.lexevs.dao.database.ibatis.valuesets.helper;

import org.LexGrid.valueSets.ValueSetDefinition;

public class ValueSetDefinitionMapHelper {
	
	public String valueSetDefinitionGuid;
	public ValueSetDefinition valueSetDefinition;
	
	public String getValueSetDefinitionGuid() {
		return valueSetDefinitionGuid;
	}
	public void setValueSetDefinitionGuid(String valueSetDefinitionGuid) {
		this.valueSetDefinitionGuid = valueSetDefinitionGuid;
	}
	public ValueSetDefinition getValueSetDefinition() {
		return valueSetDefinition;
	}
	public void setValueSetDefinition(ValueSetDefinition valueSetDefinition) {
		this.valueSetDefinition = valueSetDefinition;
	}
}