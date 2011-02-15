/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.internal.mapper;

import org.dozer.DozerBeanMapper;

/**
 * The Class BaseDozerBeanMapper.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BaseDozerBeanMapper implements BeanMapper{

	/** The dozer bean mapper. */
	private DozerBeanMapper dozerBeanMapper;
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.mapper.BeanMapper#map(java.lang.Object, java.lang.Class)
	 */
	public <T> T map(Object source, Class<T> targetClass){
		return dozerBeanMapper.map(source, targetClass);
	}

	/**
	 * Gets the dozer bean mapper.
	 *
	 * @return the dozer bean mapper
	 */
	public DozerBeanMapper getDozerBeanMapper() {
		return dozerBeanMapper;
	}

	/**
	 * Sets the dozer bean mapper.
	 *
	 * @param dozerBeanMapper the new dozer bean mapper
	 */
	public void setDozerBeanMapper(DozerBeanMapper dozerBeanMapper) {
		this.dozerBeanMapper = dozerBeanMapper;
	}

}
