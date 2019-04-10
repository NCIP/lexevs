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

import java.net.URI;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitions;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.access.revision.RevisionDao;
import org.lexevs.dao.database.access.valuesets.VSEntryStateDao;
import org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao;
import org.lexevs.dao.database.access.valuesets.VSPropertyDao.ReferenceType;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.version.VersionableEventAuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;

/**
 * The Class VersionableEventValueSetDefinitionService.
 * 
 */
public class VersionableEventValueSetDefinitionService extends AbstractDatabaseService implements ValueSetDefinitionService{

	/** The vs definition entry service. */
	VSDefinitionEntryService vsDefinitionEntryService = null;
	
	/** The vs property service. */
	VSPropertyService vsPropertyService = null;
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#getValueSetDefinitionURISForName(java.lang.String)
	 */
	@Override
	public List<String> getValueSetDefinitionURISForName(String valueSetDefinitionName) throws LBException {
		return this.getDaoManager().getCurrentValueSetDefinitionDao().getValueSetDefinitionURIsForName(valueSetDefinitionName);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#getValueSetDefinitionByUri(java.net.URI)
	 */
	@Override
	public ValueSetDefinition getValueSetDefinitionByUri(URI uri) {
		return this.getDaoManager().getCurrentValueSetDefinitionDao().getValueSetDefinitionByURI(uri.toString());
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#getValueSetDefinitionByUri(java.net.URI)
	 */
	@Override
	public Map<String,ValueSetDefinition> getValueSetDefinitionsByResgistryEntry() {
		Map<String,ValueSetDefinition> map = this.getDaoManager().getCurrentValueSetDefinitionDao().getValueSetURIMapToDefinitions();
		return map;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#insertValueSetDefinition(org.LexGrid.valueSets.ValueSetDefinition, java.lang.String, org.LexGrid.naming.Mappings)
	 */
	@Override
	public void insertValueSetDefinition(ValueSetDefinition definition,
			String systemReleaseUri, Mappings mappings) throws LBException {
		
		SystemResourceService service = LexEvsServiceLocator.getInstance().getSystemResourceService();
		String uri = definition.getValueSetDefinitionURI();
		
		// STOP if value set definition already loaded
		if (service.containsValueSetDefinitionResource(uri, null))
		{
			throw new LBException("Value Set definition with URI : " + uri + " ALREADY LOADED.");
		}
		
		// Register the value set definition uri
		service.addValueSetDefinitionResourceToSystem(uri, null);
		
		ValueSetDefinitionDao vsdDao = this.getDaoManager().getCurrentValueSetDefinitionDao();
		// load value set definition
		vsdDao.insertValueSetDefinition(systemReleaseUri, definition, mappings);
	}
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#insertValueSetDefinitions(org.LexGrid.valueSets.ValueSetDefinitions, java.lang.String)
	 */
	@Override
	public void insertValueSetDefinitions(ValueSetDefinitions valueSetDefinitions,
			String systemReleaseUri) throws LBException {
		
		Mappings mappings = valueSetDefinitions.getMappings();
		for (ValueSetDefinition vsd : valueSetDefinitions.getValueSetDefinitionAsReference())
		{
			insertValueSetDefinition(vsd, systemReleaseUri, mappings);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#listValueSetDefinitionURIs()
	 */
	@Override
	public List<String> listValueSetDefinitionURIs() {
		return this.getDaoManager().getCurrentValueSetDefinitionDao().getValueSetDefinitionURIs();
	}
	


	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#getAllValueSetDefinitionsWithNoName()
	 */
	@Override
	public List<String> getAllValueSetDefinitionsWithNoName()
			throws LBException {
		return this.getDaoManager().getCurrentValueSetDefinitionDao().getAllValueSetDefinitionsWithNoName();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#removeValueSetDefinition(java.lang.String)
	 */
	@Override
	public void removeValueSetDefinition(String valueSetDefinitionURI) {
		this.getDaoManager().getCurrentValueSetDefinitionDao().removeValueSetDefinitionByValueSetDefinitionURI(valueSetDefinitionURI);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#getValueSetDefinitionURIForSupportedTagAndValue(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getValueSetDefinitionURIForSupportedTagAndValue(
			String supportedTag, String value, String uri) {
		return this.getDaoManager().getCurrentValueSetDefinitionDao().getValueSetDefinitionURIForSupportedTagAndValue(supportedTag, value, uri);
	}
	
	@Override
	public List<AbsoluteCodingSchemeVersionReference> getValueSetDefinitionSchemeRefForTopNodeSourceCode(Node node) throws LBException {
		return this.getDaoManager().getCurrentValueSetDefinitionDao().
				getValueSetDefinitionSchemeRefForTopNodeSourceCode(node.getEntityCode());
		
	}
	
	@Override
	public List<AbsoluteCodingSchemeVersionReference> getValueSetDefinitionDefRefForTopNodeSourceCode(Node node) throws LBException {
		return this.getDaoManager().getCurrentValueSetDefinitionDao().
				getValueSetDefinitionDefRefForTopNodeSourceCode(node.getEntityCode());
		
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#insertDependentChanges(org.LexGrid.valueSets.ValueSetDefinition)
	 */
	@Override
	public void insertDependentChanges(
			ValueSetDefinition valueSetDefinition) throws LBException {

		String valueSetDefinitionURI = valueSetDefinition.getValueSetDefinitionURI();
		
		/* 1. Update entryStateUId.*/
		
		if (valueSetDefinition.getEntryState().getChangeType() == ChangeType.DEPENDENT) {

			doAddValueSetDefinitionDependentEntry(valueSetDefinition);
		}
		
		/* 2. Revise dependent definition entries. */

		DefinitionEntry[] definitionEntry = valueSetDefinition
				.getDefinitionEntry();

		for (int i = 0; i < definitionEntry.length; i++) {

			vsDefinitionEntryService.revise(valueSetDefinitionURI, definitionEntry[i]);
		}
		
		/* 3. Revise dependent properties. */
		
		if( valueSetDefinition.getProperties() != null ) {
			
			Property[] propertyList = valueSetDefinition.getProperties().getProperty();
			
			for (int i = 0; i < propertyList.length; i++) {
				vsPropertyService.reviseValueSetDefinitionProperty(valueSetDefinitionURI, propertyList[i]);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#updateVersionableAttributes(org.LexGrid.valueSets.ValueSetDefinition)
	 */
	@Override
	public void updateVersionableAttributes(
			ValueSetDefinition valueSetDefinition) throws LBException {

		String valueSetDefinitionURI = valueSetDefinition
				.getValueSetDefinitionURI();

		ValueSetDefinitionDao vsdDao = this.getDaoManager()
				.getCurrentValueSetDefinitionDao();

		String valueSetDefUId = vsdDao
				.getGuidFromvalueSetDefinitionURI(valueSetDefinitionURI);

		String prevEntryStateUId = vsdDao
				.insertHistoryValueSetDefinition(valueSetDefUId);

		String entryStateUId = vsdDao.updateValueSetDefinitionVersionableChanges(valueSetDefUId,
				valueSetDefinition);

		this.getDaoManager().getCurrentVsEntryStateDao().insertEntryState(
				entryStateUId, valueSetDefUId, ReferenceType.VALUESETDEFINITION.name(),
				prevEntryStateUId, valueSetDefinition.getEntryState());

		this.insertDependentChanges(valueSetDefinition);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#updateValueSetDefinition(org.LexGrid.valueSets.ValueSetDefinition)
	 */
	@Override
	public void updateValueSetDefinition(ValueSetDefinition valueSetDefinition) throws LBException {

		String valueSetDefinitionURI = valueSetDefinition
				.getValueSetDefinitionURI();

		ValueSetDefinitionDao vsdDao = this.getDaoManager()
				.getCurrentValueSetDefinitionDao();

		String valueSetDefUId = vsdDao
				.getGuidFromvalueSetDefinitionURI(valueSetDefinitionURI);

		String prevEntryStateUId = vsdDao
				.insertHistoryValueSetDefinition(valueSetDefUId);

		String entryStateUId = vsdDao.updateValueSetDefinition(valueSetDefUId,
				valueSetDefinition);

		this.getDaoManager().getCurrentVsEntryStateDao().insertEntryState(
				entryStateUId, valueSetDefUId, ReferenceType.VALUESETDEFINITION.name(),
				prevEntryStateUId, valueSetDefinition.getEntryState());

		this.insertDependentChanges(valueSetDefinition);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#revise(org.LexGrid.valueSets.ValueSetDefinition, org.LexGrid.naming.Mappings, java.lang.String)
	 */
	@Override
	public void revise(ValueSetDefinition valueSetDefinition, Mappings mapping,
			String releaseURI) throws LBException {

		if (validRevision(valueSetDefinition)) {

			ChangeType changeType = valueSetDefinition.getEntryState()
					.getChangeType();

			if (changeType == ChangeType.NEW) {

				this.insertValueSetDefinition(valueSetDefinition, releaseURI,
						mapping);
			} else if (changeType == ChangeType.REMOVE) {
				LexEvsServiceLocator.getInstance().getSystemResourceService().
					removeValueSetDefinitionResourceFromSystem(valueSetDefinition.getValueSetDefinitionURI(), null);
			} else if (changeType == ChangeType.MODIFY) {

				this.updateValueSetDefinition(valueSetDefinition);
			} else if (changeType == ChangeType.DEPENDENT) {

				this.insertDependentChanges(valueSetDefinition);
			} else if (changeType == ChangeType.VERSIONABLE) {

				this.updateVersionableAttributes(valueSetDefinition);
			}
		}
	}

	/**
	 * Gets the vs definition entry service.
	 * 
	 * @return the vsDefinitionEntryService
	 */
	public VSDefinitionEntryService getVsDefinitionEntryService() {
		return vsDefinitionEntryService;
	}

	/**
	 * Sets the vs definition entry service.
	 * 
	 * @param vsDefinitionEntryService the vsDefinitionEntryService to set
	 */
	public void setVsDefinitionEntryService(
			VSDefinitionEntryService vsDefinitionEntryService) {
		this.vsDefinitionEntryService = vsDefinitionEntryService;
	}

	/**
	 * Gets the vs property service.
	 * 
	 * @return the vsPropertyService
	 */
	public VSPropertyService getVsPropertyService() {
		return vsPropertyService;
	}

	/**
	 * Sets the vs property service.
	 * 
	 * @param vsPropertyService the vsPropertyService to set
	 */
	public void setVsPropertyService(VSPropertyService vsPropertyService) {
		this.vsPropertyService = vsPropertyService;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#insertDefinitionEntry(org.LexGrid.valueSets.ValueSetDefinition, org.LexGrid.valueSets.DefinitionEntry)
	 */
	@Override
	public void insertDefinitionEntry(ValueSetDefinition valueSetDefinition,
			DefinitionEntry definitionEntry) throws LBException {
		this.getDaoManager().getCurrentValueSetDefinitionDao().insertDefinitionEntry(valueSetDefinition, definitionEntry);
		
	}

	/**
	 * Valid revision.
	 * 
	 * @param valueSetDefinition the value set definition
	 * 
	 * @return true, if successful
	 * 
	 * @throws LBException the LB exception
	 */
	private boolean validRevision(ValueSetDefinition valueSetDefinition) throws LBException {
		
		if(  valueSetDefinition == null) 
			throw new LBParameterException("ValueSetDefinition is null.");
		
		EntryState entryState = valueSetDefinition.getEntryState();
	
		if (entryState == null) {
			throw new LBRevisionException("EntryState can't be null.");
		}
	
		ChangeType changeType = entryState.getChangeType();
	
		ValueSetDefinitionDao valueSetDefDao = this.getDaoManager()
				.getCurrentValueSetDefinitionDao();
		
		String valueSetDefUId = valueSetDefDao
				.getGuidFromvalueSetDefinitionURI(valueSetDefinition
						.getValueSetDefinitionURI());
		
		if (changeType == ChangeType.NEW) {
			if (entryState.getPrevRevision() != null) {
				throw new LBRevisionException(
						"Changes of type NEW are not allowed to have previous revisions.");
			}
			
			if (valueSetDefUId != null) {
				throw new LBRevisionException(
						"The value set definition being added already exist.");
			}
		} else {
	
			if (valueSetDefUId == null) {
				throw new LBRevisionException(
						"The value set definition being revised doesn't exist.");
			}
			
			String valueSetDefLatestRevisionId = valueSetDefDao.getLatestRevision(valueSetDefUId);
			
			String currentRevision = entryState.getContainingRevision();
			String prevRevision = entryState.getPrevRevision();
			
			if (entryState.getPrevRevision() == null
					&& valueSetDefLatestRevisionId != null
					&& !valueSetDefLatestRevisionId.equals(currentRevision)
					&& !valueSetDefLatestRevisionId
							.startsWith(VersionableEventAuthoringService.LEXGRID_GENERATED_REVISION)) {
				throw new LBRevisionException(
						"All changes of type other than NEW should have previous revisions.");
			} else if (valueSetDefLatestRevisionId != null
					&& !valueSetDefLatestRevisionId.equals(currentRevision)
					&& !valueSetDefLatestRevisionId.equals(prevRevision)
					&& !valueSetDefLatestRevisionId
							.startsWith(VersionableEventAuthoringService.LEXGRID_GENERATED_REVISION)) {
				throw new LBRevisionException(
						"Revision source is not in sync with the database revisions. "
								+ "Previous revision id does not match with the latest revision id of the value set definition."
								+ "Please update the authoring instance with all the revisions and regenerate the source.");
			}
		}
		return true;
	}

	/**
	 * Do add value set definition dependent entry.
	 * 
	 * @param valueSetDefinition the value set definition
	 */
	private void doAddValueSetDefinitionDependentEntry(
			ValueSetDefinition valueSetDefinition) {
	
		String valueSetDefinitionURI = valueSetDefinition
				.getValueSetDefinitionURI();
	
		ValueSetDefinitionDao valueSetDefDao = this.getDaoManager()
				.getCurrentValueSetDefinitionDao();
	
		VSEntryStateDao vsEntryStateDao = this.getDaoManager()
				.getCurrentVsEntryStateDao();
	
		String valueSetDefUId = valueSetDefDao
				.getGuidFromvalueSetDefinitionURI(valueSetDefinitionURI);
	
		String prevEntryStateUId = valueSetDefDao
				.getValueSetDefEntryStateUId(valueSetDefUId);
	
		if (!valueSetDefDao.entryStateExists(prevEntryStateUId)) {
	
			EntryState entryState = new EntryState();
	
			entryState.setChangeType(ChangeType.NEW);
			entryState.setRelativeOrder(0L);
	
			vsEntryStateDao.insertEntryState(prevEntryStateUId, valueSetDefUId,
					ReferenceType.VALUESETDEFINITION.name(), null, entryState);
		}

		EntryState currentVSDEntryState = vsEntryStateDao.getEntryStateByUId(prevEntryStateUId);
		// if the exiting VSD entry change type is non-dependent, move to history table
		if (!currentVSDEntryState.getChangeType().equals(ChangeType.DEPENDENT))
		{
			prevEntryStateUId = valueSetDefDao
					.insertHistoryValueSetDefinition(valueSetDefUId);
		}
	
		String entryStateUId = vsEntryStateDao.insertEntryState(valueSetDefUId,
				ReferenceType.VALUESETDEFINITION.name(), prevEntryStateUId,
				valueSetDefinition.getEntryState());
	
		valueSetDefDao.updateValueSetDefEntryStateUId(valueSetDefUId,
				entryStateUId);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#getValueSetDefinitionByRevision(java.lang.String, java.lang.String)
	 */
	@Override
	public ValueSetDefinition getValueSetDefinitionByRevision(String valueSetDefURI,
			String revisionId) throws LBRevisionException {
		ValueSetDefinitionDao valueSetDefDao = this.getDaoManager()
				.getCurrentValueSetDefinitionDao();
		
		ValueSetDefinition vsd = null;
		if (StringUtils.isEmpty(revisionId))
			vsd = valueSetDefDao.getValueSetDefinitionByURI(valueSetDefURI);
		else
			vsd = valueSetDefDao.getValueSetDefinitionByRevision(valueSetDefURI, revisionId);
		
		return vsd;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#getValueSetDefinitionByDate(java.lang.String, java.sql.Date)
	 */
	@Override
	public ValueSetDefinition getValueSetDefinitionByDate(String valueSetDefURI,
			Date date) throws LBRevisionException {
		
		if( date == null )
			return null;
		
		RevisionDao revisionDao = getDaoManager().getRevisionDao();

		String revisionId = revisionDao.getRevisionIdForDate(new Timestamp(date.getTime()));
		
		if( revisionId == null )
			return null;
		
		ValueSetDefinitionDao valueSetDefDao = this.getDaoManager()
				.getCurrentValueSetDefinitionDao();

		return valueSetDefDao.getValueSetDefinitionByRevision(valueSetDefURI, revisionId);
	}

	@Override
	public List<String> getVSURIsForContextURI(String uri) {
		if(uri == null) {return null;}
		ValueSetDefinitionDao valueSetDefDao = this.getDaoManager()
				.getCurrentValueSetDefinitionDao();
		return valueSetDefDao.getValueSetURIsForContext(uri);
	}

}