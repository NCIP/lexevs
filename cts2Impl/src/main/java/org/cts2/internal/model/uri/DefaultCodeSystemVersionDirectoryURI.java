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

import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.cts2.core.Directory;
import org.cts2.core.Filter;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.uri.restrict.IterableBasedResolvingRestrictionHandler;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.service.core.types.ActiveOrAll;
import org.cts2.uri.CodeSystemVersionDirectoryURI;

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
	
	private IterableBasedResolvingRestrictionHandler<CodingSchemeRendering> restrictionHandler;
	/**
	 * Instantiates a new default code system version directory uri.
	 *
	 * @param codingSchemeRenderingList the coding scheme rendering list
	 * @param beanMapper the bean mapper
	 */
	public DefaultCodeSystemVersionDirectoryURI(
			CodingSchemeRenderingList codingSchemeRenderingList,
			IterableBasedResolvingRestrictionHandler<CodingSchemeRendering> restrictionHandler,
			BeanMapper beanMapper) {
		super();
		this.codingSchemeRenderingList = codingSchemeRenderingList;
		this.restrictionHandler = restrictionHandler;
		this.beanMapper = beanMapper;
	}

	@Override
	public CodeSystemVersionDirectoryURI restrict(Filter filter) {
		List<CodingSchemeRendering> originalState = 
			Arrays.asList(this.codingSchemeRenderingList.getCodingSchemeRendering());
		
		Iterable<CodingSchemeRendering> restrictedList = 
			this.restrictionHandler.restrict(originalState, filter);
		
		CodingSchemeRenderingList newRenderingList = new CodingSchemeRenderingList();
		
		for(CodingSchemeRendering newItem : restrictedList){
			newRenderingList.addCodingSchemeRendering(newItem);
		}
		
		this.codingSchemeRenderingList = newRenderingList;
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractDirectoryURI#doCount(org.cts2.service.core.ReadContext)
	 */
	@Override
	protected int doCount(ReadContext readContext) {
		return this.codingSchemeRenderingList.getCodingSchemeRenderingCount();
	}
	
	protected CodingSchemeRenderingList restrictToActiveOrAll(CodingSchemeRenderingList csrl, ActiveOrAll activeOrAll){
		if(activeOrAll == null){
			return csrl;
		}
		
		CodingSchemeRenderingList returnList = new CodingSchemeRenderingList();
		
		for (CodingSchemeRendering csr : csrl.getCodingSchemeRendering()) {
			boolean active = 
				csr.getRenderingDetail().getVersionStatus().equals(CodingSchemeVersionStatus.ACTIVE);
			
			switch (activeOrAll) {
				case ACTIVE_ONLY : {
					if(!active){
						break;
					}
				} 
				default : {
					returnList.addCodingSchemeRendering(csr);
				}
			}
		}
		
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractResolvingDirectoryURI#doGet(org.cts2.service.core.NameOrURI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext, java.lang.Class)
	 */
	@Override
	protected <D extends Directory<?>> D doGet(
			QueryControl queryControl, 
			final ReadContext readContext, 
			final Class<D> resolveClass) {

		CodingSchemeRenderingList csrl = restrictToActiveOrAll(codingSchemeRenderingList, readContext.getActive());

		return beanMapper.map(
				csrl, 
				resolveClass);
	}

}
