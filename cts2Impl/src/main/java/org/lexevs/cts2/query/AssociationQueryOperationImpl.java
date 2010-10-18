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
package org.lexevs.cts2.query;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.BaseService;
import org.lexevs.dao.database.service.association.AssociationService.AssociationTriple;

public class AssociationQueryOperationImpl extends BaseService implements AssociationQueryOperation {

	@Override
	public boolean computeSubsumptionRelationship(String codingSystemName,
			CodingSchemeVersionOrTag versionOrTag, String associationtype,
			ConceptReference sourceCode, ConceptReference targetCode) {
		try {
			if (StringUtils.equals(sourceCode.getCodeNamespace(), targetCode
					.getCodeNamespace()) == false
					|| StringUtils.equals(sourceCode.getCodingSchemeName(),
							targetCode.getCodingSchemeName()) == false) {
				throw new LBParameterException(
						"Does not support different coding systems subsumes");
			} else {
				CodedNodeGraph cng = LexBIGServiceImpl.defaultInstance()
						.getNodeGraph(codingSystemName, versionOrTag, null);
				return cng.areCodesRelated(Constructors.createNameAndValue(
						associationtype, null), sourceCode, targetCode, false);
			}
		} catch (LBParameterException e) {
			e.printStackTrace();
		} catch (LBInvocationException e) {
			e.printStackTrace();
		} catch (LBResourceUnavailableException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public ResolvedConceptReference determineTransitiveConceptRelationship(
			String codingSystemUri, CodingSchemeVersionOrTag versionOrTag,
			String relationContainerName, String associationName,
			String sourceCode, String sourceNS, String targetCode,
			String targetNS) {

		LexBIGServiceConvenienceMethods lbscm;
		try {
			lbscm = (LexBIGServiceConvenienceMethods) LexBIGServiceImpl
					.defaultInstance().getGenericExtension(
							"LexBIGServiceConvenienceMethods");
			return lbscm.getNodesPath(codingSystemUri, versionOrTag,
					relationContainerName, associationName, sourceCode,
					sourceNS, targetCode, targetNS);
		} catch (LBParameterException e) {
			e.printStackTrace();
		} catch (LBInvocationException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ResolvedConceptReferenceList listAssociations(
			String codingSystemName, CodingSchemeVersionOrTag versionOrTag,
			String namespace, String code, String associationName,
			boolean isBackward, int depth, int maxToReturn) {
		ResolvedConceptReferenceList list = new ResolvedConceptReferenceList();

		try {
			CodedNodeGraph cng = LexBIGServiceImpl.defaultInstance()
					.getNodeGraph(codingSystemName, versionOrTag, null);
			ConceptReference conRef = new ConceptReference();
			conRef.setCode(code);
			conRef.setCodeNamespace(namespace);
			conRef.setCodingSchemeName(codingSystemName);
			conRef.setConceptCode(code);

			if (StringUtils.isEmpty(associationName) == false) {
				cng = cng.restrictToAssociations(Constructors
						.createNameAndValueList(associationName), null);
			}
			return cng.resolveAsList(conRef, !(isBackward), isBackward, -1,
					depth, null, null, null, null, maxToReturn);
		} catch (LBParameterException e) {
			e.printStackTrace();
		} catch (LBInvocationException e) {
			e.printStackTrace();
		} catch (LBResourceUnavailableException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public AssociationTriple getAssociationDetails(String codingSchemeUri,
			CodingSchemeVersionOrTag versionOrTag,
			String associationInstanceId) {

		String version;
		try {
			version = ServiceUtility.getVersion(codingSchemeUri, versionOrTag);
			return this.getDatabaseServiceManager().getAssociationService().getAssociationTripleByAssociationInstanceId(codingSchemeUri, version, associationInstanceId);
		
		} catch (LBParameterException e) {
			e.printStackTrace();
		}
		return null;
	}
}