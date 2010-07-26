package org.lexevs.cts2.author;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.cts2.core.update.RevisionInfo;
import org.lexevs.cts2.exception.author.InvalidCodeSystemSupplementException;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.locator.LexEvsServiceLocator;

public class CodeSystemAuthoringOperationImpl extends AuthoringCore implements
		CodeSystemAuthoringOperation {
	
	private AuthoringService authServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
	private IndexServiceManager indexService_ = LexEvsServiceLocator.getInstance().getIndexServiceManager();
	
	@SuppressWarnings("unused")

	@Override
	public int commitChangeSet(Revision changeSet) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int commitCodeSystem(CodingScheme codeSystem, RevisionInfo revision) throws LBException {
        
        // Create the changed entry for code system
       ChangedEntry changedEntry = new ChangedEntry();
       changedEntry.setChangedCodingSchemeEntry(codeSystem);
       
       // Create revision object
       Revision lgRevision = getLexGridRevisionObject(revision);
       
       // Add code system changed entry to revision
       lgRevision.addChangedEntry(changedEntry);
       
       // Since this is a new code system, Set Entry State NEW
       codeSystem.setEntryState(populateEntryState(ChangeType.NEW, lgRevision.getRevisionId(), null, 0L));
       
       //load as revision
       authServ_.loadRevision(lgRevision, null);
       AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();
       reference.setCodingSchemeURN(codeSystem.getCodingSchemeURI());
       reference.setCodingSchemeVersion(codeSystem.getRepresentsVersion());
       
       //index the loaded coding scheme
       indexService_.getEntityIndexService().createIndex(reference); 
		
		return 0;
	}

	@Override
	public void createAssociationType() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public CodingScheme createCodeSystem(RevisionInfo revision, String codingSchemeName, String codingSchemeURI, String formalName,
            String defaultLanguage, long approxNumConcepts, String representsVersion, List<String> localNameList,
            List<Source> sourceList, Text copyright, Mappings mappings, Properties properties, Entities entities,
            List<Relations>  relationsList) throws LBException {

	      if(codingSchemeName == null){
	            throw new LBException("Coding scheme name cannot be null");
	        }
	        if(codingSchemeURI == null){
	            throw new LBException("Coding scheme URI cannot be null");
	        }
	        if(representsVersion == null){
	            throw new LBException("Coding scheme version cannot be null");
	        }
	        if(mappings == null){
	            throw new LBException("Coding scheme mappings cannot be null");
	        }
	        
	        CodingScheme scheme = new CodingScheme();
	        
	        scheme.setCodingSchemeName(codingSchemeName);
	        
	        scheme.setCodingSchemeURI(codingSchemeURI);
	 
	        scheme.setFormalName(formalName);

	        scheme.setDefaultLanguage(defaultLanguage);
	        
	        scheme.setApproxNumConcepts(approxNumConcepts);
	        
	        scheme.setRepresentsVersion(representsVersion);

	        scheme.setLocalName(localNameList);

	        scheme.setSource(sourceList);

	        scheme.setCopyright(copyright);

	        scheme.setMappings(mappings);

	        scheme.setProperties(properties);
	        
	        scheme.setEntities(entities);
	        
		
	        // Ensure RevisionInfo is provided
	        validateRevisionInfo(revision);
	        
	        commitCodeSystem(scheme, revision);
	        
	        return scheme;
	}
	
	



	@Override
	public Revision createCodeSystemChangeSet(String agent,
			String changeInstruction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createCodeSystemSuppliment(
			AbsoluteCodingSchemeVersionReference parent,
			AbsoluteCodingSchemeVersionReference supplement) throws InvalidCodeSystemSupplementException {
		try {
			LexBIGServiceImpl.defaultInstance().getServiceManager(null).
				registerCodingSchemeAsSupplement(parent, supplement);
		} catch (LBException e) {
			throw new InvalidCodeSystemSupplementException(
					parent, 
					supplement, 
					e);
		} 
	}

	@Override
	public void createConcept(
			String codingSchemeUri, 
			String codeSystemVersion, 
			String conceptCode, 
			String namespace, 
			RevisionInfo revisionInfo) throws LBException {
		
		if(! this.getSystemResourceService().containsCodingSchemeResource(codingSchemeUri, codeSystemVersion)) {
			throw new LBException("The Coding Scheme URI: " +  codingSchemeUri +
					" Version: " + codeSystemVersion + " does not exist. Before creating a Concept, "
					+ " the Coding Scheme must exist.");
		}
		
		Revision revision = super.getLexGridRevisionObject(revisionInfo);
		
		EntryState entryState = 
			this.populateEntryState(ChangeType.NEW, revision.getRevisionId(), null, 0l);
		
		Entity entity = new Entity();
		entity.setEntityCode(conceptCode);
		entity.setEntityCodeNamespace(namespace);
		entity.setEntryState(entryState);
		
		CodingScheme cs = new CodingScheme();
		cs.setEntryState(this.populateEntryState(ChangeType.DEPENDENT, revision.getRevisionId(), null, 0l));
		
		cs.getEntities().addEntity(entity);
		
		this.getDatabaseServiceManager().getAuthoringService().loadRevision(revision, revisionInfo.getSystemReleaseURI());
	}

	@Override
	public void updateAssociationType() {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateCodeSystemSuppliment() {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateCodeSystemVersion(String codingSchemeUri,
			String codeSystemVersion) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateCodeSystemVersionStatus(String codingSchemeUri,
			String codeSystemVersion) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateConcept() {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateConceptStatus() {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}
}
