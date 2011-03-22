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

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.core.VersionTagReference;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.cts2.internal.match.OperationExecutingModelAttributeReference;
import org.cts2.service.core.NameOrURI;

/**
 * The Class DefaultEntityDescriptionRestrictionHandler.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultAssociationRestrictionHandler 
	extends AbstractNonIterableLexEvsBackedRestrictionHandler<CodedNodeGraph> implements AssociationRestrictionHandler {

	/** The lex evs identity converter. */
	private LexEvsIdentityConverter lexEvsIdentityConverter;
	
	/** The lex big service. */
	private LexBIGService lexBigService;

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.AssociationRestrictionHandler#restrictToCodeSystems(org.cts2.service.core.NameOrURI, org.cts2.core.VersionTagReference)
	 */
	@Override
	public Restriction<CodedNodeGraph> restrictToCodeSystems(
			NameOrURI codeSystems,
			VersionTagReference tag) {
		//TODO: decide strategy for implementing CodeSystem profile.
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.AssociationRestrictionHandler#restrictToCodeSystemVersions(org.cts2.service.core.NameOrURI)
	 */
	@Override
	public Restriction<CodedNodeGraph> restrictToCodeSystemVersions(
			final NameOrURI codeSystemVersions) {
		
		return new Restriction<CodedNodeGraph>(){

			@Override
			public CodedNodeGraph processRestriction(CodedNodeGraph state) {
				AbsoluteCodingSchemeVersionReference ref = 
					lexEvsIdentityConverter.nameOrUriToAbsoluteCodingSchemeVersionReference(codeSystemVersions);
				
				try {
					CodedNodeGraph versionToRestrictTo = 
						lexBigService.getNodeGraph(ref.getCodingSchemeURN(), 
								Constructors.createCodingSchemeVersionOrTagFromVersion(ref.getCodingSchemeVersion()), null);
					
					return state.intersect(versionToRestrictTo);
				} catch (LBException e) {
					//TODO: throw real CTS2 exception
					throw new RuntimeException(e);
				}
			}
		};
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.AbstractNonIterableLexEvsBackedRestrictionHandler#registerSupportedModelAttributeReferences()
	 */
	@Override
	public List<OperationExecutingModelAttributeReference<CodedNodeGraph>> registerSupportedModelAttributeReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.AbstractRestrictionHandler#registerSupportedMatchAlgorithmReferences()
	 */
	@Override
	public List<MatchAlgorithmReference> registerSupportedMatchAlgorithmReferences() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
