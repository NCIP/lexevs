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

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.cts2.constant.ExternalCts2Constants;
import org.cts2.core.MatchAlgorithmReference;
import org.cts2.core.VersionTagReference;
import org.cts2.core.types.SetOperator;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.cts2.internal.match.OperationExecutingModelAttributeReference;
import org.cts2.internal.match.OperationExecutingModelAttributeReference.RestrictionOperation;
import org.cts2.internal.profile.ProfileUtils;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;
import org.cts2.uri.AssociationDirectoryURI;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToCodeSystemVersionRestriction;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToPredicateRestriction;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToSourceEntityRestriction;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToSourceOrTargetEntityRestriction;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToTargetEntityRestriction;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToTargetLiteralRestriction;

/**
 * The Class DefaultEntityDescriptionRestrictionHandler.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class DefaultAssociationRestrictionHandler 
	extends AbstractNonIterableLexEvsBackedRestrictionHandler<CodedNodeGraph,AssociationDirectoryURI> {

	/** The lex evs identity converter. */
	//TODO provide new identity converter or supporting methods for this restriction handler  
	private LexEvsIdentityConverter lexEvsIdentityConverter;
	
	/** The lex big service. */
	private LexBIGService lexBigService;

	protected Restriction<CodedNodeGraph> restrictToCodeSystems(
			NameOrURI codeSystems,
			VersionTagReference tag) {
		//TODO: decide strategy for implementing CodeSystem profile.
		throw new UnsupportedOperationException();
	}

	/**
	 * @param codeSystemVersions
	 * @return
	 */
	protected Restriction<CodedNodeGraph> restrictToCodeSystemVersions(
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
		//Not doing filtered restrictions on the Associations so we return an empty list.
		List<OperationExecutingModelAttributeReference<CodedNodeGraph>> returnList = 
			new ArrayList<OperationExecutingModelAttributeReference<CodedNodeGraph>>();	
		return returnList;
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.AbstractRestrictionHandler#registerSupportedMatchAlgorithmReferences()
	 */
	@Override
	public List<MatchAlgorithmReference> registerSupportedMatchAlgorithmReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.AbstractNonIterableLexEvsBackedRestrictionHandler#processOtherRestictions(org.cts2.uri.DirectoryURI)
	 */
	@Override
	protected List<Restriction<CodedNodeGraph>> processOtherRestictions(
			AssociationDirectoryURI directoryURI) {
		List<Restriction<CodedNodeGraph>> returnList = new ArrayList<Restriction<CodedNodeGraph>>();
		
		AssociationDirectoryRestrictionState state = directoryURI.getRestrictionState();
		
		for(RestrictToCodeSystemVersionRestriction restriction : state.getRestrictToCodeSystemVersionRestrictions()){
			returnList.add(this.restrictToCodeSystemVersions(restriction.getCodeSystemVersion()));
		}
		for(RestrictToPredicateRestriction restriction : state.getRestrictToPredicateRestrictions()){
			returnList.add(this.restrictToPredicate(restriction.getPredicate()));
		}
		for(RestrictToPredicateRestriction restriction : state.getRestrictToPredicateRestrictions()){
			returnList.add(this.restrictToPredicate(restriction.getPredicate()));
		}
		for(RestrictToSourceEntityRestriction restriction : state.getRestrictToSourceEntityRestrictions()){
			returnList.add(this.restrictToSourceEntityRestrictions(restriction.getSourceEntity()));
		}
		for(RestrictToSourceOrTargetEntityRestriction restriction : state.getRestrictToSourceOrTargetEntityRestrictions()){
			returnList.add(this.restrictToSourceOrTargetEntityRestriction(restriction.getEntity()));
		}
		for(RestrictToTargetEntityRestriction restriction : state.getRestrictToTargetEntityRestrictions()){
			returnList.add(this.restrictToTargetEntityRestriction(restriction.getTarget()));
		}
		for(RestrictToTargetLiteralRestriction restriction : state.getRestrictToTargetLiteralRestriction()){
			returnList.add(this.restrictToTargetLiteralRestriction(restriction.getTarget()));
		}				
		return returnList;
	}
	
	/**
	 * 
	 * Restrict to the predicate (Association Name) for this association
	 * @param predicate - a wrapper class containing the name
	 * @return this restriction to the coded node graph.
	 */
	protected Restriction<CodedNodeGraph> restrictToPredicate(final EntityNameOrURI predicate) {

		return new Restriction<CodedNodeGraph>(){

			@Override
			public CodedNodeGraph processRestriction(CodedNodeGraph state) {
				NameAndValueList association = 
					Constructors.createNameAndValueList(predicate.getEntityName().getName(), null);
				try {
					state = state.restrictToAssociations(association, null);
				} catch (Exception e) {
					// TODO throw CTS2 Exception
					throw new RuntimeException(e);
				} 
				return state;
			}
		};
	}
	
	/**
	 *  Restrict to the source entity unique identifier
	 * @param sourceEntity - wrapper class for the unique identifier
	 * @return this restriction to the coded node graph.
	 */
	protected Restriction<CodedNodeGraph> restrictToSourceEntityRestrictions(final EntityNameOrURI sourceEntity) {

		return new Restriction<CodedNodeGraph>(){
			@Override
			public CodedNodeGraph processRestriction(CodedNodeGraph state) {

				try {
					//TODO find a method of restricting codes to the current restricted state of this node graph.
					CodedNodeSet codes = ProfileUtils.unionAll(lexBigService);
					ConceptReferenceList codeList = Constructors.createConceptReferenceList(sourceEntity.getEntityName().getName());
					codes.restrictToCodes(codeList);
					return state.restrictToSourceCodes(codes);
				} catch (LBException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException();
				}		
			}
		};
	}
	
	
	/**
	 * Restrict to the source or target entity unique identifier of the coded node graph
	 * @param entity - wrapper class for the unique identifier for this class
	 * @return this restriction to the coded node graph.
	 */
	protected Restriction<CodedNodeGraph> restrictToSourceOrTargetEntityRestriction(
			final EntityNameOrURI entity) {
		
		return new Restriction<CodedNodeGraph>() {
			@Override
			public CodedNodeGraph processRestriction(CodedNodeGraph state) {
				try {
				CodedNodeSet codes = ProfileUtils.unionAll(lexBigService);
				ConceptReferenceList codeList = Constructors.createConceptReferenceList(entity.getEntityName().getName());
				codes.restrictToCodes(codeList);
				return state.restrictToCodes(codes);
				} catch (LBException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException();
				}
			}
		};
	}
	
	
	/**
	 * Restrict to the unique identifier of the target entity of this association
	 * @param target - wrapper class for the target unique identifier
	 * @return this restriction to the coded node graph.
	 */
	protected Restriction<CodedNodeGraph> restrictToTargetEntityRestriction(final EntityNameOrURI target){
		
		return new Restriction<CodedNodeGraph>(){
			@Override
			public CodedNodeGraph processRestriction(CodedNodeGraph state) {

				try {
					//TODO find a method of restricting codes to the current restricted state of this node graph.
					CodedNodeSet codes = ProfileUtils.unionAll(lexBigService);
					ConceptReferenceList codeList = Constructors.createConceptReferenceList(target.getEntityName().getName());
					codes.restrictToCodes(codeList);
					return state.restrictToTargetCodes(codes);
				} catch (LBException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException();
				}		
			}
		};
	}
	
	/**
	 * Restrict to the target literal of this association
	 * @param target - the target literal
	 * @return this restriction to the coded node graph.
	 */
	protected Restriction<CodedNodeGraph> restrictToTargetLiteralRestriction(final String target){
		return new Restriction<CodedNodeGraph>(){
			@Override
			public CodedNodeGraph processRestriction(CodedNodeGraph state) {

				//TODO: decide strategy for implementing Supporting LexEVS code (Association to Data).
				throw new UnsupportedOperationException();
			}
		};
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.AbstractNonIterableLexEvsBackedRestrictionHandler#doUnion(org.cts2.uri.DirectoryURI, org.cts2.uri.DirectoryURI, org.cts2.internal.model.uri.restrict.OriginalStateProvider)
	 */
	@Override
	protected CodedNodeGraph doUnion(
			AssociationDirectoryURI directoryUri1,
			AssociationDirectoryURI directoryUri2,
			OriginalStateProvider<CodedNodeGraph> originalStateProvider) {
		return this.doSetOperation(directoryUri1, directoryUri2, originalStateProvider, SetOperator.UNION);
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.AbstractNonIterableLexEvsBackedRestrictionHandler#doIntersect(org.cts2.uri.DirectoryURI, org.cts2.uri.DirectoryURI, org.cts2.internal.model.uri.restrict.OriginalStateProvider)
	 */
	@Override
	protected CodedNodeGraph doIntersect(
			AssociationDirectoryURI directoryUri1,
			AssociationDirectoryURI directoryUri2,
			OriginalStateProvider<CodedNodeGraph> originalStateProvider) {
		return this.doSetOperation(directoryUri1, directoryUri2, originalStateProvider, SetOperator.INTERSECT);
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.AbstractNonIterableLexEvsBackedRestrictionHandler#doDifference(org.cts2.uri.DirectoryURI, org.cts2.uri.DirectoryURI, org.cts2.internal.model.uri.restrict.OriginalStateProvider)
	 */
	@Override
	protected CodedNodeGraph doDifference(
			AssociationDirectoryURI directoryUri1,
			AssociationDirectoryURI directoryUri2,
			OriginalStateProvider<CodedNodeGraph> originalStateProvider) {
		return this.doSetOperation(directoryUri1, directoryUri2, originalStateProvider, SetOperator.SUBTRACT);
	}
	
	/**
	 * @param directoryUri1
	 * @param directoryUri2
	 * @param originalState
	 * @param setOperator
	 * @return
	 */
	protected CodedNodeGraph doSetOperation(AssociationDirectoryURI directoryUri1, AssociationDirectoryURI directoryUri2, OriginalStateProvider<CodedNodeGraph> originalState, SetOperator setOperator){
		Restriction<CodedNodeGraph> restriction1 = this.compile(directoryUri1, originalState);
		Restriction<CodedNodeGraph> restriction2 = this.compile(directoryUri2, originalState);

		CodedNodeGraph cng1 = this.apply(restriction1, originalState.getOriginalState());
		CodedNodeGraph cng2 = this.apply(restriction2, originalState.getOriginalState());

		switch (setOperator){
			case UNION : {
				try {
					return cng1.union(cng2);
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
			}
			case INTERSECT : {
				try {
					return cng1.intersect(cng2);
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
			}
			case SUBTRACT : {
				throw new UnsupportedOperationException("Difference not implemented on CodedNodeGraph");
			}
		}

		throw new IllegalStateException();
	}
	
	/**
	 * @return
	 */
	public LexEvsIdentityConverter getLexEvsIdentityConverter() {
		return lexEvsIdentityConverter;
	}

	/**
	 * @param lexEvsIdentityConverter
	 */
	public void setLexEvsIdentityConverter(
			LexEvsIdentityConverter lexEvsIdentityConverter) {
		this.lexEvsIdentityConverter = lexEvsIdentityConverter;
	}

	/**
	 * @return
	 */
	public LexBIGService getLexBigService() {
		return lexBigService;
	}

	/**
	 * @param lexBigService
	 */
	public void setLexBigService(LexBIGService lexBigService) {
		this.lexBigService = lexBigService;
	}
}
