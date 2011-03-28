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

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.core.VersionTagReference;
import org.cts2.core.types.SetOperator;
import org.cts2.entity.EntityDirectory;
import org.cts2.entity.EntityList;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedEntityDirectory;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedEntityList;
import org.cts2.internal.model.uri.restrict.NonIterableBasedResolvingRestrictionHandler;
import org.cts2.internal.model.uri.restrict.Restriction;
import org.cts2.internal.profile.ProfileUtils;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.NameOrURIList;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.EntityDirectoryURI;
import org.cts2.uri.restriction.EntityDirectoryRestrictionState;
import org.cts2.uri.restriction.EntityDirectoryRestrictionState.RestrictToCodeSystemVersionsRestriction;
import org.cts2.uri.restriction.SetComposite;

/**
 * The Class DefaultEntityDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultEntityDirectoryURI extends AbstractNonIterableLexEvsBackedResolvingDirectoryURI<CodedNodeSet,EntityDirectoryURI> implements EntityDirectoryURI {
	
	/** The coded node set. */
	//private CodedNodeSet codedNodeSet;
	
	/** The bean mapper. */
	private BeanMapper beanMapper;
	
	private LexBIGService lexBigService;
	
	private EntityDirectoryRestrictionState entityDirectoryRestrictionState = new EntityDirectoryRestrictionState();
	
	/**
	 * Instantiates a new default code system version directory uri.
	 *
	 * @param codedNodeSet the coded node set
	 * @param restrictionHandler the restriction handler
	 * @param beanMapper the bean mapper
	 */
	public DefaultEntityDirectoryURI(
			LexBIGService lexBigService,
			NonIterableBasedResolvingRestrictionHandler<CodedNodeSet,EntityDirectoryURI> restrictionHandler,
			BeanMapper beanMapper) {
		super(restrictionHandler);
		this.lexBigService = lexBigService;
		this.beanMapper = beanMapper;
	}
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractDirectoryURI#doCount(org.cts2.service.core.ReadContext)
	 */
	@Override
	protected int doCount(ReadContext readContext) {
		try {
			Restriction<CodedNodeSet> restriction = 
				this.getRestrictionHandler().compile(this.getThis(), this.getOriginalStateProvider());
			
			return this.getRestrictionHandler().apply( restriction, getOriginalState()).resolve(null, null, null, null).numberRemaining();
		} catch (LBException e) {
			//TODO: real cts2 exception here
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractNonIterableLexEvsBackedResolvingDirectoryURI#getOriginalState()
	 */
	@Override
	protected CodedNodeSet getOriginalState() {
		try {
			return ProfileUtils.unionAll(this.lexBigService);
		} catch (LBException e) {
			//TODO: real cts2 exception here
			throw new IllegalStateException();
		}
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractNonIterableLexEvsBackedResolvingDirectoryURI#transform(java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected <O> O transform(
			CodedNodeSet lexevsObject, Class<O> clazz) {
		try {
			if(clazz.equals(EntityDirectory.class)){
				return (O) new ResolvedConceptReferencesIteratorBackedEntityDirectory(lexevsObject, this.beanMapper);
			}
			if(clazz.equals(EntityList.class)){
				return (O) new ResolvedConceptReferencesIteratorBackedEntityList(lexevsObject, this.beanMapper);
			}
		} catch (LBException e) {
			//TODO: real cts2 exception here
			throw new IllegalStateException();
		}
		
		//TODO: real cts2 exception here
		throw new IllegalStateException();
	}
	
	

	/* (non-Javadoc)
	 * @see org.cts2.uri.EntityDirectoryURI#restrictToCodeSystems(org.cts2.service.core.NameOrURI, org.cts2.core.VersionTagReference)
	 */
	@Override
	public EntityDirectoryURI restrictToCodeSystems(
			NameOrURI codeSystems,
			VersionTagReference tag) {
		//this.entityDirectoryRestrictionState.add...
		return clone();
	}

	/* (non-Javadoc)
	 * @see org.cts2.uri.EntityDirectoryURI#restrictToCodeSystemVersions(org.cts2.service.core.NameOrURI)
	 */
	@Override
	public EntityDirectoryURI restrictToCodeSystemVersions(
			NameOrURI codeSystemVersions) {
		RestrictToCodeSystemVersionsRestriction restriction = new RestrictToCodeSystemVersionsRestriction();
		NameOrURIList list = new NameOrURIList();
		list.addEntry(codeSystemVersions);
		
		restriction.setCodeSystemVersions(list);
		
		this.getThis().getRestrictionState().getRestrictToCodeSystemVersionsRestrictions().add(restriction);
		
		return clone();
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractNonIterableLexEvsBackedResolvingDirectoryURI#clone()
	 */
	@Override
	protected EntityDirectoryURI clone() {
		return this;
	}

	@Override
	public EntityDirectoryRestrictionState getRestrictionState() {
		return this.entityDirectoryRestrictionState;
	}

	@Override
	protected EntityDirectoryURI createSetOperatedDirectoryURI(
			SetOperator setOperator, 
			EntityDirectoryURI directoryUri1,
			EntityDirectoryURI directoryUri2) {
		DefaultEntityDirectoryURI uri = new DefaultEntityDirectoryURI(
				this.lexBigService, 
				this.getRestrictionHandler(), 
				this.beanMapper);
		
		uri.getRestrictionState().setSetComposite(new SetComposite<EntityDirectoryURI>());
		uri.getRestrictionState().getSetComposite().setSetOperator(setOperator);
		uri.getRestrictionState().getSetComposite().setDirectoryUri1(directoryUri1);
		uri.getRestrictionState().getSetComposite().setDirectoryUri2(directoryUri2);
	
		return uri;
	}
}
