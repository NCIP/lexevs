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
package org.cts2.internal.model.uri;

import java.util.concurrent.Callable;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.cts2.core.Directory;
import org.cts2.core.FilterComponent;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.CodeSystemVersionDirectoryURI;
import org.cts2.utility.ExecutionUtils;

/**
 * The Class DefaultCodeSystemVersionDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultCodeSystemVersionDirectoryURI extends AbstractResolvingDirectoryURI<CodeSystemVersionDirectoryURI> implements CodeSystemVersionDirectoryURI{
	
	/** The coding scheme rendering list. */
	private CodingSchemeRenderingList codingSchemeRenderingList;
	
	/** The bean mapper. */
	private BeanMapper beanMapper;

	/**
	 * Instantiates a new default code system version directory uri.
	 *
	 * @param codingSchemeRenderingList the coding scheme rendering list
	 * @param beanMapper the bean mapper
	 */
	public DefaultCodeSystemVersionDirectoryURI(
			CodingSchemeRenderingList codingSchemeRenderingList,
			BeanMapper beanMapper) {
		super();
		this.codingSchemeRenderingList = codingSchemeRenderingList;
		this.beanMapper = beanMapper;
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractDirectoryURI#doRestrict(org.cts2.core.FilterComponent)
	 */
	@Override
	protected void doRestrict(FilterComponent filterComponent) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractDirectoryURI#doCount(org.cts2.service.core.ReadContext)
	 */
	@Override
	protected int doCount(ReadContext readContext) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractResolvingDirectoryURI#doGet(org.cts2.service.core.NameOrURI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext, java.lang.Class)
	 */
	@Override
	protected <D extends Directory<?>> D doGet(
			NameOrURI format,
			QueryControl queryControl, 
			ReadContext readContext, 
			final Class<D> resolveClass) {
		
		return ExecutionUtils.callWithTimeout(new Callable<D>(){

			@Override
			public D call() throws Exception {
				return beanMapper.map(codingSchemeRenderingList, resolveClass);
			}
			
		}, queryControl);
	}
}
