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
import java.net.URISyntaxException;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedConceptDomain;
import org.LexGrid.naming.SupportedContext;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * Implementation LexEVS CTS 2 Value Set Authoring Operation.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class ValueSetAuthoringOperationImpl extends AuthoringCore implements
		ValueSetAuthoringOperation {
	
	private ValueSetDefinitionService vsdServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetDefinitionService();
	private AuthoringService authServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#addDefinitionEntry(java.net.URI, org.LexGrid.valueSets.DefinitionEntry, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean addDefinitionEntry(URI valueSetURI,
			DefinitionEntry newDefinitionEntry, RevisionInfo revision) throws LBException {
		validateRevisionInfo(revision);
		
		if (newDefinitionEntry == null)
			throw new LBException("New Definition Entry can not be empty");
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		
		if (vsd == null)
		{
			throw new LBException("No Value set definition found with uri : " + valueSetURI);
		}
		// remove any existing definition entry from the vsd object
		vsd.removeAllDefinitionEntry();
		vsd.removeAllRepresentsRealmOrContext();
		vsd.removeAllSource();
		vsd.setProperties(null);
		
		String prevRevisionId = vsd.getEntryState() != null?vsd.getEntryState().getContainingRevision():null;
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		
		// setup entrystate for vsd
		vsd.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), prevRevisionId, 0L));
		
		// setup entrystate for new definition entry
		newDefinitionEntry.setEntryState(populateEntryState(ChangeType.NEW,
				lgRevision.getRevisionId(), null, 0L));
		
		// add new definition entry to vsd
		vsd.addDefinitionEntry(newDefinitionEntry);
		
		ChangedEntry ce = new ChangedEntry();
		ce.setChangedValueSetDefinitionEntry(vsd);
		
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#addValueSetProperty(java.net.URI, org.LexGrid.commonTypes.Property, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean addValueSetProperty(URI valueSetURI, Property newProperty,
			RevisionInfo revision)
			throws LBException {
		if (valueSetURI == null)
			throw new LBException("Value Set Definition URI can not be empty");
		if (newProperty == null)
			throw new LBException("New property can not be empty");
		validateRevisionInfo(revision);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		if (vsd == null)
		{
			throw new LBException("No Value set definition found with uri : " + valueSetURI);
		}
		
		String prevRevisionId = vsd.getEntryState() != null?vsd.getEntryState().getContainingRevision():null;
		vsd.removeAllDefinitionEntry();
		vsd.removeAllRepresentsRealmOrContext();
		vsd.removeAllSource();
		
		// setup entry state for vsd
		vsd.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), prevRevisionId, 0L));
		
		// setup entry state for new property
		newProperty.setEntryState(populateEntryState(ChangeType.NEW, 
				lgRevision.getRevisionId(), null, 0L));
		
		Properties props = new Properties();
		props.addProperty(newProperty);
		vsd.setProperties(props);
		
		ce.setChangedValueSetDefinitionEntry(vsd);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#createValueSet(java.net.URI, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.List, org.LexGrid.commonTypes.Properties, java.util.List, org.LexGrid.commonTypes.Versionable, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)
	 */
	@Override
	public URI createValueSet(URI valueSetURI, String valueSetName,
			String defaultCodeSystem, String conceptDomainId,
			List<Source> sourceList, List<String> usageContext,
			Properties properties, List<DefinitionEntry> ruleSetList,
			Versionable versionable, RevisionInfo revision) throws LBException {
		if (valueSetURI == null)
			throw new LBException("Value Set Definition URI can not be empty");
		validateRevisionInfo(revision);
		
		ValueSetDefinition vsd = new ValueSetDefinition();
		vsd.setValueSetDefinitionURI(valueSetURI.toString());
		vsd.setValueSetDefinitionName(valueSetName);
		vsd.setDefaultCodingScheme(defaultCodeSystem);
		vsd.setConceptDomain(conceptDomainId);
		if (sourceList != null)
			vsd.setSource(sourceList);
		if (usageContext != null)
			vsd.setRepresentsRealmOrContext(usageContext);
		if (properties != null)
			vsd.setProperties(properties);
		if (ruleSetList != null)
			vsd.setDefinitionEntry(ruleSetList);
		if (versionable != null)
		{
			vsd.setEffectiveDate(versionable.getEffectiveDate());
			vsd.setExpirationDate(versionable.getExpirationDate());
			vsd.setIsActive(versionable.getIsActive());
			vsd.setOwner(versionable.getOwner());
			vsd.setStatus(versionable.getStatus());
		}
		
		return createValueSet(vsd, revision);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#createValueSet(org.LexGrid.valueSets.ValueSetDefinition, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public URI createValueSet(ValueSetDefinition valueSetDefininition,
			RevisionInfo revision) throws LBException {
		if (valueSetDefininition == null)
			throw new LBException("ValueSetDefinition object can not be empty");
		
		validateRevisionInfo(revision);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		// setup entry state for new vsd
		valueSetDefininition.setEntryState(populateEntryState(ChangeType.NEW, 
				lgRevision.getRevisionId(), null, 0L));
		
		ChangedEntry ce = new ChangedEntry();
		ce.setChangedValueSetDefinitionEntry(valueSetDefininition);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		URI vsdURI = null;
		try {
			vsdURI = new URI(valueSetDefininition.getValueSetDefinitionURI());
		} catch (URISyntaxException e) {
			throw new LBException("Problem resolving value set definition URI",e);
		}
		return vsdURI;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#updateDefinitionEntry(java.net.URI, org.LexGrid.valueSets.DefinitionEntry, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)
	 */
	@Override
	public boolean updateDefinitionEntry(URI valueSetURI,
			DefinitionEntry changedDefinitionEntry, RevisionInfo revision) throws LBException {
		if (valueSetURI == null)
			throw new LBException("ValueSetDefinitionURI can not be empty");
		
		if (changedDefinitionEntry == null)
			throw new LBException("changedDefinitionEntry can not be empty");
		
		if (changedDefinitionEntry.getRuleOrder() == null)
			throw new LBException("changedDefinitionEntry RuleOrder can not be empty. It is unique id to identify definition entry.");
		
		validateRevisionInfo(revision);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		
		if (vsd == null)
			throw new LBException("No Value Set Definition found with URI : " + valueSetURI.toString());
		
		DefinitionEntry currentDefEntry = null;
		
		for (DefinitionEntry de : vsd.getDefinitionEntryAsReference())
		{
			if (de.getRuleOrder().equals(changedDefinitionEntry.getRuleOrder()))
				currentDefEntry = de;
		}
		
		if (currentDefEntry == null)
			throw new LBException("No Definition Entry found with Rule Order : " + changedDefinitionEntry.getRuleOrder());
		
		String prevRevisionId = vsd.getEntryState() != null?vsd.getEntryState().getContainingRevision():null;
		vsd.removeAllDefinitionEntry();
		vsd.removeAllRepresentsRealmOrContext();
		vsd.removeAllSource();
		vsd.setProperties(null);
		
		// setup entry state for vsd
		vsd.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), prevRevisionId, 0L));
		
		// setup entry state for update definition entry
		String defEntryPrecRevId = currentDefEntry.getEntryState() != null?currentDefEntry.getEntryState().getContainingRevision():null;
		changedDefinitionEntry.setEntryState(populateEntryState(ChangeType.MODIFY, 
				lgRevision.getRevisionId(), defEntryPrecRevId, 0L));
		
		vsd.addDefinitionEntry(changedDefinitionEntry);
		
		ce.setChangedValueSetDefinitionEntry(vsd);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#updateValueSetMetaData(java.net.URI, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.List, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateValueSetMetaData(URI valueSetURI, String valueSetName,
			String defaultCodeSystem, String conceptDomainId,
			List<Source> sourceList, List<String> usageContext,
			RevisionInfo revision) throws LBException {
		
		if (valueSetURI == null)
			throw new LBException("ValueSetDefinitionURI can not be empty");
		
		validateRevisionInfo(revision);
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		if (vsd == null)
			throw new LBException("No Value Set Definition found with URI : " + valueSetURI.toString());
		
		String prevRevisionId = vsd.getEntryState() != null?vsd.getEntryState().getContainingRevision():null;
		vsd.removeAllDefinitionEntry();
		vsd.removeAllRepresentsRealmOrContext();
		vsd.removeAllSource();
		vsd.setProperties(null);
		
		Mappings maps = new Mappings();
		
		if (StringUtils.isNotEmpty(valueSetName))
			vsd.setValueSetDefinitionName(valueSetName);
		
		if (StringUtils.isNotEmpty(defaultCodeSystem))
		{
			vsd.setDefaultCodingScheme(defaultCodeSystem);
			SupportedCodingScheme scs = new SupportedCodingScheme();
			scs.setContent(defaultCodeSystem);
			scs.setLocalId(defaultCodeSystem);
			
			maps.addSupportedCodingScheme(scs);
		}
		
		if (conceptDomainId != null)
		{
			vsd.setConceptDomain(conceptDomainId);
			
			SupportedConceptDomain scd = new SupportedConceptDomain();
			scd.setContent(conceptDomainId);
			scd.setLocalId(conceptDomainId);
			
			maps.addSupportedConceptDomain(scd);
		}
		
		if (sourceList != null)
		{
			vsd.setSource(sourceList);
			
			for (Source src : sourceList)
			{
				SupportedSource ss = new SupportedSource();
				ss.setAssemblyRule(src.getRole());
				ss.setContent(src.getContent());
				ss.setLocalId(src.getContent());
				
				maps.addSupportedSource(ss);
			}
		}
		
		if (usageContext != null)
		{
			vsd.setRepresentsRealmOrContext(usageContext);
			
			for (String uc : usageContext)
			{
				SupportedContext sc = new SupportedContext();
				sc.setContent(uc);
				sc.setLocalId(uc);
				
				maps.addSupportedContext(sc);
			}
		}
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();
		vsd.setEntryState(populateEntryState(ChangeType.MODIFY, 
				lgRevision.getRevisionId(), prevRevisionId, 0L));
		
		vsd.setMappings(maps);
		
		ce.setChangedValueSetDefinitionEntry(vsd);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#updateValueSetProperty(java.net.URI, org.LexGrid.commonTypes.Property, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)
	 */
	@Override
	public boolean updateValueSetProperty(URI valueSetURI,
			Property changedProperty, RevisionInfo revision) throws LBException {
		
		if (valueSetURI == null)
			throw new LBException("ValueSetDefinitionURI can not be empty");
		
		if (changedProperty == null)
			throw new LBException("property can not be empty");
		
		if (StringUtils.isEmpty(changedProperty.getPropertyId()))
			throw new LBException("property ID can not be empty");
		
		validateRevisionInfo(revision);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		
		if (vsd == null)
			throw new LBException("No Value Set Definition found with URI : " + valueSetURI.toString());
		
		if (vsd.getProperties() == null)
			throw new LBException("No properties found in value set definition : " + valueSetURI);
		
		Property currentProperty = null;
		
		for (Property prop : vsd.getProperties().getPropertyAsReference())
		{
			if (prop.getPropertyId().equalsIgnoreCase(changedProperty.getPropertyId()))
				currentProperty = prop;
		}
		
		if (currentProperty == null)
			throw new LBException("No property found with id : " + changedProperty.getPropertyId());
		
		vsd.removeAllDefinitionEntry();
		vsd.removeAllRepresentsRealmOrContext();
		vsd.removeAllSource();
		
		// remove all other properties but the one that needs to be changed
		vsd.setProperties(null);		
		Properties props = new Properties();
		props.addProperty(changedProperty);
		vsd.setProperties(props);
		
		// setup entry state for vsd
		String prevRevisionId = vsd.getEntryState() != null?vsd.getEntryState().getContainingRevision():null;
		vsd.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), prevRevisionId, 0L));
		
		// setup entry state for property to be changed
		String propPrevRevId = currentProperty.getEntryState() != null?currentProperty.getEntryState().getContainingRevision():null;
		changedProperty.setEntryState(populateEntryState(ChangeType.MODIFY, 
				lgRevision.getRevisionId(), propPrevRevId, 0L));
		
		ce.setChangedValueSetDefinitionEntry(vsd);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#updateValueSetStatus(java.net.URI, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateValueSetStatus(URI valueSetURI, String status, RevisionInfo revision)
			throws LBException {
		if (valueSetURI == null)
			throw new LBException("ValueSetDefinitionURI can not be empty");
		if (status == null)
			throw new LBException("Status can not be empty");
		
		validateRevisionInfo(revision);
		
		Versionable ver = new Versionable();
		ver.setStatus(status);
		
		return this.updateValueSetVersionable(valueSetURI, ver, revision);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#updateValueSetVersionable(java.net.URI, org.LexGrid.commonTypes.Versionable, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateValueSetVersionable(URI valueSetURI,
			Versionable changedVersionable, RevisionInfo revision) throws LBException {
		if (valueSetURI == null)
			throw new LBException("ValueSetDefinitionURI can not be empty");
		
		if (changedVersionable == null)
			throw new LBException("Changed Versionable information can not be empty");
		
		validateRevisionInfo(revision);
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		if (vsd == null)
			throw new LBException("No Value Set Definition found with URI : " + valueSetURI.toString());
		
		vsd.removeAllDefinitionEntry();
		vsd.removeAllRepresentsRealmOrContext();
		vsd.removeAllSource();
		vsd.setProperties(null);
		
		if (StringUtils.isNotEmpty(changedVersionable.getOwner()))
		{
			vsd.setOwner(changedVersionable.getOwner());
		}
		
		if (StringUtils.isNotEmpty(changedVersionable.getStatus()))
		{
			vsd.setStatus(changedVersionable.getStatus());
		}
		
		if (changedVersionable.getEffectiveDate() != null)
		{
			vsd.setEffectiveDate(changedVersionable.getEffectiveDate());
		}
		
		if (changedVersionable.getExpirationDate() != null)
		{
			vsd.setExpirationDate(changedVersionable.getExpirationDate());
		}
		
		if (changedVersionable.getIsActive() != null)
		{
			vsd.setIsActive(changedVersionable.getIsActive());
		}
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();
		
		String prevRevisionId = vsd.getEntryState() != null?vsd.getEntryState().getContainingRevision():null;
		
		vsd.setEntryState(populateEntryState(ChangeType.VERSIONABLE, 
				lgRevision.getRevisionId(), prevRevisionId, 0L));
		
		ce.setChangedValueSetDefinitionEntry(vsd);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#removeDefinitionEntry(java.net.URI, java.lang.Long, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean removeDefinitionEntry(URI valueSetURI, Long ruleOrder,
			RevisionInfo revision) throws LBException {
		if (valueSetURI == null)
			throw new LBException("ValueSetDefinitionURI can not be empty");
		
		if (ruleOrder == null)
			throw new LBException("ruleOrder can not be empty");
		
		validateRevisionInfo(revision);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		
		if (vsd == null)
			throw new LBException("No Value Set Definition found with URI : " + valueSetURI.toString());
		
		DefinitionEntry currentDefEntry = null;
		
		for (DefinitionEntry de : vsd.getDefinitionEntryAsReference())
		{
			if (de.getRuleOrder().equals(ruleOrder))
				currentDefEntry = de;
		}
		
		if (currentDefEntry == null)
			throw new LBException("No Definition Entry found with Rule Order : " + ruleOrder);
		
		vsd.removeAllDefinitionEntry();
		vsd.removeAllRepresentsRealmOrContext();
		vsd.removeAllSource();
		vsd.setProperties(null);
		
		// setup entry state for vsd
		String prevRevisionId = vsd.getEntryState() != null?vsd.getEntryState().getContainingRevision():null;
		vsd.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), prevRevisionId, 0L));
		
		// setup entry state for update definition entry
		String defEntryPrevRevId = currentDefEntry.getEntryState() != null?currentDefEntry.getEntryState().getContainingRevision():null;
		currentDefEntry.setEntryState(populateEntryState(ChangeType.REMOVE, 
				lgRevision.getRevisionId(), defEntryPrevRevId, 0L));
		
		vsd.addDefinitionEntry(currentDefEntry);
		
		ce.setChangedValueSetDefinitionEntry(vsd);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#removeValueSet(java.net.URI, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean removeValueSet(URI valueSetURI, RevisionInfo revision)
			throws LBException {
		if (valueSetURI == null)
			throw new LBException("ValueSetDefinitionURI can not be empty");
		
		validateRevisionInfo(revision);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		
		if (vsd == null)
			throw new LBException("No Value Set Definition found with URI : " + valueSetURI.toString());
		
		vsd.removeAllDefinitionEntry();
		vsd.removeAllRepresentsRealmOrContext();
		vsd.removeAllSource();
		
		// setup entry state for vsd
		String prevRevisionId = vsd.getEntryState() != null?vsd.getEntryState().getContainingRevision():null;
		vsd.setEntryState(populateEntryState(ChangeType.REMOVE, 
				lgRevision.getRevisionId(), prevRevisionId, 0L));
		
		ce.setChangedValueSetDefinitionEntry(vsd);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#removeValueSetProperty(java.net.URI, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean removeValueSetProperty(URI valueSetURI, String propertyId,
			RevisionInfo revision) throws LBException {
		if (valueSetURI == null)
			throw new LBException("ValueSetDefinitionURI can not be empty");
		
		if (StringUtils.isEmpty(propertyId))
			throw new LBException("propertyId can not be empty");
		
		validateRevisionInfo(revision);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		
		if (vsd == null)
			throw new LBException("No Value Set Definition found with URI : " + valueSetURI.toString());
		
		if (vsd.getProperties() == null)
			throw new LBException("No property found with id : " + propertyId);
		
		Property currentProperty = null;
		
		for (Property prop : vsd.getProperties().getPropertyAsReference())
		{
			if (prop.getPropertyId().equalsIgnoreCase(propertyId))
				currentProperty = prop;
		}
		
		if (currentProperty == null)
			throw new LBException("No property found with id : " + propertyId);
		
		vsd.removeAllDefinitionEntry();
		vsd.removeAllRepresentsRealmOrContext();
		vsd.removeAllSource();
		
		// remove all other properties but the one that needs to be removed
		vsd.setProperties(null);		
		Properties props = new Properties();
		props.addProperty(currentProperty);
		vsd.setProperties(props);
		
		// setup entry state for vsd
		String prevRevisionId = vsd.getEntryState() != null?vsd.getEntryState().getContainingRevision():null;
		vsd.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), prevRevisionId, 0L));
		
		// setup entry state for property to be removed
		String propPrevRevId = currentProperty.getEntryState() != null?currentProperty.getEntryState().getContainingRevision():null;
		currentProperty.setEntryState(populateEntryState(ChangeType.REMOVE, 
				lgRevision.getRevisionId(), propPrevRevId, 0L));
		
		ce.setChangedValueSetDefinitionEntry(vsd);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		return true;
	}
}