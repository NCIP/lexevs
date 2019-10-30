package edu.mayo.informatics.lexgrid.convert.directConversions.graphdb;

import org.junit.Test;
import org.lexevs.dao.database.graph.LexEVSRelsToGraphDao;
import org.lexevs.locator.LexEvsServiceLocator;

public class CleanUpGraphDBDataTest {
	
	@Test
	public void removeDatabases(){
		LexEVSRelsToGraphDao graphRels = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getGraphingDatabaseService()
				.getRels2graph();
		
		graphRels.getGraphSourceMgr().getDataSource("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5").dropGraphsAndDatabaseForDataSource();
	}
}
