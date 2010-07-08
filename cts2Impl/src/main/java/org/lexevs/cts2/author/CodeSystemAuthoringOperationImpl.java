package org.lexevs.cts2.author;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.locator.LexEvsServiceLocator;

public class CodeSystemAuthoringOperationImpl implements
		CodeSystemAuthoringOperation {
	
	private AuthoringService authServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAuthoringService();
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
	public int commitCodeSystem(CodingScheme codeSystem, URI metaMata,
			Boolean stopOnErrors, Boolean async) throws LBException {
		// TODO Auto-generated method stub

       
		
		return 0;
	}

	@Override
	public void createAssociationType() {
		// TODO Auto-generated method stub

	}

	@Override
	public CodingScheme createCodeSystem(
			AbsoluteCodingSchemeVersionReference csURIAndVersion,
			Properties codeSystemProperties) throws LBException {
		// TODO Auto-generated method stub

		
		
		return null;
	}

	@Override
	public Revision createCodeSystemChangeSet(String agent,
			String changeInstruction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createCodeSystemSuppliment() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createConcept() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAssociationType() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCodeSystemSuppliment() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCodeSystemVersion() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCodeSystemVersionStatus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateConcept() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateConceptStatus() {
		// TODO Auto-generated method stub

	}

}
