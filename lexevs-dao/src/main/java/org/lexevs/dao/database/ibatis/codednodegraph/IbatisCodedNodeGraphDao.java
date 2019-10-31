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
package org.lexevs.dao.database.ibatis.codednodegraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.custom.relations.TerminologyMapBean;
import org.LexGrid.relations.Relations;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.IbatisAssociationDao;
import org.lexevs.dao.database.ibatis.association.parameter.GetCodeRelationshipsBean;
import org.lexevs.dao.database.ibatis.association.parameter.GetCountConceptReferenceBean;
import org.lexevs.dao.database.ibatis.association.parameter.GetEntityAssnUidsBean;
import org.lexevs.dao.database.ibatis.association.parameter.GetEntityAssnUidsCountBean;
import org.lexevs.dao.database.ibatis.codednodegraph.model.EntityReferencingAssociatedConcept;
import org.lexevs.dao.database.ibatis.codednodegraph.model.TripleUidReferencingResolvedConceptReference;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameter;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTriple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedParameterTuple;
import org.lexevs.dao.database.ibatis.parameter.PrefixedTableParameterBean;
import org.lexevs.dao.database.ibatis.parameter.SequentialMappedParameterBean;
import org.lexevs.dao.database.ibatis.parameter.SourceAndTargetMappingPrefixedParameter;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService.Sort;
import org.lexevs.dao.database.service.codednodegraph.model.CountConceptReference;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.CodeNamespacePair;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.util.CollectionUtils;

@Cacheable(cacheName = "IbatisCodedNodeGraphDaoCache")
public class IbatisCodedNodeGraphDao extends AbstractIbatisDao implements CodedNodeGraphDao {



/** The supported datebase version. */
private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("2.0");
	
	private static String GET_ENTITY_ASSNSTOENTITY_UID_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getEntityAssnsToEntityUids";
	private static String GET_ENTITY_ASSNSTOENTITY_UID_COUNT_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getEntityAssnsToEntityUidsCount";
	private static String GET_ASSOCIATEDCONCEPT_FROM_ASSNSTOENTITY_UID_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getAssociatedConcpetFromEntityAssnsToEntityUid";
	private static String GET_CONCEPTREFERENCE_FROM_ASSNSTOENTITY_UID_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getConceptReferenceFromEntityAssnsToEntityUid";
	private static String GET_ASSOCIATION_PREDICATE_NAMES_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getAssociationPredicatNamesFromCodingSchemeUid";
	private static String GET_DISTINCT_SOURCE_NODES_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getDistinctSources";
	private static String GET_DISTINCT_TARGET_NODES_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getDistinctTargets";
	private static String GET_TARGET_NODES_OF_SOURCE_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getTargetsOfSource";
	private static String GET_SOURCE_NODES_OF_TARGET_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getSourcesOfTarget";
	private static String GET_TAIL_ENTITY_ASSNSTOENTITY_UID_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getTailEntityAssnsToEntityUids";
	private static String GET_ROOT_ENTITY_ASSNSTOENTITY_UID_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getRootEntityAssnsToEntityUids";
	private static String GET_CODE_RELATIONSHIPS_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getCodeRelationships";
	private static String GET_COUNT_CONCEPTREFERENCES_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getCountConceptReferences";
	private static String GET_CONCEPTREFERENCES_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getConceptReferences";
	private static String GET_TRIPLE_UIDS_FOR_MAPPING_CONTAINER_SQL  = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getTripleUidsForMappingContainer";
	private static String GET_TRIPLE_UIDS_FOR_MAPPING_CONTAINER_AND_CODES_WITH_SORT_SQL  = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getTripleUidsForMappingContainerAndCodesWithSort";
	private static String GET_TRIPLE_UIDS_FOR_MAPPING_CONTAINER_AND_CODES_NO_SORT_SQL  = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getTripleUidsForMappingContainerAndCodesNoSort";
	private static String GET_TRIPLES_FOR_MAPPING_CONTAINER_SQL  = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getTriplesForMappingContainer";
	private static String GET_MINIMAL_TRIPLES_FOR_MAPPING_CONTAINER_SQL = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getMinimalTriplesForMappingContainer";
	private static String GET_TRIPLES_FOR_MAPPING_CONTAINER_COUNT_SQL  = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getTriplesForMappingContainerCount";
	private static String GET_TRIPLES_FOR_MAPPING_CONTAINER_AND_CODES_COUNT_SQL  = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getTriplesForMappingContainerAndCodesCount";
	private static String GET_MAP_AND_TERMS_FOR_MAPPING_CONTAINER_AND_REFERENCES  = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getFullMapOfTerminologyWithEntityNames";
	
	private static String GET_CODE_MAPPING_PARTICIPATION_COUNT_SQL  = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getCodeMappingParticipationCount";
	
	private static String GET_TRANSITIVE_TABLE_COUNT_SQL  = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getTransitiveTableCount";
	private static String DELETE_FROM_TRANSITIVE_TABLE_BY_CODINGSCHEME_UID_SQL  = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "deleteFromTransitiveTableByCodingSchemeUid";
	private static final String GET_VALID_TRIPLES_FOR_ASSOCIATION_UID_SQL =  IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getValidTriplesForAssociationPredicateGuid";

	private static final String VALIDATE_NODE_FOR_ASSOCIATION = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "validateNodeInAssociation";
	private static final String GET_VALID_PREDICATES_FOR_TARGET_AND_SOURCEOF = IbatisAssociationDao.ASSOCIATION_NAMESPACE + "getValidAssociationPredicatesForTargetOrSourceOf";
	
	@Override
	public int getTransitiveTableCount(String codingSchemeUid){
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameter bean = new PrefixedParameter(prefix, codingSchemeUid);
		
		return (Integer) 
			this.getSqlMapClientTemplate().queryForObject(GET_TRANSITIVE_TABLE_COUNT_SQL, bean);
	}
	
	@Override
	public int deleteFromTransitiveTableByCodingSchemeUid(String codingSchemeUid){
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameter bean = new PrefixedParameter(prefix, codingSchemeUid);
		
		return (Integer) 
			this.getSqlMapClientTemplate().delete(
					DELETE_FROM_TRANSITIVE_TABLE_BY_CODINGSCHEME_UID_SQL, bean);
	}
	
	@Override
	public List<ConceptReference> getConceptReferencesContainingObject(
			String codingSchemeUid,
			String relationsContainerName,
			List<ConceptReference> objects, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSubjectCodes,
			List<String> mustHaveSubjectNamespace,
			List<String> mustHaveSubjectEntityType,
			Boolean restrictToAnonymous, 
			List<Sort> sorts,
			int start,
			int pageSize) {
		return this.doGetConceptReferences(
				codingSchemeUid, 
				relationsContainerName, 
				objects, 
				associationNames, 
				associationQualifiers, 
				mustHaveSubjectCodes, 
				mustHaveSubjectNamespace, 
				mustHaveSubjectEntityType, 
				restrictToAnonymous, 
				TripleNode.OBJECT,
				start,
				pageSize);
	}

	@Override
	public List<ConceptReference> getConceptReferencesContainingSubject(
			String codingSchemeUid, 
			String relationsContainerName,
			List<ConceptReference> subjects, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveObjectCodes,
			List<String> mustHaveObjectNamespace,
			List<String> mustHaveObjectEntityType, 
			Boolean restrictToAnonymous,
			List<Sort> sorts, 
			int start,
			int pageSize) {
		return this.doGetConceptReferences(
				codingSchemeUid, 
				relationsContainerName, 
				subjects, 
				associationNames, 
				associationQualifiers, 
				mustHaveObjectCodes, 
				mustHaveObjectNamespace, 
				mustHaveObjectEntityType, 
				restrictToAnonymous, 
				TripleNode.SUBJECT,
				start,
				pageSize);
	}
	
	@SuppressWarnings("unchecked")
	protected List<ConceptReference> doGetConceptReferences(
			String codingSchemeUid, 
			String relationsContainerName,
			List<ConceptReference> conceptReferences, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveCodes,
			List<String> mustHaveNamespace,
			List<String> mustHaveEntityType, 
			Boolean restrictToAnonymous, 
			TripleNode tripleNode,
			int start,
			int pageSize){
		
		if(pageSize < 0) {
			pageSize = Integer.MAX_VALUE;
		}
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		GetCountConceptReferenceBean bean = new GetCountConceptReferenceBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeUid(codingSchemeUid);
		bean.setRelationsContainerName(relationsContainerName);
		bean.setConceptReferences(conceptReferences);
		bean.setAssociations(associationNames);
		bean.setAssociationQualifiers(associationQualifiers);
		bean.setMustHaveCodes(mustHaveCodes);
		bean.setMustHaveNamespaces(mustHaveNamespace);
		bean.setMustHaveEntityTypes(mustHaveEntityType);
		bean.setRestrictToAnonymous(restrictToAnonymous);
		bean.setTripleNode(tripleNode);
		
		return this.getSqlMapClientTemplate().queryForList(GET_CONCEPTREFERENCES_SQL, bean, start, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<String> listCodeRelationships(
			String codingSchemeUid,
			String relationsContainerName,
			String sourceEntityCode, String sourceEntityCodeNamespace,
			String targetEntityCode, String targetEntityCodeNamespace,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSourceCodes,
			List<CodeNamespacePair> mustHaveTargetCodes,
			List<String> mustHaveSourceNamespace,
			List<String> mustHaveTargetNamespace,
			List<String> mustHaveEntityType,
			Boolean restrictToAnonymous,
			boolean useTransitive) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		GetCodeRelationshipsBean bean = new GetCodeRelationshipsBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeUid(codingSchemeUid);
		bean.setRelationsContainerName(relationsContainerName);
		bean.setSourceCode(sourceEntityCode);
		bean.setSourceNamespace(sourceEntityCodeNamespace);
		bean.setTargetCode(targetEntityCode);
		bean.setTargetNamespace(targetEntityCodeNamespace);
		bean.setAssociations(associationNames);
		bean.setAssociationQualifiers(associationQualifiers);
		bean.setMustHaveSourceCodes(mustHaveSourceCodes);
		bean.setMustHaveTargetCodes(mustHaveTargetCodes);
		bean.setMustHaveSourceNamespaces(mustHaveSourceNamespace);
		bean.setMustHaveTargetNamespaces(mustHaveTargetNamespace);
		bean.setMustHaveEntityTypes(mustHaveEntityType);
		bean.setRestrictToAnonymous(restrictToAnonymous);
		bean.setUseTransitive(useTransitive);
		
		return this.getSqlMapClientTemplate().queryForList(GET_CODE_RELATIONSHIPS_SQL, bean);
	}

	@Override
	@CacheMethod
	public Map<String,Integer> getTripleUidsContainingObjectCount(
			String codingSchemeUid,
			String relationsContainerName,
			String objectEntityCode,
			String objectEntityCodeNamespace,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSubjectCodes,
			List<String> mustHaveSubjectNamespace,
			List<String> mustHaveSubjectEntityType,
			Boolean restrictToAnonymous) {
		return this.doGetTripleUidsCount(
				codingSchemeUid, 
				relationsContainerName,
				objectEntityCode, 
				objectEntityCodeNamespace, 
				associationNames,
				associationQualifiers,
				mustHaveSubjectCodes,
				mustHaveSubjectNamespace,
				mustHaveSubjectEntityType,
				restrictToAnonymous,
				TripleNode.OBJECT);
	}

	@Override
	@CacheMethod
	public Map<String,Integer> getTripleUidsContainingSubjectCount(
			String codingSchemeUid, 
			String relationsContainerName,
			String subjectEntityCode, 
			String subjectEntityCodeNamespace,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveObjectCodes,
			List<String> mustHaveObjectNamespace,
			List<String> mustHaveObjectEntityType,
			Boolean restrictToAnonymous){
		return this.doGetTripleUidsCount(
				codingSchemeUid,  
				relationsContainerName,
				subjectEntityCode, 
				subjectEntityCodeNamespace, 
				associationNames,
				associationQualifiers,
				mustHaveObjectCodes,
				mustHaveObjectNamespace,
				mustHaveObjectEntityType,
				restrictToAnonymous,
				TripleNode.SUBJECT);
	}

	@SuppressWarnings("unchecked")
	protected Map<String,Integer>  doGetTripleUidsCount(
			String codingSchemeUid,
			String relationsContainerName,
			String entityCode,
			String entityCodeNamespace, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveCodes,
			List<String> mustHaveNamespace,
			List<String> mustHaveEntityType,
			Boolean restrictToAnonymous,
			TripleNode tripleNode) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		GetEntityAssnUidsCountBean bean = new GetEntityAssnUidsCountBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeUid(codingSchemeUid);
		bean.setRelationsContainerName(relationsContainerName);
		bean.setEntityCode(entityCode);
		bean.setEntityCodeNamespace(entityCodeNamespace);
		bean.setAssociations(associationNames);
		bean.setAssociationQualifiers(associationQualifiers);
		bean.setMustHaveCodes(mustHaveCodes);
		bean.setMustHaveNamespaces(mustHaveNamespace);
		bean.setMustHaveEntityTypes(mustHaveEntityType);
		bean.setRestrictToAnonymous(restrictToAnonymous);
		bean.setTripleNode(tripleNode);
		
		return (Map<String,Integer> ) this.getSqlMapClientTemplate().
			queryForMap(GET_ENTITY_ASSNSTOENTITY_UID_COUNT_SQL, bean, "key", "value");
	}
	
	@Override
	@CacheMethod
	public List<String> getTripleUidsContainingSubject(
			String codingSchemeUid,
			String associationPredicateUid,
			String entityCode,
			String entityCodeNamespace, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveObjectCodes,
			List<String> mustHaveObjectNamespace,
			List<String> mustHaveObjectEntityType,
			Boolean restrictToAnonymous,
			List<Sort> sorts,
			int start, 
			int pageSize){
		return this.doGetTripleUids(
				codingSchemeUid, 
				associationPredicateUid, 
				entityCode, 
				entityCodeNamespace, 
				associationQualifiers,
				mustHaveObjectCodes,
				mustHaveObjectNamespace,
				mustHaveObjectEntityType,
				restrictToAnonymous,
				TripleNode.SUBJECT,
				sorts,
				start, 
				pageSize);		
	}

	@Override
	@CacheMethod
	public List<String> getTripleUidsContainingObject(
			String codingSchemeUid,
			String associationPredicateUid,
			String entityCode,
			String entityCodeNamespace,
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSubjectCodes,
			List<String> mustHaveSubjectNamespace,
			List<String> mustHaveSubjectEntityType,
			Boolean restrictToAnonymous,
			List<Sort> sorts,
			int start, 
			int pageSize){
		return this.doGetTripleUids(
				codingSchemeUid, 
				associationPredicateUid, 
				entityCode, 
				entityCodeNamespace,
				associationQualifiers,
				mustHaveSubjectCodes,
				mustHaveSubjectNamespace,
				mustHaveSubjectEntityType,
				restrictToAnonymous,
				TripleNode.OBJECT, 
				sorts,
				start, 
				pageSize);
		
	}
	
	@SuppressWarnings("unchecked")
	protected List<String> doGetTripleUids(
			String codingSchemeUid,
			String associationPredicateUid,
			String entityCode,
			String entityCodeNamespace,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveCodes,
			List<String> mustHaveNamespaces,
			List<String> mustHaveEntityType,
			Boolean restrictToAnonymous,
			TripleNode tripleNode,
			List<Sort> sorts,
			int start, 
			int pageSize){
		
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		GetEntityAssnUidsBean bean = new GetEntityAssnUidsBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeUid(codingSchemeUid);
		bean.setAssociationPredicateUid(associationPredicateUid);
		bean.setEntityCode(entityCode);
		bean.setEntityCodeNamespace(entityCodeNamespace);
		bean.setAssociationQualifiers(associationQualifiers);
		bean.setMustHaveCodes(mustHaveCodes);
		bean.setMustHaveNamespaces(mustHaveNamespaces);
		bean.setMustHaveEntityTypes(mustHaveEntityType);
		bean.setRestrictToAnonymous(restrictToAnonymous);
		bean.setTripleNode(tripleNode);
		bean.setSorts(sorts);

		if(pageSize < 0) {
			pageSize = Integer.MAX_VALUE;
		}
		return this.getSqlMapClientTemplate().
			queryForList(GET_ENTITY_ASSNSTOENTITY_UID_SQL, bean, start, pageSize);
	}
	
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createNonTypedList(this.supportedDatebaseVersion);
	}

	@Override
	@SuppressWarnings("unchecked")
	@CacheMethod
	public List<ConceptReference> getConceptReferencesFromUid(
			String codingSchemeUid, 
			List<String> tripleUids,
			TripleNode tripleNode,
			List<Sort> sorts) {
		if(CollectionUtils.isEmpty(tripleUids)) {
			return new ArrayList<ConceptReference>();
		}
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		SequentialMappedParameterBean bean = 
			new SequentialMappedParameterBean(tripleNode.toString(), tripleUids, sorts);
		bean.setPrefix(prefix);
		
		return this.getSqlMapClientTemplate().queryForList(
				GET_CONCEPTREFERENCE_FROM_ASSNSTOENTITY_UID_SQL, bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EntityReferencingAssociatedConcept> getAssociatedConceptsFromUid(
			String codingSchemeUid, 
			List<String> tripleUids, 
			List<Sort> sorts,
			TripleNode tripleNode) {
		if(CollectionUtils.isEmpty(tripleUids)) {
			return new ArrayList<EntityReferencingAssociatedConcept>();
		}
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		SequentialMappedParameterBean bean = 
			new SequentialMappedParameterBean(tripleNode.toString(), tripleUids, sorts, codingSchemeUid);
		bean.setPrefix(prefix);
		
		return this.getSqlMapClientTemplate().queryForList(
				GET_ASSOCIATEDCONCEPT_FROM_ASSNSTOENTITY_UID_SQL, bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<String> getAssociationPredicateNamesForCodingSchemeUid(
			String codingSchemeUid,
			String relationContainerName) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameterTuple bean = new PrefixedParameterTuple();
		bean.setPrefix(prefix);
		bean.setParam1(codingSchemeUid);
		bean.setParam2(relationContainerName);
		
		return this.getSqlMapClientTemplate().
			queryForList(GET_ASSOCIATION_PREDICATE_NAMES_SQL, 
					bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<Node> getDistinctSourceNodesForAssociationPredicate(
			String codingSchemeUid, String associationPredicateUid) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameter bean = new PrefixedParameter();
		bean.setPrefix(prefix);
		bean.setParam1(associationPredicateUid);
		
		return this.getSqlMapClientTemplate().
			queryForList(GET_DISTINCT_SOURCE_NODES_SQL, 
				bean);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<Node> getDistinctTargetNodesForAssociationPredicate(
			String codingSchemeUid, String associationPredicateUid) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameter bean = new PrefixedParameter();
		bean.setPrefix(prefix);
		bean.setParam1(associationPredicateUid);
		
		return this.getSqlMapClientTemplate().
			queryForList(GET_DISTINCT_TARGET_NODES_SQL, 
				bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<Node> getTargetNodesForSource(String codingSchemeUid,
			String associationPredicateUid, String sourceEntityCode,
			String sourceEntityCodeNamespace) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameterTriple bean = new PrefixedParameterTriple();
		bean.setPrefix(prefix);
		bean.setParam1(associationPredicateUid);
		bean.setParam2(sourceEntityCode);
		bean.setParam3(sourceEntityCodeNamespace);
		
		return this.getSqlMapClientTemplate().
			queryForList(GET_TARGET_NODES_OF_SOURCE_SQL, 
				bean);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<Node> getSourceNodesForTarget(String codingSchemeUid,
			String associationPredicateUid, String targetEntityCode,
			String targetEntityCodeNamespace) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		PrefixedParameterTriple bean = new PrefixedParameterTriple();
		bean.setPrefix(prefix);
		bean.setParam1(associationPredicateUid);
		bean.setParam2(targetEntityCode);
		bean.setParam3(targetEntityCodeNamespace);
		
		return this.getSqlMapClientTemplate().
			queryForList(GET_SOURCE_NODES_OF_TARGET_SQL, 
				bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<ConceptReference> getRootNodes(
			String codingSchemeUid,
			List<String> associationPredicateUids,
			List<QualifierNameValuePair> qualifiers, 
			List<String> mustHaveSubjectNamespace,
			List<String> mustHaveObjectNamespace,
			TraverseAssociations traverse,
			List<Sort> sorts, 
			int start, 
			int pageSize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		SequentialMappedParameterBean bean = new SequentialMappedParameterBean(
				traverse.toString(), 
				associationPredicateUids, 
				qualifiers,
				mustHaveSubjectNamespace,
				mustHaveObjectNamespace,
				sorts);
		bean.setPrefix(prefix);
		
		if(pageSize < 0) {
			pageSize = Integer.MAX_VALUE;
		}
	
		return this.getSqlMapClientTemplate().
			queryForList(GET_ROOT_ENTITY_ASSNSTOENTITY_UID_SQL, bean, start, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<ConceptReference> getTailNodes(
			String codingSchemeUid,
			List<String> associationPredicateUids, 
			List<QualifierNameValuePair> qualifiers, 
			List<String> mustHaveSubjectNamespace,
			List<String> mustHaveObjectNamespace,
			TraverseAssociations traverse,
			List<Sort> sorts, 
			int start, 
			int pageSize) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		SequentialMappedParameterBean bean = new SequentialMappedParameterBean(
				traverse.toString(), 
				associationPredicateUids, 
				qualifiers, 
				mustHaveSubjectNamespace, 
				mustHaveObjectNamespace,
				sorts);
		bean.setPrefix(prefix);
		
		if(pageSize < 0) {
			pageSize = Integer.MAX_VALUE;
		}
		
		return this.getSqlMapClientTemplate().
			queryForList(GET_TAIL_ENTITY_ASSNSTOENTITY_UID_SQL, bean, start, pageSize);
	}

	@Override
	public List<CountConceptReference> getCountConceptReferencesContainingObject(
			String codingSchemeUid, 
			String relationsContainerName,
			List<ConceptReference> objects, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveSubjectCodes,
			List<String> mustHaveSubjectNamespace,
			List<String> mustHaveObjectEntityType, 
			Boolean restrictToAnonymous) {
		return this.doGetCountConceptReferences(
				codingSchemeUid, 
				relationsContainerName, 
				objects, 
				associationNames, 
				associationQualifiers, 
				mustHaveSubjectCodes, 
				mustHaveSubjectNamespace, 
				mustHaveObjectEntityType, 
				restrictToAnonymous, 
				TripleNode.OBJECT);
	}

	@Override
	public List<CountConceptReference> getCountConceptReferencesContainingSubject(
			String codingSchemeUid, String relationsContainerName,
			List<ConceptReference> subjects, List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveObjectCodes,
			List<String> mustHaveObjectNamespace,
			List<String> mustHaveObjectEntityType, Boolean restrictToAnonymous) {
		return this.doGetCountConceptReferences(
				codingSchemeUid, 
				relationsContainerName, 
				subjects, 
				associationNames, 
				associationQualifiers, 
				mustHaveObjectCodes, 
				mustHaveObjectNamespace, 
				mustHaveObjectEntityType, 
				restrictToAnonymous, TripleNode.SUBJECT);
	}
	
	@SuppressWarnings("unchecked")
	protected List<CountConceptReference> doGetCountConceptReferences(
			String codingSchemeUid, 
			String relationsContainerName,
			List<ConceptReference> conceptReferences, 
			List<String> associationNames,
			List<QualifierNameValuePair> associationQualifiers,
			List<CodeNamespacePair> mustHaveCodes,
			List<String> mustHaveNamespace,
			List<String> mustHaveEntityType, 
			Boolean restrictToAnonymous, 
			TripleNode tripleNode){
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUid);
		
		GetCountConceptReferenceBean bean = new GetCountConceptReferenceBean();
		bean.setPrefix(prefix);
		bean.setCodingSchemeUid(codingSchemeUid);
		bean.setRelationsContainerName(relationsContainerName);
		bean.setConceptReferences(conceptReferences);
		bean.setAssociations(associationNames);
		bean.setAssociationQualifiers(associationQualifiers);
		bean.setMustHaveCodes(mustHaveCodes);
		bean.setMustHaveNamespaces(mustHaveNamespace);
		bean.setMustHaveEntityTypes(mustHaveEntityType);
		bean.setRestrictToAnonymous(restrictToAnonymous);
		bean.setTripleNode(tripleNode);
		
		return this.getSqlMapClientTemplate().queryForList(GET_COUNT_CONCEPTREFERENCES_SQL, bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<String> getTripleUidsForMappingRelationsContainer(
			String mappingCodingSchemeUid, 
			String sourceCodingSchemeUid,
			String targetCodingSchemeUid, 
			String relationsContainerName,
			List<Sort> sortList,
			int start, 
			int pageSize) {
		
		if(pageSize < 0) {
			pageSize = Integer.MAX_VALUE;
		}
		
		String mappingSchemePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(mappingCodingSchemeUid);
		String sourceSchemePrefix = null;
		String targetSchemePrefix = null;
		
		if(sourceCodingSchemeUid != null) {
			sourceSchemePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(sourceCodingSchemeUid);
		}
		
		if(targetCodingSchemeUid != null) {
			targetSchemePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(targetCodingSchemeUid);
		}
		
		MappingTripleParameterBean bean = new MappingTripleParameterBean(
				mappingSchemePrefix,
				mappingCodingSchemeUid,
				sourceCodingSchemeUid,
				sourceSchemePrefix,
				targetCodingSchemeUid,
				targetSchemePrefix,
				relationsContainerName,
				sortList);

		return this.getSqlMapClientTemplate().queryForList(GET_TRIPLE_UIDS_FOR_MAPPING_CONTAINER_SQL, bean, start, pageSize);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@CacheMethod
	public List<? extends ResolvedConceptReference> getTriplesForMappingRelationsContainer(
			String mappingCodingSchemeUid, 
			String sourceCodingSchemeUid,
			String targetCodingSchemeUid, 
			String relationsContainerName,
			List<String> tripleUids) {
		
		SequentialMappedParameterBean bean = getParameterBeanForGetTriples(
				mappingCodingSchemeUid,
				sourceCodingSchemeUid,
				targetCodingSchemeUid,
				relationsContainerName,
				tripleUids);


		List<TripleUidReferencingResolvedConceptReference> list = 
			this.getSqlMapClientTemplate().queryForList(GET_TRIPLES_FOR_MAPPING_CONTAINER_SQL, bean);
		
		return sortList(list, tripleUids);
	}
	
	private SequentialMappedParameterBean getParameterBeanForGetTriples(
			String mappingCodingSchemeUid,
			String sourceCodingSchemeUid,
			String targetCodingSchemeUid,
			String relationsContainerName,
			List<String> tripleUids){
		String mappingSchemePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(mappingCodingSchemeUid);
		String sourceSchemePrefix = null;
		String targetSchemePrefix = null;
		
		if(sourceCodingSchemeUid != null) {
			sourceSchemePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(sourceCodingSchemeUid);
		}
		
		if(targetCodingSchemeUid != null) {
			targetSchemePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(targetCodingSchemeUid);
		}
		
		SequentialMappedParameterBean bean = new SequentialMappedParameterBean(
				mappingCodingSchemeUid,
				sourceCodingSchemeUid,
				sourceSchemePrefix,
				targetCodingSchemeUid,
				targetSchemePrefix,
				relationsContainerName,
				tripleUids);

		
		bean.setPrefix(mappingSchemePrefix);
		
		return bean;
	}

	@Override
	@CacheMethod
	public int getTriplesForMappingRelationsContainerCount(
			String mappingCodingSchemeUid, 
			String relationsContainerName) {
		String mappingSchemePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(mappingCodingSchemeUid);
		
		SequentialMappedParameterBean bean = new SequentialMappedParameterBean(
				mappingCodingSchemeUid,
				relationsContainerName);

		bean.setPrefix(mappingSchemePrefix);
		
		return 
			(Integer) 
				this.getSqlMapClientTemplate().queryForObject(GET_TRIPLES_FOR_MAPPING_CONTAINER_COUNT_SQL, bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getTripleUidsForMappingRelationsContainerAndCodes(
			String mappingCodingSchemeUid, 
			String sourceCodingSchemeUid,
			String targetCodingSchemeUid, 
			String relationsContainerName,
			List<ConceptReference> sourceConceptReferences,
			List<ConceptReference> targetConceptReferences,
			List<ConceptReference> sourceOrTargetConceptReferences,
			List<Sort> sortList, 
			int start, 
			int pageSize) {
		
		String mappingSchemePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(mappingCodingSchemeUid);
		String sourceSchemePrefix = null;
		String targetSchemePrefix = null;
		
		if(sourceCodingSchemeUid != null) {
			sourceSchemePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(sourceCodingSchemeUid);
		}
		
		if(targetCodingSchemeUid != null) {
			targetSchemePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(targetCodingSchemeUid);
		}
		
		MappingTripleParameterBean bean = new RestrictingMappingTripleParameterBean(
				mappingSchemePrefix,
				mappingCodingSchemeUid,
				sourceCodingSchemeUid,
				sourceSchemePrefix,
				targetCodingSchemeUid,
				targetSchemePrefix,
				relationsContainerName,
				sourceConceptReferences,
				targetConceptReferences,
				sourceOrTargetConceptReferences,
				sortList);

		return this.getSqlMapClientTemplate().queryForList(GET_TRIPLE_UIDS_FOR_MAPPING_CONTAINER_AND_CODES_WITH_SORT_SQL, bean, start, pageSize);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getTripleUidsForMappingRelationsContainerAndCodes(
			String mappingCodingSchemeUid, 
			String relationsContainerName,
			List<ConceptReference> sourceConceptReferences,
			List<ConceptReference> targetConceptReferences,
			List<ConceptReference> sourceOrTargetConceptReferences) {
		
		String mappingSchemePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(mappingCodingSchemeUid);
		
		SequentialMappedParameterBean bean = new SequentialMappedParameterBean(
				mappingCodingSchemeUid,
				relationsContainerName,
				sourceConceptReferences, 
				targetConceptReferences,
				sourceOrTargetConceptReferences);
		
		bean.setPrefix(mappingSchemePrefix);

		return this.getSqlMapClientTemplate().queryForList(GET_TRIPLE_UIDS_FOR_MAPPING_CONTAINER_AND_CODES_NO_SORT_SQL, bean);
	}

	@Override
	public int getTriplesForMappingRelationsContainerAndCodesCount(
			String mappingCodingSchemeUid, 
			String relationsContainerName,
			List<ConceptReference> sourceConceptReferences,
			List<ConceptReference> targetConceptReferences,
			List<ConceptReference> sourceOrTargetConceptReferences) {
		String mappingSchemePrefix = this.getPrefixResolver().resolvePrefixForCodingScheme(mappingCodingSchemeUid);
		
		SequentialMappedParameterBean bean = new SequentialMappedParameterBean(
				mappingCodingSchemeUid,
				relationsContainerName,
				sourceConceptReferences,
				targetConceptReferences,
				sourceOrTargetConceptReferences);

		bean.setPrefix(mappingSchemePrefix);
		
		return 
			(Integer) 
				this.getSqlMapClientTemplate().queryForObject(GET_TRIPLES_FOR_MAPPING_CONTAINER_AND_CODES_COUNT_SQL, bean);
	}
	
	@SuppressWarnings("unchecked")
	@CacheMethod
	@Override
	public List<TerminologyMapBean> getMapAndTermsForMappingAndReferences(
			String mappingCodingSchemUid,
			String sourceCodingSchemeUid,
			String targetCodingSchemeUid,
			Relations rel,
			String qualifierName
			){
		String mappingSchemePrefix = this.getPrefixResolver().
				resolvePrefixForCodingScheme(mappingCodingSchemUid);

		String sourceSchemePrefix = this.getPrefixResolver().
				resolvePrefixForCodingScheme(sourceCodingSchemeUid);
		
		String targetSchemePrefix = this.getPrefixResolver().
				resolvePrefixForCodingScheme(targetCodingSchemeUid);
		
		SourceAndTargetMappingPrefixedParameter bean = new SourceAndTargetMappingPrefixedParameter(
				mappingSchemePrefix, 
				sourceSchemePrefix, 
				targetSchemePrefix, 
				qualifierName);

				return (List<TerminologyMapBean>) 
						this.getSqlMapClientTemplate().
						queryForList(
								GET_MAP_AND_TERMS_FOR_MAPPING_CONTAINER_AND_REFERENCES,
								bean);
		
	}
	
	@SuppressWarnings("unchecked")
	@CacheMethod
	@Override
	public List<Triple> getTriplesForMappingRelationsContainer(String mappingCodingSchemeUid,
			String relationsContainerName) {
		String mappingSchemePrefix = this.getPrefixResolver().
				resolvePrefixForCodingScheme(mappingCodingSchemeUid);
		PrefixedParameter bean = new PrefixedParameter();
		bean.setPrefix(mappingSchemePrefix);
//		bean.setParam1(mappingCodingSchemeUid);
		bean.setParam1(relationsContainerName);
		return (List<Triple>) 
				this.getSqlMapClientTemplate().
				queryForList(
						GET_MINIMAL_TRIPLES_FOR_MAPPING_CONTAINER_SQL,
						bean);
	}

	@CacheMethod
	@Override
	public boolean doesEntityParticipateInRelationships(
			String mappingCodingSchemeUid, 
			String relationsContainerName,
			String code,
			String namespace) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(mappingCodingSchemeUid);
		
		SequentialMappedParameterBean bean = new SequentialMappedParameterBean(
				mappingCodingSchemeUid,
				namespace, 
				code);
		
		bean.setPrefix(prefix);
		
		return 
			(Integer) 
				this.getSqlMapClientTemplate().queryForObject(GET_CODE_MAPPING_PARTICIPATION_COUNT_SQL, bean) > 0;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Triple> getValidTriplesOfAssociation(String codingSchemeUid, String assocUid) {
		String codingSchemePrefix = this.getPrefixResolver().
				resolvePrefixForCodingScheme(codingSchemeUid);
		PrefixedParameter bean = new PrefixedParameter();
		bean.setPrefix(codingSchemePrefix);
		bean.setParam1(assocUid);
		return (List<Triple>) 
				this.getSqlMapClientTemplate().
				queryForList(
						GET_VALID_TRIPLES_FOR_ASSOCIATION_UID_SQL,
						bean);
	}
	
	@Override
	public Integer validateNodeInAssociation(String codingSchemeUid, String assocUid, String entityCode) {
		String codingSchemePrefix = this.getPrefixResolver().
				resolvePrefixForCodingScheme(codingSchemeUid);
		PrefixedParameterTuple bean = new PrefixedParameterTuple();
		bean.setPrefix(codingSchemePrefix);
		bean.setParam1(assocUid);
		bean.setParam2(entityCode);
		return  
			(Integer) this.getSqlMapClientTemplate().queryForObject(VALIDATE_NODE_FOR_ASSOCIATION, bean);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getValidPredicatesForTargetandSourceOf(String codingSchemeUid, String entityCode) {
		String codingSchemePrefix = this.getPrefixResolver().
				resolvePrefixForCodingScheme(codingSchemeUid);
		PrefixedParameter bean = new PrefixedParameter();
		bean.setPrefix(codingSchemePrefix);
		bean.setParam1(entityCode);
		return  
			(List<String>) this.getSqlMapClientTemplate().queryForList(GET_VALID_PREDICATES_FOR_TARGET_AND_SOURCEOF, bean);
	}

	private List<? extends ResolvedConceptReference> sortList(List<TripleUidReferencingResolvedConceptReference> list, List<String> tripleUids){
		Map<String,ResolvedConceptReference> keyedMap = new HashMap<String,ResolvedConceptReference>();
		for(TripleUidReferencingResolvedConceptReference ref : list) {
			keyedMap.put(ref.getTripleUid(), ref);
		}

		List<ResolvedConceptReference> returnList = new ArrayList<ResolvedConceptReference>();
		for(String tripleUid : tripleUids) {
			returnList.add(keyedMap.get(tripleUid));
		}

		return returnList;
	}
	
	public static class RestrictingMappingTripleParameterBean extends MappingTripleParameterBean {
		
		private List<ConceptReference> sourceConceptReferences;
		private List<ConceptReference> targetConceptReferences;
		private List<ConceptReference> sourceOrTargetConceptReferences;
		
		public RestrictingMappingTripleParameterBean(
				String prefix,
				String mappingCodingSchemeUid,
				String sourceCodingSchemeUid, 
				String sourceSchemePrefix,
				String targetCodingSchemeUid, 
				String targetSchemePrefix,
				String relationsContainerName, 
				List<ConceptReference> sourceConceptReferences,
				List<ConceptReference> targetConceptReferences,
				List<ConceptReference> sourceOrTargetConceptReferences,
				List<Sort> sortList){
			super(  prefix,
					mappingCodingSchemeUid,
					sourceCodingSchemeUid, 
					sourceSchemePrefix,
					targetCodingSchemeUid, 
					targetSchemePrefix,
					relationsContainerName, 
					sortList);
			
			this.sourceConceptReferences = sourceConceptReferences;
			this.targetConceptReferences = targetConceptReferences;
			this.sourceOrTargetConceptReferences = sourceOrTargetConceptReferences;
		}

		public List<ConceptReference> getSourceConceptReferences() {
			return sourceConceptReferences;
		}

		public void setSourceConceptReferences(
				List<ConceptReference> sourceConceptReferences) {
			this.sourceConceptReferences = sourceConceptReferences;
		}

		public List<ConceptReference> getTargetConceptReferences() {
			return targetConceptReferences;
		}

		public void setTargetConceptReferences(
				List<ConceptReference> targetConceptReferences) {
			this.targetConceptReferences = targetConceptReferences;
		}

		public List<ConceptReference> getSourceOrTargetConceptReferences() {
			return sourceOrTargetConceptReferences;
		}

		public void setSourceOrTargetConceptReferences(
				List<ConceptReference> sourceOrTargetConceptReferences) {
			this.sourceOrTargetConceptReferences = sourceOrTargetConceptReferences;
		}
	}
	
	public static class MappingTripleParameterBean extends PrefixedTableParameterBean {

		private static final long serialVersionUID = -8564556798840145817L;

		String mappingCodingSchemeUid;
		String sourceCodingSchemeUid;
		String sourceSchemePrefix;
		String targetCodingSchemeUid;
		String targetSchemePrefix;
		String relationsContainerName;
		List<Sort> sortList;

		public MappingTripleParameterBean(
				String prefix,
				String mappingCodingSchemeUid,
				String sourceCodingSchemeUid, 
				String sourceSchemePrefix,
				String targetCodingSchemeUid, 
				String targetSchemePrefix,
				String relationsContainerName, 
				List<Sort> sortList) {
			super(prefix);
			this.mappingCodingSchemeUid = mappingCodingSchemeUid;
			this.sourceCodingSchemeUid = sourceCodingSchemeUid;
			this.sourceSchemePrefix = sourceSchemePrefix;
			this.targetCodingSchemeUid = targetCodingSchemeUid;
			this.targetSchemePrefix = targetSchemePrefix;
			this.relationsContainerName = relationsContainerName;
			this.sortList = sortList;
		}
		public String getMappingCodingSchemeUid() {
			return mappingCodingSchemeUid;
		}
		public void setMappingCodingSchemeUid(String mappingCodingSchemeUid) {
			this.mappingCodingSchemeUid = mappingCodingSchemeUid;
		}
		public String getSourceCodingSchemeUid() {
			return sourceCodingSchemeUid;
		}
		public void setSourceCodingSchemeUid(String sourceCodingSchemeUid) {
			this.sourceCodingSchemeUid = sourceCodingSchemeUid;
		}
		public String getSourceSchemePrefix() {
			return sourceSchemePrefix;
		}
		public void setSourceSchemePrefix(String sourceSchemePrefix) {
			this.sourceSchemePrefix = sourceSchemePrefix;
		}
		public String getTargetCodingSchemeUid() {
			return targetCodingSchemeUid;
		}
		public void setTargetCodingSchemeUid(String targetCodingSchemeUid) {
			this.targetCodingSchemeUid = targetCodingSchemeUid;
		}
		public String getTargetSchemePrefix() {
			return targetSchemePrefix;
		}
		public void setTargetSchemePrefix(String targetSchemePrefix) {
			this.targetSchemePrefix = targetSchemePrefix;
		}
		public String getRelationsContainerName() {
			return relationsContainerName;
		}
		public void setRelationsContainerName(String relationsContainerName) {
			this.relationsContainerName = relationsContainerName;
		}
		public List<Sort> getSortList() {
			return sortList;
		}
		public void setSortList(List<Sort> sortList) {
			this.sortList = sortList;
		}
	}


}