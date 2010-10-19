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
package org.lexgrid.loader.reader;

import java.util.ArrayList;
import java.util.List;

import org.lexgrid.loader.reader.support.NeverSkipPolicy;
import org.lexgrid.loader.reader.support.SkipPolicy;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * The Class FactoryBeanReader.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class FactoryBeanReader<T> implements FactoryBean, InitializingBean{
	
	/** The delegate. */
	private ItemReader<T> delegate;
	
	/** The return list. */
	private List<T> returnList = new ArrayList<T>();
	
	/** The skip policy. */
	private SkipPolicy<T> skipPolicy = new NeverSkipPolicy();

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		for(T item = delegate.read(); item != null; item = delegate.read()){
			if(!skipPolicy.toSkip(item)){
				returnList.add(item);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return List.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Gets the delegate.
	 * 
	 * @return the delegate
	 */
	public ItemReader<T> getDelegate() {
		return delegate;
	}

	/**
	 * Sets the delegate.
	 * 
	 * @param delegate the new delegate
	 */
	public void setDelegate(ItemReader<T> delegate) {
		this.delegate = delegate;
	}

	/**
	 * Gets the skip policy.
	 * 
	 * @return the skip policy
	 */
	public SkipPolicy<T> getSkipPolicy() {
		return skipPolicy;
	}

	/**
	 * Sets the skip policy.
	 * 
	 * @param skipPolicy the new skip policy
	 */
	public void setSkipPolicy(SkipPolicy<T> skipPolicy) {
		this.skipPolicy = skipPolicy;
	}
}