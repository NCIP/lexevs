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

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.naming.Mappings;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
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
import org.lexgrid.conceptdomain.util.ConceptDomainConstants;

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
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#createConceptDomainCodeSystem(org.lexevs.cts2.core.update.RevisionInfo, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long, java.lang.String, java.util.List, java.util.List, org.LexGrid.commonTypes.Text, org.LexGrid.naming.Mappings, org.LexGrid.commonTypes.Properties)
	 */
	public CodingScheme createConceptDomainCodeSystem(RevisionInfo revision, String codeSystemName, String codeSystemURI, String formalName,
            String defaultLanguage, long approxNumConcepts, String representsVersion, List<String> localNameList,
            List<Source> sourceList, Text copyright, Mappings mappings) throws LBException{
		CodeSystemAuthoringOperation csAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
		
		return csAuthOp.createCodeSystem(revision, codeSystemName, codeSystemURI, formalName, defaultLanguage, 0, 
				representsVersion, localNameList, sourceList, copyright, mappings);
    }
	
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#createConceptDomain(java.lang.String, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo, java.lang.String, java.lang.String, boolean, org.LexGrid.commonTypes.Properties, java.lang.String, java.lang.String)
	 */
	@Override
	public String createConceptDomain(
			String conceptDomainId,
			String conceptDomainName, 
			String namespace,
			RevisionInfo revisionInfo, 
			String description, 
			String status,
			boolean isActive,
			Properties properties, 
			String codeSystemNameOrURI,
			String codeSystemVersion) throws LBException{
		
		if (StringUtils.isEmpty(conceptDomainId))
			conceptDomainId = createUniqueId();
		
		if (StringUtils.isEmpty(conceptDomainName))
			throw new LBException("concept domain name can not be empty");
		
		// create an entity object for concept domain
		Entity entity = new Entity();
		entity.setEntityCode(conceptDomainId);
		entity.setEntityCodeNamespace(namespace);
		EntityDescription ed = new EntityDescription();
		ed.setContent(conceptDomainName);
		entity.setEntityDescription(ed);
		entity.setStatus(status);
		entity.setIsActive(isActive);
		entity.addEntityType(ConceptDomainConstants.CONCEPT_DOMAIN_ENTITY_TYPE);
		
		Presentation pres = new Presentation();
		pres.setPropertyName(SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION);
		Text text = new Text();
		text.setContent(conceptDomainName);
		pres.setValue(text);
		pres.setIsPreferred(true);
		
		entity.addPresentation(pres);
		
		if (StringUtils.isNotEmpty(description))
		{
			Definition def = new Definition();
			def.setPropertyName("Description");
			text = new Text();
			text.setContent(description);
			def.setValue(text);
			entity.addDefinition(def);
		}
		
		if (properties != null)
			entity.addAnyProperties(properties.getPropertyAsReference());
			
		//insert
		this.doReviseEntity(getCodeSystemURI(codeSystemNameOrURI), codeSystemVersion, entity, ChangeType.NEW, null, 0L, revisionInfo);
		
		return conceptDomainId;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#updateConceptDomainStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateConceptDomainStatus(String conceptDomainId, String namespace,
			String newStatus, String codeSystemNameOrURI,
			String codeSystemVersion, RevisionInfo revisionInfo) throws LBException
	{
		if (StringUtils.isEmpty(conceptDomainId))
			throw new LBException("Concept Domain Id can not be empty");
		
		Versionable ver = new Versionable();
		ver.setStatus(newStatus);
		
		return updateConceptDomainVersionable(conceptDomainId, namespace, ver, getCodeSystemURI(codeSystemNameOrURI), codeSystemVersion, revisionInfo);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#activateConceptDomain(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean activateConceptDomain(String conceptDomainId, String namespace, 
			String codeSystemNameOrURI, String codeSystemVersion, RevisionInfo revisionInfo) throws LBException
	{
		if (StringUtils.isEmpty(conceptDomainId))
			throw new LBException("Concept Domain Id can not be empty");
		
		Versionable ver = new Versionable();
		ver.setIsActive(true);
		
		return updateConceptDomainVersionable(conceptDomainId, namespace, ver, getCodeSystemURI(codeSystemNameOrURI), codeSystemVersion, revisionInfo);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#deactivateConceptDomain(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean deactivateConceptDomain(String conceptDomainId, String namespace,
			String codeSystemNameOrURI, String codeSystemVersion, RevisionInfo revisionInfo) throws LBException
	{
		if (StringUtils.isEmpty(conceptDomainId))
			throw new LBException("Concept Domain Id can not be empty");
		
		Versionable ver = new Versionable();
		ver.setIsActive(false);
		
		return updateConceptDomainVersionable(conceptDomainId, namespace, ver, getCodeSystemURI(codeSystemNameOrURI), codeSystemVersion, revisionInfo);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#updateConceptDomainVersionable(java.lang.String, java.lang.String, org.LexGrid.commonTypes.Versionable, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateConceptDomainVersionable(String conceptDomainId, String namespace,
			Versionable changedVersionable, String codeSystemNameOrURI,
			String codeSystemVersion, RevisionInfo revision) throws LBException {
		if (conceptDomainId == null)
			throw new LBException("conceptDomainId can not be empty");
		
		if (changedVersionable == null)
			throw new LBException("Changed Versionable information can not be empty");
		
		validateRevisionInfo(revision);
		
		String csURI = getCodeSystemURI(codeSystemNameOrURI);
		
		Entity conceptDomain = this.getEntityShell(conceptDomainId, namespace, csURI, codeSystemVersion, revision.getRevisionId(), ChangeType.VERSIONABLE);
		
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
		
		CodingScheme conceptDomainCS = this.getCodeSystemShell(csURI, codeSystemVersion, lgRevision.getRevisionId(), ChangeType.DEPENDENT);
		
		Entities entities = new Entities();
		entities.addEntity(conceptDomain);
		
		conceptDomainCS.setEntities(entities);
		
		ce.setChangedCodingSchemeEntry(conceptDomainCS);
		
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#addConceptDomainProperty(java.lang.String, java.lang.String, org.LexGrid.commonTypes.Property, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean addConceptDomainProperty(String conceptDomainId, String namespace, Property newProperty, String codeSystemNameOrURI,
			String codeSystemVersion, 
			RevisionInfo revision)
			throws LBException {
		if (conceptDomainId == null)
			throw new LBException("Concept Domain Id can not be empty");
		if (newProperty == null)
			throw new LBException("New property can not be empty");
		
		validateRevisionInfo(revision);
		
		String csURI = getCodeSystemURI(codeSystemNameOrURI);
		
		CodeSystemAuthoringOperation csAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
		csAuthOp.addNewConceptProperty(csURI, codeSystemVersion, conceptDomainId, namespace, newProperty, revision);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#updateConceptDomainProperty(java.lang.String, java.lang.String, org.LexGrid.commonTypes.Property, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateConceptDomainProperty(String conceptDomainId, String namespace, Property changedProperty, String codeSystemNameOrURI,
			String codeSystemVersion, 
			RevisionInfo revision)
			throws LBException {
		if (conceptDomainId == null)
			throw new LBException("Concept Domain Id can not be empty");
		if (changedProperty == null)
			throw new LBException("Changed property can not be empty");
		
		validateRevisionInfo(revision);
		
		String csURI = getCodeSystemURI(codeSystemNameOrURI);
		
		CodeSystemAuthoringOperation csAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
		csAuthOp.updateConceptProperty(csURI, codeSystemVersion, conceptDomainId, namespace, changedProperty, revision);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#removeConceptDomainProperty(java.lang.String, java.lang.String, org.LexGrid.commonTypes.Property, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean removeConceptDomainProperty(String conceptDomainId, String namespace, Property property, String codeSystemNameOrURI,
			String codeSystemVersion, RevisionInfo revision) throws LBException {
		if (conceptDomainId == null)
			throw new LBException("Concept Domain Id can not be empty");
		if (property == null)
			throw new LBException("property can not be empty");
			
		validateRevisionInfo(revision);		
		
		String csURI = getCodeSystemURI(codeSystemNameOrURI);
		
		CodeSystemAuthoringOperation csAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
		csAuthOp.deleteConceptProperty(csURI, codeSystemVersion, conceptDomainId, namespace, property, revision);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#addConceptDomainToValueSetBinding(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean addConceptDomainToValueSetBinding(String conceptDomainId, String namespace, String codeSystemNameOrURI,
			String codeSystemVersion, List<URI> valueSetURIS, RevisionInfo revisionInfo) throws LBException {
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
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#removeConceptDomain(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean removeConceptDomain(String conceptDomainId, String namespace, String codeSystemNameOrURI,
			String codeSystemVersion, RevisionInfo revision) throws LBException {
		if (conceptDomainId == null)
			throw new LBException("Concept Domain Id can not be empty");
		
		validateRevisionInfo(revision);
		
		String csURI = getCodeSystemURI(codeSystemNameOrURI);
		
		CodeSystemAuthoringOperation csAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
		csAuthOp.deleteConcept(csURI, codeSystemVersion, conceptDomainId, namespace, revision);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ConceptDomainAuthoringOperation#removeConceptDomainToValueSetBinding(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean removeConceptDomainToValueSetBinding(String conceptDomainId, String namespace, String codeSystemNameOrURI,
			String codeSystemVersion, List<URI> valueSetURIS, RevisionInfo revisionInfo) throws LBException {
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
	
	protected void doReviseEntity(
			String codingSchemeUri, 
			String codingSchemeVersion, 
			Entity entity, 
			ChangeType changeType,
			String prevRevisionId,
			Long relativeOrder,
			RevisionInfo revisionInfo) throws LBException {
		
		this.validatedCodingScheme(codingSchemeUri, codingSchemeVersion);
		
		Revision revision = this.populateRevisionShell(
				codingSchemeUri, 
				codingSchemeVersion, 
				entity, 
				changeType, 
				prevRevisionId, 
				relativeOrder, 
				revisionInfo);
		
		this.getDatabaseServiceManager().getAuthoringService().loadRevision(revision, revisionInfo.getSystemReleaseURI(), null);
	}
	
	protected void doReviseEntityProperty(
			String codingSchemeUri, 
			String codingSchemeVersion, 
			String entityCode, 
			String entityCodeNamespace, 
			Property property,
			ChangeType changeType,
			String prevRevisionId,
			Long relativeOrder,
			RevisionInfo revisionInfo) throws LBException {
		
		this.validatedCodingScheme(codingSchemeUri, codingSchemeVersion);
		
		Revision revision = this.populateRevisionShell(
				codingSchemeUri, 
				codingSchemeVersion, 
				entityCode, 
				entityCodeNamespace,
				property,
				changeType, 
				prevRevisionId, 
				relativeOrder, 
				revisionInfo);
		
		this.getDatabaseServiceManager().getAuthoringService().loadRevision(revision, revisionInfo.getSystemReleaseURI(), null);
	}
}
