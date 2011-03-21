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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.apache.commons.lang.StringUtils;
import org.cts2.constant.ExternalCts2Constants;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.core.VersionTagReference;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.cts2.internal.match.OperationExecutingModelAttributeReference;
import org.cts2.internal.match.OperationExecutingModelAttributeReference.Operation;
import org.cts2.internal.profile.ProfileUtils;
import org.cts2.service.core.NameOrURI;
import org.springframework.beans.factory.InitializingBean;

/**
 * The Class DefaultEntityDescriptionRestrictionHandler.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultEntityDescriptionRestrictionHandler 
	extends AbstractNonIterableLexEvsBackedRestrictionHandler<CodedNodeSet> implements EntityDescriptionRestrictionHandler, InitializingBean {

	/** The lex evs identity converter. */
	private LexEvsIdentityConverter lexEvsIdentityConverter;
	
	/** The lex big service. */
	private LexBIGService lexBigService;
	
	/** The match algorithm reference to search name. */
	private Map<MatchAlgorithmReference,String> matchAlgorithmReferenceToSearchName = 
		new HashMap<MatchAlgorithmReference,String>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.registerSupportedMatchAlgorithmReferences();
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.AbstractNonIterableLexEvsBackedRestrictionHandler#registerSupportedModelAttributeReferences()
	 */
	@Override
	public List<OperationExecutingModelAttributeReference<CodedNodeSet>> registerSupportedModelAttributeReferences() {
		List<OperationExecutingModelAttributeReference<CodedNodeSet>> returnList = 
			new ArrayList<OperationExecutingModelAttributeReference<CodedNodeSet>>();

		OperationExecutingModelAttributeReference<CodedNodeSet> restrictToDesignations = 
			new OperationExecutingModelAttributeReference<CodedNodeSet>(
					new RestrictToMatchingDesignationsOperation());

		restrictToDesignations.setContent(ExternalCts2Constants.ENTITY_DESCRIPTION_DESIGNATION_NAME);
		restrictToDesignations.setMeaning(ExternalCts2Constants.ENTITY_DESCRIPTION_DESIGNATION_URI);
		
		returnList.add(restrictToDesignations);
		
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.EntityDescriptionRestrictionHandler#restrictToCodeSystems(org.cts2.service.core.NameOrURI, org.cts2.core.VersionTagReference)
	 */
	@Override
	public Restriction<CodedNodeSet> restrictToCodeSystems(
			NameOrURI codeSystems,
			VersionTagReference tag) {
		//TODO: decide strategy for implementing CodeSystem profile.
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.EntityDescriptionRestrictionHandler#restrictToCodeSystemVersions(org.cts2.service.core.NameOrURI)
	 */
	@Override
	public Restriction<CodedNodeSet> restrictToCodeSystemVersions(
			final NameOrURI codeSystemVersions) {
		
		return new Restriction<CodedNodeSet>(){

			@Override
			public CodedNodeSet processRestriction(CodedNodeSet state) {
				AbsoluteCodingSchemeVersionReference ref = 
					lexEvsIdentityConverter.nameOrUriToAbsoluteCodingSchemeVersionReference(codeSystemVersions);
				
				try {
					CodedNodeSet versionToRestrictTo = 
						lexBigService.getNodeSet(ref.getCodingSchemeURN(), 
								Constructors.createCodingSchemeVersionOrTagFromVersion(ref.getCodingSchemeVersion()), null);
					
					return state.intersect(versionToRestrictTo);
				} catch (LBException e) {
					//TODO: throw real CTS2 exception
					throw new RuntimeException(e);
				}
			}
		};
	}
	
	/**
	 * The Class RestrictToMatchingDesignationsOperation.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class RestrictToMatchingDesignationsOperation implements Operation<CodedNodeSet>{
		
		/* (non-Javadoc)
		 * @see org.cts2.internal.match.OperationExecutingModelAttributeReference.Operation#union(java.lang.Object, java.lang.String, org.cts2.core.MatchAlgorithmReference)
		 */
		@Override
		public CodedNodeSet union(CodedNodeSet stateObject, String matchText, MatchAlgorithmReference algorithm) {
			try {
				CodedNodeSet restriction = ProfileUtils.unionAll(lexBigService);
				restriction = restriction.restrictToMatchingDesignations(matchText, SearchDesignationOption.ALL, getSearchName(algorithm), null);
				return stateObject.union(restriction);
			} catch (LBException e) {
				//TODO: throw real CTS2 exception
				throw new RuntimeException(e);
			}
		}

		/* (non-Javadoc)
		 * @see org.cts2.internal.match.OperationExecutingModelAttributeReference.Operation#intersect(java.lang.Object, java.lang.String, org.cts2.core.MatchAlgorithmReference)
		 */
		@Override
		public CodedNodeSet intersect(CodedNodeSet stateObject, String matchText, MatchAlgorithmReference algorithm) {
			try {
				return stateObject.restrictToMatchingDesignations(matchText, SearchDesignationOption.ALL, getSearchName(algorithm), null);
			} catch (LBException e) {
				//TODO: throw real CTS2 exception
				throw new RuntimeException(e);
			}
		}

		/* (non-Javadoc)
		 * @see org.cts2.internal.match.OperationExecutingModelAttributeReference.Operation#subtract(java.lang.Object, java.lang.String, org.cts2.core.MatchAlgorithmReference)
		 */
		@Override
		public CodedNodeSet subtract(CodedNodeSet stateObject, String matchText, MatchAlgorithmReference algorithm) {
			try {
				CodedNodeSet restriction = ProfileUtils.unionAll(lexBigService);
				restriction = restriction.restrictToMatchingDesignations(matchText, SearchDesignationOption.ALL, getSearchName(algorithm), null);
				return stateObject.difference(restriction);
			} catch (LBException e) {
				//TODO: throw real CTS2 exception
				throw new RuntimeException(e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.AbstractRestrictionHandler#registerSupportedMatchAlgorithmReferences()
	 */
	@Override
	public List<MatchAlgorithmReference> registerSupportedMatchAlgorithmReferences() {
		List<MatchAlgorithmReference> returnList = new ArrayList<MatchAlgorithmReference>();
		
		ModuleDescriptionList matchAlgorithms = this.lexBigService.getMatchAlgorithms();
		
		if(matchAlgorithms == null || matchAlgorithms.getModuleDescriptionCount() == 0){
			return returnList;
		}
		
		for(ModuleDescription moduleDescription : this.lexBigService.getMatchAlgorithms().getModuleDescription()){
			returnList.add(this.buildMatchAlgorithmReference(moduleDescription));
		}
		
		return returnList;
	}
	
	/**
	 * Builds the match algorithm reference.
	 *
	 * @param moduleDescription the module description
	 * @return the match algorithm reference
	 */
	protected MatchAlgorithmReference buildMatchAlgorithmReference(ModuleDescription moduleDescription){
		MatchAlgorithmReference ref = new MatchAlgorithmReference();
		
		String searchName = moduleDescription.getName();

		ref.setContent(searchName);
		ref.setMeaning(ExternalCts2Constants.CTS2_URI + ExternalCts2Constants.CONCAT_STRING + searchName);
		
		this.matchAlgorithmReferenceToSearchName.put(ref, searchName);
		
		return ref;
	}
	
	/**
	 * Gets the search name.
	 *
	 * @param reference the reference
	 * @return the search name
	 */
	private String getSearchName(MatchAlgorithmReference reference){
		for(Entry<MatchAlgorithmReference, String> entry : this.matchAlgorithmReferenceToSearchName.entrySet()){
			if(StringUtils.equals(reference.getContent(), entry.getKey().getContent())
					||
					(StringUtils.equals(reference.getHref(), entry.getKey().getHref()))){
						return entry.getValue();
					}
		}
		
		//TODO: throw real CTS2 exception
		throw new IllegalStateException();
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
