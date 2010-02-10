package org.lexevs.system.constants;

import org.springframework.beans.factory.FactoryBean;

public class IndividualVariableFactory implements FactoryBean {

	private SystemVariables systemVariables; 
	
	private String variableName;
	
	public Object getObject() throws Exception {
		return 
			SystemVariables.class.
				getMethod("get"+variableName, null).invoke(systemVariables, null);
	}

	public Class getObjectType() {
		return String.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableName() {
		return variableName;
	}
}
