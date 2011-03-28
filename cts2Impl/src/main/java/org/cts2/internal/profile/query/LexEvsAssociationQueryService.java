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
package org.cts2.internal.profile.query;

import org.cts2.association.AssociationDirectory;
import org.cts2.association.AssociationList;
import org.cts2.core.TargetExpression;
import org.cts2.profile.query.AssociationQueryService;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.AssociationDirectoryURI;
import org.cts2.uri.EntityDirectoryURI;

/**
 * The class LexEVSAssociationQuery
 * 
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class LexEvsAssociationQueryService extends 
	AbstractBaseQueryService<AssociationDirectoryURI> 
	implements AssociationQueryService {


	public AssociationDirectoryURI getAssociations(){
		return this.getDirectoryURIFactory().getDirectoryURI();
	}
	
	@Override
	public AssociationDirectory resolve(
			AssociationDirectoryURI associationQueryURI,
			QueryControl queryControl, ReadContext readContext) {
		return associationQueryURI.get(queryControl, readContext, AssociationDirectory.class);
	}

	@Override
	public AssociationList resolveAsList(
			AssociationDirectoryURI associationQueryURI,
			QueryControl queryControl, ReadContext readContext) {
		return associationQueryURI.get(queryControl, readContext, AssociationList.class);
	}

	@Override
	public EntityDirectoryURI getAllSourceAndTargetEntities(
			EntityDirectoryURI directory, QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityDirectoryURI getPredicates(AssociationDirectoryURI directory,
			QueryControl queryControl, ReadContext context) {
		
		return directory.getPredicates(queryControl, context);
	}

	@Override
	public EntityDirectoryURI getSourceEntities(
			AssociationDirectoryURI directory, QueryControl queryControl,
			ReadContext context) {
		return directory.getSourceEntities(queryControl, context);
	}

	@Override
	public EntityDirectoryURI getTargetEntities(
			AssociationDirectoryURI directory, QueryControl queryControl,
			ReadContext context) {
		return directory.getTargetEntities(queryControl, context);
	}

	@Override
	public AssociationDirectoryURI restrictToCodeSystemVersion(
			AssociationDirectoryURI directory, NameOrURI codeSystemVersion) {
		return directory.restrictToCodeSystemVersion(codeSystemVersion);
	}

	@Override
	public AssociationDirectoryURI restrictToPredicate(
			AssociationDirectoryURI directory, EntityNameOrURI predicate) {
		return directory.restrictToPredicate(predicate);
	}

	@Override
	public AssociationDirectoryURI restrictToSourceEntity(
			AssociationDirectoryURI directory, EntityNameOrURI sourceEntity) {
		return directory.restrictToSourceEntity(sourceEntity);
	}

	@Override
	public AssociationDirectoryURI restrictToSourceOrTargetEntity(
			AssociationDirectoryURI directory, EntityNameOrURI entity) {
		return directory.restrictToSourceOrTargetEntity(entity);
	}

	@Override
	public AssociationDirectoryURI restrictToTargetEntity(
			AssociationDirectoryURI directory, EntityNameOrURI target) {
		return directory.restrictToTargetEntity(target);
	}

	@Override
	public AssociationDirectoryURI restrictToTargetExpression(
			AssociationDirectoryURI directory, TargetExpression target) {
		return directory.restrictToTargetExpression(target);
	}

	@Override
	public AssociationDirectoryURI restrictToTargetLiteral(
			AssociationDirectoryURI directory, String target) {
		return directory.restrictToTargetLiteral(target);
	}






}
