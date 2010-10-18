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
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationSource;

public interface AssociationDataService {
	public static final String INSERT_ASSOCIATIONDATA_ERROR = "INSERT-ASSOCIATIONDATA-ERROR";
	public static final String UPDATE_ASSOCIATIONDATA_ERROR = "UPDATE-ASSOCIATIONDATA-ERROR";
	public static final String REMOVE_ASSOCIATIONDATA_ERROR = "REMOVE-ASSOCIATIONDATA-ERROR";
	public static final String INSERT_ASSOCIATIONDATA_VERSIONABLE_CHANGES_ERROR = "INSERT-ASSOCIATIONDATA-VERSIONABLE-CHANGES-ERROR";

	public void insertAssociationData(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			String sourceEntityCode,
			String sourceEntityCodeNamespace,
			AssociationData data);

	public void updateAssociationData(
			String codingSchemeUri, 
			String version,
			AssociationData data);

	public void removeAssociationData(
			String codingSchemeUri, 
			String version,
			AssociationData data);

	public void revise(
			String codingSchemeUri, 
			String version, 
			String relationContainerName, 
			String associationPredicateName,
			String sourceEntityCode,
			String sourceEntityCodeNamespace,
			AssociationData data) throws LBException;

	public AssociationData resolveAssociationDataByRevision(
			String codingSchemeUri,
			String version, 
			String relationContainerName,
			String associationPredicateName, 
			String associationInstanceId,
			String revisionId) throws LBRevisionException;

	public void revise(
			String codingSchemeUri, 
			String version,
			String relationContainerName, 
			String associationPredicateName,
			AssociationSource source, 
			AssociationData data) throws LBException;
}