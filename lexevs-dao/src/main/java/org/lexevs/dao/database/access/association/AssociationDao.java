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
package org.lexevs.dao.database.access.association;

import java.util.List;

import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.access.association.batch.AssociationSourceBatchInsertItem;

/**
 * The Interface AssociationDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface AssociationDao extends LexGridSchemaVersionAwareDao {
	
	/**
	 * Insert association predicate.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param relationId the relation id
	 * @param associationPredicate the association predicate
	 * 
	 * @return the string
	 */
	public String insertAssociationPredicate(
			String codingSchemeId, 
			String relationId, 
			AssociationPredicate associationPredicate,
			boolean cascade);
	
	/**
	 * Insert association qualifier.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param associatableInstanceId the associatable instance id
	 * @param qualifier the qualifier
	 */
	public void insertAssociationQualifier(
			String codingSchemeId, 
			String associatableInstanceId, AssociationQualification qualifier);
	
	/**
	 * Gets the association predicate id.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param relationContainerId the relation container id
	 * @param associationPredicateName the association predicate name
	 * 
	 * @return the association predicate id
	 */
	public String getAssociationPredicateId(String codingSchemeId, String relationContainerId, String associationPredicateName);
	
	public String getAssociationPredicateNameForId(String codingSchemeId, String associationPredicateId);
	/**
	 * Insert association source.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param associationPredicateId the association predicate id
	 * @param source the source
	 */
	public void insertAssociationSource(String codingSchemeId, String associationPredicateId, AssociationSource source);

	/**
	 * Insert batch association sources.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param batch the batch
	 */
	public void insertBatchAssociationSources(String codingSchemeId,
			List<AssociationSourceBatchInsertItem> batch);
	
	/**
	 * Insert batch association sources.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param associationPredicateId the association predicate id
	 * @param batch the batch
	 */
	public void insertBatchAssociationSources(String codingSchemeId, String associationPredicateId,
			List<AssociationSource> batch);
	
	/**
	 * Insert relations.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param relations the relations
	 * 
	 * @return the string
	 */
	public String insertRelations(String codingSchemeId, Relations relations, boolean cascade);
	
	/**
	 * Gets the relations id.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param relationsName the relations name
	 * 
	 * @return the relations id
	 */
	public String getRelationsId(String codingSchemeId, String relationsName);

	/**
	 * Insert into transitive closure.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param associationPredicateId the association predicate id
	 * @param sourceEntityCode the source entity code
	 * @param sourceEntityCodeNamesapce the source entity code namesapce
	 * @param targetEntityCode the target entity code
	 * @param targetEntityCodeNamespace the target entity code namespace
	 * 
	 * @return the string
	 */
	public String insertIntoTransitiveClosure(
			String codingSchemeId, 
			String associationPredicateId,
			String sourceEntityCode, 
			String sourceEntityCodeNamesapce, 
			String targetEntityCode, 
			String targetEntityCodeNamespace);
}
