package org.LexGrid.LexBIG.LexBIGService;

import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.Exceptions.LBException;

public interface LexEVSAuthoringService {
	
	public void createCodingScheme()throws LBException;
	public void createEntity()throws LBException;
	public void createRelationsContainer()throws LBException;
	public void createAssociationPredicate()throws LBException;
	public void createAssociationSource()throws LBException;
	public void createAssociationTarget()throws LBException;
	public Association createAssociation()throws LBException;
	public void createMapping()throws LBException;
	

}
