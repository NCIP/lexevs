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
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.cts2.exception.author.InvalidCodeSystemSupplementException;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.locator.LexEvsServiceLocator;

public class CodeSystemAuthoringOperationImpl implements
		CodeSystemAuthoringOperation {
	
	private AuthoringService authServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
	private IndexServiceManager indexService_ = LexEvsServiceLocator.getInstance().getIndexServiceManager();
	@SuppressWarnings("unused")
	private LexEvsCTS2 lexEvsCts2_;
	
	public CodeSystemAuthoringOperationImpl(LexEvsCTS2 lexEvsCts2){
    	this.lexEvsCts2_ = lexEvsCts2;
    }

	@Override
	public int commitChangeSet(Revision changeSet) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int commitCodeSystem(CodingScheme codeSystem, Revision revision, EntryState entryState) throws LBException {
		// TODO Auto-generated method stub
	      
		//Ensure RevisionId consistency
        revision.setRevisionId(entryState.getContainingRevision());
        
        //Ensure Previous revision id is correct in the entry state. 
       // enforcePreviousRevisionId(scheme, entryState); 
        
        
        EntryState newEntryState = new EntryState();
        
        newEntryState.setChangeType(ChangeType.NEW);
        
        if(entryState.getContainingRevision()!= null)
        	newEntryState.setContainingRevision(entryState.getContainingRevision());
        if(entryState.getPrevRevision()!= null)
        	newEntryState.setPrevRevision(entryState.getPrevRevision());
        if(entryState.getRelativeOrder()!= null)
        	newEntryState.setRelativeOrder(entryState.getRelativeOrder());
                
        newEntryState.setPrevRevision(null);
        
        codeSystem.setEntryState(newEntryState);
        //Set some entry states

       ChangedEntry changedEntry = new ChangedEntry();
       changedEntry.setChangedCodingSchemeEntry(codeSystem);
       
       revision.addChangedEntry(changedEntry);
       
       //load as revision
       authServ_.loadRevision(revision, null);
       AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();
       reference.setCodingSchemeURN(codeSystem.getCodingSchemeURI());
       reference.setCodingSchemeVersion(codeSystem.getRepresentsVersion());
       
       //index the loaded mapping coding scheme
       indexService_.getEntityIndexService().createIndex(reference); 
       
		
		return 0;
	}

	@Override
	public void createAssociationType() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public CodingScheme createCodeSystem(Revision revision, String codingSchemeName, String codingSchemeURI, String formalName,
            String defaultLanguage, long approxNumConcepts, String representsVersion, List<String> localNameList,
            List<Source> sourceList, Text copyright, Mappings mappings, Properties properties, Entities entities,
            List<Relations>  relationsList, EntryState entryState) throws LBException {
		// TODO Auto-generated method stub

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
	public void createConcept() {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
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
