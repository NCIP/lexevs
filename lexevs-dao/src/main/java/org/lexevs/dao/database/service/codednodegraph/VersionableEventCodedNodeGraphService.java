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
package org.lexevs.dao.database.service.codednodegraph;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventVersionService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventCodedNodeGraphService extends AbstractDatabaseService implements CodedNodeGraphService {

	@Override
	@Transactional
	public AssociatedConcept getAssociatedConceptFromUidSource(
			String codingSchemeUri, 
			String codingSchemeVersion,
			String tripleUid) {
		String codingSchemeUid = this.getCodingSchemeId(codingSchemeUri, codingSchemeVersion);
		
		return this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getAssociatedConceptFromUid(
					codingSchemeUid, 
					tripleUid, 
					TripleNode.SUBJECT);
	}
	
	@Override
	@Transactional
	public AssociatedConcept getAssociatedConceptFromUidTarget(
			String codingSchemeUri, 
			String codingSchemeVersion,
			String tripleUid) {
		String codingSchemeUid = this.getCodingSchemeId(codingSchemeUri, codingSchemeVersion);
		
		return this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getAssociatedConceptFromUid(
					codingSchemeUid, 
					tripleUid, 
					TripleNode.OBJECT);
	}

	@Override
	@Transactional
	public List<String> getTripleUidsContainingObject(
			String codingSchemeUri,
			String codingSchemeVersion, 
			String relationsContainerName,
			String associationPredicateName,
			String objectEntityCode,
			String objectEntityCodeNamespace, 
			GraphQuery query, 
			int start,
			int pageSize) {
		String codingSchemeUid = this.getCodingSchemeId(codingSchemeUri, codingSchemeVersion);
		
		String associationPredicateUid = this.
			getAssociationPredicateUid(
					codingSchemeUri, 
					codingSchemeVersion, 
					codingSchemeUid, 
					relationsContainerName, 
					associationPredicateName);
		
		return this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getTripleUidsContainingObject(
					codingSchemeUid, 
					objectEntityCode, 
					objectEntityCodeNamespace, 
					associationPredicateUid,
					query.getRestrictToAssociationsQualifiers(),
					start, 
					pageSize);
	}

	@Override
	public List<String> getAssociationPredicateNamesForCodingScheme(
			String codingSchemeUri, String codingSchemeVersion) {
		String codingSchemeUid = this.getCodingSchemeId(codingSchemeUri, codingSchemeVersion);
		
		return this.getDaoManager().
			getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
				getAssociationPredicateNamesForCodingSchemeUid(codingSchemeUid);
	}

	@Override
	public int getTripleUidsContainingObjectCount(String codingSchemeUri,
			String codingSchemeVersion, String relationsContainerName,
			String associationPredicateName, String objectEntityCode,
			String objectEntityCodeNamespace, GraphQuery query) {
		String codingSchemeUid = this.getCodingSchemeId(codingSchemeUri, codingSchemeVersion);
		
		String associationPredicateUid = this.
		getAssociationPredicateUid(
				codingSchemeUri, 
				codingSchemeVersion, 
				codingSchemeUid, 
				relationsContainerName, 
				associationPredicateName);
		
		return this.getDaoManager().
		getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getTripleUidsContainingObjectCount(
					codingSchemeUid, 
					associationPredicateUid, 
					objectEntityCode, 
					objectEntityCodeNamespace, 
					query.getRestrictToAssociationsQualifiers());
	}

	@Override
	public List<String> getTripleUidsContainingSubject(String codingSchemeUri,
			String codingSchemeVersion, String relationsContainerName,
			String associationPredicateName, String subjectEntityCode,
			String subjectEntityCodeNamespace, GraphQuery query, int start,
			int pageSize) {
	String codingSchemeUid = this.getCodingSchemeId(codingSchemeUri, codingSchemeVersion);
		
		String associationPredicateUid = this.
		getAssociationPredicateUid(
				codingSchemeUri, 
				codingSchemeVersion, 
				codingSchemeUid, 
				relationsContainerName, 
				associationPredicateName);
		
		return this.getDaoManager().
		getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getTripleUidsContainingSubject(
					codingSchemeUid, 
					associationPredicateUid, 
					subjectEntityCode, 
					subjectEntityCodeNamespace, 
					query.getRestrictToAssociationsQualifiers(),
					start,
					pageSize);
	}

	@Override
	public int getTripleUidsContainingSubjectCount(String codingSchemeUri,
			String codingSchemeVersion, String relationsContainerName,
			String associationPredicateName, String subjectEntityCode,
			String subjectEntityCodeNamespace, GraphQuery query) {
		String codingSchemeUid = this.getCodingSchemeId(codingSchemeUri, codingSchemeVersion);
		
		String associationPredicateUid = this.
		getAssociationPredicateUid(
				codingSchemeUri, 
				codingSchemeVersion, 
				codingSchemeUid, 
				relationsContainerName, 
				associationPredicateName);
		
		return this.getDaoManager().
		getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getTripleUidsContainingSubjectCount(
					codingSchemeUid, 
					associationPredicateUid, 
					subjectEntityCode, 
					subjectEntityCodeNamespace, 
					query.getRestrictToAssociationsQualifiers());
	}

	@Transactional
	protected String getAssociationPredicateUid(
			String uri,
			String version,
			String codingSchemeUid,
			String relationsContainerName, 
			String associationPredicateName) {
		AssociationDao associationDao =
			this.getDaoManager().getAssociationDao(
				uri, version);
		String relationsUid = associationDao.
			getRelationsId(codingSchemeUid, relationsContainerName);
		
		return associationDao.
			getAssociationPredicateId(
					codingSchemeUid, 
					relationsUid, 
					associationPredicateName);
	}
}
