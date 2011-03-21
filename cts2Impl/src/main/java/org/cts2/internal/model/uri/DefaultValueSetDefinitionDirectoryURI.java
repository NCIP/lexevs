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

import org.LexGrid.valueSets.ValueSetDefinition;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.uri.restrict.ValueSetDefinitionRestrictionHandler;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.ValueSetDefinitionDirectoryURI;

import com.google.common.collect.Iterators;

/**
 * The Class DefaultCodeSystemVersionDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultValueSetDefinitionDirectoryURI extends AbstractIterableLexEvsBackedResolvingDirectoryURI<ValueSetDefinition,ValueSetDefinitionDirectoryURI> 
	implements ValueSetDefinitionDirectoryURI{

	private BeanMapper beanMapper;
	
	private List<ValueSetDefinition> valueSetDefinitionList;
	
	//private IterableBasedResolvingRestrictionHandler<CodingSchemeRendering> restrictionHandler;
	/**
	 * Instantiates a new default code system version directory uri.
	 *
	 * @param codingSchemeRenderingList the coding scheme rendering list
	 * @param beanMapper the bean mapper
	 */
	public DefaultValueSetDefinitionDirectoryURI(
			List<ValueSetDefinition> valueSetDefinitionList,
			ValueSetDefinitionRestrictionHandler restrictionHandler,
			BeanMapper beanMapper) {
		super(restrictionHandler);
		this.beanMapper = beanMapper;
	}

	@Override
	protected Iterable<ValueSetDefinition> getOriginalState() {
		return this.valueSetDefinitionList;
	}

	@Override
	protected <O> O transform(Iterable<ValueSetDefinition> lexevsObject,
			Class<O> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ValueSetDefinitionDirectoryURI clone() {
		return this;
	}

	@Override
	protected int doCount(ReadContext readContext) {
		return Iterators.size(this.runRestrictions(this.valueSetDefinitionList).iterator());
	}
}
