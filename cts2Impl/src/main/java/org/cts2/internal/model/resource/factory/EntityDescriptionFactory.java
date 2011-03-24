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

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.apache.commons.lang.BooleanUtils;
import org.cts2.core.CodeSystemVersionReference;
import org.cts2.core.EntityReference;
import org.cts2.core.ScopedEntityName;
import org.cts2.entity.AnonymousEntityDescription;
import org.cts2.entity.AnonymousIndividualDescription;
import org.cts2.entity.EntityDescription;
import org.cts2.entity.EntityDescriptionChoice;
import org.cts2.entity.EntityList;
import org.cts2.entity.EntityListEntry;
import org.cts2.entity.NamedEntityDescription;
import org.cts2.entity.NamedIndividualDescription;
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
			NameOrURI codeSystemVersionNameOrUri) {
		AbsoluteCodingSchemeVersionReference ref = this.lexEvsIdentityConverter
				.nameOrUriToAbsoluteCodingSchemeVersionReference(codeSystemVersionNameOrUri);

		ConceptReference conceptReference = this.lexEvsIdentityConverter
				.entityNameOrUriToConceptReference(entityDescriptionNameOrUri);
		try {
			CodedNodeSet cns = this.lexBigService.getNodeSet(ref
					.getCodingSchemeURN(), Constructors
					.createCodingSchemeVersionOrTagFromVersion(ref
							.getCodingSchemeVersion()), null);
			ConceptReferenceList conceptRefList = new ConceptReferenceList();
			conceptRefList.addConceptReference(conceptReference);
			CodedNodeSet restCns = cns.restrictToCodes(conceptRefList);
			ResolvedConceptReferencesIterator iterator = restCns.resolve(null,
					null, null, null, true);
			if (iterator.hasNext()) {
				ResolvedConceptReference conRef = iterator.next();
				return this.ResolvedConceptReferenceToEntityDescription(conRef);
			}
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public EntityList getEntityDescriptionList(EntityNameOrURI entityNameOrUri) {
		EntityList list = new EntityList();

		ConceptReference conceptReference = this.lexEvsIdentityConverter
				.entityNameOrUriToConceptReference(entityNameOrUri);

		CodingSchemeRenderingList schemeList;
		try {
			schemeList = this.lexBigService.getSupportedCodingSchemes();
			for (CodingSchemeRendering csr : schemeList
					.getCodingSchemeRendering()) {
				CodingSchemeSummary css = csr.getCodingSchemeSummary();
				CodedNodeSet cns = this.lexBigService.getNodeSet(css
						.getCodingSchemeURI(), Constructors
						.createCodingSchemeVersionOrTagFromVersion(css
								.getRepresentsVersion()), null);
				ConceptReferenceList conceptRefList = new ConceptReferenceList();
				conceptRefList.addConceptReference(conceptReference);
				CodedNodeSet restCns = cns.restrictToCodes(conceptRefList);
				ResolvedConceptReferencesIterator iterator = restCns.resolve(
						null, null, null, null, true);
				if (iterator.hasNext()) {
					EntityListEntry entry = new EntityListEntry();
					ResolvedConceptReference conRef = iterator.next();
					entry.setItem(this
							.ResolvedConceptReferenceToEntityDescriptionChoice(conRef));
					list.addEntry(entry);
				}
			}
		} catch (LBInvocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (LBException e) {
			e.printStackTrace();
		}
		return list;
	}

	public EntityReference availableDescriptions(EntityNameOrURI entityNameOrURI) {
		EntityReference entityReference = new EntityReference();

		ConceptReference conceptReference = this.lexEvsIdentityConverter
				.entityNameOrUriToConceptReference(entityNameOrURI);
		entityReference.setAbout(entityNameOrURI.getUri());
		ScopedEntityName scopedEntityName = new ScopedEntityName();
		scopedEntityName.setName(conceptReference.getCode());
		scopedEntityName.setNamespace(conceptReference.getCodeNamespace());
		entityReference.setLocalEntityName(scopedEntityName);

		try {
			CodingSchemeRenderingList schemeList = this.lexBigService
					.getSupportedCodingSchemes();
			for (CodingSchemeRendering csr : schemeList
					.getCodingSchemeRendering()) {
				CodeSystemVersionReference codeSystemVersionReference = new CodeSystemVersionReference();

				CodingSchemeSummary css = csr.getCodingSchemeSummary();
				CodingScheme cs = LexEvsServiceLocator
						.getInstance()
						.getDatabaseServiceManager()
						.getCodingSchemeService()
						.getCodingSchemeByUriAndVersion(
								css.getCodingSchemeURI(),
								css.getRepresentsVersion());
				CodedNodeSet cns = this.lexBigService.getNodeSet(css
						.getCodingSchemeURI(), Constructors
						.createCodingSchemeVersionOrTagFromVersion(css
								.getRepresentsVersion()), null);
				ConceptReferenceList conceptRefList = new ConceptReferenceList();
				conceptRefList.addConceptReference(conceptReference);
				CodedNodeSet restCns = cns.restrictToCodes(conceptRefList);
				ResolvedConceptReferencesIterator iterator = restCns.resolve(
						null, null, null, null, true);
				if (iterator.hasNext()) {
					codeSystemVersionReference
							.setContent(this.lexEvsIdentityConverter
									.codingSchemeSummaryToCodeSystemVersionName(css));
					codeSystemVersionReference
							.setMeaning(this.lexEvsIdentityConverter
									.codingSchemeToCodeSystemVersionDocumentUri(cs));
					// codeSystemVersionReference.setCodeSystem(codeSystem) TODO
					entityReference
							.addDescribingCodeSystemVersion(codeSystemVersionReference);
				}
			}
		} catch (LBInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entityReference;
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
	 * @param beanMapper
	 *            the new bean mapper
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
	 * @param lexBigService
	 *            the new lex big service
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
	 * @param lexEvsIdentityConverter
	 *            the new lex evs identity converter
	 */
	public void setLexEvsIdentityConverter(
			LexEvsIdentityConverter lexEvsIdentityConverter) {
		this.lexEvsIdentityConverter = lexEvsIdentityConverter;
	}

	private EntityDescription ResolvedConceptReferenceToEntityDescription(
			ResolvedConceptReference conRef) {
		if (conRef.getEntityType(0).equalsIgnoreCase(
				EntityTypes.CONCEPT.toString())) {
			if (!BooleanUtils.toBoolean(conRef.getEntity().isIsAnonymous())) {
				return this.beanMapper
						.map(conRef, NamedEntityDescription.class);
			} else {
				return this.beanMapper.map(conRef,
						AnonymousEntityDescription.class);
			}
		} else if (conRef.getEntityType(0).equalsIgnoreCase(
				EntityTypes.INSTANCE.toString())) {
			if (!BooleanUtils.toBoolean(conRef.getEntity().isIsAnonymous())) {
				return this.beanMapper.map(conRef,
						NamedIndividualDescription.class);
			} else {
				return this.beanMapper.map(conRef,
						AnonymousIndividualDescription.class);
			}
		} else {
			// TODO entity type is ASSOCIATION, map to?
			return null;
		}
	}

	private EntityDescriptionChoice ResolvedConceptReferenceToEntityDescriptionChoice(
			ResolvedConceptReference conRef) {
		EntityDescriptionChoice entityDescriptionChoice = new EntityDescriptionChoice();
		EntityDescription ed = this
				.ResolvedConceptReferenceToEntityDescription(conRef);
		if (ed instanceof NamedIndividualDescription)
			entityDescriptionChoice
					.setNamedIndividual((NamedIndividualDescription) ed);
		else if (ed instanceof AnonymousIndividualDescription)
			entityDescriptionChoice
					.setAnonymousIndividual((AnonymousIndividualDescription) ed);
		else if (ed instanceof NamedEntityDescription)
			entityDescriptionChoice.setNamedEntity((NamedEntityDescription) ed);
		else if (ed instanceof AnonymousEntityDescription)
			entityDescriptionChoice
					.setAnonymousEntity((AnonymousEntityDescription) ed);
		else {
			// TODO ClassDescription and PredicateDescription
		}

		return entityDescriptionChoice;
	}
}