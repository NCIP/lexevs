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

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.cts2.core.EntityReference;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.uri.restrict.IterableBasedResolvingRestrictionHandler;
import org.cts2.internal.model.uri.restrict.IterableRestriction;
import org.cts2.internal.profile.ProfileUtils;
import org.cts2.service.core.ReadContext;
import org.cts2.service.core.types.ActiveOrAll;
import org.cts2.service.core.types.RestrictionType;
import org.cts2.uri.CodeSystemVersionDirectoryURI;

import scala.actors.threadpool.Arrays;

import com.google.common.collect.Iterables;

/**
 * The Class DefaultCodeSystemVersionDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultCodeSystemVersionDirectoryURI extends AbstractIterableLexEvsBackedResolvingDirectoryURI<CodingSchemeRendering,CodeSystemVersionDirectoryURI> 
	implements CodeSystemVersionDirectoryURI{
	
	/** The coding scheme rendering list. */
	private CodingSchemeRenderingList codingSchemeRenderingList;
	
	/** The bean mapper. */
	private BeanMapper beanMapper;
	
	private LexBIGService lexBigService;
	
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
			IterableBasedResolvingRestrictionHandler<CodingSchemeRendering> restrictionHandler,
			BeanMapper beanMapper) {
		super(restrictionHandler);
		this.lexBigService = lexBigService;
		this.codingSchemeRenderingList = codingSchemeRenderingList;
		this.beanMapper = beanMapper;
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractDirectoryURI#doCount(org.cts2.service.core.ReadContext)
	 */
	@Override
	protected int doCount(ReadContext readContext) {
		return this.codingSchemeRenderingList.getCodingSchemeRenderingCount();
	}
	
	
	/* (non-Javadoc)
	 * @see org.cts2.uri.CodeSystemVersionDirectoryURI#restrictToEntities(java.util.List, org.cts2.service.core.types.RestrictionType, org.cts2.service.core.types.ActiveOrAll)
	 */
	@Override
	public CodeSystemVersionDirectoryURI restrictToEntities(
			final List<EntityReference> entities, 
			final RestrictionType allOrSome,
			final ActiveOrAll active) {
		
		this.addRestriction(new IterableRestriction<CodingSchemeRendering>(){

			@Override
			public Iterable<CodingSchemeRendering> processRestriction(
					Iterable<CodingSchemeRendering> state) {
				
				List<CodingSchemeRendering> returnList = new ArrayList<CodingSchemeRendering>();
				
				for(CodingSchemeRendering rendering : state){
					String uri = rendering.getCodingSchemeSummary().getCodingSchemeURI();
					String version = rendering.getCodingSchemeSummary().getRepresentsVersion();
					
					try {
						CodedNodeSet cns = lexBigService.getNodeSet(uri, Constructors.createCodingSchemeVersionOrTagFromVersion(version), null);
						
						cns = cns.restrictToCodes(ProfileUtils.entityReferenceToConceptReferenceList(entities));
						
						switch (active) {
							case ACTIVE_ONLY : {
								cns = cns.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);
								break;
							}
							
							case ACTIVE_AND_INACTIVE : {
								cns = cns.restrictToStatus(ActiveOption.ALL, null);
								break;
							}
						}
						
						int number = cns.resolve(null, null, null, null, false).numberRemaining();
						
						switch (allOrSome) {
							case ALL : {
								if(number == entities.size()){
									returnList.add(rendering);
								}
								break;
							}
							
							case AT_LEAST_ONE : {
								if(number > 0){
									returnList.add(rendering);
								}
								break;
							}
						}
					
					} catch (LBException e) {
						//TODO: Throw CTS2 Exception here.
						throw new RuntimeException(e);
					}
				}

				return returnList;
			}
		});
		
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
}
