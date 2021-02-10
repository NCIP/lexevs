
package org.lexevs.dao.database.service.property;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.access.property.batch.PropertyBatchInsertItem;
import org.lexevs.dao.database.access.versions.VersionsDao;
import org.lexevs.dao.database.access.versions.VersionsDao.EntryStateType;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService;
import org.lexevs.dao.database.service.RevisableAbstractDatabaseService.ParentUidReferencingId;
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
public class VersionableEventPropertyService extends RevisableAbstractDatabaseService<Property,ParentUidReferencingId>
		implements PropertyService {
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#doInsertDependentChanges(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, org.LexGrid.commonTypes.Versionable)
	 */
	@Override
	protected void doInsertDependentChanges(
			ParentUidReferencingId id, Property revisedEntry)
			throws LBException {
		//
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#entryStateExists(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String)
	 */
	@Override
	protected boolean entryStateExists(ParentUidReferencingId id,
			String entryStateUid) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri,
				version);
		
		PropertyDao propertyDao = getDaoManager().getPropertyDao(codingSchemeUri, version);
	
		return propertyDao.entryStateExists(codingSchemeUid, entryStateUid);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getCurrentEntry(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String)
	 */
	@Override
	protected Property getCurrentEntry(ParentUidReferencingId id,
			String entryUId) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
	
		PropertyDao propertyDao = getDaoManager().getPropertyDao(codingSchemeUri, version);
	
		String codingSchemeId = this.getCodingSchemeUId(codingSchemeUri,
				version);
		
		return propertyDao.getPropertyByUid(codingSchemeId, entryUId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getCurrentEntryStateUid(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String)
	 */
	@Override
	protected String getCurrentEntryStateUid(
			ParentUidReferencingId id, String entryUid) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri,
				version);
		
		return this.getDaoManager().getPropertyDao(codingSchemeUri, version).
			getEntryStateUId(codingSchemeUid, entryUid);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getEntryUid(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, org.LexGrid.commonTypes.Versionable)
	 */
	@Override
	protected String getEntryUid(ParentUidReferencingId id,
			Property entry) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		PropertyDao propertyDao = getDaoManager().getPropertyDao(codingSchemeUri, version);
		
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri,
				version);
		
		return propertyDao.getPropertyUIdByPropertyIdAndName(
				codingSchemeUid, 
				id.getParentUid(), 
				entry.getPropertyId(), 
				entry.getPropertyName());
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#insertIntoHistory(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, org.LexGrid.commonTypes.Versionable, java.lang.String)
	 */
	@Override
	protected void insertIntoHistory(ParentUidReferencingId id,
			Property currentEntry, String entryUId) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		PropertyDao propertyDao = getDaoManager().getPropertyDao(codingSchemeUri, version);
		
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri,
				version);
		
		propertyDao.insertHistoryProperty(codingSchemeUid, entryUId, currentEntry);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#addDependentAttributesByRevisionId(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String, org.LexGrid.commonTypes.Versionable, java.lang.String)
	 */
	@Override
	protected Property addDependentAttributesByRevisionId(
			ParentUidReferencingId id, String entryUid, Property entry, String revisionId) {
		//no dependent attributes on a Property
		return entry;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getHistoryEntryByRevisionId(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String, java.lang.String)
	 */
	@Override
	protected Property getHistoryEntryByRevisionId(ParentUidReferencingId id,
			String entryUid, String revisionId) {
		String codingSchemeUid = this.getCodingSchemeUId(
				id.getCodingSchemeUri(),
				id.getCodingSchemeVersion()
				);
		return this.getPropertyDao(id).getHistoryPropertyByRevisionId(codingSchemeUid, entryUid, revisionId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#getLatestRevisionId(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String)
	 */
	@Override
	protected String getLatestRevisionId(ParentUidReferencingId id,
			String entryUId) {
		String codingSchemeUid = this.getCodingSchemeUId(
				id.getCodingSchemeUri(),
				id.getCodingSchemeVersion()
				);
		
		return this.getPropertyDao(id).getLatestRevision(codingSchemeUid, entryUId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.RevisableAbstractDatabaseService#updateEntryVersionableAttributes(org.lexevs.dao.database.service.RevisableAbstractDatabaseService.CodingSchemeUriVersionBasedEntryId, java.lang.String, org.LexGrid.commonTypes.Versionable)
	 */
	@Override
	protected String updateEntryVersionableAttributes(
			ParentUidReferencingId id, String entryUId,
			Property revisedEntity) {
		String codingSchemeUri = id.getCodingSchemeUri();
		String version = id.getCodingSchemeVersion();
		
		PropertyDao propertyDao = getDaoManager().getPropertyDao(codingSchemeUri, version);
		
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri,
				version);
		
		return propertyDao.updatePropertyVersionableAttrib(codingSchemeUid, entryUId, revisedEntity);
	}

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

		this.doInsertProperty(codingSchemeUri, version, codingSchemeUId, property, PropertyType.CODINGSCHEME);
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

		this.doInsertProperty(codingSchemeUri, version, entityUId, property, PropertyType.ENTITY);
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

		this.doInsertProperty(codingSchemeUri, version, relationUId, property, PropertyType.RELATION);
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

		String codingSchemeUId = 
			this.getCodingSchemeUId(codingSchemeUri, version);

		this.doRemoveProperty(codingSchemeUri, version, codingSchemeUId, property, PropertyType.CODINGSCHEME);

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

		this.doRemoveProperty(codingSchemeUri, version, entityUId, property, PropertyType.ENTITY);
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

		this.doRemoveProperty(codingSchemeUri, version, relationUId, property, PropertyType.RELATION);
	}
	
	/**
	 * Do insert property.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param parentUid the parent uid
	 * @param property the property
	 * @param propertyType the property type
	 */
	protected void doInsertProperty(
			String codingSchemeUri, 
			String version,
			final String parentUid,
			final Property property, 
			final PropertyType propertyType) {
		
		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		this.getDaoManager().getPropertyDao(codingSchemeUri, version)
			.insertProperty(codingSchemeUId, parentUid,
					propertyType, property);
		
		if( propertyType == PropertyType.ENTITY) {
			Entity entity = this.getDaoManager().getEntityDao(codingSchemeUri,
					version).getEntityByUId(codingSchemeUId, parentUid);
			
			if (entity != null) {
				
				this.firePostPropertyInsertEvent(new PropertyUpdateEvent(
						codingSchemeUri,
						version, 
						entity, 
						property));
			}
		}
		
	}
	
	/**
	 * Do remove property.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param parentUid the parent uid
	 * @param property the property
	 * @param propertyType the property type
	 */
	protected void doRemoveProperty(
			String codingSchemeUri, 
			String version,
			final String parentUid,
			final Property property, 
			final PropertyType propertyType) {

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri,
				version);

		PropertyDao propertyDao = getDaoManager().getPropertyDao(
				codingSchemeUri, version);

		VersionsDao versionsDao = getDaoManager().getVersionsDao(
				codingSchemeUri, version);

		String propertyUId = propertyDao
				.getPropertyUIdByPropertyIdAndName(codingSchemeUId, parentUid, property
						.getPropertyId(), property.getPropertyName());

		/* 1. Remove all entry state entries of property. */
		versionsDao.deleteAllEntryStateEntriesByEntryUId(codingSchemeUId,
				propertyUId);

		/* 2. Remove property. */
		propertyDao.removePropertyByUId(codingSchemeUId, propertyUId);
		
		/* 3. If propertyType is 'ENTITY', then update lucene indexes.*/
		if( propertyType == PropertyType.ENTITY) {
			Entity entity = this.getDaoManager().getEntityDao(codingSchemeUri,
					version).getEntityByUId(codingSchemeUId, parentUid);
			
			if (entity != null) {
				
				this.firePostPropertyRemoveEvent(new PropertyUpdateEvent(
						codingSchemeUri,
						version, 
						entity, 
						property));
			}
		}
	}

	/**
	 * Do revise property.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param parentUid the parent uid
	 * @param property the property
	 * @param propertyType the property type
	 * 
	 * @throws LBException the LB exception
	 */
	protected void doReviseProperty(
			String codingSchemeUri, 
			String version,
			final String parentUid,
			final Property property, 
			final PropertyType propertyType) throws LBException {
		
		if (validRevision(new ParentUidReferencingId(codingSchemeUri, version, parentUid), property)) {

			ChangeType changeType = property.getEntryState().getChangeType();

			if (changeType == ChangeType.NEW) {

				this.doInsertProperty(codingSchemeUri, version, parentUid, property, propertyType);
			} else if (changeType == ChangeType.REMOVE) {

				this.doRemoveProperty(codingSchemeUri, version, parentUid, property, propertyType);
			} else if (changeType == ChangeType.MODIFY) {

				this.doUpdateProperty(codingSchemeUri, version, parentUid, property, propertyType);
			} else if (changeType == ChangeType.VERSIONABLE) {
				
				ParentUidReferencingId id = new ParentUidReferencingId(codingSchemeUri, version, parentUid);
				
				this.insertVersionableChanges(id, property, EntryStateType.PROPERTY);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.property.PropertyService#resolvePropertiesOfCodingSchemeByRevision(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Property> resolvePropertiesOfCodingSchemeByRevision(
			String codingSchemeURI,
			String version,
			String revisionId){
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeURI,
				version);
		return this.doResolvePropertiesOfParentByRevision(codingSchemeURI, version, codingSchemeUid, revisionId);	
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.property.PropertyService#resolvePropertiesOfEntityByRevision(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Property> resolvePropertiesOfEntityByRevision(
			String codingSchemeURI,
			String version, 
			String entityCode, 
			String entityCodeNamespace,
			String revisionId){
		EntityDao entityDao = this.getDaoManager().getEntityDao(codingSchemeURI, version);
		
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeURI,
				version);
		
		String entityUid = entityDao.getEntityUId(codingSchemeUid, entityCode, entityCodeNamespace);

		return this.doResolvePropertiesOfParentByRevision(codingSchemeURI, version, entityUid, revisionId);		
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.property.PropertyService#resolvePropertiesOfRelationByRevision(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Property> resolvePropertiesOfRelationByRevision(
			String codingSchemeURI, String version, String relationsName,
			String revisionId) {
		AssociationDao associationDao = 
			this.getDaoManager().getAssociationDao(codingSchemeURI, version);
		
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeURI,
				version);
		
		String relationsUid = associationDao.getRelationUId(codingSchemeUid, relationsName);
		
		return this.doResolvePropertiesOfParentByRevision(codingSchemeURI, version, relationsUid, revisionId);		
	}

	/**
	 * Do resolve properties of parent by revision.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param parentUid the parent uid
	 * @param revisionId the revision id
	 * 
	 * @return the list< property>
	 */
	protected List<Property> doResolvePropertiesOfParentByRevision(
			String codingSchemeUri, 
			String version,
			String parentUid,
			String revisionId) {
		
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri,
				version);

		PropertyDao propertyDao = getDaoManager().getPropertyDao(
				codingSchemeUri, version);

		List<String> propertyUids = 
			propertyDao.getAllHistoryPropertyUidsOfParentByRevisionId(
				codingSchemeUid, parentUid, revisionId);
		
		List<Property> returnList = new ArrayList<Property>();
		
		for(String propertyUid : propertyUids) {
			Property prop;
			try {
				prop = this.resolveEntryByRevision(new ParentUidReferencingId(codingSchemeUri,version,parentUid), propertyUid, revisionId);
			} catch (LBRevisionException e) {
				throw new RuntimeException(e);
			}
			if(prop != null) {
				returnList.add(prop);
			}
		}
		
		return returnList;
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
		
		String parentUid = this.getCodingSchemeUId(codingSchemeUri, version);
		
		this.doReviseProperty(codingSchemeUri, version, parentUid, property, PropertyType.CODINGSCHEME);
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
		
		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, version);
		
		String parentUid = this.getDaoManager().getEntityDao(codingSchemeUri, version).
			getEntityUId(codingSchemeUid, entityCode, entityCodeNamespace);

		this.doReviseProperty(codingSchemeUri, version, parentUid, property, PropertyType.ENTITY);
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

		String codingSchemeUid = this.getCodingSchemeUId(codingSchemeUri, version);
		
		String parentUid = this.getDaoManager().getAssociationDao(
				codingSchemeUri, version).
					getRelationUId(
						codingSchemeUid,
						relationContainerName);

		this.doReviseProperty(codingSchemeUri, version, parentUid, property, PropertyType.RELATION);
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
		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri, version);

		try {
			this.doUpdateProperty(codingSchemeUri, version, codingSchemeUId, property, PropertyType.CODINGSCHEME);
		} catch (LBException e) {
			throw new RuntimeException(e);
		}
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
		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri, version);

		try {
			this.doUpdateProperty(codingSchemeUri, version, codingSchemeUId, property, PropertyType.ENTITY);
		} catch (LBException e) {
			throw new RuntimeException(e);
		}
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

		String codingSchemeUId = this.getCodingSchemeUId(codingSchemeUri, version);

		try {
			this.doUpdateProperty(codingSchemeUri, version, codingSchemeUId, property, PropertyType.RELATION);
		} catch (LBException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Do update property.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param parentUid the parent uid
	 * @param property the property
	 * @param propertyType the property type
	 * 
	 * @throws LBException the LB exception
	 */
	protected void doUpdateProperty(
			String codingSchemeUri, 
			String version,
			final String parentUid,
			final Property property, 
			final PropertyType propertyType) throws LBException {
		Assert.notNull(property);
		Assert.notNull(property.getPropertyId());
		Assert.notNull(property.getPropertyName());
		
		final ParentUidReferencingId id = 
			new ParentUidReferencingId(codingSchemeUri, version, parentUid);
		
		final PropertyDao propertyDao = getDaoManager().getPropertyDao(codingSchemeUri, version);
		final CodingSchemeDao codingSchemeDao = getDaoManager().getCodingSchemeDao(codingSchemeUri, version);
		
		final String codingSchemeUId = codingSchemeDao.
			getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
	
		final String propertyUid = getEntryUid(id, property);
		
		this.updateEntry(id, property, EntryStateType.PROPERTY, new UpdateTemplate() {

			@Override
			public String update() {
				
				return propertyDao.updateProperty(codingSchemeUId, parentUid, propertyUid, propertyType, property);
				
			}
		});
		
		if( propertyType == PropertyType.ENTITY) {
			Entity entity = this.getDaoManager().getEntityDao(codingSchemeUri,
					version).getEntityByUId(codingSchemeUId, parentUid);
			
			if (entity != null) {
				
				this.firePropertyUpdateEvent(new PropertyUpdateEvent(
						codingSchemeUri,
						version, 
						entity, 
						property));
			}
		}
	}

	/**
	 * Property list to batch insert list.
	 * 
	 * @param parentId the parent id
	 * @param props the props
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
	
	/**
	 * Gets the property dao.
	 * 
	 * @param id the id
	 * 
	 * @return the property dao
	 */
	private PropertyDao getPropertyDao(ParentUidReferencingId id) {
		return this.getDaoManager().getPropertyDao(id.getCodingSchemeUri(), id.getCodingSchemeVersion());
	}
}