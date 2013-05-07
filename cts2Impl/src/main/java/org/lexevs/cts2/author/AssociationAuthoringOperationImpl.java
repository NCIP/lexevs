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

import java.util.Arrays;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexEVSAuthoringServiceImpl;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;

/**
 * @author  <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class AssociationAuthoringOperationImpl implements
		AssociationAuthoringOperation {

	LexEVSAuthoringServiceImpl authoring;
	public AssociationAuthoringOperationImpl(){

		authoring = new LexEVSAuthoringServiceImpl();
	}
	
	
	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.AssociationAuthoringOperation#createAssociation(boolean, org.LexGrid.versions.Revision, org.LexGrid.versions.EntryState, org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.relations.AssociationQualification[])
	 */
	@Override
	public AssociationSource createAssociation(
			boolean createMappingScheme,
			Revision revision,
			EntryState entryState,
			AbsoluteCodingSchemeVersionReference mappingScheme,
			AbsoluteCodingSchemeVersionReference sourceCodeSystemIdentifier,
			AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier,
			String sourceConceptCodeIdentifier,
			String targetConceptCodeIdentifier, String relationsContainerName,
			String associationType,
			AssociationQualification[] associationQualifiers)
			throws LBException {
		AssociationSource source = null;
		if (createMappingScheme == true) 
		{
			CodingScheme scheme = authoring.getCodingSchemeMetaData(sourceCodeSystemIdentifier);
			AssociationTarget target = authoring.createAssociationTarget(
					entryState, 
					null,
					null,
					false,
					false,
					null,
					null,
					targetCodeSystemIdentifier,
					targetConceptCodeIdentifier);
			String namespace = authoring.getCodingSchemeNamespace(scheme,
					sourceCodeSystemIdentifier.getCodingSchemeURN());
			source = new AssociationSource();
			source.setSourceEntityCode(sourceConceptCodeIdentifier);
			source.setSourceEntityCodeNamespace(namespace);
			source.setTarget(Arrays.asList(target));
			AssociationSource[] sources = new AssociationSource[] { source };
			String sourceCodingSchemeName = authoring.getCodingSchemeNameForMininumReference(sourceCodeSystemIdentifier);
			String targetCodingSchemeName = authoring.getCodingSchemeNameForMininumReference(targetCodeSystemIdentifier);
			authoring.createMappingWithDefaultValues(sources,
					sourceCodingSchemeName,
					sourceCodeSystemIdentifier.getCodingSchemeVersion(),
					targetCodingSchemeName,
					targetCodeSystemIdentifier.getCodingSchemeVersion(),
					associationType, false);
		}
		if (createMappingScheme == false && mappingScheme != null){
			CodingScheme sourceScheme = authoring.getCodingSchemeMetaData(sourceCodeSystemIdentifier);
			AssociationTarget target = authoring.createAssociationTarget(
					entryState, 
					null,
					null,
					false,
					false,
					null,
					null,
					targetCodeSystemIdentifier,
					targetConceptCodeIdentifier);
			String namespace = authoring.getCodingSchemeNamespace(sourceScheme,
					sourceScheme.getCodingSchemeURI());
			source = new AssociationSource();
			source.setSourceEntityCode(sourceConceptCodeIdentifier);
			source.setSourceEntityCodeNamespace(namespace);
			source.setTarget(Arrays.asList(target));
			AssociationSource[] sources = new AssociationSource[] { source };
			Date effectiveDate = new Date();
			authoring.createAssociationMapping(entryState, mappingScheme,
					sourceCodeSystemIdentifier, targetCodeSystemIdentifier,
					sources, associationType, relationsContainerName,
					effectiveDate, associationQualifiers, revision, true);
		}
		if (createMappingScheme == false && mappingScheme == null){
			AssociationTarget target = authoring.createAssociationTarget(
					entryState,
					null,
					null,
					false,
					false,
					null,
					null,
					targetCodeSystemIdentifier,
					targetConceptCodeIdentifier);
			AssociationTarget[] targets = new AssociationTarget[] { target };
			source = authoring.createAssociationSource(revision, entryState,
					sourceCodeSystemIdentifier, sourceConceptCodeIdentifier,
					relationsContainerName, associationType, targets);
		}
		return source;
	}

	@Override
	public void createLexicalAssociation() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createRuleBasedAssociation() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.AssociationAuthoringOperation#updateAssociationStatus(org.LexGrid.versions.Revision, org.LexGrid.versions.EntryState, org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public boolean updateAssociationStatus(Revision revision,
			EntryState entryState, AbsoluteCodingSchemeVersionReference scheme,
			String relationsContainer, String associationName,
			String sourceCode, String sourceNamespace, String targetCode,
			String targetNamespace, String instanceId, String status,
			boolean isActive) throws LBException {
		
		return authoring.setAssociationStatus(revision, entryState, scheme,
				relationsContainer, associationName, sourceCode,
				sourceNamespace, targetCode, targetNamespace, instanceId,
				status, isActive);
	}

}