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

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.core.EntityReference;
import org.cts2.core.types.SetOperator;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.uri.restrict.IterableBasedResolvingRestrictionHandler;
import org.cts2.service.core.types.ActiveOrAll;
import org.cts2.service.core.types.RestrictionType;
import org.cts2.uri.CodeSystemVersionDirectoryURI;
import org.cts2.uri.restriction.CodeSystemVersionRestrictionState;
import org.cts2.uri.restriction.CodeSystemVersionRestrictionState.RestrictToEntitiesRestriction;
import org.cts2.uri.restriction.SetComposite;

import scala.actors.threadpool.Arrays;

import com.google.common.collect.Iterables;

/**
 * The Class DefaultCodeSystemVersionDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultCodeSystemVersionDirectoryURI extends 
	AbstractIterableLexEvsBackedResolvingDirectoryURI<CodingSchemeRendering,CodeSystemVersionDirectoryURI> 
	implements CodeSystemVersionDirectoryURI{
	
	/** The coding scheme rendering list. */
	private CodingSchemeRenderingList codingSchemeRenderingList;
	
	/** The bean mapper. */
	private BeanMapper beanMapper;
	
	private LexBIGService lexBigService;

	private CodeSystemVersionRestrictionState restrictionState = new CodeSystemVersionRestrictionState();
	
	/**
	 * Instantiates a new default code system version directory uri.
	 *
	 * @param codingSchemeRenderingList the coding scheme rendering list
	 * @param restrictionHandler the restriction handler
	 * @param beanMapper the bean mapper
	 */
	public DefaultCodeSystemVersionDirectoryURI(
			LexBIGService lexBigService,
			CodingSchemeRenderingList codingSchemeRenderingList,
			IterableBasedResolvingRestrictionHandler<CodingSchemeRendering,CodeSystemVersionDirectoryURI> restrictionHandler,
			BeanMapper beanMapper) {
		super(restrictionHandler);
		this.lexBigService = lexBigService;
		this.codingSchemeRenderingList = codingSchemeRenderingList;
		this.beanMapper = beanMapper;
	}

	/* (non-Javadoc)
	 * @see org.cts2.uri.CodeSystemVersionDirectoryURI#restrictToEntities(java.util.List, org.cts2.service.core.types.RestrictionType, org.cts2.service.core.types.ActiveOrAll)
	 */
	@Override
	public CodeSystemVersionRestrictionState getRestrictionState() {
		return this.restrictionState;
	}

	@Override
	public CodeSystemVersionDirectoryURI restrictToEntities(
			final List<EntityReference> entities, 
			final RestrictionType allOrSome,
			final ActiveOrAll active) {
		
		RestrictToEntitiesRestriction restrictToEntities = new RestrictToEntitiesRestriction();
		
		restrictToEntities.setActive(active);
		restrictToEntities.setAllOrSome(allOrSome);
		restrictToEntities.setEntities(entities);
		
		this.getRestrictionState().getRestrictToEntitiesRestrictions().add(restrictToEntities);
		
		return clone();
	}

	/**
	 * Restrict to active or all.
	 *
	 * @param csrl the csrl
	 * @param activeOrAll the active or all
	 * @return the coding scheme rendering list
	 */
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
	 * @see org.cts2.internal.model.uri.AbstractIterableLexEvsBackedResolvingDirectoryURI#getOriginalState()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Iterable<CodingSchemeRendering> getOriginalState() {
		return Arrays.asList(this.codingSchemeRenderingList.getCodingSchemeRendering());
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractIterableLexEvsBackedResolvingDirectoryURI#transform(java.lang.Iterable, java.lang.Class)
	 */
	@Override
	protected <O> O transform(
			Iterable<CodingSchemeRendering> lexevsObject,
			Class<O> clazz) {
		CodingSchemeRenderingList csrl = new CodingSchemeRenderingList();
		csrl.setCodingSchemeRendering(Iterables.toArray(lexevsObject,CodingSchemeRendering.class));
		
		return this.beanMapper.map(csrl, clazz);
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractIterableLexEvsBackedResolvingDirectoryURI#clone()
	 */
	@Override
	protected CodeSystemVersionDirectoryURI clone() {
		//TODO: implement no-destructive clone
		return this;
	}


	@Override
	protected CodeSystemVersionDirectoryURI createSetOperatedDirectoryURI(
			SetOperator setOperator,
			CodeSystemVersionDirectoryURI directoryUri1,
			CodeSystemVersionDirectoryURI directoryUri2) {
		DefaultCodeSystemVersionDirectoryURI newUri = 
			new DefaultCodeSystemVersionDirectoryURI(
					this.lexBigService, 
					this.codingSchemeRenderingList, 
					this.getRestrictionHandler(), 
					this.beanMapper);
		
		newUri.getRestrictionState().setSetComposite(new SetComposite<CodeSystemVersionDirectoryURI>());
		newUri.getRestrictionState().getSetComposite().setDirectoryUri1(directoryUri1);
		newUri.getRestrictionState().getSetComposite().setDirectoryUri2(directoryUri2);
		
		newUri.getRestrictionState().getSetComposite().setSetOperator(setOperator);
		
		return newUri;
	}
}
