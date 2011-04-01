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
package org.cts2.internal.model.uri.restrict;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.cts2.core.CodeSystemVersionReference;
import org.cts2.core.EntityReference;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.internal.match.AttributeResolver;
import org.cts2.internal.match.ResolvableModelAttributeReference;
import org.cts2.uri.ValueSetDefinitionDirectoryURI;
import org.cts2.uri.restriction.ValueSetDefinitionRestrictionState.RestrictToEntitiesRestriction;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;

/**
 * The Class ValueSetDefinitionRestrictionHandler.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ValueSetDefinitionRestrictionHandler extends AbstractIterableLexEvsBackedRestrictionHandler<ValueSetDefinition,ValueSetDefinitionDirectoryURI>{

	/** The lexEVSValueSetDefinition service. */
	private LexEVSValueSetDefinitionServices lexEVSValueSetDefinitionService;
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.AbstractIterableLexEvsBackedRestrictionHandler#registerSupportedModelAttributes()
	 */
	@Override
	public List<ResolvableModelAttributeReference<ValueSetDefinition>> registerSupportedModelAttributeReferences() {
		List<ResolvableModelAttributeReference<ValueSetDefinition>> returnList = 
			new ArrayList<ResolvableModelAttributeReference<ValueSetDefinition>>();
		
		
		
		AttributeResolver<ValueSetDefinition> valueSetDefinitionNameResolver = new AttributeResolver<ValueSetDefinition>(){

			@Override
			public String resolveAttribute(ValueSetDefinition modelObject) {
				return modelObject.getValueSetDefinitionName();
			}
		};
	
		//TODO: Example only
		ResolvableModelAttributeReference<ValueSetDefinition> valueSetDefinitionName = new ResolvableModelAttributeReference<ValueSetDefinition>(valueSetDefinitionNameResolver);
		valueSetDefinitionName.setContent("valueSetDefinitionName");
		
		returnList.add(valueSetDefinitionName);
		
		
		
		return returnList;
	}

	@Override
	public List<IterableRestriction<ValueSetDefinition>> processOtherRestictions(
			ValueSetDefinitionDirectoryURI directoryUri) {
		
		List<IterableRestriction<ValueSetDefinition>> returnList = new ArrayList<IterableRestriction<ValueSetDefinition>>();
		
		Iterator<RestrictToEntitiesRestriction> restrictions = directoryUri.getRestrictionState().getRestrictToEntitiesRestriction().iterator();
		
		while (restrictions.hasNext())
		{
			RestrictToEntitiesRestriction restriction = restrictions.next();
			returnList.add(this.restrictToEntities(restriction));
		}
		
		return returnList;
	}
	
	protected IterableRestriction<ValueSetDefinition> restrictToEntities(final RestrictToEntitiesRestriction restriction) {
		return new IterableRestriction<ValueSetDefinition>(){

			@Override
			public Iterable<ValueSetDefinition> processRestriction(
					Iterable<ValueSetDefinition> state) {
				
				List<EntityReference> entities = restriction.getEntityReferences();
				
				List<ValueSetDefinition> returnList = new ArrayList<ValueSetDefinition>();
				
				for(ValueSetDefinition vsd : state){
					for (EntityReference entity : entities)
					{
						String entityCode = entity.getLocalEntityName().getName();
						String entityCodeNamespace = entity.getLocalEntityName().getNamespace();
						
						AbsoluteCodingSchemeVersionReferenceList csVersionList = new AbsoluteCodingSchemeVersionReferenceList();
						
						//TODO . . get code system version list
//						for (CodeSystemVersionReference csvr : entity.getDescribingCodeSystemVersion())
//						{
//							csVersionList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(csvr.getCodeSystem().g, version))
//						}
						
						try {
							if (lexEVSValueSetDefinitionService.isEntityInValueSet(entityCode, new URI(entityCodeNamespace), new URI(vsd.getValueSetDefinitionURI()), null, csVersionList, null) != null)
							{
								returnList.add(vsd);
								System.out.println("vsd.uri : " + vsd.getValueSetDefinitionURI());
								break;
							}
						} catch (LBException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				return returnList;
			}
		};
	}

	@Override
	public List<MatchAlgorithmReference> registerSupportedMatchAlgorithmReferences() {
		// TODO Auto-generated method stub
		return null;
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
