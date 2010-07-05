/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and Research
 * (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the triple-shield Mayo
 * logo are trademarks and service marks of MFMER.
 * 
 * Except as contained in the copyright notice above, or as used to identify
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.lexevs.cts2.author;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;

public interface AssociationAuthoringOperation {
	public AssociationSource createAssociation(
			Revision revision,
			EntryState entryState,
			AbsoluteCodingSchemeVersionReference baseScheme,
			AbsoluteCodingSchemeVersionReference  sourceCodeSystemIdentifier,
			AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier,
			String sourceConceptCodeIdentifier,
			String targetConceptCodeIdentifier, 
			String relationsContainerName, 
			String associationType,
			AssociationQualification[] associationQualifiers)
			throws LBException;

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

	public void createLexicalAssociation();

	public void createRuleBasedAssociation();
}
