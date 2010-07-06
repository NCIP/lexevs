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
import org.lexevs.cts2.BaseService;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * @author m004181
 *
 */
public class ValueSetAuthoringOperationImpl extends BaseService implements
		ValueSetAuthoringOperation {
	
	private ValueSetDefinitionService vsdServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetDefinitionService();
	private AuthoringService authServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
	@SuppressWarnings("unused")
	private LexEvsCTS2 lexEvsCts2_;
	
	public ValueSetAuthoringOperationImpl(LexEvsCTS2 lexEvsCts2){
    	this.lexEvsCts2_ = lexEvsCts2;
    }
	
	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#addDefinitionEntry(java.net.URI, org.LexGrid.valueSets.DefinitionEntry, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)
	 */
	@Override
	public boolean addDefinitionEntry(URI valueSetURI,
			DefinitionEntry newDefinitionEntry, RevisionInfo revision,
			EntryState definitionEntryState) throws LBException {
		if (revision == null)
			throw new LBException("Revision information can not be empty");
		
		if (definitionEntryState == null)
			throw new LBException("Entry state information can not be empty");
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		
		if (vsd == null)
		{
			throw new LBException("No Value set definition found with uri : " + valueSetURI);
		}
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		newDefinitionEntry.setEntryState(definitionEntryState);
		
		EntryState vsdEntryState = vsd.getEntryState();
		
		if (vsdEntryState == null)
		{
			vsdEntryState = new EntryState();
		}
		
		String vsdPrevRevisionId = vsdEntryState.getContainingRevision();
		
		vsdEntryState.setChangeType(ChangeType.DEPENDENT);
		vsdEntryState.setContainingRevision(definitionEntryState.getContainingRevision());
		vsdEntryState.setRelativeOrder(definitionEntryState.getRelativeOrder());
		vsdEntryState.setPrevRevision(vsdPrevRevisionId);
		
		vsd.setEntryState(vsdEntryState);
		
		// remove any existing definition entry from the vsd object
		vsd.removeAllDefinitionEntry();		
		vsd.addDefinitionEntry(newDefinitionEntry);
		
		ChangedEntry ce = new ChangedEntry();
		ce.setChangedValueSetDefinitionEntry(vsd);
		
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, null);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#addValueSetProperty(java.net.URI, org.LexGrid.commonTypes.Property, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)
	 */
	@Override
	public boolean addValueSetProperty(URI valueSetURI, Property newProperty,
			RevisionInfo revision, EntryState propertyEntryState)
			throws LBException {
		if (valueSetURI == null)
			throw new LBException("Value Set Definition URI can not be empty");
		if (newProperty == null)
			throw new LBException("New property can not be empty");
		if (propertyEntryState == null)
			throw new LBException("Entry state object for new property can not be empty");
		if (revision == null)
			throw new LBException("Revision information can not be empty");
		if (!propertyEntryState.getChangeType().equals(ChangeType.NEW))
			throw new LBException("Change type for new property should be 'NEW'");
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		ChangedEntry ce = new ChangedEntry();
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		String prevRevisionId = vsd.getEntryState() != null?vsd.getEntryState().getContainingRevision():null;
		vsd.removeAllDefinitionEntry();
		vsd.removeAllRepresentsRealmOrContext();
		vsd.removeAllSource();
		
		EntryState vsdEntryState = new EntryState();
		vsdEntryState.setChangeType(ChangeType.DEPENDENT);
		vsdEntryState.setContainingRevision(propertyEntryState.getContainingRevision());
		vsdEntryState.setPrevRevision(prevRevisionId);
		vsdEntryState.setRelativeOrder(0L);
		vsd.setEntryState(vsdEntryState);
		
		newProperty.setEntryState(propertyEntryState);
		Properties props = new Properties();
		props.addProperty(newProperty);
		vsd.setProperties(props);
		
		ce.setChangedValueSetDefinitionEntry(vsd);
		lgRevision.addChangedEntry(ce);
		
		authServ_.loadRevision(lgRevision, null);
		// TODO Auto-generated method stub
		return false;
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
			Versionable versionable, RevisionInfo revision,
			EntryState entryState) throws LBException {
		if (valueSetURI == null)
			throw new LBException("Value Set Definition URI can not be empty");
		
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
		
		return createValueSet(vsd, revision, entryState);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#createValueSet(org.LexGrid.valueSets.ValueSetDefinition, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)
	 */
	@Override
	public URI createValueSet(ValueSetDefinition valueSetDefininition,
			RevisionInfo revision, EntryState valueSetEntryState)
			throws LBException {
		if (valueSetDefininition == null)
			throw new LBException("ValueSetDefinition object can not be empty");
		
		if (revision == null)
			throw new LBException("Revision information can not be empty");
		
		if (valueSetEntryState == null)
			throw new LBException("valueSet entry state information can not be empty");
		
		if (!valueSetEntryState.getChangeType().equals(ChangeType.NEW))
			throw new LBException("Change type for new value set definition should be 'NEW'");
		
		Revision lgRevision = getLexGridRevisionObject(revision);
		valueSetDefininition.setEntryState(valueSetEntryState);
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
			DefinitionEntry changedDefinitionEntry, RevisionInfo revision,
			EntryState definitionEntryState) throws LBException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#updateValueSetMetaData(java.net.URI, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.List, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateValueSetMetaData(URI valueSetURI, String valueSetName,
			String defaultCodeSystem, String conceptDomainId,
			List<Source> sourceList, List<String> usageContext,
			RevisionInfo revision) throws LBException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#updateValueSetProperty(java.net.URI, org.LexGrid.commonTypes.Property, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)
	 */
	@Override
	public boolean updateValueSetProperty(URI valueSetURI,
			Property changedProperty, RevisionInfo revision,
			EntryState propertyEntryState) throws LBException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#updateValueSetStatus(java.net.URI, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)
	 */
	@Override
	public boolean updateValueSetStatus(URI valueSetURI, String status,
			RevisionInfo revision, EntryState valueSetEntryState)
			throws LBException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#updateValueSetVersionable(java.net.URI, org.LexGrid.commonTypes.Versionable, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)
	 */
	@Override
	public boolean updateValueSetVersionable(URI valueSetURI,
			Versionable changedVersionable, RevisionInfo revision,
			EntryState valueSetEntryState) throws LBException {
		// TODO Auto-generated method stub
		return false;
	}
}
