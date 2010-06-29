/**
 * 
 */
package org.lexevs.cts2.author;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * @author m004181
 *
 */
public class ValueSetAuthoringOperationImpl implements
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
		
		Revision lexEVSrevision = getLexGridRevisionObject(revision);
		
		ValueSetDefinition vsd = vsdServ_.getValueSetDefinitionByUri(valueSetURI);
		
		//TODO
		
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#addValueSetProperty(java.net.URI, org.LexGrid.commonTypes.Property, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)
	 */
	@Override
	public boolean addValueSetProperty(URI valueSetURI, Property newProperty,
			RevisionInfo revision, EntryState propertyEntryState)
			throws LBException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.ValueSetAuthoringOperation#createValueSet(java.net.URI, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.List, org.LexGrid.commonTypes.Properties, org.LexGrid.valueSets.DefinitionEntry, org.LexGrid.commonTypes.Versionable, org.lexevs.cts2.core.update.RevisionInfo, org.LexGrid.versions.EntryState)
	 */
	@Override
	public URI createValueSet(URI valueSetURI, String valueSetName,
			String defaultCodeSystem, String conceptDomainId,
			List<Source> sourceList, List<String> usageContext,
			Properties properties, DefinitionEntry ruleSet,
			Versionable versionable, RevisionInfo revision,
			EntryState entryState) throws LBException {
		// TODO Auto-generated method stub
		return null;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private Revision getLexGridRevisionObject(RevisionInfo revisionInfo)
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
		lgRevision.setRevisionDate(revisionInfo.getRevisionDate());
		lgRevision.setRevisionId(revisionInfo.getRevisionId());
		
		return lgRevision;
	}

}
