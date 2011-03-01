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
package org.cts2.internal.uri;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.core.FilterComponent;
import org.cts2.entity.EntityDirectory;
import org.cts2.entity.EntityList;
import org.cts2.internal.directory.ResolvedConceptReferencesIteratorBackedEntityDirectory;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.profile.ProfileUtils;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.EntityDirectoryURI;

/**
 * The Class CodeSystemDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultEntityDirectoryURI 
	extends AbstractResolvingDirectoryURI<CodedNodeSet, EntityDirectoryURI, EntityDirectory, EntityList> 
	implements EntityDirectoryURI {

	/**
	 * Instantiates a new code system directory uri.
	 *
	 * @param lexBIGService the lex big service
	 * @param beanMapper the bean mapper
	 */
	public DefaultEntityDirectoryURI(
			LexBIGService lexBIGService,
			BeanMapper beanMapper) {
		super(lexBIGService, beanMapper);
	}

	/* (non-Javadoc)
	 * @see org.cts2.uri.DirectoryURI#count(org.cts2.service.core.ReadContext)
	 */
	@Override
	public int count(ReadContext readContext) {
		try {
			return this.getLexEvsBackingObject().resolve(null, null, null, null, false).numberRemaining();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.cts2.uri.DirectoryURI#marshall()
	 */
	@Override
	public Object marshall() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.cts2.uri.DirectoryURI#unmarshall()
	 */
	@Override
	public void unmarshall() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.uri.AbstractResolvingDirectoryURI#doResolve(java.lang.Object, org.cts2.service.core.NameOrURI, java.lang.Long, org.cts2.service.core.ReadContext)
	 */
	@Override
	protected EntityDirectory doResolve(
			CodedNodeSet lexEvsBackingObject,
			NameOrURI format, 
			Long maxToReturn, 
			ReadContext readContext) {
		try {
			return new ResolvedConceptReferencesIteratorBackedEntityDirectory(lexEvsBackingObject, this.getBeanMapper());
		} catch (LBException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.uri.AbstractResolvingDirectoryURI#doResolveAsList(java.lang.Object, org.cts2.service.core.NameOrURI, java.lang.Long, org.cts2.service.core.ReadContext)
	 */
	@Override
	protected EntityList doResolveAsList(
			CodedNodeSet lexEvsBackingObject,
			NameOrURI format, 
			Long maxToReturn, 
			ReadContext readContext) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.uri.AbstractRestrictingDirectoryURI#applyFilterComponent(java.lang.Object, org.cts2.core.FilterComponent)
	 */
	@Override
	protected void applyFilterComponent(
			CodedNodeSet lexEvsBackingObject,
			FilterComponent filterComponent) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.uri.AbstractLexEvsDirectoryURI#initializeLexEvsBackingObject()
	 */
	@Override
	protected CodedNodeSet initializeLexEvsBackingObject() throws LBException {
		return ProfileUtils.unionAll(this.getLexBIGService());
	}
}
