package org.lexevs.dao.database.service.valuesets;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.service.AbstractDatabaseService;

public class VersionableEventVSPropertyService extends AbstractDatabaseService implements VSPropertyService{

	@Override
	public void insertValueSetDefinitionProperty(String valueSetDefinitionUri,
			Property property) {
		String valueSetDefGuid = this.getDaoManager().getCurrentValueSetDefinitionDao().getGuidFromvalueSetDefinitionURI(valueSetDefinitionUri);
		
		this.getDaoManager().getCurrentVsPropertyDao().insertProperty(valueSetDefGuid, ReferenceType.VALUESETDEFINITION, property);
	}

	@Override
	public void updateValueSetDefinitionProperty(String valueSetDefinitionUri,
			Property property) {
	
		String valueSetDefUId = this.getDaoManager()
				.getCurrentValueSetDefinitionDao()
				.getGuidFromvalueSetDefinitionURI(valueSetDefinitionUri);
		
		updateProperty(property, valueSetDefUId, ReferenceType.VALUESETDEFINITION);
	}

	@Override
	public void removeValueSetDefinitionProperty(String valueSetDefinitionUri,
			Property property) {
	
		String valueSetDefUId = this.getDaoManager()
				.getCurrentValueSetDefinitionDao()
				.getGuidFromvalueSetDefinitionURI(valueSetDefinitionUri);

		removeProperty(property, valueSetDefUId);
	}

	@Override
	public void insertValueSetDefPropVersionableChanges(
			String valueSetDefinitionUri, Property property) {
	
		String valueSetDefUId = this.getDaoManager()
				.getCurrentValueSetDefinitionDao()
				.getGuidFromvalueSetDefinitionURI(valueSetDefinitionUri);
	
		updateVersionableAttributes(property, valueSetDefUId, ReferenceType.VALUESETDEFINITION);
	}

	@Override
	public void insertPickListDefinitionProperty(String pickListId,
			Property property) {
		String pickListGuid = this.getDaoManager().getCurrentPickListDefinitionDao().getPickListGuidFromPickListId(pickListId);
		
		this.getDaoManager().getCurrentVsPropertyDao().insertProperty(pickListGuid, ReferenceType.PICKLISTDEFINITION, property);
		
	}

	@Override
	public void updatePickListDefinitionProperty(String pickListId,
			Property property) {

		String pickListDefUId = this.getDaoManager()
				.getCurrentPickListDefinitionDao()
				.getPickListGuidFromPickListId(pickListId);

		updateProperty(property, pickListDefUId,
				ReferenceType.PICKLISTDEFINITION);
	}

	@Override
	public void removePickListDefinitionProperty(String pickListId,
			Property property) {

		String pickListDefUId = this.getDaoManager()
				.getCurrentPickListDefinitionDao()
				.getPickListGuidFromPickListId(pickListId);

		removeProperty(property, pickListDefUId);
	}

	@Override
	public void insertPickListDefPropVersionableChanges(String pickListId,
			Property property) {

		String pickListUId = this.getDaoManager()
				.getCurrentPickListDefinitionDao()
				.getPickListGuidFromPickListId(pickListId);

		updateVersionableAttributes(property, pickListUId,
				ReferenceType.PICKLISTDEFINITION);

	}

	@Override
	public void insertPickListEntryNodeProperty(String pickListId,
			String pickListEntryNodeId, Property property) {
		String pickListEntryNodeGuid = this.getDaoManager().getCurrentPickListDefinitionDao().getPickListEntryNodeGuidByPickListIdAndPLEntryId(pickListId, pickListEntryNodeId);
		
		this.getDaoManager().getCurrentVsPropertyDao().insertProperty(pickListEntryNodeGuid, ReferenceType.PICKLISTENTRY, property);
		
	}

	@Override
	public void updatePickListEntryNodeProperty(String pickListId,
			String pickListEntryNodeId, Property property) {

		String pickListEntryNodeUId = this.getDaoManager()
				.getCurrentPickListDefinitionDao()
				.getPickListEntryNodeGuidByPickListIdAndPLEntryId(pickListId, pickListEntryNodeId);

		updateProperty(property, pickListEntryNodeUId, ReferenceType.PICKLISTENTRY);
	}

	@Override
	public void removePickListEntryNodeProperty(String pickListId,
			String pickListEntryNodeId, Property property) {
		
		String pickListDefUId = this.getDaoManager()
				.getCurrentPickListEntryNodeDao()
				.getPickListEntryNodeUId(pickListId, pickListEntryNodeId);

		removeProperty(property, pickListDefUId);
	}

	@Override
	public void insertPickListEntryNodePropVersionableChanges(
			String pickListId, String pickListEntryNodeId, Property property) {

		String pickListUId = this.getDaoManager()
				.getCurrentPickListEntryNodeDao().getPickListEntryNodeUId(
						pickListId, pickListEntryNodeId);

		updateVersionableAttributes(property, pickListUId,
				ReferenceType.PICKLISTENTRY);
	}

	@Override
	public void revisePickListDefinitionProperty(String pickListId,
			Property property) throws LBException {

		if (property == null)
			throw new LBParameterException("Property object is not supplied.");

		EntryState entryState = property.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		String revisionId = entryState.getContainingRevision();
		ChangeType changeType = entryState.getChangeType();

		String pickListDefUId = this.getDaoManager()
				.getCurrentPickListDefinitionDao()
				.getPickListGuidFromPickListId(pickListId);
		
		VSPropertyDao vsPropertyDao = this.getDaoManager().getCurrentVsPropertyDao();
		
		String propertyUId = vsPropertyDao.getPropertyGuidFromParentGuidAndPropertyId(pickListDefUId,
				property.getPropertyId());
		
		String propLatestRevisionId = vsPropertyDao.getLatestRevision(propertyUId);
		
		if (revisionId != null && changeType != null) {

			if (changeType == ChangeType.NEW) {
				if (entryState.getPrevRevision() != null) {
					throw new LBRevisionException(
							"Changes of type NEW are not allowed to have previous revisions.");
				}
			} else if (propertyUId == null) {
				throw new LBRevisionException(
						"The pick list definition property being revised doesn't exist.");
			} else if (entryState.getPrevRevision() == null) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (propLatestRevisionId != null
					&& !propLatestRevisionId.equalsIgnoreCase(entryState
							.getPrevRevision())) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. Previous revision id does not match with the latest revision id of the pick list definition property."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
			
			if (changeType == ChangeType.NEW) {

				this.insertPickListDefinitionProperty(pickListId,
						property);
			} else if (changeType == ChangeType.REMOVE) {

				this.removePickListDefinitionProperty(pickListId,
						property);
			} else if (changeType == ChangeType.MODIFY) {

				this.updatePickListDefinitionProperty(pickListId,
						property);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertPickListDefPropVersionableChanges(
						pickListId, property);
			}
		}
	}

	@Override
	public void revisePickListEntryNodeProperty(String pickListId,
			String pickListEntryNodeId, Property property) throws LBException {

		if (property == null)
			throw new LBParameterException("Property object is not supplied.");

		EntryState entryState = property.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		String revisionId = entryState.getContainingRevision();
		ChangeType changeType = entryState.getChangeType();
		
		String pickListEntryNodeUId = this.getDaoManager()
				.getCurrentPickListEntryNodeDao().getPickListEntryNodeUId(
						pickListId, pickListEntryNodeId);
		
		VSPropertyDao vsPropertyDao = this.getDaoManager().getCurrentVsPropertyDao();
		
		String propertyUId = vsPropertyDao.getPropertyGuidFromParentGuidAndPropertyId(pickListEntryNodeUId,
				property.getPropertyId());
		
		String propLatestRevisionId = vsPropertyDao.getLatestRevision(propertyUId);
		
		if (revisionId != null && changeType != null) {

			if (changeType == ChangeType.NEW) {
				if (entryState.getPrevRevision() != null) {
					throw new LBRevisionException(
							"Changes of type NEW are not allowed to have previous revisions.");
				}
			} else if (propertyUId == null) {
				throw new LBRevisionException(
						"The pick list entry node property being revised doesn't exist.");
			} else if (entryState.getPrevRevision() == null) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (propLatestRevisionId != null
					&& !propLatestRevisionId.equalsIgnoreCase(entryState
							.getPrevRevision())) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. Previous revision id does not match with the latest revision id of the pick list entry node property."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
			
			if (changeType == ChangeType.NEW) {

				this.insertPickListEntryNodeProperty(pickListId,
						pickListEntryNodeId, property);
			} else if (changeType == ChangeType.REMOVE) {

				this.removePickListEntryNodeProperty(pickListId,
						pickListEntryNodeId, property);
			} else if (changeType == ChangeType.MODIFY) {

				this.updatePickListEntryNodeProperty(pickListId,
						pickListEntryNodeId, property);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertPickListEntryNodePropVersionableChanges(pickListId,
						pickListEntryNodeId, property);
			}
		}
	}

	@Override
	public void reviseValueSetDefinitionProperty(String valueSetDefinitionUri,
			Property property) throws LBException {

		if (property == null)
			throw new LBParameterException("Property object is not supplied.");

		EntryState entryState = property.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}

		String revisionId = entryState.getContainingRevision();
		ChangeType changeType = entryState.getChangeType();

		String valueSetDefUId = this.getDaoManager()
				.getCurrentValueSetDefinitionDao()
				.getGuidFromvalueSetDefinitionURI(valueSetDefinitionUri);

		VSPropertyDao vsPropertyDao = this.getDaoManager()
				.getCurrentVsPropertyDao();

		String propertyUId = vsPropertyDao
				.getPropertyGuidFromParentGuidAndPropertyId(valueSetDefUId,
						property.getPropertyId());

		String propLatestRevisionId = vsPropertyDao
				.getLatestRevision(propertyUId);
		
		if (revisionId != null && changeType != null) {

			if (changeType == ChangeType.NEW) {
				if (entryState.getPrevRevision() != null) {
					throw new LBRevisionException(
							"Changes of type NEW are not allowed to have previous revisions.");
				}
			} else if (propertyUId == null) {
				throw new LBRevisionException(
						"The value set definition property being revised doesn't exist.");
			} else if (entryState.getPrevRevision() == null) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (propLatestRevisionId != null
					&& !propLatestRevisionId.equalsIgnoreCase(entryState
							.getPrevRevision())) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. " +
						"Previous revision id does not match with the latest revision id of the value set definition property. " + 
						"Please update the authoring instance with all the revisions and regenerate the source.");
			}

			if (changeType == ChangeType.NEW) {

				this.insertValueSetDefinitionProperty(valueSetDefinitionUri,
						property);
			} else if (changeType == ChangeType.REMOVE) {

				this.removeValueSetDefinitionProperty(valueSetDefinitionUri,
						property);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateValueSetDefinitionProperty(valueSetDefinitionUri,
						property);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.insertValueSetDefPropVersionableChanges(
						valueSetDefinitionUri, property);
			}
		}
	}

	private void updateProperty(Property property, String valueSetDefUId, ReferenceType type) {
		
		VSPropertyDao propertyDao = this.getDaoManager()
				.getCurrentVsPropertyDao();
	
		String propertyUId = propertyDao
				.getPropertyGuidFromParentGuidAndPropertyId(valueSetDefUId,
						property.getPropertyId());
	
		String prevEntryStateUId = propertyDao.insertHistoryProperty(
				valueSetDefUId, propertyUId, type, property);
	
		String entryStateUId = propertyDao.updateProperty(valueSetDefUId,
				propertyUId, type, property);
	
		this.getDaoManager().getCurrentVsEntryStateDao().insertEntryState(
				entryStateUId, propertyUId, type.name(), prevEntryStateUId,
				property.getEntryState());
	}

	private void updateVersionableAttributes(Property property,
			String parentUId, ReferenceType type) {
		
		VSPropertyDao propertyDao = this.getDaoManager()
				.getCurrentVsPropertyDao();
	
		String propertyUId = propertyDao
				.getPropertyGuidFromParentGuidAndPropertyId(parentUId,
						property.getPropertyId());
	
		String prevEntryStateUId = propertyDao.insertHistoryProperty(
				parentUId, propertyUId, type,
				property);
	
		String entryStateUId = propertyDao.updateVersionableAttributes(parentUId,
				propertyUId, type, property);
	
		this.getDaoManager().getCurrentVsEntryStateDao().insertEntryState(
				entryStateUId, propertyUId,
				type.name(), prevEntryStateUId,
				property.getEntryState());
	}

	private void removeProperty(Property property, String parentUId) {
		
		VSPropertyDao propertyDao = this.getDaoManager()
				.getCurrentVsPropertyDao();
		
		String propertyUId = propertyDao
				.getPropertyGuidFromParentGuidAndPropertyId(parentUId,
						property.getPropertyId());
	
		propertyDao.deletePropertyByUId(propertyUId);
	}
}
