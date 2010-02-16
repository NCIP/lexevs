package org.lexevs.system.constants;

import org.springframework.beans.factory.FactoryBean;

public class IndividualVariableFactory<T> implements FactoryBean {

	private T bean; 
	
	private String variableName;
	
	public Object getObject() throws Exception {
		return 
			bean.getClass().
				getMethod("get"+variableName, null).invoke(bean, null);
	}

	public Class getObjectType() {
		return String.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public T getBean() {
		return bean;
	}

	public void setBean(T bean) {
		this.bean = bean;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
}
