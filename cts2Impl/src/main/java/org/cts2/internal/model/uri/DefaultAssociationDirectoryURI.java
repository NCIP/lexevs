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

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.association.AssociationDirectory;
import org.cts2.association.AssociationList;
import org.cts2.core.TargetExpression;
import org.cts2.core.types.SetOperator;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedAssociationDirectory;
import org.cts2.internal.model.directory.ResolvedConceptReferencesIteratorBackedAssociationList;
import org.cts2.internal.model.uri.restrict.NonIterableBasedResolvingRestrictionHandler;
import org.cts2.internal.profile.ProfileUtils;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.AssociationDirectoryURI;
import org.cts2.uri.EntityDirectoryURI;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState;
import org.cts2.uri.restriction.SetComposite;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToCodeSystemVersionRestriction;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToPredicateRestriction;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToSourceEntityRestriction;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToSourceOrTargetEntityRestriction;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToTargetEntityRestriction;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToTargetExpressionRestriction;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState.RestrictToTargetLiteralRestriction;
/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class DefaultAssociationDirectoryURI 
	extends AbstractNonIterableLexEvsBackedResolvingDirectoryURI<CodedNodeGraph,AssociationDirectoryURI> implements AssociationDirectoryURI {

	/**
	 * The common LexBIGService.
	 */
	private LexBIGService lexBigService;
	/**
	 *  The beanmapper for this class.
	 */
	private BeanMapper beanMapper;
	/**
	 * a restriction state for the associations
	 */
	private AssociationDirectoryRestrictionState restrictionState = new AssociationDirectoryRestrictionState();
	
	public DefaultAssociationDirectoryURI(
			LexBIGService lexBigService, 
			NonIterableBasedResolvingRestrictionHandler<CodedNodeGraph,AssociationDirectoryURI> restrictionHandler, 
			BeanMapper beanMapper){
		super(restrictionHandler);
		this.beanMapper = beanMapper;
		this.lexBigService = lexBigService;
	}
 
	@SuppressWarnings("unchecked")
	@Override
	protected <O> O transform(
			CodedNodeGraph lexevsObject, Class<O> clazz) {
		try {
			if(clazz.equals(AssociationDirectory.class)){
				return (O) new ResolvedConceptReferencesIteratorBackedAssociationDirectory(lexevsObject, this.beanMapper);
			}
			if(clazz.equals(AssociationList.class)){
				return (O) new ResolvedConceptReferencesIteratorBackedAssociationList(lexevsObject, this.beanMapper);
			}
		} catch (LBException e) {
			//TODO: real cts2 exception here
			throw new IllegalStateException();
		}
		
		//TODO: real cts2 exception here
		throw new IllegalStateException();
	}
	@Override
	protected int doCount(ReadContext readContext) {
		return 0;
	}
	
	public EntityDirectoryURI getAllSourceAndTargetEntities(
			EntityDirectoryURI directory, QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public EntityDirectoryURI getPredicates(QueryControl queryControl, ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public EntityDirectoryURI getSourceEntities(QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public EntityDirectoryURI getTargetEntities(
			AssociationDirectoryURI directory, QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityDirectoryURI getTargetEntities(QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	public AssociationDirectoryURI restrictToCodeSystemVersion(
			NameOrURI codeSystemVersions) {
		RestrictToCodeSystemVersionRestriction restriction = new RestrictToCodeSystemVersionRestriction();
		restriction.setCodeSystemVersion(codeSystemVersions);
		this.getRestrictionState().getRestrictToCodeSystemVersionRestrictions()
				.add(restriction);

		return this.clone();
	}

	public AssociationDirectoryURI restrictToPredicate(EntityNameOrURI predicate) {
		RestrictToPredicateRestriction restriction = new RestrictToPredicateRestriction();
		restriction.setPredicate(predicate);
		this.getRestrictionState().getRestrictToPredicateRestrictions();
		return this.clone();
	}

	
	public AssociationDirectoryURI restrictToSourceEntity(EntityNameOrURI sourceEntity) {
		RestrictToSourceEntityRestriction restriction = new RestrictToSourceEntityRestriction();
		restriction.setSourceEntity(sourceEntity);
		this.getRestrictionState().getRestrictToPredicateRestrictions();
		return this.clone();
	}

	
	public AssociationDirectoryURI restrictToSourceOrTargetEntity(EntityNameOrURI entity) {
		RestrictToSourceOrTargetEntityRestriction restriction = new RestrictToSourceOrTargetEntityRestriction();
		restriction.setEntity(entity);
		this.getRestrictionState().getRestrictToSourceEntityRestrictions();
		return this.clone();
	}

	
	public AssociationDirectoryURI restrictToTargetEntity(EntityNameOrURI target) {
		RestrictToTargetEntityRestriction restriction = new RestrictToTargetEntityRestriction();
		restriction.setTarget(target);
		this.getRestrictionState().getRestrictToTargetEntityRestrictions();
		return this.clone();
	}

	
	public AssociationDirectoryURI restrictToTargetExpression(TargetExpression target) {
		RestrictToTargetExpressionRestriction restriction = new RestrictToTargetExpressionRestriction();
		restriction.setTarget(target);
		this.getRestrictionState().getRestrictToTargetExpressionRestrictions();
		return this.clone();
	}

	
	public AssociationDirectoryURI restrictToTargetLiteral(String target) {
		RestrictToTargetLiteralRestriction restriction = new RestrictToTargetLiteralRestriction();
		restriction.setTarget(target);
		this.getRestrictionState().getRestrictToTargetLiteralRestriction();
		return this.clone();
	}

	@Override
	protected CodedNodeGraph getOriginalState() {
		try {
			return ProfileUtils.unionAllGraphs(this.lexBigService);
		} catch (LBException e) {
			//TODO: real cts2 exception here
			throw new IllegalStateException();
		}
	}

	@Override
	protected AssociationDirectoryURI clone() {
		return this;
	}

	@Override
	public AssociationDirectoryRestrictionState getRestrictionState() {
		return this.restrictionState;
	}

	@Override
	protected AssociationDirectoryURI createSetOperatedDirectoryURI(
			SetOperator setOperator, AssociationDirectoryURI directoryUri1,
			AssociationDirectoryURI directoryUri2) {
		DefaultAssociationDirectoryURI uri = new DefaultAssociationDirectoryURI(
				this.lexBigService, 
				this.getRestrictionHandler(), 
				this.beanMapper);
		
		uri.getRestrictionState().setSetComposite(new SetComposite<AssociationDirectoryURI>());
		uri.getRestrictionState().getSetComposite().setSetOperator(setOperator);
		uri.getRestrictionState().getSetComposite().setDirectoryUri1(directoryUri1);
		uri.getRestrictionState().getSetComposite().setDirectoryUri2(directoryUri2);
	
		return uri;
	}

}
