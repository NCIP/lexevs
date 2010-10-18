/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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