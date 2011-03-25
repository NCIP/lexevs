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

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.cts2.core.EntityReference;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.cts2.internal.match.AttributeResolver;
import org.cts2.internal.match.ResolvableModelAttributeReference;
import org.cts2.internal.profile.ProfileUtils;
import org.cts2.service.core.types.ActiveOrAll;
import org.cts2.service.core.types.RestrictionType;
import org.cts2.uri.CodeSystemVersionDirectoryURI;
import org.cts2.uri.restriction.CodeSystemVersionRestrictionState.RestrictToEntitiesRestriction;

/**
 * The Class CodeSystemVersionRestrictionHandler.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultCodeSystemVersionRestrictionHandler 
	extends AbstractIterableLexEvsBackedRestrictionHandler<CodingSchemeRendering, CodeSystemVersionDirectoryURI> {
	
	/** The lex evs identity converter. */
	private LexEvsIdentityConverter lexEvsIdentityConverter;
	
	private LexBIGService lexBigService;

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.AbstractIterableLexEvsBackedRestrictionHandler#registerSupportedModelAttributes()
	 */
	@Override
	public List<ResolvableModelAttributeReference<CodingSchemeRendering>> registerSupportedModelAttributeReferences() {
		List<ResolvableModelAttributeReference<CodingSchemeRendering>> returnList = 
			new ArrayList<ResolvableModelAttributeReference<CodingSchemeRendering>>();
		
		ResolvableModelAttributeReference<CodingSchemeRendering> codeSystmeVersionName = 
			new ResolvableModelAttributeReference<CodingSchemeRendering>(new CodeSystemVersionNameAttributeResolver());
		
		returnList.add(codeSystmeVersionName);
		
		return returnList;
	}
	
	@Override
	public List<IterableRestriction<CodingSchemeRendering>> processOtherRestictions(CodeSystemVersionDirectoryURI directoryUri) {
		List<IterableRestriction<CodingSchemeRendering>> returnList = new ArrayList<IterableRestriction<CodingSchemeRendering>>();
		
		for(RestrictToEntitiesRestriction restriction : directoryUri.getRestrictionState().getRestrictToEntitiesRestrictions()){
			returnList.add(this.restrictToEntities(restriction));
		}
		
		return returnList;
	}

	protected IterableRestriction<CodingSchemeRendering> restrictToEntities(final RestrictToEntitiesRestriction restriction) {
		return new IterableRestriction<CodingSchemeRendering>(){

			@Override
			public Iterable<CodingSchemeRendering> processRestriction(
					Iterable<CodingSchemeRendering> state) {
				
				ActiveOrAll activeOrAll = restriction.getActive();
				RestrictionType allOrSome = restriction.getAllOrSome();
				List<EntityReference> entities = restriction.getEntities();
				
				List<CodingSchemeRendering> returnList = new ArrayList<CodingSchemeRendering>();
				
				for(CodingSchemeRendering rendering : state){
					String uri = rendering.getCodingSchemeSummary().getCodingSchemeURI();
					String version = rendering.getCodingSchemeSummary().getRepresentsVersion();
					
					try {
						CodedNodeSet cns = lexBigService.getNodeSet(uri, Constructors.createCodingSchemeVersionOrTagFromVersion(version), null);
						
						cns = cns.restrictToCodes(ProfileUtils.entityReferenceToConceptReferenceList(entities));
						
						switch (activeOrAll) {
							case ACTIVE_ONLY : {
								cns = cns.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);
								break;
							}
							
							case ACTIVE_AND_INACTIVE : {
								cns = cns.restrictToStatus(ActiveOption.ALL, null);
								break;
							}
						}
						
						int number = cns.resolve(null, null, null, null, false).numberRemaining();
						
						switch (allOrSome) {
							case ALL : {
								if(number == entities.size()){
									returnList.add(rendering);
								}
								break;
							}
							
							case AT_LEAST_ONE : {
								if(number > 0){
									returnList.add(rendering);
								}
								break;
							}
						}
					
					} catch (LBException e) {
						//TODO: Throw CTS2 Exception here.
						throw new RuntimeException(e);
					}
				}

				return returnList;
			}
		};
	}

	/**
	 * The Class CodeSystemVersionNameAttributeResolver.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class CodeSystemVersionNameAttributeResolver implements AttributeResolver<CodingSchemeRendering> {

		/* (non-Javadoc)
		 * @see org.cts2.internal.match.AttributeResolver#resolveAttribute(java.lang.Object)
		 */
		@Override
		public String resolveAttribute(
				CodingSchemeRendering modelObject) {
			
			return lexEvsIdentityConverter.codingSchemeSummaryToCodeSystemVersionName(modelObject.getCodingSchemeSummary());
		}
	}

	@Override
	public List<MatchAlgorithmReference> registerSupportedMatchAlgorithmReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	public LexEvsIdentityConverter getLexEvsIdentityConverter() {
		return lexEvsIdentityConverter;
	}

	public void setLexEvsIdentityConverter(
			LexEvsIdentityConverter lexEvsIdentityConverter) {
		this.lexEvsIdentityConverter = lexEvsIdentityConverter;
	}

	public LexBIGService getLexBigService() {
		return lexBigService;
	}

	public void setLexBigService(LexBIGService lexBigService) {
		this.lexBigService = lexBigService;
	}
}
