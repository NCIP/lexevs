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
import org.cts2.core.VersionTagReference;
import org.cts2.entity.EntityDirectory;
import org.cts2.entity.EntityList;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedEntityDirectory;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedEntityList;
import org.cts2.internal.model.uri.restrict.EntityDescriptionRestrictionHandler;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.EntityDirectoryURI;

/**
 * The Class DefaultEntityDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultEntityDirectoryURI extends AbstractNonIterableLexEvsBackedResolvingDirectoryURI<CodedNodeSet,EntityDirectoryURI> implements EntityDirectoryURI {
	
	/** The coded node set. */
	private CodedNodeSet codedNodeSet;
	
	/** The bean mapper. */
	private BeanMapper beanMapper;
	
	/**
	 * Instantiates a new default code system version directory uri.
	 *
	 * @param codedNodeSet the coded node set
	 * @param restrictionHandler the restriction handler
	 * @param beanMapper the bean mapper
	 */
	public DefaultEntityDirectoryURI(
			CodedNodeSet codedNodeSet,
			EntityDescriptionRestrictionHandler restrictionHandler,
			BeanMapper beanMapper) {
		super(restrictionHandler);
		this.codedNodeSet = codedNodeSet;
		this.beanMapper = beanMapper;
	}
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractDirectoryURI#doCount(org.cts2.service.core.ReadContext)
	 */
	@Override
	protected int doCount(ReadContext readContext) {
		try {
			return 
				this.runRestrictions(getOriginalState()).resolve(null, null, null, null, false).numberRemaining();
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
		return this.codedNodeSet;
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
		this.getRestrictionHandler().restrictToCodeSystems(codeSystems, tag);
		
		return clone();
	}

	/* (non-Javadoc)
	 * @see org.cts2.uri.EntityDirectoryURI#restrictToCodeSystemVersions(org.cts2.service.core.NameOrURI)
	 */
	@Override
	public EntityDirectoryURI restrictToCodeSystemVersions(
			NameOrURI codeSystemVersions) {
		this.getRestrictionHandler().restrictToCodeSystemVersions(codeSystemVersions);
		
		return clone();
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractNonIterableLexEvsBackedResolvingDirectoryURI#clone()
	 */
	@Override
	protected EntityDirectoryURI clone() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.AbstractNonIterableLexEvsBackedResolvingDirectoryURI#getRestrictionHandler()
	 */
	@Override
	protected EntityDescriptionRestrictionHandler getRestrictionHandler() {
		return (EntityDescriptionRestrictionHandler) super.getRestrictionHandler();
	}
}
