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
package org.cts2.uri;

import org.cts2.core.TargetExpression;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.restriction.AssociationDirectoryRestrictionState;

/**
 * The Interface AssociationDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface AssociationDirectoryURI 
	extends DirectoryURI, SetOperable<AssociationDirectoryURI> {
	
	/**
	 * 
	 * @param directory
	 * @param queryControl
	 * @param context
	 */
	public EntityDirectoryURI getAllSourceAndTargetEntities(
			EntityDirectoryURI directory, QueryControl queryControl,
			ReadContext context);

	/**
	 * 
	 * @param directory
	 * @param queryControl
	 * @param context
	 */
	public EntityDirectoryURI getPredicates(QueryControl queryControl, ReadContext context);

	/**
	 * 
	 * @param directory
	 * @param queryControl
	 * @param context
	 */
	public EntityDirectoryURI getSourceEntities(QueryControl queryControl, ReadContext context);

	/**
	 * 
	 * @param directory
	 * @param queryControl
	 * @param context
	 */
	public EntityDirectoryURI getTargetEntities(QueryControl queryControl, ReadContext context);

	/**
	 * 
	 * @param directory
	 * @param codeSystemVersion
	 */
	public AssociationDirectoryURI restrictToCodeSystemVersion(NameOrURI codeSystemVersion);

	/**
	 * 
	 * @param directory
	 * @param predicate
	 */
	public AssociationDirectoryURI restrictToPredicate(EntityNameOrURI predicate);

	/**
	 * 
	 * @param directory
	 * @param sourceEntity
	 */
	public AssociationDirectoryURI restrictToSourceEntity(EntityNameOrURI sourceEntity);

	/**
	 * 
	 * @param directory
	 * @param entity
	 */
	public AssociationDirectoryURI restrictToSourceOrTargetEntity(EntityNameOrURI entity);

	/**
	 * 
	 * @param directory
	 * @param target
	 */
	public AssociationDirectoryURI restrictToTargetEntity(EntityNameOrURI target);

	/**
	 * 
	 * @param directory
	 * @param target
	 */
	public AssociationDirectoryURI restrictToTargetExpression(TargetExpression target);

	/**
	 * 
	 * @param directory
	 * @param target
	 */
	public AssociationDirectoryURI restrictToTargetLiteral(String target);

	public AssociationDirectoryRestrictionState getRestrictionState();
}
