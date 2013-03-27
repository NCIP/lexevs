package org.lexevs.graph.load.connect;

import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.association.AssociationService;
import org.lexevs.dao.database.service.association.AssociationTargetService;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.locator.LexEvsServiceLocator;

public class LexEVSServiceConnect {
	public LexEVSServiceConnect(){
		locator = getServiceLocator();
	}

	LexEvsServiceLocator locator;
	
	public LexEvsServiceLocator getServiceLocator(){
		return LexEvsServiceLocator.getInstance();
	}
	
	public DatabaseServiceManager getLexEvsDataBaseService(){
		return locator.getDatabaseServiceManager();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
