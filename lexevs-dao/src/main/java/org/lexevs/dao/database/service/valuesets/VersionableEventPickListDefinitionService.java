/**
 * 
 */
package org.lexevs.dao.database.service.valuesets;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListDefinitions;
import org.LexGrid.valueSets.PickListEntryNode;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.valuesets.PickListDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventPickListDefinitionService.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 *
 */
public class VersionableEventPickListDefinitionService extends AbstractDatabaseService implements
		PickListDefinitionService {

	VSPropertyService vsPropertyService = null;
	
	PickListEntryNodeService pickListEntryNodeService = null;
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#getPickListDefinitionByPickListId(java.lang.String)
	 */
	@Override
	public PickListDefinition getPickListDefinitionByPickListId(
			String pickListId) {
		return this.getDaoManager().getCurrentPickListDefinitionDao().getPickListDefinitionById(pickListId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#getPickListDefinitionIdForValueSetDefinitionUri(java.lang.String)
	 */
	@Override
	public List<String> getPickListDefinitionIdForValueSetDefinitionUri(
			String valueSetDefURI) {
		return this.getDaoManager().getCurrentPickListDefinitionDao().getPickListDefinitionIdForValueSetDefinitionURI(valueSetDefURI);
	}

	@Override
	public List<String> getPickListDefinitionIdForEntityReference(
			String entityCode, String entityCodeNameSpace, String propertyId) {
		return this.getDaoManager().getCurrentPickListDefinitionDao().getPickListDefinitionIdForEntityReference(entityCode, entityCodeNameSpace, propertyId);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#insertPickListDefinition(org.LexGrid.valueSets.PickListDefinition)
	 */
	@Transactional
//	@DatabaseErrorIdentifier(errorCode=INSERT_PICKLIST_ERROR)
	public void insertPickListDefinition(PickListDefinition definition, String systemReleaseUri, Mappings mappings) throws LBParameterException, LBException {
		SystemResourceService service = LexEvsServiceLocator.getInstance().getSystemResourceService();
		
		String pickListId = definition.getPickListId();
		if (service.containsPickListDefinitionResource(pickListId, null))
		{
			throw new LBException("Pick List definition with ID : " + pickListId + " ALREADY LOADED.");
		}
		
		// Register picklist id
		service.addPickListDefinitionResourceToSystem(definition.getPickListId(), null);
		
		PickListDao plDao = this.getDaoManager().getCurrentPickListDefinitionDao();
		// load pick list definition
		plDao.insertPickListDefinition(definition, systemReleaseUri, mappings);
		
//		this.fireCodingSchemeInsertEvent(definition);
	}
	
	@Transactional
//	@DatabaseErrorIdentifier(errorCode=INSERT_PICKLIST_ERROR)
	public void insertPickListDefinitions(PickListDefinitions definitions, String systemReleaseUri) {
		PickListDao plDao = this.getDaoManager().getCurrentPickListDefinitionDao();
		
		Mappings mappings = definitions.getMappings();
		
		for (PickListDefinition definition : definitions.getPickListDefinitionAsReference())
		{
			plDao.insertPickListDefinition(definition, systemReleaseUri, mappings);
		}
		
//		this.fireCodingSchemeInsertEvent(definition);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#listPickListIds()
	 */
	@Override
	public List<String> listPickListIds() {
		return this.getDaoManager().getCurrentPickListDefinitionDao().getPickListIds();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.PickListDefinitionService#removePickListDefinitionByPickListId(java.lang.String)
	 */
	@Override
	public void removePickListDefinitionByPickListId(String pickListId) {
		PickListDao plDao = this.getDaoManager().getCurrentPickListDefinitionDao();
		
		plDao.removePickListDefinitionByPickListId(pickListId);
	}

	@Override
	public List<String> getPickListDefinitionIdForSupportedTagAndValue(
			String supportedTag, String value) {
		return this.getDaoManager().getCurrentPickListDefinitionDao()
				.getPickListDefinitionIdForSupportedTagAndValue(supportedTag,
						value);
	}

	@Override
	public void removePickListDefinition(PickListDefinition definition) {

		removePickListDefinitionByPickListId(definition.getPickListId());
	}

	@Override
	public void updatePickListDefinition(PickListDefinition definition) throws LBException {

		String pickListId = definition.getPickListId();
		
		PickListDao pickListDefDao = this.getDaoManager().getCurrentPickListDefinitionDao();
		
		String pickListDefUId = pickListDefDao.getPickListGuidFromPickListId(pickListId);
		
		String entryStateUId = pickListDefDao.insertHistoryPickListDefinition(pickListDefUId, pickListId);
		
		String prevEntryStateUId = pickListDefDao.updatePickListDefinition(pickListDefUId, definition);
		
		this.getDaoManager().getCurrentVsEntryStateDao().insertEntryState(
				entryStateUId, pickListDefUId, ReferenceType.PICKLISTDEFINITION.name(),
				prevEntryStateUId, definition.getEntryState());
		
		this.insertDependentChanges(definition);
	}

	@Override
	public void updateVersionableAttributes(PickListDefinition definition) throws LBException {

		String pickListId = definition.getPickListId();
		
		PickListDao pickListDefDao = this.getDaoManager().getCurrentPickListDefinitionDao();
		
		String pickListDefUId = pickListDefDao.getPickListGuidFromPickListId(pickListId);
		
		String entryStateUId = pickListDefDao.insertHistoryPickListDefinition(pickListDefUId, pickListId);
		
		String prevEntryStateUId = pickListDefDao.updateVersionableAttributes(pickListDefUId, definition);
		
		this.getDaoManager().getCurrentVsEntryStateDao().insertEntryState(
				entryStateUId, pickListDefUId, ReferenceType.PICKLISTDEFINITION.name(),
				prevEntryStateUId, definition.getEntryState());
		
		this.insertDependentChanges(definition);
	}

	@Override
	public void insertDependentChanges(PickListDefinition definition) throws LBException {

		String pickListId = definition.getPickListId();
		
		PickListDao pickListDefDao = this.getDaoManager().getCurrentPickListDefinitionDao();
		
		String pickListDefUId = pickListDefDao.getPickListGuidFromPickListId(pickListId);
		
		/* 1. Insert EntryState entry.*/
		String prevEntryStateUId = pickListDefDao.getPickListEntryStateUId(pickListDefUId);
		
		String entryStateUId = this.getDaoManager().getCurrentVsEntryStateDao().insertEntryState(
				pickListDefUId, ReferenceType.PICKLISTDEFINITION.name(),
				prevEntryStateUId, definition.getEntryState());
		
		pickListDefDao.updateEntryStateUId(pickListDefUId, entryStateUId);
		
		/* 2. Revise dependent pickList Entry nodes.*/
		PickListEntryNode[] pickListNode = definition.getPickListEntryNode();
		
		for (int i = 0; i < pickListNode.length; i++) {
			
			pickListEntryNodeService.revise(pickListId, pickListNode[i]);
		}
		
		/* 3. Revise dependent pickList definition properties.*/
		if (definition.getProperties() != null) {

			Property[] propertyList = definition.getProperties().getProperty();

			for (int i = 0; i < propertyList.length; i++) {
				vsPropertyService.revisePickListDefinitionProperty(pickListId,
						propertyList[i]);
			}
		}
	}

	@Override
	public void revise(PickListDefinition pickListDefinition, Mappings mapping,
			String releaseURI) throws LBException {
	
		if (validRevision(pickListDefinition)) {
			ChangeType changeType = pickListDefinition.getEntryState()
					.getChangeType();

			if (changeType == ChangeType.NEW) {

				this.insertPickListDefinition(pickListDefinition, releaseURI,
						mapping);
			} else if (changeType == ChangeType.REMOVE) {

				this.removePickListDefinition(pickListDefinition);
			} else if (changeType == ChangeType.MODIFY) {

				this.updatePickListDefinition(pickListDefinition);
			} else if (changeType == ChangeType.DEPENDENT) {

				this.insertDependentChanges(pickListDefinition);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.updateVersionableAttributes(pickListDefinition);
			}
		}
	}

	/**
	 * @return the vsPropertyService
	 */
	public VSPropertyService getVsPropertyService() {
		return vsPropertyService;
	}

	/**
	 * @param vsPropertyService the vsPropertyService to set
	 */
	public void setVsPropertyService(VSPropertyService vsPropertyService) {
		this.vsPropertyService = vsPropertyService;
	}

	/**
	 * @return the pickListEntryNodeService
	 */
	public PickListEntryNodeService getPickListEntryNodeService() {
		return pickListEntryNodeService;
	}

	/**
	 * @param pickListEntryNodeService the pickListEntryNodeService to set
	 */
	public void setPickListEntryNodeService(
			PickListEntryNodeService pickListEntryNodeService) {
		this.pickListEntryNodeService = pickListEntryNodeService;
	}

	private boolean validRevision(PickListDefinition pickListDefinition) throws LBException {
		
		if(  pickListDefinition == null) 
			throw new LBParameterException("pickListDefinition is null.");
		
		EntryState entryState = pickListDefinition.getEntryState();
	
		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}
	
		ChangeType changeType = entryState.getChangeType();
	
		PickListDao pickListDao = this.getDaoManager()
				.getCurrentPickListDefinitionDao();

		String pickListDefUId = pickListDao
				.getPickListGuidFromPickListId(pickListDefinition
						.getPickListId());
		
		if (changeType == ChangeType.NEW) {
			if (entryState.getPrevRevision() != null) {
				throw new LBRevisionException(
						"Changes of type NEW are not allowed to have previous revisions.");
			}

			if (pickListDefUId != null) {
				throw new LBRevisionException(
						"The picklist definition being added already exist.");
			}

		} else {

			if (pickListDefUId == null) {
				throw new LBRevisionException(
						"The picklist definition being revised doesn't exist.");
			}

			String pickListDefLatestRevisionId = pickListDao
					.getLatestRevision(pickListDefUId);

			if (entryState.getPrevRevision() == null
					&& pickListDefLatestRevisionId != null) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (pickListDefLatestRevisionId != null
					&& !pickListDefLatestRevisionId.equalsIgnoreCase(entryState
							.getPrevRevision())) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. "
								+ "Previous revision id does not match with the latest revision id of the picklist definition."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
		}
			
		return true;
	}
}
