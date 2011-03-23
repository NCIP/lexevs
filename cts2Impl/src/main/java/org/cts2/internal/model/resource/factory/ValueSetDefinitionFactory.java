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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.cts2.core.NameOrURI;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.valueset.ValueSetDefinition;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;

/**
 * A factory for creating ValueSetDefinition objects.
 */
public class ValueSetDefinitionFactory {
	
	/** The bean mapper. */
	private BeanMapper beanMapper;
	
	/** The lexEVSValueSetDefinition service. */
	private LexEVSValueSetDefinitionServices lexEVSValueSetDefinitionService;
	
	/**
	 * Gets the value set definition for a URI.
	 *
	 * @param valueSetDefinitionURI the URI of value set definition
	 * @return the value set definition
	 */
	public ValueSetDefinition getValueSetDefinition(URI valueSetDefinitionURI){
		
		org.LexGrid.valueSets.ValueSetDefinition vsd = null;
		try {
			vsd = this.lexEVSValueSetDefinitionService.getValueSetDefinition(valueSetDefinitionURI, null);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (vsd != null)
		{
			return this.beanMapper.map(vsd, ValueSetDefinition.class);
		}
		
		return null;
	}

	/**
	 * Gets the value set definition for a name.
	 *
	 * @param valueSetDefinitionName the name of Value Set Definition
	 * @return the value set definition
	 * @throws LBParameterException 
	 * @throws LBException 
	 */
	public ValueSetDefinition getValueSetDefinition(String valueSetDefinitionName) throws LBParameterException {
		
		org.LexGrid.valueSets.ValueSetDefinition vsd = null;
		List<String> vsds = null;
		try {
			vsds = this.lexEVSValueSetDefinitionService.listValueSetDefinitions(valueSetDefinitionName);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		if (vsds.isEmpty())
			return null;
		
		if (vsds.size() > 1)
			throw new LBParameterException("More than 1 Value Set Definition found for name : " + valueSetDefinitionName);			
		
		try {
			vsd = this.getLexEVSValueSetDefinitionService().getValueSetDefinition(new URI(vsds.get(0)), null);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (vsd != null)
		{
			return this.beanMapper.map(vsd, ValueSetDefinition.class);
		}
		
		return null;
	}
	
	/**
	 * Gets the value set definition for a name or URI.
	 *
	 * @param valueSetDefinitionNameOrURI the name or URI of Value Set Definition
	 * @return the value set definition
	 * @throws LBParameterException 
	 * @throws LBException 
	 */
	public ValueSetDefinition getValueSetDefinition(NameOrURI valueSetDefinitionNameOrURI) throws LBParameterException {
		
		if (valueSetDefinitionNameOrURI.getUri() != null)
			return this.getValueSetDefinition(valueSetDefinitionNameOrURI.getUri());
		else
			return this.getValueSetDefinition(valueSetDefinitionNameOrURI.getName());
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

	
}
