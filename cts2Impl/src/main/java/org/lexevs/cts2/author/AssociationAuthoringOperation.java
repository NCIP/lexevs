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
package org.lexevs.cts2.author;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;

/**
 * The Interface AssociationAuthoringOperation.
 */
public interface AssociationAuthoringOperation {
	
	/**
	 * Creates the association.
	 * 
	 * @param createMappingScheme - create a mapping coding scheme if one does not exist
	 * @param revision - revision data
	 * @param entryState - revision data container granular to the versionable class.
	 * @param mappingScheme - existing mapping scheme, required if adding mapped association
	 * @param sourceCodeSystemIdentifier - minimum code system identification
	 * @param targetCodeSystemIdentifier - minimum code system identification
	 * @param sourceConceptCodeIdentifier - source concept code
	 * @param targetConceptCodeIdentifier - target concept code
	 * @param relationsContainerName - relations container identifier
	 * @param associationType - association type identifier
	 * @param associationQualifiers - qualifications to add to this association
	 * 
	 * @return - returns a representation of this association as a LexGrid object.
	 * 
	 * @throws LBException the LB exception
	 */
	public AssociationSource createAssociation(
			boolean createMappingScheme,
			Revision revision,
			EntryState entryState,
			AbsoluteCodingSchemeVersionReference mappingScheme,
			AbsoluteCodingSchemeVersionReference  sourceCodeSystemIdentifier,
			AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier,
			String sourceConceptCodeIdentifier,
			String targetConceptCodeIdentifier, 
			String relationsContainerName, 
			String associationType,
			AssociationQualification[] associationQualifiers)
			throws LBException;

	/**
	 * Update association status.
	 * 
	 * @param revision the revision
	 * @param entryState the entry state
	 * @param scheme the scheme
	 * @param relationsContainer the relations container
	 * @param associationName the association name
	 * @param sourcCode the sourc code
	 * @param sourceNamespace the source namespace
	 * @param targetCode the target code
	 * @param targetNamespace the target namespace
	 * @param instanceId the instance id
	 * @param status the status
	 * @param isActive the is active
	 * 
	 * @return true, if update association status
	 * 
	 * @throws LBException the LB exception
	 */
	public boolean updateAssociationStatus(
			Revision revision, 
			EntryState entryState, 
			AbsoluteCodingSchemeVersionReference scheme, 
			String relationsContainer, 
			String associationName, 
			String sourcCode, 
			String sourceNamespace, 
			String targetCode, 
			String targetNamespace, 
			String instanceId, 
			String status, 
			boolean isActive) throws LBException;

	/**
	 * Creates the lexical association.
	 */
	public void createLexicalAssociation();

	/**
	 * Creates the rule based association.
	 */
	public void createRuleBasedAssociation();
}