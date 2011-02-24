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

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.codesystem.CodeSystemDirectory;
import org.cts2.codesystem.CodeSystemList;
import org.cts2.core.FilterComponent;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.CodeSystemDirectoryURI;

/**
 * The Class CodeSystemDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultCodeSystemDirectoryURI 
	extends AbstractResolvingDirectoryURI<CodingSchemeRenderingList, CodeSystemDirectoryURI, CodeSystemDirectory, CodeSystemList> implements CodeSystemDirectoryURI {

	/**
	 * Instantiates a new code system directory uri.
	 *
	 * @param lexBIGService the lex big service
	 * @param beanMapper the bean mapper
	 */
	public DefaultCodeSystemDirectoryURI(LexBIGService lexBIGService,
			BeanMapper beanMapper) {
		super(lexBIGService, beanMapper);
	}

	@Override
	public int count(ReadContext readContext) {
		return this.getLexEvsBackingObject().getCodingSchemeRenderingCount();
	}

	@Override
	public Object marshall() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unmarshall() {
		// TODO Auto-generated method stub	
	}

	@Override
	protected CodeSystemDirectory doResolve(
			CodingSchemeRenderingList lexEvsBackingObject, 
			NameOrURI format,
			long maxToReturn, 
			ReadContext readContext) {
		return this.getBeanMapper().map(this.getLexEvsBackingObject(), CodeSystemDirectory.class);
	}

	@Override
	protected CodeSystemList doResolveAsList(
			CodingSchemeRenderingList lexEvsBackingObject, 
			NameOrURI format,
			long maxToReturn, 
			ReadContext readContext) {
		return this.getBeanMapper().map(this.getLexEvsBackingObject(), CodeSystemList.class);
	}

	@Override
	protected void applyFilterComponent(
			CodingSchemeRenderingList lexEvsBackingObject,
			FilterComponent filterComponent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected CodingSchemeRenderingList initializeLexEvsBackingObject() throws LBException{
		return this.getLexBIGService().getSupportedCodingSchemes();
	}
}
