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

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.concepts.Entity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.dao.database.ibatis.codednodegraph.model.EntityReferencingAssociatedConcept;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventVersionService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventCodedNodeGraphService extends AbstractDatabaseService implements CodedNodeGraphService {

	@Override
	public List<String> getRelationNamesForCodingScheme(
			String codingSchemeUri,
			String codingSchemeVersion) {
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);
		
		AssociationDao associationDao = 
			this.getDaoManager().getAssociationDao(codingSchemeUri, codingSchemeVersion);
		
		return associationDao.getRelationsNamesForCodingSchemeUId(codingSchemeUid);
	}
	
	@Override
	@Transactional
	public List<String> listCodeRelationships(
			String codingSchemeUri,
			String codingSchemeVersion, 
			String relationsContainerName,
			String sourceEntityCode, 
			String sourceEntityCodeNamespace,
			String targetEntityCode, 
			String targetEntityCodeNamespace,
			GraphQuery query, 
			boolean useTransitive) {
		List<String> returnList = new ArrayList<String>();
		
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);
		
		List<String> associationPredicateUids = this.getDaoManager().
			getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
				listCodeRelationships(
						codingSchemeUid, 
						relationsContainerName,
						sourceEntityCode, 
						sourceEntityCodeNamespace, 
						targetEntityCode, 
						targetEntityCodeNamespace, 
						query.getRestrictToAssociations(), 
						query.getRestrictToAssociationsQualifiers(), 
						DaoUtility.toCodeNamespacePair(query.getRestrictToSourceCodes()), 
						DaoUtility.toCodeNamespacePair(query.getRestrictToTargetCodes()), 
						query.getRestrictToSourceCodeSystem(), 
						query.getRestrictToTargetCodeSystem(), 
						query.getRestrictToEntityTypes(),
						query.isRestrictToAnonymous(),
						useTransitive);
		
		
		AssociationDao associationDao = 
			this.getDaoManager().getAssociationDao(codingSchemeUri, codingSchemeVersion);
		
		for(String uid : associationPredicateUids) {
			returnList.add(
					associationDao.getAssociationPredicateNameForUId(codingSchemeUid, uid));
		}
		
		return returnList;
	}

	@Override
	@Transactional
	public AssociatedConcept getAssociatedConceptFromUidSource(
			String codingSchemeUri, 
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
			String tripleUid) {
		return this.getAssociatedConceptsFromUidSource(
				codingSchemeUri, 
				codingSchemeVersion, 
				resolve,
				propertyNames,
				propertyTypes,
				null,
				DaoUtility.createNonTypedList(tripleUid)).get(0);
	}
	
	@Override
	@Transactional
	public AssociatedConcept getAssociatedConceptFromUidTarget(
			String codingSchemeUri, 
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
			String tripleUid) {
		return this.getAssociatedConceptsFromUidTarget(
				codingSchemeUri, 
				codingSchemeVersion, 
				resolve,
				propertyNames,
				propertyTypes,
				null,
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
			List<Sort> sorts,
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
					query.getRestrictToEntityTypes(),
					query.isRestrictToAnonymous(),
					sorts,
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
					query.getRestrictToSourceCodeSystem(),
					query.getRestrictToEntityTypes(),
					query.isRestrictToAnonymous());
	}

	@Override
	@Transactional
	public List<String> getTripleUidsContainingSubject(
			String codingSchemeUri,
			String codingSchemeVersion, 
			String relationsContainerName,
			String associationPredicateName, 
			String subjectEntityCode,
			String subjectEntityCodeNamespace, 
			GraphQuery query, 
			List<Sort> sorts,
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
					query.getRestrictToEntityTypes(),
					query.isRestrictToAnonymous(),
					sorts,
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
					query.getRestrictToTargetCodeSystem(),
					query.getRestrictToEntityTypes(),
					query.isRestrictToAnonymous());
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
	public List<? extends AssociatedConcept> getAssociatedConceptsFromUidSource(
			String codingSchemeUri, 
			String codingSchemeVersion, 
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
	        List<Sort> sorts,
			List<String> tripleUids) {

			return doGetAssociatedConcepts(
					codingSchemeUri, 
					codingSchemeVersion,
					resolve,
					propertyNames,
					propertyTypes,
					tripleUids, 
					sorts,
					TripleNode.SUBJECT);
	}
	
	protected List<? extends AssociatedConcept> doGetAssociatedConcepts(
			String codingSchemeUri, 
			String codingSchemeVersion,
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes, 
			List<String> tripleUids, 
			List<Sort> sorts,
			TripleNode tripleNode){
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);
		
		List<EntityReferencingAssociatedConcept> associatedConcepts = this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getAssociatedConceptsFromUid(
				codingSchemeUid, 
				tripleUids, 
				sorts,
				tripleNode);
		
		if(resolve) {
			List<String> entityUids = this.getEntityUids(associatedConcepts);
			
			Map<String,Entity> entityMap = this.getDaoManager().getEntityDao(codingSchemeUri, codingSchemeVersion).
				getEntitiesWithUidMap(
						codingSchemeUid, 
						DaoUtility.localNameListToString(propertyNames), 
						DaoUtility.propertyTypeArrayToString(propertyTypes), 
						entityUids);
			
			for(EntityReferencingAssociatedConcept associatedConcept : associatedConcepts) {
				Entity entity = entityMap.get(associatedConcept.getEntityGuid());
				associatedConcept.setEntity(entity);
			}
		}
		
		return associatedConcepts;
	}
	
	private List<String> getEntityUids(List<EntityReferencingAssociatedConcept> associatedConcepts){
		List<String> returnList = new ArrayList<String>();
		for(EntityReferencingAssociatedConcept assocConcept : associatedConcepts) {
			String entityGuid = assocConcept.getEntityGuid();
			if(StringUtils.isNotBlank(entityGuid)) {
				returnList.add(assocConcept.getEntityGuid());
			}
		}
		return returnList;
	}


	@Override
	@Transactional
	public List<? extends AssociatedConcept> getAssociatedConceptsFromUidTarget(
			String codingSchemeUri, 
			String codingSchemeVersion, 
			boolean resolve,
			LocalNameList propertyNames, 
	        PropertyType[] propertyTypes,
	        List<Sort> sorts,
			List<String> tripleUids) {
		
		return doGetAssociatedConcepts(
				codingSchemeUri, 
				codingSchemeVersion,
				resolve,
				propertyNames,
				propertyTypes,
				tripleUids, 
				sorts,
				TripleNode.OBJECT);
	}

	@Override
	@Transactional
	public List<ConceptReference> getConceptReferencesFromUidSource(
			String codingSchemeUri, 
			String codingSchemeVersion,
			List<Sort> sorts,
			List<String> tripleUids) {
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);

		return this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
		getConceptReferencesFromUid(
				codingSchemeUid, 
				tripleUids, 
				TripleNode.SUBJECT, 
				sorts);
	}

	@Override
	public List<ConceptReference> getConceptReferencesFromUidTarget(
			String codingSchemeUri, 
			String codingSchemeVersion,
			List<Sort> sorts,
			List<String> tripleUids) {
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);

		return this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
		getConceptReferencesFromUid(
				codingSchemeUid, 
				tripleUids, 
				TripleNode.OBJECT,
				sorts);
	}

	@Override
	@Transactional
	public List<ConceptReference> getRootConceptReferences(
			String codingSchemeUri,
			String codingSchemeVersion, 
			String relationsContainerName,
			List<String> associationPredicateNames, 
			List<QualifierNameValuePair> qualifiers,
			List<String> subjectEntityCodeNamespaces,  
			List<String> objectEntityCodeNamespaces,  
			TraverseAssociations traverse,
			List<Sort> sorts,
			int start,
			int pageSize) {

		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);

		List<String> associationPredicateUids = 
			getAssociationPredicateUids(
					codingSchemeUri, 
					codingSchemeVersion,
					codingSchemeUid,
					relationsContainerName, 
					associationPredicateNames);
		
		//This means there was an AssociationPredicateName that was passed in
		//that isn't part of this Coding Scheme -- there will be no roots for it.
		if(CollectionUtils.isNotEmpty(associationPredicateNames) &&
				CollectionUtils.isEmpty(associationPredicateUids)){
			return null;
		}
		
		return this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getRootNodes(
					codingSchemeUid, 
					associationPredicateUids, 
					qualifiers, 
					subjectEntityCodeNamespaces, 
					objectEntityCodeNamespaces, 
					traverse, 
					sorts, 
					start,
					pageSize);
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
			List<QualifierNameValuePair> qualifiers,
			List<String> subjectEntityCodeNamespaces, 
			List<String> objectEntityCodeNamespaces,  
			TraverseAssociations traverse,
			List<Sort> sorts,
			int start,
			int pageSize) {

		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);

		List<String> associationPredicateUids = 
			getAssociationPredicateUids(
					codingSchemeUri, 
					codingSchemeVersion,
					codingSchemeUid,
					relationsContainerName, 
					associationPredicateNames);
		
		//This means there was an AssociationPredicateName that was passed in
		//that isn't part of this Coding Scheme -- there will be no roots for it.
		if(CollectionUtils.isNotEmpty(associationPredicateNames) &&
				CollectionUtils.isEmpty(associationPredicateUids)){
			return null;
		}
		
		return this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getTailNodes(
					codingSchemeUid, 
					associationPredicateUids, 
					qualifiers, 
					subjectEntityCodeNamespaces, 
					objectEntityCodeNamespaces, 
					traverse, 
					sorts, 
					start,
					pageSize);
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

	@Override
	public List<String> getTripleUidsForMappingRelationsContainer(
			String codingSchemeUri,
			String codingSchemeVersion, 
			AbsoluteCodingSchemeVersionReference sourceCodingScheme,
			AbsoluteCodingSchemeVersionReference targetCodingScheme,
			String relationsContainerName,
			List<Sort> sorts,
			int start, 
			int pageSize) {
		String mappingCodingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);
		
		String sourceCodingSchemeUid = null;
		if(sourceCodingScheme != null) {
			sourceCodingSchemeUid = 
				this.getCodingSchemeUId(
						sourceCodingScheme.getCodingSchemeURN(), 
						sourceCodingScheme.getCodingSchemeVersion());
		}
		
		String targetCodingSchemeUid = null;
		if(targetCodingScheme != null) {
			targetCodingSchemeUid = 
				this.getCodingSchemeUId(
						targetCodingScheme.getCodingSchemeURN(), 
						targetCodingScheme.getCodingSchemeVersion());
		}

		
		return this.getDaoManager().getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
			getTripleUidsForMappingRelationsContainer(
					mappingCodingSchemeUid, 
					sourceCodingSchemeUid, 
					targetCodingSchemeUid, 
					relationsContainerName, 
					sorts, 
					start,
					pageSize);
	}
	
	@Override
	public List<? extends ResolvedConceptReference> getMappingTriples(
			String codingSchemeUri,
			String codingSchemeVersion, 
			AbsoluteCodingSchemeVersionReference sourceCodingScheme,
			AbsoluteCodingSchemeVersionReference targetCodingScheme,
			String relationsContainerName,
			List<String> tripleUids) {
		String mappingCodingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);
		
		String sourceCodingSchemeUid = null;
		if(sourceCodingScheme != null) {
			sourceCodingSchemeUid = 
				this.getCodingSchemeUId(
						sourceCodingScheme.getCodingSchemeURN(), 
						sourceCodingScheme.getCodingSchemeVersion());
		}
		
		String targetCodingSchemeUid = null;
		if(targetCodingScheme != null) {
			targetCodingSchemeUid = 
				this.getCodingSchemeUId(
						targetCodingScheme.getCodingSchemeURN(), 
						targetCodingScheme.getCodingSchemeVersion());
		}
		
		return this.getDaoManager().
			getCodedNodeGraphDao(
					codingSchemeUri, 
					codingSchemeVersion).
						getTriplesForMappingRelationsContainer(
								mappingCodingSchemeUid, 
								sourceCodingSchemeUid, 
								targetCodingSchemeUid, 
								relationsContainerName, 
								tripleUids);
	}

	@Override
	public int getMappingTriplesCount(
			String codingSchemeUri,
			String codingSchemeVersion, 
			String relationsContainerName) {
		String mappingCodingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, codingSchemeVersion);
		
		return this.getDaoManager().
			getCodedNodeGraphDao(
					codingSchemeUri, 
					codingSchemeVersion).
						getTriplesForMappingRelationsContainerCount(
								mappingCodingSchemeUid, 
								relationsContainerName);
	}
}
