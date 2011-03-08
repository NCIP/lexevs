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
package org.cts2.internal.model.resource.factory;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.concepts.Entity;
import org.cts2.entity.EntityDescription;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * A factory for creating EntityDescription objects.
 */
public class EntityDescriptionFactory {
	
	/** The bean mapper. */
	private BeanMapper beanMapper;
	
	/** The lex big service. */
	private LexBIGService lexBigService;
	
	private LexEvsIdentityConverter lexEvsIdentityConverter;
		
	public EntityDescription getEntityDescription(
			EntityNameOrURI entityDescriptionNameOrUri, 
			NameOrURI codeSystemVersionNameOrUri){
		AbsoluteCodingSchemeVersionReference ref = 
			this.lexEvsIdentityConverter.nameOrUriToAbsoluteCodingSchemeVersionReference(codeSystemVersionNameOrUri);
		
		ConceptReference conceptReference = 
			this.lexEvsIdentityConverter.entityNameOrUriToConceptReference(entityDescriptionNameOrUri);
		
		Entity entity = 
			LexEvsServiceLocator.getInstance().
			getDatabaseServiceManager().
				getEntityService().getEntity(
						ref.getCodingSchemeURN(), 
						ref.getCodingSchemeVersion(), 
						conceptReference.getCode(), 
						conceptReference.getCodeNamespace(), 
						null, 
						null);
		
		return this.beanMapper.map(entity, EntityDescription.class);
	}

	/**
	 * Gets the bean mapper.
	 *
	 * @return the bean mapper
	 */
	public BeanMapper getBeanMapper() {
		return beanMapper;
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
	 * Gets the lex big service.
	 *
	 * @return the lex big service
	 */
	public LexBIGService getLexBigService() {
		return lexBigService;
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
	 * Gets the lex evs identity converter.
	 *
	 * @return the lex evs identity converter
	 */
	public LexEvsIdentityConverter getLexEvsIdentityConverter() {
		return lexEvsIdentityConverter;
	}

	/**
	 * Sets the lex evs identity converter.
	 *
	 * @param lexEvsIdentityConverter the new lex evs identity converter
	 */
	public void setLexEvsIdentityConverter(
			LexEvsIdentityConverter lexEvsIdentityConverter) {
		this.lexEvsIdentityConverter = lexEvsIdentityConverter;
	}
}