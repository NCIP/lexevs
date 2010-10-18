/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.database.service.association;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;

public interface AssociationTargetService {
	public static final String INSERT_ASSOCIATIONTARGET_ERROR = "INSERT-ASSOCIATIONTARGET-ERROR";
	public static final String UPDATE_ASSOCIATIONTARGET_ERROR = "UPDATE-ASSOCIATIONTARGET-ERROR";
	public static final String REMOVE_ASSOCIATIONTARGET_ERROR = "REMOVE-ASSOCIATIONTARGET-ERROR";
	public static final String INSERT_ASSOCIATIONTARGET_VERSIONABLE_CHANGES_ERROR = "INSERT-ASSOCIATIONTARGET-VERSIONABLE-CHANGES-ERROR";
	
	public AssociationTarget getAssociationTarget(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			String associationInstanceId);
	
	public AssociationTarget resolveAssociationTargetByRevision(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			String associationInstanceId,
			String revisionId) throws LBRevisionException;

	public void insertAssociationTarget(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			String sourceEntityCode,
			String sourceEntityCodeNamespace,
			AssociationTarget target);

	public void updateAssociationTarget(
			String codingSchemeUri,
			String version, 
			AssociationTarget source);

	public void removeAssociationTarget(
			String codingSchemeUri,
			String version, 
			AssociationTarget target);

	public void revise(String codingSchemeUri, 
			String version, 
			String relationContainerName,
			String associationPredicateName, 
			String sourceEntityCode,
			String sourceEntityCodeNamespace,
			AssociationTarget target)
			throws LBException;

	public void revise(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			AssociationSource source, 
			AssociationTarget target)
			throws LBException;
}