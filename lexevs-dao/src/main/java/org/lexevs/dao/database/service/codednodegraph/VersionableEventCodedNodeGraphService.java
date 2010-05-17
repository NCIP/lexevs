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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.database.utility.DaoUtility;
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
		return this.getAssociatedConceptsFromUidSource(
				codingSchemeUri, 
				codingSchemeVersion, 
				DaoUtility.createNonTypedList(tripleUid)).get(0);
	}
	
	@Override
	@Transactional
	public AssociatedConcept getAssociatedConceptFromUidTarget(
			String codingSchemeUri, 
			String codingSchemeVersion,
			String tripleUid) {
		return this.getAssociatedConceptsFromUidTarget(
				codingSchemeUri, 
				codingSchemeVersion, 
				DaoUtility.createNonTypedList(tripleUid)).get(0);
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
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);
		
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
					associationPredicateUid,
					objectEntityCode, 
					objectEntityCodeNamespace, 
					query.getRestrictToAssociations(),
					query.getRestrictToAssociationsQualifiers(),
					DaoUtility.toCodeNamespacePair(query.getRestrictToSourceCodes()),
					query.getRestrictToSourceCodeSystem(),
					start, 
					pageSize);
	}
	
	@Override
	@Transactional
	public List<String> getAssociationPredicateNamesForCodingScheme(
			String codingSchemeUri, 
			String codingSchemeVersion,
			String relationsContainerName) {
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);
		
		return this.getDaoManager().
			getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
				getAssociationPredicateNamesForCodingSchemeUid(codingSchemeUid, relationsContainerName);
	}

	@Override
	@Transactional
	public Map<String,Integer> getTripleUidsContainingObjectCount(
			String codingSchemeUri,
			String codingSchemeVersion, 
			String relationsContainerName,
			String objectEntityCode,
			String objectEntityCodeNamespace, 
			GraphQuery query) {
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);
		
		return this.getDaoManager().
		getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getTripleUidsContainingObjectCount(
					codingSchemeUid, 
					relationsContainerName, 
					objectEntityCode, 
					objectEntityCodeNamespace, 
					query.getRestrictToAssociations(),
					query.getRestrictToAssociationsQualifiers(),
					DaoUtility.toCodeNamespacePair(query.getRestrictToSourceCodes()),
					query.getRestrictToSourceCodeSystem());
	}

	@Override
	@Transactional
	public List<String> getTripleUidsContainingSubject(String codingSchemeUri,
			String codingSchemeVersion, String relationsContainerName,
			String associationPredicateName, String subjectEntityCode,
			String subjectEntityCodeNamespace, GraphQuery query, int start,
			int pageSize) {
	String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);
		
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
					query.getRestrictToAssociations(),
					query.getRestrictToAssociationsQualifiers(),
					DaoUtility.toCodeNamespacePair(query.getRestrictToTargetCodes()),
					query.getRestrictToTargetCodeSystem(),
					start,
					pageSize);
	}

	@Override
	@Transactional
	public Map<String,Integer> getTripleUidsContainingSubjectCount(
			String codingSchemeUri,
			String codingSchemeVersion, 
			String relationsContainerName,
			String subjectEntityCode,
			String subjectEntityCodeNamespace, 
			GraphQuery query) {
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);

		return this.getDaoManager().
		getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getTripleUidsContainingSubjectCount(
					codingSchemeUid, 
					relationsContainerName, 
					subjectEntityCode, 
					subjectEntityCodeNamespace, 
					query.getRestrictToAssociations(),
					query.getRestrictToAssociationsQualifiers(),
					DaoUtility.toCodeNamespacePair(query.getRestrictToTargetCodes()),
					query.getRestrictToTargetCodeSystem());
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
		
		if(StringUtils.isBlank(relationsContainerName)) {
			List<String> uuids = associationDao.getAssociationPredicateUidsForAssociationName(
					codingSchemeUid, 
					relationsContainerName, 
					associationPredicateName);
			
			if(uuids.size() > 1) {
				throw new RuntimeException("The AssociationPredicateName: " + associationPredicateName + " is " +
						" ambiguous. Please specify a RelationsContainer Name.");
			}
			
			if(uuids.size() == 1) {
				return uuids.get(0);
			}
		}
		return associationDao.
			getAssociationPredicateUIdByContainerName(
					codingSchemeUid, 
					relationsContainerName,
					associationPredicateName);
	}
	
	@Transactional
	protected List<String> getAssociationPredicateUids(
			String uri,
			String version,
			String codingSchemeUid,
			String relationsContainerName, 
			String associationPredicateName) {
		AssociationDao associationDao =
			this.getDaoManager().getAssociationDao(
				uri, version);
		
		if(StringUtils.isBlank(relationsContainerName)) {
			return associationDao.getAssociationPredicateUidsForAssociationName(
					codingSchemeUid, 
					relationsContainerName, 
					associationPredicateName);
		}
		return DaoUtility.createNonTypedList(associationDao.
			getAssociationPredicateUIdByContainerName(
					codingSchemeUid, relationsContainerName, associationPredicateName));
	}

	@Override
	@Transactional
	public List<AssociatedConcept> getAssociatedConceptsFromUidSource(
			String codingSchemeUri, String codingSchemeVersion,
			List<String> tripleUids) {
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);
		
		return this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getAssociatedConceptsFromUid(
					codingSchemeUid, 
					tripleUids, 
					TripleNode.SUBJECT);
	}

	@Override
	@Transactional
	public List<AssociatedConcept> getAssociatedConceptsFromUidTarget(
			String codingSchemeUri, String codingSchemeVersion,
			List<String> tripleUids) {
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);
		
		return this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getAssociatedConceptsFromUid(
					codingSchemeUid, 
					tripleUids, 
					TripleNode.OBJECT);
	}

	@Override
	@Transactional
	public List<ConceptReference> getConceptReferencesFromUidSource(
			String codingSchemeUri, String codingSchemeVersion,
			List<String> tripleUids) {
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);

		return this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
		getConceptReferencesFromUid(
				codingSchemeUid, 
				tripleUids, 
				TripleNode.SUBJECT);
	}

	@Override
	public List<ConceptReference> getConceptReferencesFromUidTarget(
			String codingSchemeUri, String codingSchemeVersion,
			List<String> tripleUids) {
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);

		return this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
		getConceptReferencesFromUid(
				codingSchemeUid, 
				tripleUids, 
				TripleNode.OBJECT);
	}

	@Override
	@Transactional
	public List<ConceptReference> getRootConceptReferences(String codingSchemeUri,
			String codingSchemeVersion, String relationsContainerName,
			List<String> associationPredicateNames, TraverseAssociations traverse) {

		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);

		List<String> associationPredicateUids = 
			getAssociationPredicateUids(
					codingSchemeUri, 
					codingSchemeVersion,
					codingSchemeUid,
					relationsContainerName, 
					associationPredicateNames);
		
		return this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getRootNodes(codingSchemeUid, associationPredicateUids, traverse);
	}

	private List<String> getAssociationPredicateUids(
			String codingSchemeUri,
			String codingSchemeVersion, 
			String codingSchemeUid,
			String relationsContainerName,
			List<String> associationPredicateNames) {
		
		List<String> associationPredicateUids = new ArrayList<String>();
		
		if(CollectionUtils.isNotEmpty(associationPredicateNames)) {
			for(String predicate : associationPredicateNames) {
				associationPredicateUids.addAll(
						this.getAssociationPredicateUids(
								codingSchemeUri, 
								codingSchemeVersion, 
								codingSchemeUid, 
								relationsContainerName, 
								predicate));
			}
		}
		
		return associationPredicateUids;
	}

	@Override
	public List<ConceptReference> getTailConceptReferences(
			String codingSchemeUri,
			String codingSchemeVersion, 
			String relationsContainerName,
			List<String> associationPredicateNames,
			TraverseAssociations traverse) {

		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);

		List<String> associationPredicateUids = 
			getAssociationPredicateUids(
					codingSchemeUri, 
					codingSchemeVersion,
					codingSchemeUid,
					relationsContainerName, 
					associationPredicateNames);
		
		return this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getTailNodes(codingSchemeUid, associationPredicateUids, traverse);
	}

	@Override
	public List<String> getAssociationPredicateUidsForNames(
			String codingSchemeUri, String codingSchemeVersion,
			String relationsContainerName, List<String> associationNames) {
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);
		return this.getAssociationPredicateUids(
				codingSchemeUri, 
				codingSchemeVersion, 
				codingSchemeUid, 
				relationsContainerName, 
				associationNames);
	}
}
