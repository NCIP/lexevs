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
		String pickListEntryNodeUId = this.getDaoManager()
				.getCurrentPickListEntryNodeDao().getPickListEntryNodeUId(
						pickListId, pickListEntryNodeId);
		
		this.getDaoManager().getCurrentVsPropertyDao().insertProperty(pickListEntryNodeUId, ReferenceType.PICKLISTENTRY, property);
		
	}

	@Override
	public void updatePickListEntryNodeProperty(String pickListId,
			String pickListEntryNodeId, Property property) {

		String pickListEntryNodeUId = this.getDaoManager()
				.getCurrentPickListEntryNodeDao().getPickListEntryNodeUId(
						pickListId, pickListEntryNodeId);

		updateProperty(property, pickListEntryNodeUId, ReferenceType.PICKLISTENTRY);
	}

	@Override
	public void removePickListEntryNodeProperty(String pickListId,
			String pickListEntryNodeId, Property property) {
		
		String pickListEntryNodeUId = this.getDaoManager()
				.getCurrentPickListEntryNodeDao().getPickListEntryNodeUId(
						pickListId, pickListEntryNodeId);

		removeProperty(property, pickListEntryNodeUId);
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

		if( validPickListDefinitionRevision(pickListId, property)) {
			
			ChangeType changeType = property.getEntryState().getChangeType();
			
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

		if( validPLEntryRevision(pickListId, pickListEntryNodeId, property)) {
			
			ChangeType changeType = property.getEntryState().getChangeType();
			
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

		if( validValueSetDefinitionRevision(valueSetDefinitionUri, property)) {
			
			ChangeType changeType = property.getEntryState().getChangeType();
			
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

	@Override
	public Property resolveValueSetDefinitionPropertyByRevision(String valueSetDefURI, String propertyId,
			String revisionId) throws LBRevisionException {
		
		String valueSetDefUId = this.getDaoManager()
				.getCurrentValueSetDefinitionDao()
				.getGuidFromvalueSetDefinitionURI(valueSetDefURI);
		
		VSPropertyDao vsPropertyDao = this.getDaoManager()
				.getCurrentVsPropertyDao();

		return vsPropertyDao.resolveVSPropertyByRevision(valueSetDefUId, propertyId, revisionId);
	}
	
	@Override
	public Property resolvePickListDefinitionPropertyByRevision(String pickListId, String propertyId,
			String revisionId) throws LBRevisionException {
		
		String pickListUId = this.getDaoManager()
				.getCurrentPickListDefinitionDao()
				.getPickListGuidFromPickListId(pickListId);
		
		VSPropertyDao vsPropertyDao = this.getDaoManager()
				.getCurrentVsPropertyDao();

		return vsPropertyDao.resolveVSPropertyByRevision(pickListUId, propertyId, revisionId);
	}

	@Override
	public Property resolvePickListEntryNodePropertyByRevision(String pickListId, String plEntryId, String propertyId,
			String revisionId) throws LBRevisionException {
		
		String pickListEntryNodeUId = this.getDaoManager()
				.getCurrentPickListEntryNodeDao().getPickListEntryNodeUId(
						pickListId, plEntryId);

		VSPropertyDao vsPropertyDao = this.getDaoManager()
				.getCurrentVsPropertyDao();

		return vsPropertyDao.resolveVSPropertyByRevision(pickListEntryNodeUId,
				propertyId, revisionId);
	}
	
	private void updateProperty(Property property, String parentUId, ReferenceType type) {
		
		VSPropertyDao propertyDao = this.getDaoManager()
				.getCurrentVsPropertyDao();
	
		String propertyUId = propertyDao
				.getPropertyGuidFromParentGuidAndPropertyId(parentUId,
						property.getPropertyId());
	
		String prevEntryStateUId = propertyDao.insertHistoryProperty(
				parentUId, propertyUId, type, property);
	
		String entryStateUId = propertyDao.updateProperty(parentUId,
				propertyUId, type, property);
	
		this.getDaoManager().getCurrentVsEntryStateDao().insertEntryState(
				entryStateUId, propertyUId, ReferenceType.VSPROPERTY.name(),
				prevEntryStateUId, property.getEntryState());
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
				ReferenceType.VSPROPERTY.name(), prevEntryStateUId,
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
	
	private boolean validValueSetDefinitionRevision(
			String valueSetDefinitionUri, Property property) throws LBException {
		
		String valueSetDefUId = null;
		
		if (property == null)
			throw new LBParameterException("Property object is not supplied.");
	
		try {
			valueSetDefUId = this.getDaoManager()
					.getCurrentValueSetDefinitionDao()
					.getGuidFromvalueSetDefinitionURI(valueSetDefinitionUri);
	
		} catch (Exception e) {
			throw new LBRevisionException(
					"The ValueSet definition to which the property belongs to doesn't exist.");
		}
		
		VSPropertyDao vsPropertyDao = this.getDaoManager()
				.getCurrentVsPropertyDao();
	
		String propertyUId = vsPropertyDao
				.getPropertyGuidFromParentGuidAndPropertyId(valueSetDefUId,
						property.getPropertyId());
	
		return validate(property, vsPropertyDao, propertyUId);
	}

	private boolean validPickListDefinitionRevision(String pickListId,
			Property property) throws LBException {
	
		String pickListDefUId = null;

		if (property == null)
			throw new LBParameterException("Property object is not supplied.");

		try {
			pickListDefUId = this.getDaoManager()
					.getCurrentPickListDefinitionDao()
					.getPickListGuidFromPickListId(pickListId);
		} catch (Exception e) {
			throw new LBRevisionException(
					"The PickList definition to which the property belongs to doesn't exist.");
		}

		VSPropertyDao vsPropertyDao = this.getDaoManager()
				.getCurrentVsPropertyDao();

		String propertyUId = vsPropertyDao
				.getPropertyGuidFromParentGuidAndPropertyId(pickListDefUId,
						property.getPropertyId());

		return validate(property, vsPropertyDao, propertyUId);
	}

	private boolean validPLEntryRevision(String pickListId,
			String pickListEntryNodeId, Property property) throws LBException {
		
		String pickListEntryNodeUId = null;

		if (property == null)
			throw new LBParameterException("Property object is not supplied.");

		try {
			pickListEntryNodeUId = this.getDaoManager()
					.getCurrentPickListEntryNodeDao().getPickListEntryNodeUId(
							pickListId, pickListEntryNodeId);

		} catch (Exception e) {
			throw new LBRevisionException(
					"The PickList entry node to which the property belongs to doesn't exist.");
		}

		VSPropertyDao vsPropertyDao = this.getDaoManager()
				.getCurrentVsPropertyDao();

		String propertyUId = vsPropertyDao
				.getPropertyGuidFromParentGuidAndPropertyId(
						pickListEntryNodeUId, property.getPropertyId());

		return validate(property, vsPropertyDao, propertyUId);
	}

	private boolean validate(Property property,
			VSPropertyDao vsPropertyDao, String propertyUId) throws LBException {

		EntryState entryState = property.getEntryState();

		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}
		
		ChangeType changeType = entryState.getChangeType();

		if (changeType == ChangeType.NEW) {
			if (entryState.getPrevRevision() != null) {
				throw new LBRevisionException(
						"Changes of type NEW are not allowed to have previous revisions.");
			}

			if (propertyUId != null) {
				throw new LBRevisionException(
						"The property being revised already exist.");
			}
		} else {

			if (propertyUId == null) {
				throw new LBRevisionException(
						"The property being revised doesn't exist.");
			}

			String propLatestRevisionId = vsPropertyDao
					.getLatestRevision(propertyUId);
			
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
								+ "Previous revision id does not match with the latest revision id of the pick list entry node property."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
		}

		return true;
	}
}