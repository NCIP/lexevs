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
package org.cts2.internal.model.uri.factory;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.uri.DirectoryURI;
import org.lexgrid.valuesets.LexEVSPickListDefinitionServices;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;

/**
 * A factory for creating AbstractDirectoryURI objects.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractCompositeDirectoryURIFactory<T extends DirectoryURI> implements DirectoryURIFactory<T>{

	/** The bean mapper. */
	private BeanMapper beanMapper;
	
	/** The lex big service. */
	private LexBIGService lexBigService;
	
	/** The LexEVS Value Set Definition Service. */
	private LexEVSValueSetDefinitionServices lexEVSValueSetDefinitionService;

	/** The LexEVS Pick List Definition Service. */
	private LexEVSPickListDefinitionServices lexEVSPickListDefinitionService;

	@Override
	public T getDirectoryURI() {
		return  
			this.doBuildDirectoryURI();	
	}
	
	protected abstract T doBuildDirectoryURI();
	
	/**
	 * Gets the bean mapper.
	 *
	 * @return the bean mapper
	 */
	public BeanMapper getBeanMapper() {
		return this.beanMapper;
	}

	/**
	 * Sets the bean mapper.
	 *
	 * @param beanMapper the new bean mapper
	 */
	public void setBeanMapper(BeanMapper beanMapper) {
		this.beanMapper = beanMapper;
	}

	/**
	 * Sets the lex big service.
	 *
	 * @param lexBigService the new lex big service
	 */
	public void setLexBigService(LexBIGService lexBigService) {
		this.lexBigService = lexBigService;
	}

	/**
	 * Gets the lex big service.
	 *
	 * @return the lex big service
	 */
	public LexBIGService getLexBigService() {
		return this.lexBigService;
	}

	/**
	 * @return the lexEVSValueSetDefinitionService
	 */
	public LexEVSValueSetDefinitionServices getLexEVSValueSetDefinitionService() {
		return lexEVSValueSetDefinitionService;
	}

	/**
	 * @param lexEVSValueSetDefinitionService the lexEVSValueSetDefinitionService to set
	 */
	public void setLexEVSValueSetDefinitionService(
			LexEVSValueSetDefinitionServices lexEVSValueSetDefinitionService) {
		this.lexEVSValueSetDefinitionService = lexEVSValueSetDefinitionService;
	}

	/**
	 * @return the lexEVSPickListDefinitionService
	 */
	public LexEVSPickListDefinitionServices getLexEVSPickListDefinitionService() {
		return lexEVSPickListDefinitionService;
	}

	/**
	 * @param lexEVSPickListDefinitionService the lexEVSPickListDefinitionService to set
	 */
	public void setLexEVSPickListDefinitionService(
			LexEVSPickListDefinitionServices lexEVSPickListDefinitionService) {
		this.lexEVSPickListDefinitionService = lexEVSPickListDefinitionService;
	}	
}
