/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.mapping;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexEVSAuthoringServiceImpl;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;

public class MappingTestUtility {

	private LexEVSAuthoringServiceImpl authoring;

	public MappingTestUtility() {
		authoring = new LexEVSAuthoringServiceImpl();
	}


	public AssociationTarget createTarget(String code, String namespace) {
		AssociationTarget target = new AssociationTarget();
		target.setTargetEntityCode(code);
		target.setTargetEntityCodeNamespace(namespace);
		return target;
	}

	public AssociationTarget createTargetWithValuesPopulated()
			throws LBException {
		Versionable versionableData = new Versionable();
		versionableData.setEffectiveDate(new Date());
		versionableData.setExpirationDate(new Date());
		versionableData.setIsActive(Boolean.TRUE);
		versionableData.setOwner("Mayo");
		versionableData.setStatus("ACTIVE");
		String instanceId = "instance_001";

		List<String> usageContextList = null;
		AssociationQualification qualifier = new AssociationQualification();
		qualifier.setAssociationQualifier("qualifier");
		Text text = new Text();
		text.setContent("qualifies SY");
		qualifier.setQualifierText(text);
		List<AssociationQualification> associationQualifiers = Arrays
				.asList(qualifier);
		AbsoluteCodingSchemeVersionReference targetCodeSystemIdentifier = new AbsoluteCodingSchemeVersionReference();
		targetCodeSystemIdentifier.setCodingSchemeURN("urn:oid:11.11.0.1");
		targetCodeSystemIdentifier
				.setCodingSchemeVersion(MappingTestConstants.TARGET_VERSION);
		String targetConceptCodeIdentifier = "005";
		return authoring.createAssociationTarget(null, versionableData,
				instanceId, Boolean.FALSE, Boolean.TRUE, usageContextList,
				associationQualifiers, targetCodeSystemIdentifier,
				targetConceptCodeIdentifier);
	}

}