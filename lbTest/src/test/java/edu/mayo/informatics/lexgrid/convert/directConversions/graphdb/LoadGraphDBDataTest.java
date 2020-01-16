package edu.mayo.informatics.lexgrid.convert.directConversions.graphdb;

import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.junit.Test;
import org.lexevs.dao.database.graph.LexEVSRelsToGraphDao;
import org.lexevs.locator.LexEvsServiceLocator;

public class LoadGraphDBDataTest {

	static LexEVSRelsToGraphDao graphRels;
	static LexBIGService lbs;

	@Test
	public void setUp() throws Exception {
		graphRels = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getGraphingDatabaseService()
				.getRels2graph();
		LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getGraphingDatabaseService()
				.loadGraphsForTerminologyURIAndVersion("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		Thread.sleep(10000);
		lbs = LexBIGServiceImpl.defaultInstance();
	}
	
}
