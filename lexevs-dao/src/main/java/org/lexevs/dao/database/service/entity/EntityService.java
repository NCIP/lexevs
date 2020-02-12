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
package org.lexevs.dao.database.service.entity;

import java.sql.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationEntity;

/**
 * The Interface EntityService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface EntityService {
	
	/** The Constant INSERT_ENTITY_ERROR. */
	public static final String INSERT_ENTITY_ERROR = "INSERT-ENTITY-ERROR";
	
	/** The Constant INSERT_BATCH_ENTITY_ERROR. */
	public static final String INSERT_BATCH_ENTITY_ERROR = "INSERT-BATCH-ENTITY-ERROR";
	
	/** The Constant UPDATE_ENTITY_ERROR. */
	public static final String UPDATE_ENTITY_ERROR = "UPDATE-ENTITY-ERROR";
	
	/** The Constant REMOVE_ENTITY_ERROR. */
	public static final String REMOVE_ENTITY_ERROR = "REMOVE-ENTITY-ERROR";
	
	/** The Constant INSERT_ENTITY_VERSIONABLE_CHANGES_ERROR. */
	public static final String INSERT_ENTITY_VERSIONABLE_CHANGES_ERROR = "INSERT-ENTITY-VERSIONABLE-CHANGES-ERROR";
	
	/** The Constant INSERT_ENTITY_DEPENDENT_CHANGES_ERROR. */
	public static final String INSERT_ENTITY_DEPENDENT_CHANGES_ERROR = "INSERT-ENTITY-DEPENDENT-CHANGES-ERROR";
	
	/**
	 * Insert entity.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entity the entity
	 */
	public void insertEntity(
			String codingSchemeUri, 
			String version, 
			Entity entity);
	
	/**
	 * Gets the entity count.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the entity count
	 */
	public int getEntityCount(
			String codingSchemeUri, 
			String version);
	
	/**
	 * Gets the entity.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * 
	 * @return the entity
	 */
	public Entity getEntity(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace);
	
	/**
	 * Gets the entity.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param propertyNames the property names
	 * @param propertyTypes the property types
	 * 
	 * @return the entity
	 */
	public Entity getEntity(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			List<String> propertyNames,
			List<String> propertyTypes);
	
	/**
	 * Gets the association entity.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * 
	 * @return the association entity
	 */
	public AssociationEntity getAssociationEntity(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace);
	
	/**
	 * Gets the entities.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param start the start
	 * @param pageSize the page size
	 * 
	 * @return the entities
	 */
	public List<? extends Entity> getEntities(
			String codingSchemeUri, 
			String version, 
			int start,
			int pageSize);
	
	/**
	 * Gets the resolved coded node reference.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param resolve the resolve
	 * @param propertyNames the property names
	 * @param propertyTypes the property types
	 * 
	 * @return the resolved coded node reference
	 */
	public ResolvedConceptReference getResolvedCodedNodeReference(
			String codingSchemeUri, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			boolean resolve, 
			List<String> propertyNames,
			List<String> propertyTypes);

	/**
	 * Insert batch entities.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entities the entities
	 */
	public void insertBatchEntities(
			String codingSchemeUri, 
			String version,
			List<? extends Entity> entities);
	
	/**
	 * Update entity.
	 * 
	 * @param version the version
	 * @param entity the entity
	 * @param codingSchemeUri the coding scheme uri
	 * 
	 * @throws LBException the LB exception
	 */
	public void updateEntity(
			String codingSchemeUri, 
			String version, 
			Entity entity) throws LBException;
	
	/**
	 * Removes the entity.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entity the entity
	 * 
	 * @throws LBException the LB exception
	 */
	public void removeEntity(
			String codingSchemeUri, 
			String version,
			Entity entity) throws LBException;
	
	/**
	 * Revise.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param revisedEntity the revised entity
	 * 
	 * @throws LBException the LB exception
	 */
	public void revise(String codingSchemeUri, String version,
			Entity revisedEntity) throws LBException;
	
	/**
	 * Resolve entity by revision.
	 * 
	 * @param codingSchemeURI the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param revisionId the revision id
	 * 
	 * @return the entity
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	public Entity resolveEntityByRevision(String codingSchemeURI,
			String version, String entityCode, String entityCodeNamespace, String revisionId) throws LBRevisionException;
	
	/**
	 * Resolve entity by date.
	 * 
	 * @param codingSchemeURI the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param date the date
	 * 
	 * @return the entity
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	public Entity resolveEntityByDate(String codingSchemeURI,
			String version, String entityCode, String entityCodeNamespace,
			Date date) throws LBRevisionException;

	/**
	 * Gets the entity description.
	 * 
	 * @param codingSchemeURI the coding scheme uri
	 * @param version the version
	 * @param code the code
	 * @param codeNamespace the code namespace
	 * 
	 * @return the entity description
	 */
	public EntityDescription getEntityDescription(String codingSchemeURI, String version,
			String code, String codeNamespace);

	public String getEntityDescriptionAsString(String codingSchemeUri, String codingSchemeVersion, String entityCode,
			String entityCodeNamespace);
}