/**
 * 
 */
package org.lexevs.cts2.author;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
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
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;
import org.lexgrid.usagecontext.util.UsageContextConstants;

/**
 * Implementation of LexEVS CTS2 Usage Context Authoring Operation.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class UsageContextAuthoringOperationImpl extends AuthoringCore implements
		UsageContextAuthoringOperation {

	private AuthoringService authServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#activateUsageContext(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean activateUsageContext(String usageContextId, String namespace, String codeSystemNameOrURI, 
			String codeSystemVersion, RevisionInfo revisionInfo) throws LBException
	{
		if (StringUtils.isEmpty(usageContextId))
			throw new LBException("Usage COntext Id can not be empty");
		
		Versionable ver = new Versionable();
		ver.setIsActive(true);
		
		return updateUsageContextVersionable(usageContextId, namespace, ver, codeSystemNameOrURI, codeSystemVersion, revisionInfo);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#addUsageContextProperty(java.lang.String, java.lang.String, org.LexGrid.commonTypes.Property, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean addUsageContextProperty(String usageContextId, String namespace, Property newProperty, 
			String codeSystemNameOrURI, String codeSystemVersion, RevisionInfo revision) throws LBException {
		if (usageContextId == null)
			throw new LBException("Concept Domain Id can not be empty");
		if (newProperty == null)
			throw new LBException("New property can not be empty");
		validateRevisionInfo(revision);
		
		String csURI = getCodeSystemURI(codeSystemNameOrURI);
		
		CodeSystemAuthoringOperation csAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
		csAuthOp.addNewConceptProperty(csURI, codeSystemVersion, usageContextId, namespace, newProperty, revision);
		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#createUsageContext(java.lang.String, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo, java.lang.String, java.lang.String, boolean, org.LexGrid.commonTypes.Properties, java.lang.String, java.lang.String)
	 */
	@Override
	public String createUsageContext(String usageContextId, String usageContextName, String namespace,
			RevisionInfo revisionInfo, String description, String status, boolean isActive,
			Properties properties, String codeSystemNameOrURI, String codeSystemVersion) throws LBException {
		
		if (StringUtils.isEmpty(usageContextId))
			usageContextId = createUniqueId();
		
		if (StringUtils.isEmpty(usageContextName))
			throw new LBException("usage context name can not be empty");
		
		// create an entity object for usageContext
		Entity entity = new Entity();
		entity.setEntityCode(usageContextId);
		entity.setEntityCodeNamespace(namespace);
		EntityDescription ed = new EntityDescription();
		ed.setContent(usageContextId);
		entity.setEntityDescription(ed);
		entity.setStatus(status);
		entity.setIsActive(isActive);
		entity.addEntityType(UsageContextConstants.USAGE_CONTEXT_ENTITY_TYPE);
		
		Presentation pres = new Presentation();
		pres.setPropertyName(SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION);
		Text text = new Text();
		text.setContent(usageContextName);
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
		
		return usageContextId;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#createUsageContextCodeSystem(org.lexevs.cts2.core.update.RevisionInfo, java.lang.String, java.lang.String, java.lang.String, java.lang.String, long, java.lang.String, java.util.List, java.util.List, org.LexGrid.commonTypes.Text, org.LexGrid.naming.Mappings)
	 */
	@Override
	public CodingScheme createUsageContextCodeSystem(RevisionInfo revision, String codeSystemName, String codeSystemURI, String formalName,
            String defaultLanguage, long approxNumConcepts, String representsVersion, List<String> localNameList,
            List<Source> sourceList, Text copyright, Mappings mappings) throws LBException{
		CodeSystemAuthoringOperation csAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
		
		return csAuthOp.createCodeSystem(revision, codeSystemName, codeSystemURI, formalName, defaultLanguage, 0, 
				representsVersion, localNameList, sourceList, copyright, mappings);
    }
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#deactivateUsageContext(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean deactivateUsageContext(String usageContextId, String namespace, String codeSystemNameOrURI,
			String codeSystemVersion, RevisionInfo revisionInfo) throws LBException {
		if (StringUtils.isEmpty(usageContextId))
			throw new LBException("Usage Context Id can not be empty");
		
		Versionable ver = new Versionable();
		ver.setIsActive(false);
		
		return updateUsageContextVersionable(usageContextId, namespace, ver, codeSystemNameOrURI, codeSystemVersion, revisionInfo);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#removeUsageContext(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean removeUsageContext(String usageContextId, String namespace, String codeSystemNameOrURI,
			String codeSystemVersion, RevisionInfo revision) throws LBException {
		if (usageContextId == null)
			throw new LBException("Usage Context Id can not be empty");
		
		validateRevisionInfo(revision);
		
		String csURI = getCodeSystemURI(codeSystemNameOrURI);
		
		CodeSystemAuthoringOperation csAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
		csAuthOp.deleteConcept(csURI, codeSystemVersion, usageContextId, namespace, revision);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#removeUsageContextProperty(java.lang.String, java.lang.String, org.LexGrid.commonTypes.Property, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean removeUsageContextProperty(String usageContextId, String namespace, Property property, 
			String codeSystemNameOrURI, String codeSystemVersion, RevisionInfo revision) throws LBException {
		if (usageContextId == null)
			throw new LBException("Usage Context Id can not be empty");
		if (property == null)
			throw new LBException("property can not be empty");
		
		validateRevisionInfo(revision);		
		
		String csURI = getCodeSystemURI(codeSystemNameOrURI);
		
		CodeSystemAuthoringOperation csAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
		csAuthOp.deleteConceptProperty(csURI, codeSystemVersion, usageContextId, namespace, property, revision);
		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#updateUsageContextProperty(java.lang.String, java.lang.String, org.LexGrid.commonTypes.Property, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateUsageContextProperty(String usageContextId, String namespace, Property changedProperty, 
			String codeSystemNameOrURI, String codeSystemVersion, RevisionInfo revision) throws LBException {
		if (usageContextId == null)
			throw new LBException("Usage Context Id can not be empty");
		if (changedProperty == null)
			throw new LBException("Changed property can not be empty");
		
		validateRevisionInfo(revision);
		
		String csURI = getCodeSystemURI(codeSystemNameOrURI);
		
		CodeSystemAuthoringOperation csAuthOp = LexEvsCTS2Impl.defaultInstance().getAuthoringOperation().getCodeSystemAuthoringOperation();
		csAuthOp.updateConceptProperty(csURI, codeSystemVersion, usageContextId, namespace, changedProperty, revision);
		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#updateUsageContextStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateUsageContextStatus(String usageContextId, String namespace, String newStatus, 
			String codeSystemNameOrURI, String codeSystemVersion, RevisionInfo revisionInfo) throws LBException {
		if (StringUtils.isEmpty(usageContextId))
			throw new LBException("Usage Context Id can not be empty");
		
		Versionable ver = new Versionable();
		ver.setStatus(newStatus);
		
		return updateUsageContextVersionable(usageContextId, namespace, ver, codeSystemNameOrURI, codeSystemVersion, revisionInfo);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#updateUsageContextVersionable(java.lang.String, java.lang.String, org.LexGrid.commonTypes.Versionable, java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateUsageContextVersionable(String usageContextId, String namespace, Versionable changedVersionable, 
			String codeSystemNameOrURI, String codeSystemVersion, RevisionInfo revision) throws LBException {
		if (usageContextId == null)
			throw new LBException("usageContextId can not be empty");
		
		if (changedVersionable == null)
			throw new LBException("Changed Versionable information can not be empty");
		
		validateRevisionInfo(revision);
		
		String csURI = getCodeSystemURI(codeSystemNameOrURI);
		
		Entity conceptDomain = this.getEntityShell(usageContextId, namespace, csURI, codeSystemVersion, revision.getRevisionId(), ChangeType.VERSIONABLE);
		
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
	
	private String getCodeSystemURI(String codeSystemNameOrUri) throws LBParameterException{
		SystemResourceService systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
		
		return systemResourceService.getUriForUserCodingSchemeName(codeSystemNameOrUri);
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
	
	private CodingScheme getCodeSystemShell(String codeSystemURI, String codeSystemVersion, String revisionId, ChangeType changeType) throws LBException{
		CodingScheme conceptDomainCS = new CodingScheme();
		conceptDomainCS.setCodingSchemeURI(codeSystemURI);
		conceptDomainCS.setRepresentsVersion(codeSystemVersion);
		
		conceptDomainCS.setEntryState(populateEntryState(changeType, revisionId, null, 0L));
		
		return conceptDomainCS;
	}
	
	private Entity getEntityShell(String entityId, String namespace, String codeSystemNameOrURI, String codeSystemVersion, String revisionId, ChangeType changeType) throws LBException{
		
		Entity conceptDomain = new Entity();
		conceptDomain.setEntityCode(entityId);
		conceptDomain.setEntityCodeNamespace(namespace);
		
		conceptDomain.setEntryState(populateEntryState(changeType, revisionId, null, 0L));
		
		return conceptDomain;
	}
}
