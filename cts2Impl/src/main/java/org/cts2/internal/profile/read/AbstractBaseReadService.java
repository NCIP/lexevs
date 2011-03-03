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
package org.cts2.internal.profile.read;

import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.profile.AbstractBaseService;
import org.cts2.profile.read.BaseReadService;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;

/**
 * The Class AbstractBaseReadService.
 *
 * @param <T> the
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBaseReadService<T> extends AbstractBaseService implements BaseReadService<T> {
	
	/** The bean mapper. */
	private BeanMapper beanMapper;

	/* (non-Javadoc)
	 * @see org.cts2.profile.read.BaseReadService#read(org.cts2.service.core.NameOrURI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)
	 */
	@Override
	public T read(
			NameOrURI id, 
			QueryControl queryControl, 
			ReadContext readContext) {
		
		return this.doRead(id);
	}
	
	/**
	 * Do read.
	 *
	 * @param nameOrUri the name or uri
	 * @return the t
	 */
	protected abstract T doRead(NameOrURI nameOrUri);

	/* (non-Javadoc)
	 * @see org.cts2.profile.read.BaseReadService#exists(org.cts2.service.core.NameOrURI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)
	 */
	@Override
	public boolean exists(
			NameOrURI id, 
			QueryControl queryControl,
			ReadContext readContext) {
		throw new RuntimeException("Not implemented yet.");
	}

	/**
	 * Gets the bean mapper.
	 *
	 * @return the bean mapper
	 */
	public BeanMapper getBeanMapper() {
		return beanMapper;
	}

	/**
	 * Sets the bean mapper.
	 *
	 * @param beanMapper the new bean mapper
	 */
	public void setBeanMapper(BeanMapper beanMapper) {
		this.beanMapper = beanMapper;
	}
}
