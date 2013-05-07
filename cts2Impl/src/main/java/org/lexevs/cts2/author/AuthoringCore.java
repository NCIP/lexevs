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

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.BaseService;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.cts2.core.update.SystemReleaseInfo;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;

public class AuthoringCore extends BaseService{
	private AuthoringService authServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
	public String createSystemRelease(SystemReleaseInfo systemReleaseInfo) throws LBException{
		if (systemReleaseInfo == null)
			throw new LBException("Problem : System release information is empty");
		if (StringUtils.isEmpty(systemReleaseInfo.getReleaseURI()))
			throw new LBException("Problem : System release URI is empty");
		return authServ_.insertSystemReleaseMetadata(getLexGridSystemRelease(systemReleaseInfo));
	}
	
	public SystemReleaseInfo getSystemReleaseInfoByReleaseId(String releaseId) throws LBException{
		SystemRelease sr = authServ_.getSystemReleaseMetadataById(releaseId);
		if (sr == null)
			throw new LBException("Problem : No System Release found for releaseId : " + releaseId);
		
		return getSystemReleaseInfo(sr);
	}
	
	public SystemReleaseInfo getSystemReleaseInfoByReleaseURI(URI releaseURI) throws LBException{
		if (releaseURI == null)
			throw new LBException("Problem : Release URI is empty");
		
		SystemRelease sr = authServ_.getSystemReleaseMetadataByUri(releaseURI.toString());
		if (sr == null)
			throw new LBException("Problem : No System Release found for releaseURI : " + releaseURI);
		
		return getSystemReleaseInfo(sr);
	}
	
	private SystemReleaseInfo getSystemReleaseInfo(SystemRelease lgSystemRelease){
		SystemReleaseInfo srInfo = new SystemReleaseInfo();
		srInfo.setBasedOnRelease(lgSystemRelease.getBasedOnRelease());
		srInfo.setReleaseAgency(lgSystemRelease.getReleaseAgency());
		srInfo.setReleaseDate(lgSystemRelease.getReleaseDate());
		srInfo.setReleaseId(lgSystemRelease.getReleaseId());
		srInfo.setReleaseURI(lgSystemRelease.getReleaseURI());
		if (lgSystemRelease.getEntityDescription() != null)
			srInfo.setDescription(lgSystemRelease.getEntityDescription().getContent());
		return srInfo;
	}
	
	private SystemRelease getLexGridSystemRelease(SystemReleaseInfo systemReleaseInfo){
		SystemRelease sr = new SystemRelease();
		sr.setBasedOnRelease(systemReleaseInfo.getBasedOnRelease());
		sr.setReleaseAgency(systemReleaseInfo.getReleaseAgency());
		sr.setReleaseDate(systemReleaseInfo.getReleaseDate());
		sr.setReleaseId(systemReleaseInfo.getReleaseId());
		sr.setReleaseURI(systemReleaseInfo.getReleaseURI());
		if (StringUtils.isNotEmpty(systemReleaseInfo.getDescription()))
		{
			EntityDescription ed = new EntityDescription();
			ed.setContent(systemReleaseInfo.getDescription());
			sr.setEntityDescription(ed);
		}
		return sr;
	}
	
	public boolean validateRevisionInfo(RevisionInfo revisionInfo) throws LBException{
		if (revisionInfo == null)
			throw new LBException("Revision information can not be empty");
		
		if (revisionInfo.getRevisionId() == null)
			throw new LBException("Revision ID can not be empty");
		
		return true;
	}
	
	public Revision getLexGridRevisionObject(RevisionInfo revisionInfo)
	{
		Revision lgRevision = new Revision();
		lgRevision.setChangeAgent(revisionInfo.getChangeAgent());
		lgRevision.setEditOrder(revisionInfo.getEditOrder());
		if (revisionInfo.getDescription() != null)
		{
			EntityDescription ed= new EntityDescription();
			ed.setContent(revisionInfo.getDescription());
			lgRevision.setEntityDescription(ed);
		}
		if (StringUtils.isNotEmpty(revisionInfo.getChangeInstruction()))
		{
			Text text = new Text();
			text.setContent(revisionInfo.getChangeInstruction());
			lgRevision.setChangeInstructions(text);
		}
		lgRevision.setRevisionDate(revisionInfo.getRevisionDate());
		lgRevision.setRevisionId(revisionInfo.getRevisionId());
		
		return lgRevision;
	}
	
	protected <T extends Versionable> T addEntryState(
			T versionable, 
			ChangeType changeType, 
			String revisionId, String prevRevisionId, 
			Long relativeOrder){
		EntryState entryState = this.populateEntryState(
				changeType,
				revisionId, 
				prevRevisionId, 
				relativeOrder);
		
		versionable.setEntryState(entryState);
		
		return versionable;
	}

	protected Revision populateRevisionShell(
			String codingSchemeUri,
			String codingSchemeVersion,
			String entityCode,
			String entityCodeNamespace,
			Property property,
			ChangeType changeType,
			String prevRevisionId,
			Long relativeOrder,
			RevisionInfo revisionInfo) {
		Property revisableProperty = 
			this.addEntryState(property, changeType, revisionInfo.getRevisionId(), prevRevisionId, relativeOrder);
		
		Entity entity = new Entity();
		entity.setEntityCode(entityCode);
		entity.setEntityCodeNamespace(entityCodeNamespace);
		entity.addAnyProperty(revisableProperty);
		
		return this.populateRevisionShell(
				codingSchemeUri, 
				codingSchemeVersion, 
				entity, 
				ChangeType.DEPENDENT, 
				null, 
				0l, 
				revisionInfo);
	}

	protected Revision populateRevisionShell(
			String codingSchemeUri,
			String codingSchemeVersion,
			Entity entity, 
			ChangeType changeType,
			String prevRevisionId,
			Long relativeOrder,
			RevisionInfo revisionInfo) {
		Entity revisableEntity = 
			this.addEntryState(entity, changeType, revisionInfo.getRevisionId(), prevRevisionId, relativeOrder);
		
		CodingScheme codingScheme = new CodingScheme();
		codingScheme.setCodingSchemeURI(codingSchemeUri);
		codingScheme.setRepresentsVersion(codingSchemeVersion);
		
		codingScheme.setEntities(new Entities());
		codingScheme.getEntities().addEntity(revisableEntity);
		
		return this.populateRevisionShell(
				codingScheme, 
				ChangeType.DEPENDENT, 
				null, 
				0l, 
				revisionInfo);
	}
	
	protected Revision populateRevisionShell(
			CodingScheme codingScheme,
			ChangeType changeType,
			String prevRevisionId,
			Long relativeOrder,
			RevisionInfo revisionInfo) {
		CodingScheme revisableCodingScheme = 
			this.addEntryState(codingScheme, changeType, revisionInfo.getRevisionId(), prevRevisionId, relativeOrder);
		
		ChangedEntry changeEntry = new ChangedEntry();
		changeEntry.setChangedCodingSchemeEntry(revisableCodingScheme);
		
		Revision revision = this.getLexGridRevisionObject(revisionInfo);
		revision.addChangedEntry(changeEntry);
		
		return revision;
	}
	
	public EntryState populateEntryState(ChangeType changeType, String revisionId, String prevRevisionId, Long relativeOrder){
		EntryState entryState = new EntryState();
		entryState.setChangeType(changeType);
		entryState.setContainingRevision(revisionId);
		entryState.setPrevRevision(prevRevisionId);
		entryState.setRelativeOrder(0L);
		return entryState;
	}
	
	protected void validatedCodingScheme(String codingSchemeUri, String codingSchemeVersion) throws LBException {
		if(! this.getSystemResourceService().containsCodingSchemeResource(codingSchemeUri, codingSchemeVersion)) {
			throw new LBException("The Coding Scheme URI: " +  codingSchemeUri +
					" Version: " + codingSchemeVersion + " does not exist. Before creating a Concept, "
					+ " the Coding Scheme must exist.");
		}
	}
	
	protected CodingScheme getCodeSystemShell(String codeSystemURI, String codeSystemVersion, String revisionId, ChangeType changeType) throws LBException{
		CodingScheme conceptDomainCS = new CodingScheme();
		conceptDomainCS.setCodingSchemeURI(codeSystemURI);
		conceptDomainCS.setRepresentsVersion(codeSystemVersion);
		
		conceptDomainCS.setEntryState(populateEntryState(changeType, revisionId, null, 0L));
		
		return conceptDomainCS;
	}
	
	protected Entity getEntityShell(String entityId, String namespace, String codeSystemNameOrURI, String codeSystemVersion, String revisionId, ChangeType changeType) throws LBException{
		
		Entity conceptDomain = new Entity();
		conceptDomain.setEntityCode(entityId);
		conceptDomain.setEntityCodeNamespace(namespace);
		
		conceptDomain.setEntryState(populateEntryState(changeType, revisionId, null, 0L));
		
		return conceptDomain;
	}
	
	protected String getCodeSystemURI(String codeSystemNameOrUri, String version) throws LBParameterException{
		SystemResourceService systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
		
		return systemResourceService.getUriForUserCodingSchemeName(codeSystemNameOrUri, version);
	}	
}