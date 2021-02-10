
package org.lexevs.system.constants;

import org.springframework.beans.factory.FactoryBean;

/**
 * A factory for creating IndividualVariable objects.
 */
public class IndividualVariableFactory<T> implements FactoryBean {

	/** The bean. */
	private T bean; 
	
	/** The variable name. */
	private String variableName;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return 
			bean.getClass().
				getMethod("get"+variableName, null).invoke(bean, null);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return String.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Gets the bean.
	 * 
	 * @return the bean
	 */
	public T getBean() {
		return bean;
	}

	/**
	 * Sets the bean.
	 * 
	 * @param bean the new bean
	 */
	public void setBean(T bean) {
		this.bean = bean;
	}

	/**
	 * Gets the variable name.
	 * 
	 * @return the variable name
	 */
	public String getVariableName() {
		return variableName;
	}

	/**
	 * Sets the variable name.
	 * 
	 * @param variableName the new variable name
	 */
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
}