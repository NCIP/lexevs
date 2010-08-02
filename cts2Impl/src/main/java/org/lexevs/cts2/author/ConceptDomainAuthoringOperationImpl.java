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
package org.lexevs.cts2.author;

import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.cts2.query.ValueSetQueryOperation;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.conceptdomain.LexEVSConceptDomainServices;
import org.lexgrid.conceptdomain.impl.LexEVSConceptDomainServicesImpl;

/**
 * Implementation of LexEVS CTS2 Concept Domain Authoring Operation.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class  ConceptDomainAuthoringOperationImpl extends AuthoringCore implements ConceptDomainAuthoringOperation {
	private AuthoringService authServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#createConceptDomain(java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo, java.lang.String, java.lang.String, boolean, org.LexGrid.commonTypes.Properties, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	public String createConceptDomain(
			String conceptDomainId,
			String conceptDomainName, 
			RevisionInfo revisionInfo, 
			String description, 
			String status,
			boolean isActive,
			Properties properties, 
			CodingSchemeVersionOrTag versionOrTag) throws LBException{
		String revisionId = revisionInfo == null ? null : revisionInfo.getRevisionId();
		
		if (StringUtils.isEmpty(conceptDomainId))
			conceptDomainId = createUniqueId();
		
		this.getLexEVSConceptDomainServices().insertConceptDomain(
				conceptDomainId, 
				conceptDomainName, 
				revisionId, 
				description, 
				status,
				isActive,
				properties, 
				versionOrTag);
		
		return conceptDomainId;
	}
	
	public boolean updateConceptDomainStatus(String conceptDomainId,
			String newStatus, CodingSchemeVersionOrTag versionOrTag, RevisionInfo revisionInfo) throws LBException
	{
		if (StringUtils.isEmpty(conceptDomainId))
			throw new LBException("Concept Domain Id can not be empty");
		
		Versionable ver = new Versionable();
		ver.setStatus(newStatus);
		
		return updateConceptDomainVersionable(conceptDomainId, ver, versionOrTag, revisionInfo);
	}
	
	public boolean activateConceptDomain(String conceptDomainId,
			CodingSchemeVersionOrTag versionOrTag, RevisionInfo revisionInfo) throws LBException
	{
		if (StringUtils.isEmpty(conceptDomainId))
			throw new LBException("Concept Domain Id can not be empty");
		
		Versionable ver = new Versionable();
		ver.setIsActive(true);
		
		return updateConceptDomainVersionable(conceptDomainId, ver, versionOrTag, revisionInfo);
	}
	
	public boolean deactivateConceptDomain(String conceptDomainId,
			CodingSchemeVersionOrTag versionOrTag, RevisionInfo revisionInfo) throws LBException
	{
		if (StringUtils.isEmpty(conceptDomainId))
			throw new LBException("Concept Domain Id can not be empty");
		
		Versionable ver = new Versionable();
		ver.setIsActive(false);
		
		return updateConceptDomainVersionable(conceptDomainId, ver, versionOrTag, revisionInfo);
	}
	
	public boolean updateConceptDomainVersionable(String conceptDomainId,
			Versionable changedVersionable, CodingSchemeVersionOrTag versionOrTag, RevisionInfo revision) throws LBException {
		if (conceptDomainId == null)
			throw new LBException("conceptDomainId can not be empty");
		
		if (changedVersionable == null)
			throw new LBException("Changed Versionable information can not be empty");
		
		validateRevisionInfo(revision);
		
		Entity conceptDomain = this.getLexEVSConceptDomainServices().getConceptDomainEntity(conceptDomainId, versionOrTag);

		if (conceptDomain == null)
			throw new LBException("No concept domain found with id : " + conceptDomainId);
		
		conceptDomain.removeAllComment();
		conceptDomain.removeAllDefinition();
		conceptDomain.removeAllEntityType();
		conceptDomain.removeAllPresentation();
		conceptDomain.removeAllProperty();
		conceptDomain.removeAllPropertyLink();
		
		if (StringUtils.isNotEmpty(changedVersionable.getOwner()))
		{
			conceptDomain.setOwner(changedVersionable.getOwner());
		}
		
		if (StringUtils.isNotEmpty(changedVersionable.getStatus()))
		{
			conceptDomain.setStatus(changedVersionable.getStatus());
		}
		
		if (changedVersionable.getEffectiveDate() != null)
		{
			conceptDomain.setEffectiveDate(changedVersionable.getEffectiveDate());
		}
		
		if (changedVersionable.getExpirationDate() != null)
		{
			conceptDomain.setExpirationDate(changedVersionable.getExpirationDate());
		}
		
		if (changedVersionable.getIsActive() != null)
		{
			conceptDomain.setIsActive(changedVersionable.getIsActive());
		}
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();
		
		String prevRevisionId = conceptDomain.getEntryState() != null? conceptDomain.getEntryState().getContainingRevision():null;
		
		conceptDomain.setEntryState(populateEntryState(ChangeType.VERSIONABLE, 
				lgRevision.getRevisionId(), prevRevisionId, 0L));
		
		CodingScheme conceptDomainCS = this.getLexEVSConceptDomainServices().getConceptDomainCodingScheme(versionOrTag);
		
		String csPrevRevisionId = conceptDomainCS.getEntryState() != null? conceptDomainCS.getEntryState().getContainingRevision():null;
		
		conceptDomainCS.removeAllLocalName();
		conceptDomainCS.removeAllRelations();
		conceptDomainCS.removeAllSource();
		conceptDomainCS.setEntities(null);
		
		Entities entities = new Entities();
		entities.addEntity(conceptDomain);
		
		conceptDomainCS.setEntities(entities);
		
		conceptDomainCS.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), csPrevRevisionId, 0L));
		
		ce.setChangedCodingSchemeEntry(conceptDomainCS);
		
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#addConceptDomainProperty(java.lang.String, org.LexGrid.commonTypes.Property, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean addConceptDomainProperty(String conceptDomainId, Property newProperty, CodingSchemeVersionOrTag versionOrTag, 
			RevisionInfo revision)
			throws LBException {
		if (conceptDomainId == null)
			throw new LBException("Concept Domain Id can not be empty");
		if (newProperty == null)
			throw new LBException("New property can not be empty");
		validateRevisionInfo(revision);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();		
		
		// get concept domain entity
		Entity conceptDomain = this.getLexEVSConceptDomainServices().getConceptDomainEntity(conceptDomainId, versionOrTag);

		if (conceptDomain == null)
			throw new LBException("No concept domain found with id : " + conceptDomainId);
		
		// remove all other properties
		conceptDomain.removeAllComment();
		conceptDomain.removeAllDefinition();
		conceptDomain.removeAllEntityType();
		conceptDomain.removeAllPresentation();
		conceptDomain.removeAllProperty();
		conceptDomain.removeAllPropertyLink();
		
		// setup entry state for new property
		newProperty.setEntryState(populateEntryState(ChangeType.NEW, 
				lgRevision.getRevisionId(), null, 0L));
		
		// add the new property to the concept domain entity
		conceptDomain.addProperty(newProperty);
		
		// get current revision id from the concept domain entity
		String cdPrevRevisionId = conceptDomain.getEntryState() != null? conceptDomain.getEntryState().getContainingRevision():null;
		
		// populate entry state for concept domain entity as dependent change type
		conceptDomain.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), cdPrevRevisionId, 0L));
		
		// get the concept domain coding scheme
		CodingScheme conceptDomainCS = this.getLexEVSConceptDomainServices().getConceptDomainCodingScheme(versionOrTag);
		
		// get the current revision id of the coding scheme
		String csPrevRevisionId = conceptDomainCS.getEntryState() != null? conceptDomainCS.getEntryState().getContainingRevision():null;
		
		// remove all other attributes
		conceptDomainCS.removeAllLocalName();
		conceptDomainCS.removeAllRelations();
		conceptDomainCS.removeAllSource();
		conceptDomainCS.setEntities(null);
		
		// add only the entity that has new property added
		Entities entities = new Entities();
		entities.addEntity(conceptDomain);
		
		conceptDomainCS.setEntities(entities);
		
		// populate entry state for concept domain coding scheme with dependent change type
		conceptDomainCS.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), csPrevRevisionId, 0L));
		
		// add concept domain coding scheme to the changed entry object
		ce.setChangedCodingSchemeEntry(conceptDomainCS);
		
		// add the changed entry object to the revision object
		lgRevision.addChangedEntry(ce);
		
		// submit the revision with the change set to the authoring service
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#updateConceptDomainProperty(java.lang.String, org.LexGrid.commonTypes.Property, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateConceptDomainProperty(String conceptDomainId, Property changedProperty, CodingSchemeVersionOrTag versionOrTag, 
			RevisionInfo revision)
			throws LBException {
		if (conceptDomainId == null)
			throw new LBException("Concept Domain Id can not be empty");
		if (changedProperty == null)
			throw new LBException("Changed property can not be empty");
		validateRevisionInfo(revision);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();		
		
		// get concept domain entity
		Entity conceptDomain = this.getLexEVSConceptDomainServices().getConceptDomainEntity(conceptDomainId, versionOrTag);

		if (conceptDomain == null)
			throw new LBException("No concept domain found with id : " + conceptDomainId);
		
		Property currentProperty = null;
		
		// get the property that needs to be modified
		for (Property prop : conceptDomain.getAllProperties())
		{
			if (prop.getPropertyId().equalsIgnoreCase(changedProperty.getPropertyId()))
				currentProperty = prop;
		}
		
		if (currentProperty == null)
			throw new LBException("No property found with id : " + changedProperty.getPropertyId());
		
		// remove all other properties
		conceptDomain.removeAllComment();
		conceptDomain.removeAllDefinition();
		conceptDomain.removeAllEntityType();
		conceptDomain.removeAllPresentation();
		conceptDomain.removeAllProperty();
		conceptDomain.removeAllPropertyLink();
		
		// setup entry state for changed property
		changedProperty.setEntryState(populateEntryState(ChangeType.MODIFY, 
				lgRevision.getRevisionId(), null, 0L));
		
		// add the changed property to the concept domain entity
		conceptDomain.addProperty(changedProperty);
		
		// get current revision id from the concept domain entity
		String cdPrevRevisionId = conceptDomain.getEntryState() != null? conceptDomain.getEntryState().getContainingRevision():null;
		
		// populate entry state for concept domain entity as dependent change type
		conceptDomain.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), cdPrevRevisionId, 0L));
		
		// get the concept domain coding scheme
		CodingScheme conceptDomainCS = this.getLexEVSConceptDomainServices().getConceptDomainCodingScheme(versionOrTag);
		
		// get the current revision id of the coding scheme
		String csPrevRevisionId = conceptDomainCS.getEntryState() != null? conceptDomainCS.getEntryState().getContainingRevision():null;
		
		// remove all other attributes
		conceptDomainCS.removeAllLocalName();
		conceptDomainCS.removeAllRelations();
		conceptDomainCS.removeAllSource();
		conceptDomainCS.setEntities(null);
		
		// add only the entity that has modified property
		Entities entities = new Entities();
		entities.addEntity(conceptDomain);
		
		conceptDomainCS.setEntities(entities);
		
		// populate entry state for concept domain coding scheme with dependent change type
		conceptDomainCS.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), csPrevRevisionId, 0L));
		
		// add concept domain coding scheme to the changed entry object
		ce.setChangedCodingSchemeEntry(conceptDomainCS);
		
		// add the changed entry object to the revision object
		lgRevision.addChangedEntry(ce);
		
		// submit the revision with the change set to the authoring service
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#removeConceptDomainProperty(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean removeConceptDomainProperty(String conceptDomainId, String propertyId, CodingSchemeVersionOrTag versionOrTag, 
			RevisionInfo revision)
			throws LBException {
		if (conceptDomainId == null)
			throw new LBException("Concept Domain Id can not be empty");
		if (StringUtils.isEmpty(propertyId))
			throw new LBException("propertyId can not be empty");
		validateRevisionInfo(revision);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();		
		
		// get concept domain entity
		Entity conceptDomain = this.getLexEVSConceptDomainServices().getConceptDomainEntity(conceptDomainId, versionOrTag);

		if (conceptDomain == null)
			throw new LBException("No concept domain found with id : " + conceptDomainId);
		
		Property currentProperty = null;
		
		// get the property that needs to be modified
		for (Property prop : conceptDomain.getAllProperties())
		{
			if (prop.getPropertyId().equalsIgnoreCase(propertyId))
				currentProperty = prop;
		}
		
		if (currentProperty == null)
			throw new LBException("No property found with id : " + propertyId);
		
		// remove all other properties
		conceptDomain.removeAllComment();
		conceptDomain.removeAllDefinition();
		conceptDomain.removeAllEntityType();
		conceptDomain.removeAllPresentation();
		conceptDomain.removeAllProperty();
		conceptDomain.removeAllPropertyLink();
		
		// setup entry state for property that needs to be removed
		currentProperty.setEntryState(populateEntryState(ChangeType.REMOVE, 
				lgRevision.getRevisionId(), null, 0L));
		
		// add the property that needs to be removed to the concept domain entity
		conceptDomain.addProperty(currentProperty);
		
		// get current revision id from the concept domain entity
		String cdPrevRevisionId = conceptDomain.getEntryState() != null? conceptDomain.getEntryState().getContainingRevision():null;
		
		// populate entry state for concept domain entity as dependent change type
		conceptDomain.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), cdPrevRevisionId, 0L));
		
		// get the concept domain coding scheme
		CodingScheme conceptDomainCS = this.getLexEVSConceptDomainServices().getConceptDomainCodingScheme(versionOrTag);
		
		// get the current revision id of the coding scheme
		String csPrevRevisionId = conceptDomainCS.getEntryState() != null? conceptDomainCS.getEntryState().getContainingRevision():null;
		
		// remove all other attributes
		conceptDomainCS.removeAllLocalName();
		conceptDomainCS.removeAllRelations();
		conceptDomainCS.removeAllSource();
		conceptDomainCS.setEntities(null);
		
		// add only the entity that has property to be removed
		Entities entities = new Entities();
		entities.addEntity(conceptDomain);
		
		conceptDomainCS.setEntities(entities);
		
		// populate entry state for concept domain coding scheme with dependent change type
		conceptDomainCS.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), csPrevRevisionId, 0L));
		
		// add concept domain coding scheme to the changed entry object
		ce.setChangedCodingSchemeEntry(conceptDomainCS);
		
		// add the changed entry object to the revision object
		lgRevision.addChangedEntry(ce);
		
		// submit the revision with the change set to the authoring service
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#addConceptDomainToValueSetBinding(java.lang.String, java.util.List, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean addConceptDomainToValueSetBinding(String conceptDomainId, List<URI> valueSetURIS, RevisionInfo revisionInfo) throws LBException {
		if (StringUtils.isEmpty(conceptDomainId))
			throw new LBException("Concept Domain Id can not be empty");
		if (valueSetURIS == null)
			throw new LBException("Value Set URI list can not be empty");
		
		ValueSetAuthoringOperation vsAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getValueSetAuthoringOperation();
		
		for (URI vsURI : valueSetURIS)
		{
			vsAuthOp.updateValueSetMetaData(vsURI, null, null, conceptDomainId, null, null, revisionInfo);
		}
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#removeConceptDomain(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean removeConceptDomain(String conceptDomainId, CodingSchemeVersionOrTag versionOrTag, 
			RevisionInfo revision) throws LBException {
		if (conceptDomainId == null)
			throw new LBException("Concept Domain Id can not be empty");
		
		validateRevisionInfo(revision);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();		
		
		// get concept domain entity
		Entity conceptDomain = this.getLexEVSConceptDomainServices().getConceptDomainEntity(conceptDomainId, versionOrTag);

		if (conceptDomain == null)
			throw new LBException("No concept domain found with id : " + conceptDomainId);
		
		// remove all other properties
		conceptDomain.removeAllComment();
		conceptDomain.removeAllDefinition();
		conceptDomain.removeAllEntityType();
		conceptDomain.removeAllPresentation();
		conceptDomain.removeAllProperty();
		conceptDomain.removeAllPropertyLink();
		
		// get current revision id from the concept domain entity
		String cdPrevRevisionId = conceptDomain.getEntryState() != null? conceptDomain.getEntryState().getContainingRevision():null;
		
		// populate entry state for concept domain entity as remove change type
		conceptDomain.setEntryState(populateEntryState(ChangeType.REMOVE, 
				lgRevision.getRevisionId(), cdPrevRevisionId, 0L));
		
		// get the concept domain coding scheme
		CodingScheme conceptDomainCS = this.getLexEVSConceptDomainServices().getConceptDomainCodingScheme(versionOrTag);
		
		// get the current revision id of the coding scheme
		String csPrevRevisionId = conceptDomainCS.getEntryState() != null? conceptDomainCS.getEntryState().getContainingRevision():null;
		
		// remove all other attributes
		conceptDomainCS.removeAllLocalName();
		conceptDomainCS.removeAllRelations();
		conceptDomainCS.removeAllSource();
		conceptDomainCS.setEntities(null);
		
		// add only the entity that needs to be removed
		Entities entities = new Entities();
		entities.addEntity(conceptDomain);
		
		conceptDomainCS.setEntities(entities);
		
		// populate entry state for concept domain coding scheme with dependent change type
		conceptDomainCS.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), csPrevRevisionId, 0L));
		
		// add concept domain coding scheme to the changed entry object
		ce.setChangedCodingSchemeEntry(conceptDomainCS);
		
		// add the changed entry object to the revision object
		lgRevision.addChangedEntry(ce);
		
		// submit the revision with the change set to the authoring service
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#removeConceptDomainToValueSetBinding(java.lang.String, java.util.List, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean removeConceptDomainToValueSetBinding(String conceptDomainId, List<URI> valueSetURIS, RevisionInfo revisionInfo) throws LBException {
		if (StringUtils.isEmpty(conceptDomainId))
			throw new LBException("Concept Domain Id can not be empty");
		if (valueSetURIS == null)
			throw new LBException("Value Set URI list can not be empty");
		
		ValueSetAuthoringOperation vsAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getValueSetAuthoringOperation();
		ValueSetQueryOperation vsQueryOp = LexEvsCTS2Impl.defaultInstance().getQueryOperation().getValueSetQueryOperation();
		
		for (URI vsURI : valueSetURIS)
		{
			ValueSetDefinition vsd = vsQueryOp.getValueSetDetails(vsURI.toString(), null);
			if (vsd != null)
			{
				if (vsd.getConceptDomain().equalsIgnoreCase(conceptDomainId))
					vsAuthOp.updateValueSetMetaData(vsURI, null, null, " ", null, null, revisionInfo);
			}
		}
		
		return true;
	}
	
	/**
	 * Gets the lex evs concept domain services.
	 * 
	 * @return the lex evs concept domain services
	 */
	private LexEVSConceptDomainServices getLexEVSConceptDomainServices() {
		return LexEVSConceptDomainServicesImpl.defaultInstance();
	}
}
