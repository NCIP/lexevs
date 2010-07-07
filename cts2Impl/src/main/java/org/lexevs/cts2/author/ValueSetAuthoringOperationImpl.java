/**
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
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * @author m004181
 *
 */
public class ValueSetAuthoringOperationImpl extends AuthoringCore implements
		ValueSetAuthoringOperation {
	
	private ValueSetDefinitionService vsdServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetDefinitionService();
	private AuthoringService authServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
	@SuppressWarnings("unused")
	private LexEvsCTS2 lexEvsCts2_;
	
	public ValueSetAuthoringOperationImpl(LexEvsCTS2 lexEvsCts2){
    	this.lexEvsCts2_ = lexEvsCts2;
    }
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#addDefinitionEntry(java.net.URI, org.LexGrid.valueSets.DefinitionEntry, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean addDefinitionEntry(URI valueSetURI,
			DefinitionEntry newDefinitionEntry, RevisionInfo revision) throws LBException {
		if (revision == null)
			throw new LBException("Revision information can not be empty");
		
		if (revision.getRevisionId() == null)
			throw new LBException("Revision ID can not be empty");
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		
		if (vsd == null)
		{
			throw new LBException("No Value set definition found with uri : " + valueSetURI);
		}
		// remove any existing definition entry from the vsd object
		vsd.removeAllDefinitionEntry();
		vsd.removeAllRepresentsRealmOrContext();
		vsd.removeAllSource();
		
		String prevRevisionId = vsd.getEntryState() != null?vsd.getEntryState().getContainingRevision():null;
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		
		// setup entrystate for vsd
		EntryState entryState = new EntryState();
		entryState.setChangeType(ChangeType.DEPENDENT);
		entryState.setContainingRevision(lgRevision.getRevisionId());
		entryState.setRelativeOrder(0L);
		entryState.setPrevRevision(prevRevisionId);		
		vsd.setEntryState(entryState);
		
		// setup entrystate for new definition entry
		entryState = new EntryState();
		entryState.setChangeType(ChangeType.NEW);
		entryState.setContainingRevision(lgRevision.getRevisionId());
		entryState.setRelativeOrder(0L);
		entryState.setPrevRevision(null);		
		newDefinitionEntry.setEntryState(entryState);
		
		// add new definition entry to vsd
		vsd.addDefinitionEntry(newDefinitionEntry);
		
		ChangedEntry ce = new ChangedEntry();
		ce.setChangedValueSetDefinitionEntry(vsd);
		
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, null);
		
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
		if (revision == null)
			throw new LBException("Revision information can not be empty");
		if (revision.getRevisionId() == null)
			throw new LBException("Revision ID can not be empty");
		
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
		EntryState entryState = new EntryState();
		entryState.setChangeType(ChangeType.DEPENDENT);
		entryState.setContainingRevision(lgRevision.getRevisionId());
		entryState.setPrevRevision(prevRevisionId);
		entryState.setRelativeOrder(0L);
		vsd.setEntryState(entryState);
		
		// setup entry state for new property
		entryState = new EntryState();
		entryState.setChangeType(ChangeType.NEW);
		entryState.setContainingRevision(lgRevision.getRevisionId());
		entryState.setPrevRevision(prevRevisionId);
		entryState.setRelativeOrder(0L);
		newProperty.setEntryState(entryState);
		
		Properties props = new Properties();
		props.addProperty(newProperty);
		vsd.setProperties(props);
		
		ce.setChangedValueSetDefinitionEntry(vsd);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, null);
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
		if (revision == null)
			throw new LBException("Revision information can not be empty");
		if (revision.getRevisionId() == null)
			throw new LBException("Revision ID can not be empty");
		
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
		
		if (revision == null)
			throw new LBException("Revision information can not be empty");
		
		if (revision.getRevisionId() == null)
			throw new LBException("Revision ID can not be empty");
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		// setup entry state for new vsd
		EntryState entryState = new EntryState();
		entryState.setChangeType(ChangeType.NEW);
		entryState.setContainingRevision(lgRevision.getRevisionId());
		entryState.setPrevRevision(null);
		entryState.setRelativeOrder(0L);
		valueSetDefininition.setEntryState(entryState);
		
		ChangedEntry ce = new ChangedEntry();
		ce.setChangedValueSetDefinitionEntry(valueSetDefininition);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, null);
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
		
		if (revision == null)
			throw new LBException("Revision information can not be empty");
		
		if (revision.getRevisionId() == null)
			throw new LBException("Revision ID can not be empty");
		
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
			throw new LBException("No Definition Entry found with Rule Order : " + valueSetURI.toString());
		
		String prevRevisionId = vsd.getEntryState() != null?vsd.getEntryState().getContainingRevision():null;
		vsd.removeAllDefinitionEntry();
		vsd.removeAllRepresentsRealmOrContext();
		vsd.removeAllSource();
		
		// setup entry state for vsd
		EntryState entryState = new EntryState();
		entryState.setChangeType(ChangeType.DEPENDENT);
		entryState.setContainingRevision(lgRevision.getRevisionId());
		entryState.setPrevRevision(prevRevisionId);
		entryState.setRelativeOrder(0L);
		vsd.setEntryState(entryState);
		
		// setup entry state for update definition entry
		String defEntryPrecRevId = currentDefEntry.getEntryState() != null?currentDefEntry.getEntryState().getContainingRevision():null;
		entryState = new EntryState();
		entryState.setChangeType(ChangeType.MODIFY);
		entryState.setContainingRevision(lgRevision.getRevisionId());
		entryState.setPrevRevision(defEntryPrecRevId);
		entryState.setRelativeOrder(0L);
		changedDefinitionEntry.setEntryState(entryState);
		
		vsd.addDefinitionEntry(changedDefinitionEntry);
		
		ce.setChangedValueSetDefinitionEntry(vsd);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, null);
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
		
		if (revision == null)
			throw new LBException("Revision information can not be empty");
		
		if (revision.getRevisionId() == null)
			throw new LBException("Revision ID can not be empty");
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		if (vsd == null)
			throw new LBException("No Value Set Definition found with URI : " + valueSetURI.toString());
		
		String prevRevisionId = vsd.getEntryState() != null?vsd.getEntryState().getContainingRevision():null;
		vsd.removeAllDefinitionEntry();
		vsd.removeAllRepresentsRealmOrContext();
		vsd.removeAllSource();
		vsd.setProperties(null);
		
		if (StringUtils.isNotEmpty(valueSetName))
			vsd.setValueSetDefinitionName(valueSetName);
		
		if (StringUtils.isNotEmpty(defaultCodeSystem))
			vsd.setDefaultCodingScheme(defaultCodeSystem);
		
		if (StringUtils.isNotEmpty(conceptDomainId))
			vsd.setConceptDomain(conceptDomainId);
		
		if (sourceList != null)
			vsd.setSource(sourceList);
		
		if (usageContext != null)
			vsd.setRepresentsRealmOrContext(usageContext);
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();
		EntryState vsdEntryState = new EntryState();
		vsdEntryState.setChangeType(ChangeType.MODIFY);
		vsdEntryState.setContainingRevision(lgRevision.getRevisionId());
		vsdEntryState.setPrevRevision(prevRevisionId);
		vsdEntryState.setRelativeOrder(0L);
		vsd.setEntryState(vsdEntryState);
		
		ce.setChangedValueSetDefinitionEntry(vsd);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, null);
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#updateValueSetProperty(java.net.URI, org.LexGrid.commonTypes.Property, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)
	 */
	@Override
	public boolean updateValueSetProperty(URI valueSetURI,
			Property changedProperty, RevisionInfo revision) throws LBException {
		// TODO Auto-generated method stub
		return false;
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
		if (revision == null)
			throw new LBException("Revision information can not be empty");
		
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
		
		if (revision == null)
			throw new LBException("Revision information can not be empty");
		
		if (changedVersionable == null)
			throw new LBException("Changed Versionable information can not be empty");
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		if (vsd == null)
			throw new LBException("No Value Set Definition found with URI : " + valueSetURI.toString());
		
		String prevRevisionId = vsd.getEntryState() != null?vsd.getEntryState().getContainingRevision():null;
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
		
		EntryState vsdEntryState = new EntryState();
		vsdEntryState.setChangeType(ChangeType.VERSIONABLE);
		vsdEntryState.setContainingRevision(lgRevision.getRevisionId());
		vsdEntryState.setPrevRevision(prevRevisionId);
		vsdEntryState.setRelativeOrder(0L);
		
		vsd.setEntryState(vsdEntryState);
		
		ce.setChangedValueSetDefinitionEntry(vsd);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, null);
		return true;
	}
}
