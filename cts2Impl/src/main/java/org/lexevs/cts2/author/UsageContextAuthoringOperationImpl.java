/**
 * 
 */
package org.lexevs.cts2.author;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.usagecontext.LexEVSUsageContextServices;
import org.lexgrid.usagecontext.impl.LexEVSUsageContextServicesImpl;

/**
 * Implementation of LexEVS CTS2 Usage Context Authoring Operation.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class UsageContextAuthoringOperationImpl extends AuthoringCore implements
		UsageContextAuthoringOperation {

	private AuthoringService authServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
	
	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#activateUsageContext(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean activateUsageContext(String usageContextId,
			CodingSchemeVersionOrTag versionOrTag, RevisionInfo revisionInfo)
			throws LBException 
	{
		if (StringUtils.isEmpty(usageContextId))
			throw new LBException("Usage COntext Id can not be empty");
		
		Versionable ver = new Versionable();
		ver.setIsActive(true);
		
		return updateUsageContextVersionable(usageContextId, ver, versionOrTag, revisionInfo);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#addUsageContextProperty(java.lang.String, org.LexGrid.commonTypes.Property, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean addUsageContextProperty(String usageContextId,
			Property newProperty, CodingSchemeVersionOrTag versionOrTag,
			RevisionInfo revision) throws LBException {
		if (usageContextId == null)
			throw new LBException("Concept Domain Id can not be empty");
		if (newProperty == null)
			throw new LBException("New property can not be empty");
		validateRevisionInfo(revision);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();		
		
		// get usage context entity
		Entity usageContext = this.getLexEVSUsageContextServices().getUsageContextEntity(usageContextId, versionOrTag);

		if (usageContext == null)
			throw new LBException("No concept domain found with id : " + usageContextId);
		
		// remove all other properties
		usageContext.removeAllComment();
		usageContext.removeAllDefinition();
		usageContext.removeAllEntityType();
		usageContext.removeAllPresentation();
		usageContext.removeAllProperty();
		usageContext.removeAllPropertyLink();
		
		// setup entry state for new property
		newProperty.setEntryState(populateEntryState(ChangeType.NEW, 
				lgRevision.getRevisionId(), null, 0L));
		
		// add the new property to the usage context entity
		usageContext.addProperty(newProperty);
		
		// get current revision id from the usage context entity
		String ucPrevRevisionId = usageContext.getEntryState() != null? usageContext.getEntryState().getContainingRevision():null;
		
		// populate entry state for usage context entity as dependent change type
		usageContext.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), ucPrevRevisionId, 0L));
		
		// get the usage context coding scheme
		CodingScheme usageContextCS = this.getLexEVSUsageContextServices().getUsageContextCodingScheme(versionOrTag);
		
		// get the current revision id of the coding scheme
		String csPrevRevisionId = usageContextCS.getEntryState() != null? usageContextCS.getEntryState().getContainingRevision():null;
		
		// remove all other attributes
		usageContextCS.removeAllLocalName();
		usageContextCS.removeAllRelations();
		usageContextCS.removeAllSource();
		usageContextCS.setEntities(null);
		
		// add only the entity that has new property added
		Entities entities = new Entities();
		entities.addEntity(usageContext);
		
		usageContextCS.setEntities(entities);
		
		// populate entry state for usage context coding scheme with dependent change type
		usageContextCS.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), csPrevRevisionId, 0L));
		
		// add usage context coding scheme to the changed entry object
		ce.setChangedCodingSchemeEntry(usageContextCS);
		
		// add the changed entry object to the revision object
		lgRevision.addChangedEntry(ce);
		
		// submit the revision with the change set to the authoring service
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#createUsageContext(java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo, java.lang.String, java.lang.String, boolean, org.LexGrid.commonTypes.Properties, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public String createUsageContext(String usageContextId,
			String usageContextName, RevisionInfo revisionInfo,
			String description, String status, boolean isActive,
			Properties properties, CodingSchemeVersionOrTag versionOrTag)
			throws LBException {
		
		String revisionId = revisionInfo == null ? null : revisionInfo.getRevisionId();
		
		if (StringUtils.isEmpty(usageContextId))
			usageContextId = createUniqueId();
		
		this.getLexEVSUsageContextServices().insertUsageContext(
				usageContextId, 
				usageContextName, 
				revisionId, 
				description, 
				status,
				isActive,
				properties, 
				versionOrTag);
		
		return usageContextId;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#deactivateUsageContext(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean deactivateUsageContext(String usageContextId,
			CodingSchemeVersionOrTag versionOrTag, RevisionInfo revisionInfo)
			throws LBException {
		if (StringUtils.isEmpty(usageContextId))
			throw new LBException("Usage Context Id can not be empty");
		
		Versionable ver = new Versionable();
		ver.setIsActive(false);
		
		return updateUsageContextVersionable(usageContextId, ver, versionOrTag, revisionInfo);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#removeUsageContextProperty(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean removeUsageContextProperty(String usageContextId,
			String propertyId, CodingSchemeVersionOrTag versionOrTag,
			RevisionInfo revision) throws LBException {
		if (usageContextId == null)
			throw new LBException("Usage Context Id can not be empty");
		if (StringUtils.isEmpty(propertyId))
			throw new LBException("propertyId can not be empty");
		validateRevisionInfo(revision);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();		
		
		// get usage context entity
		Entity usageContext = this.getLexEVSUsageContextServices().getUsageContextEntity(usageContextId, versionOrTag);

		if (usageContext == null)
			throw new LBException("No usage context found with id : " + usageContextId);
		
		Property currentProperty = null;
		
		// get the property that needs to be modified
		for (Property prop : usageContext.getAllProperties())
		{
			if (prop.getPropertyId().equalsIgnoreCase(propertyId))
				currentProperty = prop;
		}
		
		if (currentProperty == null)
			throw new LBException("No property found with id : " + propertyId);
		
		// remove all other properties
		usageContext.removeAllComment();
		usageContext.removeAllDefinition();
		usageContext.removeAllEntityType();
		usageContext.removeAllPresentation();
		usageContext.removeAllProperty();
		usageContext.removeAllPropertyLink();
		
		// setup entry state for property that needs to be removed
		currentProperty.setEntryState(populateEntryState(ChangeType.REMOVE, 
				lgRevision.getRevisionId(), null, 0L));
		
		// add the property that needs to be removed to the usage context entity
		usageContext.addProperty(currentProperty);
		
		// get current revision id from the usage context entity
		String ucPrevRevisionId = usageContext.getEntryState() != null? usageContext.getEntryState().getContainingRevision():null;
		
		// populate entry state for usage context entity as dependent change type
		usageContext.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), ucPrevRevisionId, 0L));
		
		// get the usage context coding scheme
		CodingScheme usageContextCS = this.getLexEVSUsageContextServices().getUsageContextCodingScheme(versionOrTag);
		
		// get the current revision id of the coding scheme
		String csPrevRevisionId = usageContextCS.getEntryState() != null? usageContextCS.getEntryState().getContainingRevision():null;
		
		// remove all other attributes
		usageContextCS.removeAllLocalName();
		usageContextCS.removeAllRelations();
		usageContextCS.removeAllSource();
		usageContextCS.setEntities(null);
		
		// add only the entity that has property to be removed
		Entities entities = new Entities();
		entities.addEntity(usageContext);
		
		usageContextCS.setEntities(entities);
		
		// populate entry state for usage context coding scheme with dependent change type
		usageContextCS.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), csPrevRevisionId, 0L));
		
		// add usage context coding scheme to the changed entry object
		ce.setChangedCodingSchemeEntry(usageContextCS);
		
		// add the changed entry object to the revision object
		lgRevision.addChangedEntry(ce);
		
		// submit the revision with the change set to the authoring service
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#updateUsageContextProperty(java.lang.String, org.LexGrid.commonTypes.Property, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateUsageContextProperty(String usageContextId,
			Property changedProperty, CodingSchemeVersionOrTag versionOrTag,
			RevisionInfo revision) throws LBException {
		if (usageContextId == null)
			throw new LBException("Usage Context Id can not be empty");
		if (changedProperty == null)
			throw new LBException("Changed property can not be empty");
		validateRevisionInfo(revision);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();		
		
		// get UsageContext entity
		Entity usageContext = this.getLexEVSUsageContextServices().getUsageContextEntity(usageContextId, versionOrTag);

		if (usageContext == null)
			throw new LBException("No UsageContext found with id : " + usageContextId);
		
		Property currentProperty = null;
		
		// get the property that needs to be modified
		for (Property prop : usageContext.getAllProperties())
		{
			if (prop.getPropertyId().equalsIgnoreCase(changedProperty.getPropertyId()))
				currentProperty = prop;
		}
		
		if (currentProperty == null)
			throw new LBException("No property found with id : " + changedProperty.getPropertyId());
		
		// remove all other properties
		usageContext.removeAllComment();
		usageContext.removeAllDefinition();
		usageContext.removeAllEntityType();
		usageContext.removeAllPresentation();
		usageContext.removeAllProperty();
		usageContext.removeAllPropertyLink();
		
		// setup entry state for changed property
		changedProperty.setEntryState(populateEntryState(ChangeType.MODIFY, 
				lgRevision.getRevisionId(), null, 0L));
		
		// add the changed property to the UsageContext entity
		usageContext.addProperty(changedProperty);
		
		// get current revision id from the UsageContext entity
		String ucPrevRevisionId = usageContext.getEntryState() != null? usageContext.getEntryState().getContainingRevision():null;
		
		// populate entry state for UsageContext entity as dependent change type
		usageContext.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), ucPrevRevisionId, 0L));
		
		// get the UsageContext coding scheme
		CodingScheme usageContextCS = this.getLexEVSUsageContextServices().getUsageContextCodingScheme(versionOrTag);
		
		// get the current revision id of the coding scheme
		String csPrevRevisionId = usageContextCS.getEntryState() != null? usageContextCS.getEntryState().getContainingRevision():null;
		
		// remove all other attributes
		usageContextCS.removeAllLocalName();
		usageContextCS.removeAllRelations();
		usageContextCS.removeAllSource();
		usageContextCS.setEntities(null);
		
		// add only the entity that has modified property
		Entities entities = new Entities();
		entities.addEntity(usageContext);
		
		usageContextCS.setEntities(entities);
		
		// populate entry state for UsageContext coding scheme with dependent change type
		usageContextCS.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), csPrevRevisionId, 0L));
		
		// add UsageContext coding scheme to the changed entry object
		ce.setChangedCodingSchemeEntry(usageContextCS);
		
		// add the changed entry object to the revision object
		lgRevision.addChangedEntry(ce);
		
		// submit the revision with the change set to the authoring service
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#updateUsageContextStatus(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateUsageContextStatus(String usageContextId,
			String newStatus, CodingSchemeVersionOrTag versionOrTag,
			RevisionInfo revisionInfo) throws LBException {
		if (StringUtils.isEmpty(usageContextId))
			throw new LBException("Usage Context Id can not be empty");
		
		Versionable ver = new Versionable();
		ver.setStatus(newStatus);
		
		return updateUsageContextVersionable(usageContextId, ver, versionOrTag, revisionInfo);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#updateUsageContextVersionable(java.lang.String, org.LexGrid.commonTypes.Versionable, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateUsageContextVersionable(String usageContextId,
			Versionable changedVersionable,
			CodingSchemeVersionOrTag versionOrTag, RevisionInfo revision)
			throws LBException {
		if (usageContextId == null)
			throw new LBException("usageContextId can not be empty");
		
		if (changedVersionable == null)
			throw new LBException("Changed Versionable information can not be empty");
		
		validateRevisionInfo(revision);
		
		Entity usageContext = this.getLexEVSUsageContextServices().getUsageContextEntity(usageContextId, versionOrTag);

		if (usageContext == null)
			throw new LBException("No Usage Context found with id : " + usageContextId);
		
		usageContext.removeAllComment();
		usageContext.removeAllDefinition();
		usageContext.removeAllEntityType();
		usageContext.removeAllPresentation();
		usageContext.removeAllProperty();
		usageContext.removeAllPropertyLink();
		
		if (StringUtils.isNotEmpty(changedVersionable.getOwner()))
		{
			usageContext.setOwner(changedVersionable.getOwner());
		}
		
		if (StringUtils.isNotEmpty(changedVersionable.getStatus()))
		{
			usageContext.setStatus(changedVersionable.getStatus());
		}
		
		if (changedVersionable.getEffectiveDate() != null)
		{
			usageContext.setEffectiveDate(changedVersionable.getEffectiveDate());
		}
		
		if (changedVersionable.getExpirationDate() != null)
		{
			usageContext.setExpirationDate(changedVersionable.getExpirationDate());
		}
		
		if (changedVersionable.getIsActive() != null)
		{
			usageContext.setIsActive(changedVersionable.getIsActive());
		}
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();
		
		String prevRevisionId = usageContext.getEntryState() != null? usageContext.getEntryState().getContainingRevision():null;
		
		usageContext.setEntryState(populateEntryState(ChangeType.VERSIONABLE, 
				lgRevision.getRevisionId(), prevRevisionId, 0L));
		
		CodingScheme usageContextCS = this.getLexEVSUsageContextServices().getUsageContextCodingScheme(versionOrTag);
		
		String csPrevRevisionId = usageContextCS.getEntryState() != null? usageContextCS.getEntryState().getContainingRevision():null;
		
		usageContextCS.removeAllLocalName();
		usageContextCS.removeAllRelations();
		usageContextCS.removeAllSource();
		usageContextCS.setEntities(null);
		
		Entities entities = new Entities();
		entities.addEntity(usageContext);
		
		usageContextCS.setEntities(entities);
		
		usageContextCS.setEntryState(populateEntryState(ChangeType.DEPENDENT, 
				lgRevision.getRevisionId(), csPrevRevisionId, 0L));
		
		ce.setChangedCodingSchemeEntry(usageContextCS);
		
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, revision.getSystemReleaseURI(), null);
		
		return true;
	}
	
	/**
	 * Gets the LexEVS Usage Context services.
	 * 
	 * @return the LexEVS Usage Context services
	 */
	private LexEVSUsageContextServices getLexEVSUsageContextServices() {
		return LexEVSUsageContextServicesImpl.defaultInstance();
	}
}
