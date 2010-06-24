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
package org.lexevs.dao.database.service.property;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.access.property.batch.PropertyBatchInsertItem;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.error.DatabaseErrorIdentifier;
import org.lexevs.dao.database.service.event.property.PropertyUpdateEvent;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * The Class VersionableEventPropertyService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @author <a href="mailto:rao.ramachandra@mayo.edu">Ramachandra Rao (satya)</a>
 */
public class VersionableEventPropertyService extends AbstractDatabaseService
		implements PropertyService {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.lexevs.dao.database.service.property.PropertyService#
	 * insertBatchEntityProperties(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_BATCH_PROPERTY_ERROR)
	public void insertBatchEntityProperties(String codingSchemeUri,
			String version, String entityCode, String entityCodeNamespace,
			List<Property> items) {

		String codingSchemeId = this.getCodingSchemeUId(codingSchemeUri,
				version);
		String entityId = this.getDaoManager().getEntityDao(codingSchemeUri,
				version).getEntityUId(codingSchemeId, entityCode,
				entityCodeNamespace);

		this.getDaoManager().getPropertyDao(codingSchemeUri, version)
				.insertBatchProperties(codingSchemeId, PropertyType.ENTITY,
						this.propertyListToBatchInsertList(entityId, items));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.lexevs.dao.database.service.property.PropertyService#
	 * insertCodingSchemeProperty(java.lang.String, java.lang.String,
	 * org.LexGrid.commonTypes.Property)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_CODINGSCHEME_PROPERTY_ERROR)
	public void insertCodingSchemeProperty(String codingSchemeUri,
			String version, Property property) {
		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		this.getDaoManager().getPropertyDao(codingSchemeUri, version)
				.insertProperty(codingSchemeUId, codingSchemeUId,
						PropertyType.CODINGSCHEME, property);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.lexevs.dao.database.service.property.PropertyService#
	 * insertCodingSchemePropertyVersionableChanges(java.lang.String,
	 * java.lang.String, org.LexGrid.commonTypes.Property)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_CODINGSCHEME_PROPERTY_VERSIONABLE_CHANGES_ERROR)
	public void insertCodingSchemePropertyVersionableChanges(
			String codingSchemeUri, String version, Property property) {

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		PropertyDao propertyDao = this.getDaoManager().getPropertyDao(
				codingSchemeUri, version);

		String propertyUId = propertyDao.getPropertyUIdByPropertyIdAndName(codingSchemeUId,
				codingSchemeUId, property.getPropertyId(), property
						.getPropertyName());

		String prevEntryStateUId = propertyDao.insertHistoryProperty(
				codingSchemeUId, propertyUId, property);

		this.getDaoManager().getPropertyDao(codingSchemeUri, version)
				.updatePropertyVersionableAttrib(codingSchemeUId,
						codingSchemeUId, propertyUId,
						PropertyType.CODINGSCHEME, property);

		this.getDaoManager().getVersionsDao(codingSchemeUri, version)
				.insertEntryState(
						codingSchemeUId,
						propertyUId, 
						EntryStateType.PROPERTY, 
						prevEntryStateUId,
						property.getEntryState());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.service.property.PropertyService#insertEntityProperty
	 * (java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * org.LexGrid.commonTypes.Property)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_ENTITY_PROPERTY_ERROR)
	public void insertEntityProperty(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace, Property property) {
		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);
		String entityUId = this.getDaoManager().getEntityDao(codingSchemeUri,
				version).getEntityUId(codingSchemeUId, entityCode,
				entityCodeNamespace);

		this.getDaoManager().getPropertyDao(codingSchemeUri, version)
				.insertProperty(codingSchemeUId, entityUId,
						PropertyType.ENTITY, property);
		
		this.firePropertyUpdateEvent(new PropertyUpdateEvent(
				codingSchemeUri,
				version, 
				entityCode,
				entityCodeNamespace, 
				property));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.lexevs.dao.database.service.property.PropertyService#
	 * insertEntityVersionableChanges(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, org.LexGrid.commonTypes.Property)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_ENTITY_PROPERTY_VERSIONABLE_CHANGES_ERROR)
	public void insertEntityPropertyVersionableChanges(String codingSchemeUri,
			String version, String entityCode, String entityCodeNamespace,
			Property property) {

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		String entityUId = this.getDaoManager().getEntityDao(codingSchemeUri,
				version).getEntityUId(codingSchemeUId, entityCode,
				entityCodeNamespace);

		PropertyDao propertyDao = this.getDaoManager().getPropertyDao(
				codingSchemeUri, version);

		String propertyUId = propertyDao
				.getPropertyUIdByPropertyIdAndName(codingSchemeUId, entityUId, property
						.getPropertyId(), property.getPropertyName());

		String prevEntryStateUId = propertyDao.insertHistoryProperty(
				codingSchemeUId, propertyUId, property);

		propertyDao.updatePropertyVersionableAttrib(codingSchemeUId, entityUId,
				propertyUId, PropertyType.ENTITY, property);

		this.getDaoManager().getVersionsDao(codingSchemeUri, version)
				.insertEntryState(
						codingSchemeUId,
						propertyUId,
						EntryStateType.PROPERTY,
						prevEntryStateUId, property.getEntryState());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.lexevs.dao.database.service.property.PropertyService#
	 * insertRelationProperty(java.lang.String, java.lang.String,
	 * java.lang.String, org.LexGrid.commonTypes.Property)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_RELATION_PROPERTY_ERROR)
	public void insertRelationProperty(String codingSchemeUri, String version,
			String relationContainerName, Property property) {

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);
		String relationUId = this.getDaoManager().getAssociationDao(
				codingSchemeUri, version).getRelationUId(codingSchemeUId,
				relationContainerName);

		this.getDaoManager().getPropertyDao(codingSchemeUri, version)
				.insertProperty(codingSchemeUId, relationUId,
						PropertyType.RELATION, property);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.lexevs.dao.database.service.property.PropertyService#
	 * insertRelationPropertyVersionableChanges(java.lang.String,
	 * java.lang.String, java.lang.String, org.LexGrid.commonTypes.Property)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=INSERT_RELATION_PROPERTY_VERSIONABLE_CHANGES_ERROR)
	public void insertRelationPropertyVersionableChanges(
			String codingSchemeUri, String version,
			String relationContainerName, Property property) {

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		String relationUId = this.getDaoManager().getAssociationDao(
				codingSchemeUri, version).getRelationUId(codingSchemeUId,
				relationContainerName);

		PropertyDao propertyDao = this.getDaoManager().getPropertyDao(
				codingSchemeUri, version);

		String propertyUId = propertyDao.getPropertyUIdByPropertyIdAndName(codingSchemeUId, 
				relationUId, property.getPropertyId(), property
						.getPropertyName());

		String prevEntryStateUId = propertyDao.insertHistoryProperty(
				codingSchemeUId, propertyUId, property);

		propertyDao.updatePropertyVersionableAttrib(codingSchemeUId,
				relationUId, propertyUId, PropertyType.RELATION, property);

		this.getDaoManager().getVersionsDao(codingSchemeUri, version)
				.insertEntryState(
						codingSchemeUId,
						propertyUId,
						EntryStateType.PROPERTY, 
						prevEntryStateUId,
						property.getEntryState());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.lexevs.dao.database.service.property.PropertyService#
	 * removeCodingSchemeProperty(java.lang.String, java.lang.String,
	 * org.LexGrid.commonTypes.Property)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=REMOVE_CODINGSCHEME_PROPERTY_ERROR)
	public void removeCodingSchemeProperty(String codingSchemeUri,
			String version, Property property) {

		CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(
				codingSchemeUri, version);

		PropertyDao propertyDao = getDaoManager().getPropertyDao(
				codingSchemeUri, version);

		VersionsDao versionsDao = getDaoManager().getVersionsDao(
				codingSchemeUri, version);

		String codingSchemeUId = codingSchemeDao
				.getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);

		String propertyUId = propertyDao.getPropertyUIdByPropertyIdAndName(codingSchemeUId,
				codingSchemeUId, property.getPropertyId(), property
						.getPropertyName());

		/* 1. Remove all entry state entries of property. */
		versionsDao.deleteAllEntryStateEntriesByEntryUId(codingSchemeUId,
				propertyUId);

		/* 2. Remove property. */
		propertyDao.removePropertyByUId(codingSchemeUId, propertyUId);

		/* 3. Remove search (lucene) indexes. */

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.service.property.PropertyService#removeEntityProperty
	 * (java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * org.LexGrid.commonTypes.Property)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=REMOVE_ENTITY_PROPERTY_ERROR)
	public void removeEntityProperty(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace, Property property) {

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		String entityUId = this.getDaoManager().getEntityDao(codingSchemeUri,
				version).getEntityUId(codingSchemeUId, entityCode,
				entityCodeNamespace);

		PropertyDao propertyDao = getDaoManager().getPropertyDao(
				codingSchemeUri, version);

		VersionsDao versionsDao = getDaoManager().getVersionsDao(
				codingSchemeUri, version);

		String propertyUId = propertyDao
				.getPropertyUIdByPropertyIdAndName(codingSchemeUId, entityUId, property
						.getPropertyId(), property.getPropertyName());

		/* 1. Remove all entry state entries of property. */
		versionsDao.deleteAllEntryStateEntriesByEntryUId(codingSchemeUId,
				propertyUId);

		/* 2. Remove property. */
		propertyDao.removePropertyByUId(codingSchemeUId, propertyUId);

		this.firePropertyUpdateEvent(new PropertyUpdateEvent(
				codingSchemeUri,
				version, 
				entityCode,
				entityCodeNamespace, 
				property));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.lexevs.dao.database.service.property.PropertyService#
	 * removeRelationProperty(java.lang.String, java.lang.String,
	 * java.lang.String, org.LexGrid.commonTypes.Property)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=REMOVE_RELATION_PROPERTY_ERROR)
	public void removeRelationProperty(String codingSchemeUri, String version,
			String relationContainerName, Property property) {

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		String relationUId = this.getDaoManager().getAssociationDao(
				codingSchemeUri, version).getRelationUId(codingSchemeUId,
				relationContainerName);

		PropertyDao propertyDao = getDaoManager().getPropertyDao(
				codingSchemeUri, version);

		VersionsDao versionsDao = getDaoManager().getVersionsDao(
				codingSchemeUri, version);

		String propertyUId = propertyDao.getPropertyUIdByPropertyIdAndName(codingSchemeUId,
				relationUId, property.getPropertyId(), property
						.getPropertyName());

		/* 1. Remove all entry state entries of property. */
		versionsDao.deleteAllEntryStateEntriesByEntryUId(codingSchemeUId,
				propertyUId);

		/* 2. Remove property. */
		propertyDao.removePropertyByUId(codingSchemeUId, propertyUId);

		/* 3. Remove search (lucene) indexes. */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.lexevs.dao.database.service.property.PropertyService#
	 * reviseCodingSchemeProperty(java.lang.String, java.lang.String,
	 * org.LexGrid.commonTypes.Property)
	 */
	@Override
	public void reviseCodingSchemeProperty(String codingSchemeUri,
			String version, Property property) throws LBException {
		
		if (validCodingSchemeRevision(codingSchemeUri, version, property)) {

			ChangeType changeType = property.getEntryState().getChangeType();

			if (changeType == ChangeType.NEW) {

				this.insertCodingSchemeProperty(codingSchemeUri, version,
						property);
			} else if (changeType == ChangeType.REMOVE) {

				this.removeCodingSchemeProperty(codingSchemeUri, version,
						property);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateCodingSchemeProperty(codingSchemeUri, version,
						property);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertCodingSchemePropertyVersionableChanges(
						codingSchemeUri, version, property);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.service.property.PropertyService#reviseEntityProperty
	 * (java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * org.LexGrid.commonTypes.Property)
	 */
	@Override
	public void reviseEntityProperty(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace, Property property)
			throws LBException {

		if( validEntityRevision(codingSchemeUri, version, entityCode, entityCodeNamespace, property)) {
			
			ChangeType changeType = property.getEntryState().getChangeType();

			if (changeType == ChangeType.NEW) {

				this.insertEntityProperty(codingSchemeUri, version, entityCode,
						entityCodeNamespace, property);
			} else if (changeType == ChangeType.REMOVE) {

				this.removeEntityProperty(codingSchemeUri, version, entityCode,
						entityCodeNamespace, property);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateEntityProperty(codingSchemeUri, version, entityCode,
						entityCodeNamespace, property);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertEntityPropertyVersionableChanges(codingSchemeUri,
						version, entityCode, entityCodeNamespace, property);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.lexevs.dao.database.service.property.PropertyService#
	 * reviseRelationProperty(java.lang.String, java.lang.String,
	 * java.lang.String, org.LexGrid.commonTypes.Property)
	 */
	@Override
	public void reviseRelationProperty(String codingSchemeUri, String version,
			String relationContainerName, Property property) throws LBException {

		if( validRelationRevision(codingSchemeUri, version, relationContainerName, property)) {
			
			ChangeType changeType = property.getEntryState().getChangeType();
			
			if (changeType == ChangeType.NEW) {

				this.insertRelationProperty(codingSchemeUri, version,
						relationContainerName, property);
			} else if (changeType == ChangeType.REMOVE) {

				this.removeRelationProperty(codingSchemeUri, version,
						relationContainerName, property);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateRelationProperty(codingSchemeUri, version,
						relationContainerName, property);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertRelationPropertyVersionableChanges(codingSchemeUri,
						version, relationContainerName, property);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.lexevs.dao.database.service.property.PropertyService#
	 * updateCodingSchemeProperty(java.lang.String, java.lang.String,
	 * org.LexGrid.commonTypes.Property)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=UPDATE_CODINGSCHEME_PROPERTY_ERROR)
	public void updateCodingSchemeProperty(String codingSchemeUri,
			String version, Property property) {
		String codingSchemeUId = this.getDaoManager().getCodingSchemeDao(
				codingSchemeUri, version).getCodingSchemeUIdByUriAndVersion(
				codingSchemeUri, version);

		String propertyUId = this.getDaoManager().getPropertyDao(
				codingSchemeUri, version).getPropertyUIdByPropertyIdAndName(codingSchemeUId,
				codingSchemeUId, property.getPropertyId(),
				property.getPropertyName());

		PropertyDao propertyDao = this.getDaoManager().getPropertyDao(
				codingSchemeUri, version);

		/* 1. Insert current property data into history. */
		String prevEntryStateUId = propertyDao.insertHistoryProperty(
				codingSchemeUId, propertyUId, property);

		/* 2. Update the property data */
		propertyDao.updateProperty(codingSchemeUId, codingSchemeUId,
				propertyUId, PropertyType.CODINGSCHEME, property);

		/* 3. Insert entryState details. */
		this.getDaoManager().getVersionsDao(codingSchemeUri, version)
				.insertEntryState(
						codingSchemeUId,
						propertyUId, 
						EntryStateType.PROPERTY, 
						prevEntryStateUId,
						property.getEntryState());

		/* 4. Update search (Lucene) indexes. */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexevs.dao.database.service.property.PropertyService#updateEntityProperty
	 * (java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * org.LexGrid.commonTypes.Property)
	 */
	@Transactional
	@Override
	@DatabaseErrorIdentifier(errorCode=UPDATE_ENTITY_PROPERTY_ERROR)
	public void updateEntityProperty(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace, Property property) {
		
		Assert.notNull(property);
		Assert.notNull(property.getPropertyId());
		Assert.notNull(property.getPropertyName());

		PropertyDao propertyDao = this.getDaoManager().getPropertyDao(
				codingSchemeUri, version);

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		String entityUId = this.getDaoManager().getEntityDao(codingSchemeUri,
				version).getEntityUId(codingSchemeUId, entityCode,
				entityCodeNamespace);

		String propertyUId = propertyDao
				.getPropertyUIdByPropertyIdAndName(codingSchemeUId, entityUId, property
						.getPropertyId(), property.getPropertyName());

		/* 1. Insert current property data into history. */
		String prevEntryStateUId = propertyDao.insertHistoryProperty(
				codingSchemeUId, propertyUId, property);

		/* 2. Update the property data */
		propertyDao.updateProperty(codingSchemeUId, entityUId, propertyUId,
				PropertyType.ENTITY, property);

		/* 3. Insert entryState details. */
		this.getDaoManager().getVersionsDao(codingSchemeUri, version)
				.insertEntryState(
						codingSchemeUId,
						propertyUId, 
						EntryStateType.PROPERTY, 
						prevEntryStateUId,
						property.getEntryState());

		this.firePropertyUpdateEvent(new PropertyUpdateEvent(
				codingSchemeUri,
				version, 
				entityCode,
				entityCodeNamespace, 
				property));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.lexevs.dao.database.service.property.PropertyService#
	 * updateRelationProperty(java.lang.String, java.lang.String,
	 * java.lang.String, org.LexGrid.commonTypes.Property)
	 */
	@Override
	@DatabaseErrorIdentifier(errorCode=UPDATE_ENTITY_PROPERTY_ERROR)
	public void updateRelationProperty(String codingSchemeUri, String version,
			String relationContainerName, Property property) {

		PropertyDao propertyDao = this.getDaoManager().getPropertyDao(
				codingSchemeUri, version);

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		String relationUId = this.getDaoManager().getAssociationDao(
				codingSchemeUri, version).getRelationUId(codingSchemeUId,
				relationContainerName);

		String propertyUId = propertyDao.getPropertyUIdByPropertyIdAndName(codingSchemeUId,
				relationUId, property.getPropertyId(), property
						.getPropertyName());

		/* 1. Insert current property data into history. */
		String prevEntryStateUId = propertyDao.insertHistoryProperty(
				codingSchemeUId, propertyUId, property);

		/* 2. Update the property data */
		propertyDao.updateProperty(codingSchemeUId, relationUId, propertyUId,
				PropertyType.RELATION, property);

		/* 3. Insert entryState details. */
		this.getDaoManager().getVersionsDao(codingSchemeUri, version)
				.insertEntryState(
						codingSchemeUId,
						propertyUId,
						EntryStateType.PROPERTY,
						prevEntryStateUId, property.getEntryState());

		/* 4. Update search (Lucene) indexes. */
	}

	/**
	 * Property list to batch insert list.
	 * 
	 * @param parentId
	 *            the parent id
	 * @param props
	 *            the props
	 * 
	 * @return the list< property batch insert item>
	 */
	protected List<PropertyBatchInsertItem> propertyListToBatchInsertList(
			String parentId, List<Property> props) {
		List<PropertyBatchInsertItem> returnList = new ArrayList<PropertyBatchInsertItem>();
		for (Property prop : props) {
			returnList.add(new PropertyBatchInsertItem(parentId, prop));
		}
		return returnList;
	}

	private boolean validCodingSchemeRevision(String codingSchemeUri,
			String version,Property property) throws LBException {
		
		String csUId = null;
		PropertyDao propertyDao = null; 
		String propertyUId = null;
		
		if (property == null)
			throw new LBParameterException("property is null.");

		EntryState entryState = property.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		try {
			csUId = this.getDaoManager().getCodingSchemeDao(
					codingSchemeUri, version)
					.getCodingSchemeUIdByUriAndVersion(codingSchemeUri,
							version);
		} catch (Exception e) {
			throw new LBRevisionException(
					"The coding scheme to which the property belongs to doesnt exist.");
		}

		propertyDao = this.getDaoManager().getPropertyDao(codingSchemeUri, version);

		propertyUId = propertyDao.getPropertyUIdByPropertyIdAndName(csUId,csUId,
				property.getPropertyId(), property.getPropertyName());


		ChangeType changeType = property.getEntryState().getChangeType();

		if (changeType == ChangeType.NEW) {
			if (entryState.getPrevRevision() != null) {
				throw new LBRevisionException(
						"Changes of type NEW are not allowed to have previous revisions.");
			}
			
			if (propertyUId != null) {
				throw new LBRevisionException(
						"The property being added already exist.");
			}
		} else {
			
			if (propertyUId == null) {
				throw new LBRevisionException(
						"The property being revised doesn't exist.");
			} 
			
			String propLatestRevisionId = propertyDao.getLatestRevision(csUId, propertyUId);
			
			String currentRevision = entryState.getContainingRevision();
			String prevRevision = entryState.getPrevRevision();
			
			if (entryState.getPrevRevision() == null 
					&& propLatestRevisionId != null
					&& !propLatestRevisionId.equals(currentRevision)) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (propLatestRevisionId != null
					&& !propLatestRevisionId.equals(currentRevision)
					&& !propLatestRevisionId.equals(prevRevision)) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. Previous revision id does not match with the latest revision id of the codingScheme property."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
		}
			
		return true;
	}
	
	private boolean validEntityRevision(String codingSchemeUri,
			String version, String entityCode, String entityCodeNamespace, Property property) throws LBException {
		
		String csUId = null;
		PropertyDao propertyDao = null;
		String entityUId = null;
		String propertyUId = null;
		
		if (property == null)
			throw new LBParameterException("property is null.");

		EntryState entryState = property.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		try {
			csUId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri,
					version).getCodingSchemeUIdByUriAndVersion(codingSchemeUri,
					version);
		} catch (Exception e) {
			throw new LBRevisionException(
			"The coding scheme to which the property belongs to doesn't exist.");
		}

		propertyDao = this.getDaoManager().getPropertyDao(codingSchemeUri,
				version);

		entityUId = this.getDaoManager().getEntityDao(codingSchemeUri,
				version).getEntityUId(csUId, entityCode,
						entityCodeNamespace);

		propertyUId = propertyDao.getPropertyUIdByPropertyIdAndName(csUId,
				entityUId, property.getPropertyId(), property
				.getPropertyName());
		
		ChangeType changeType = property.getEntryState().getChangeType();
		
		if (changeType == ChangeType.NEW) {
			if (entryState.getPrevRevision() != null) {
				throw new LBRevisionException(
						"Changes of type NEW are not allowed to have previous revisions.");
			}
			
			if( propertyUId != null ) {
				throw new LBRevisionException(
						"The property being revised already exist.");
			}
		} else {
			
			if (propertyUId == null) {
				throw new LBRevisionException(
						"The property being revised doesn't exist.");
			} 
			
			String propLatestRevisionId = propertyDao.getLatestRevision(csUId, propertyUId);
			
			String currentRevision = entryState.getContainingRevision();
			String prevRevision = entryState.getPrevRevision();
			
			if (entryState.getPrevRevision() == null
					&& propLatestRevisionId != null
					&& !propLatestRevisionId.equals(currentRevision)) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (propLatestRevisionId != null
					&& !propLatestRevisionId.equals(currentRevision)
					&& !propLatestRevisionId.equals(prevRevision)) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. "
								+ "Previous revision id does not match with the latest revision id of the codingScheme property."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
		}
			
		return true;
		
	}
	
	private boolean validRelationRevision(String codingSchemeUri,
			String version, String containerRelationName, Property property) throws LBException {
		
		String csUId = null;
		String relationUId = null;
		PropertyDao propertyDao = null;
		String propertyUId = null;
		
		if (property == null)
			throw new LBParameterException("property is null.");

		EntryState entryState = property.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		try { 
			csUId = this.getDaoManager().getCodingSchemeDao(codingSchemeUri,
					version).getCodingSchemeUIdByUriAndVersion(codingSchemeUri,
					version);
		} catch (Exception e) {
			throw new LBRevisionException(
					"The coding scheme to which the property belongs to doesn't exist.");
		}

		relationUId = this.getDaoManager().getAssociationDao(
				codingSchemeUri, version).getRelationUId(csUId,
						containerRelationName);

		propertyDao = this.getDaoManager().getPropertyDao(
				codingSchemeUri, version);

		propertyUId = propertyDao.getPropertyUIdByPropertyIdAndName(
				csUId, relationUId, property.getPropertyId(), property
				.getPropertyName());

		ChangeType changeType = property.getEntryState().getChangeType();

		if (changeType == ChangeType.NEW) {
			if (entryState.getPrevRevision() != null) {
				throw new LBRevisionException(
						"Changes of type NEW are not allowed to have previous revisions.");
			}
			
			if( propertyUId != null ) {
				throw new LBRevisionException(
						"The property being revised already exist.");
			}
		} else {
			
			if (propertyUId == null) {
				throw new LBRevisionException(
						"The property being revised doesn't exist.");
			}
			
			String propLatestRevisionId = propertyDao.getLatestRevision(csUId, propertyUId);
			
			String currentRevision = entryState.getContainingRevision();
			String prevRevision = entryState.getPrevRevision();
			
			if (entryState.getPrevRevision() == null
					&& propLatestRevisionId != null
					&& !propLatestRevisionId.equals(currentRevision)) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (propLatestRevisionId != null
					&& !propLatestRevisionId.equals(currentRevision)
					&& !propLatestRevisionId.equals(prevRevision)) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. "
								+ "Previous revision id does not match with the latest revision id of the codingScheme property."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
		}
		
		return true;
	}
}
