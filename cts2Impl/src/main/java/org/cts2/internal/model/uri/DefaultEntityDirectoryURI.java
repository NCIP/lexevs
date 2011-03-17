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

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.cts2.core.Directory;
import org.cts2.core.Filter;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.uri.restrict.NonIterableBasedResolvingRestrictionHandler;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.EntityDirectoryURI;

/**
 * The Class DefaultEntityDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultEntityDirectoryURI extends AbstractResolvingDirectoryURI<EntityDirectoryURI> implements EntityDirectoryURI {
	
	private CodedNodeSet codedNodeSet;
	
	/** The bean mapper. */
	private BeanMapper beanMapper;
	
	/** The restriction handler. */
	private NonIterableBasedResolvingRestrictionHandler<CodedNodeSet> restrictionHandler;
	
	/**
	 * Instantiates a new default code system version directory uri.
	 *
	 * @param codedNodeSet the coded node set
	 * @param restrictionHandler the restriction handler
	 * @param beanMapper the bean mapper
	 */
	public DefaultEntityDirectoryURI(
			CodedNodeSet codedNodeSet,
			NonIterableBasedResolvingRestrictionHandler<CodedNodeSet> restrictionHandler,
			BeanMapper beanMapper) {
		super();
		this.codedNodeSet = codedNodeSet;
		this.restrictionHandler = restrictionHandler;
		this.beanMapper = beanMapper;
	}
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractResolvingDirectoryURI#doGet(org.cts2.service.core.NameOrURI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext, java.lang.Class)
	 */
	@Override
	protected <D extends Directory<?>> D doGet(
			QueryControl queryControl, 
			ReadContext readContext,
			Class<D> resolveClass) {
		//TODO: deal with ReadContext, format, etc...
		ResolvedConceptReferenceList list;
		try {
			list = this.codedNodeSet.resolveToList(null, null, null, null, false, queryControl.getMaxToReturn().intValue());
		} catch (LBException e) {
			//TODO: throw real CTS2 exception here
			throw new RuntimeException(e);
		} 
		
		return this.beanMapper.map(list, resolveClass);
	}
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractDirectoryURI#restrict(org.cts2.core.Filter)
	 */
	@Override
	public EntityDirectoryURI restrict(Filter filter) {
		this.codedNodeSet = this.restrictionHandler.restrict(this.codedNodeSet, filter);
		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractDirectoryURI#doCount(org.cts2.service.core.ReadContext)
	 */
	@Override
	protected int doCount(ReadContext readContext) {
		try {
			return this.codedNodeSet.resolve(null, null, null, null, false).numberRemaining();
		} catch (LBException e) {
			//TODO: throw real CTS2 exception here
			throw new RuntimeException(e);
		}
	}
}
