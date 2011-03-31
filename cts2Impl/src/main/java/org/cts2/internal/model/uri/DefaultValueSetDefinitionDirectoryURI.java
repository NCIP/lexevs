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

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitions;
import org.cts2.core.EntityReference;
import org.cts2.core.types.SetOperator;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.uri.restrict.IterableBasedResolvingRestrictionHandler;
import org.cts2.uri.ValueSetDefinitionDirectoryURI;
import org.cts2.uri.restriction.SetComposite;
import org.cts2.uri.restriction.ValueSetDefinitionRestrictionState;
import org.cts2.uri.restriction.ValueSetDefinitionRestrictionState.RestrictToEntitiesRestriction;

import com.google.common.collect.Iterables;

import scala.actors.threadpool.Arrays;

/**
 * The Class DefaultCodeSystemVersionDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultValueSetDefinitionDirectoryURI extends AbstractIterableLexEvsBackedResolvingDirectoryURI<ValueSetDefinition,ValueSetDefinitionDirectoryURI> 
	implements ValueSetDefinitionDirectoryURI{

	private BeanMapper beanMapper;
	
	private ValueSetDefinitions valueSetDefinitions;
	
	private ValueSetDefinitionRestrictionState valueSetDefinitionRestrictionState = new ValueSetDefinitionRestrictionState();

	/**
	 * Instantiates a new default value set definition directory uri.
	 *
	 * @param valueSetDefinitionList the list value set definition
	 * @param beanMapper the bean mapper
	 */
	public DefaultValueSetDefinitionDirectoryURI(
			ValueSetDefinitions valueSetDefinitions,
			IterableBasedResolvingRestrictionHandler<ValueSetDefinition,ValueSetDefinitionDirectoryURI> restrictionHandler,
			BeanMapper beanMapper) {
		super(restrictionHandler);
		this.valueSetDefinitions = valueSetDefinitions;
		this.beanMapper = beanMapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Iterable<ValueSetDefinition> getOriginalState() {
		return Arrays.asList(this.valueSetDefinitions.getValueSetDefinition());
	}

	@Override
	protected ValueSetDefinitionDirectoryURI clone() {
		return this;
	}

	@Override
	public ValueSetDefinitionRestrictionState getRestrictionState() {
		return this.valueSetDefinitionRestrictionState;
	}

	@Override
	protected <O> O transform(Iterable<ValueSetDefinition> lexevsObject,
			Class<O> clazz) {
		ValueSetDefinitions vsds = new ValueSetDefinitions();
		vsds.setValueSetDefinition(Iterables.toArray(lexevsObject,ValueSetDefinition.class));
		return this.beanMapper.map(vsds, clazz);
	}

	@Override
	public ValueSetDefinitionDirectoryURI restrictToEntities(
			List<EntityReference> entityList) {
		RestrictToEntitiesRestriction restriction = new RestrictToEntitiesRestriction();
		restriction.setEntityReferences(entityList);
		
		this.getRestrictionState().getRestrictToEntitiesRestriction().add(restriction);
		
		return this.clone();
	}

	@Override
	protected ValueSetDefinitionDirectoryURI createSetOperatedDirectoryURI(
			SetOperator setOperator,
			ValueSetDefinitionDirectoryURI directoryUri1,
			ValueSetDefinitionDirectoryURI directoryUri2) {
		DefaultValueSetDefinitionDirectoryURI newUri = 
			new DefaultValueSetDefinitionDirectoryURI(
					this.valueSetDefinitions,
					this.getRestrictionHandler(),
					this.beanMapper);
		
		newUri.getRestrictionState().setSetComposite(new SetComposite<ValueSetDefinitionDirectoryURI>());
		newUri.getRestrictionState().getSetComposite().setDirectoryUri1(directoryUri1);
		newUri.getRestrictionState().getSetComposite().setDirectoryUri2(directoryUri2);
		
		newUri.getRestrictionState().getSetComposite().setSetOperator(setOperator);
		
		return newUri;
	}
}
