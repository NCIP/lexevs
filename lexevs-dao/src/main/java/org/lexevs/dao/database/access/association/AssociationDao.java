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
package org.lexevs.dao.database.access.association;

import java.util.List;

import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.access.association.batch.AssociationSourceBatchInsertItem;
import org.lexevs.dao.database.access.association.batch.TransitiveClosureBatchInsertItem;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;

/**
 * The Interface AssociationDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface AssociationDao extends LexGridSchemaVersionAwareDao {

	/**
	 * Insert association predicate.
	 * 
	 * @param codingSchemeUId
	 *            the coding scheme id
	 * @param relationUId
	 *            the relation id
	 * @param associationPredicate
	 *            the association predicate
	 * 
	 * @return the string
	 */
	public String insertAssociationPredicate(String codingSchemeUId,
			String relationUId, AssociationPredicate associationPredicate,
			boolean cascade);

	/**
	 * Insert association qualifier.
	 * 
	 * @param codingSchemeUId
	 *            the coding scheme id
	 * @param associatableInstanceUId
	 *            the associatable instance id
	 * @param qualifier
	 *            the qualifier
	 */
	public void insertAssociationQualifier(String codingSchemeUId,
			String associatableInstanceUId, AssociationQualification qualifier);

	public void deleteAssociationQualificationsByCodingSchemeUId(
			String codingSchemeUId);

	/**
	 * Gets the association predicate id.
	 * 
	 * @param codingSchemeUId
	 *            the coding scheme id
	 * @param relationContainerUId
	 *            the relation container id
	 * @param associationPredicateName
	 *            the association predicate name
	 * 
	 * @return the association predicate id
	 */
	public String getAssociationPredicateUIdByContainerUId(String codingSchemeUId,
			String relationContainerUId, String associationPredicateName);
	
	public String getAssociationPredicateUIdByContainerName(String codingSchemeUId,
			String relationContainerName, String associationPredicateName);
	
	public List<String> getAssociationPredicateUidsForAssociationName(
			String codingSchemeUid,
			String relationContainerName, 
			String associationPredicateName);

	public List<String> getAssociationPredicateUidsForDirectionalName(String codingSchemeId, String directionalName);

	public String getAssociationPredicateNameForUId(String codingSchemeUId,
			String associationPredicateUId);
	/**
	 * Insert association source.
	 * 
	 * @param codingSchemeUId
	 *            the coding scheme id
	 * @param associationPredicateUId
	 *            the association predicate id
	 * @param source
	 *            the source
	 */
	public void insertAssociationSource(String codingSchemeUId,
			String associationPredicateUId, AssociationSource source);

	/**
	 * Insert batch association sources.
	 * 
	 * @param codingSchemeUId
	 *            the coding scheme id
	 * @param batch
	 *            the batch
	 */
	public void insertBatchAssociationSources(String codingSchemeUId,
			List<AssociationSourceBatchInsertItem> batch);

	/**
	 * Insert batch association sources.
	 * 
	 * @param codingSchemeUId
	 *            the coding scheme id
	 * @param associationPredicateUId
	 *            the association predicate id
	 * @param batch
	 *            the batch
	 */
	public void insertBatchAssociationSources(String codingSchemeUId,
			String associationPredicateUId, List<AssociationSource> batch);

	/**
	 * Insert relations.
	 * 
	 * @param codingSchemeUId
	 *            the coding scheme id
	 * @param relations
	 *            the relations
	 * 
	 * @return the string
	 */
	public String insertRelations(String codingSchemeUId, Relations relations,
			boolean cascade);

	/**
	 * Gets the relations id.
	 * 
	 * @param codingSchemeUId
	 *            the coding scheme id
	 * @param relationsName
	 *            the relations name
	 * 
	 * @return the relations id
	 */
	public String getRelationUId(String codingSchemeUId, String relationsName);
	
	public String getNodesPath(String codingSchemeUid, String sourceCode, String sourceNS, String targetCode, String targetNS, String associationUid);
	public String getRelationEntryStateUId(String codingSchemeUId, String relationUId);

	public Relations getRelationsByUId(String codingSchemeUId,
			String relationsUId, Boolean getAssocPredicates);

	public AssociationPredicate getAssociationPredicateByUId(
			String codingSchemeUId, String associationPredicateUId);

	public List<String> getRelationsUIdsForCodingSchemeUId(
			String codingSchemeUId);
	
	public List<String> getRelationsNamesForCodingSchemeUId(
			String codingSchemeUId);

	public List<String> getAssociationPredicateUIdsForRelationsUId(
			String codingSchemeUId, String relationsUId);

	public List<Triple> getAllTriplesOfCodingScheme(String codingSchemeUId,
			String associationPredicateUId, int start, int pageSize);

	public List<GraphDbTriple> getAllGraphDbTriplesOfCodingScheme(String codingSchemeUId,
			String associationPredicateUId, int start, int pageSize);
	
	public String getRelationsContainerNameForAssociationInstanceId(String codingSchemeUId,
			String associationInstanceId);
	
	public String getAssociationPredicateNameForAssociationInstanceId(String codingSchemeUId,
			String associationInstanceId);
	
	/**
	 * Insert into transitive closure.
	 * 
	 * @param codingSchemeUId
	 *            the coding scheme id
	 * @param associationPredicateUId
	 *            the association predicate id
	 * @param sourceEntityCode
	 *            the source entity code
	 * @param sourceEntityCodeNamesapce
	 *            the source entity code namesapce
	 * @param targetEntityCode
	 *            the target entity code
	 * @param targetEntityCodeNamespace
	 *            the target entity code namespace
	 * 
	 * @return the string
	 */
	public String insertIntoTransitiveClosure(String codingSchemeUId,
			String associationPredicateUId, String sourceEntityCode,
			String sourceEntityCodeNamesapce, String targetEntityCode,
			String targetEntityCodeNamespace, String path);

	public String insertHistoryRelation(String codingSchemeUId,
			String relationUId, Relations relation);

	public String updateRelation(String codingSchemeUId, String relationUId,
			Relations relation);

	public void deleteAssociationQualificationsByRelationUId(
			String codingSchemeUId, String relationUId);

	public void removeRelationByUId(String codingSchemeUId, String relationUId);

	public String updateRelationVersionableChanges(String codingSchemeUId,
			String relationUId, Relations relation);

	public void updateRelationEntryStateUId(String codingSchemeUId,
			String relationUId, String entryStateUId);

	public String getRelationLatestRevision(String csUId, String relationUId);
	
	public void insertBatchTransitiveClosure(final String codingSchemeId,
			final List<TransitiveClosureBatchInsertItem> batch);

	public boolean entryStateExists(String codingSchemeUId, String entryStateUId);

	public Relations getHistoryRelationByRevisionId(String codingSchemeUid,
			String entryUid, String revisionId);

	String getAnonDesignationForPredicate(String codingSchemeId,
			String associationPredicateId);

	public List<String> getAllEntityAssocToEntityGuidsOfCodingScheme(
			String codingSchemeId, String associationPredicateId, int start,
			int pageSize);

	public List<GraphDbTriple> getAllGraphDbTriplesOfCodingScheme(
			String codingSchemeId,
			List<String> guids);

	List<GraphDbTriple> getAllAncestorTriplesTrOfCodingScheme(
			String codingSchemeId, String associationName, String code,
			int start, int pagesize);

	List<GraphDbTriple> getAllDescendantTriplesTrOfCodingScheme(
			String codingSchemeId, String associationName, String code,
			int start, int pagesize);

	public String getKeyForAssociationInstanceId(String codingSchemeIdInDb, String parentId);


}