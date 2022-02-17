
package org.LexGrid.valuesets.sourceasserted.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.valuesets.LexEVSTreeItem;
import org.lexevs.dao.database.service.valuesets.ValueSetHierarchyServiceImpl;
import org.lexgrid.valuesets.sourceasserted.impl.SourceAssertedValueSetHierarchyServicesImpl;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;


public class TestAssertedValueSetInitialization {
	SourceAssertedValueSetHierarchyServicesImpl hservice;
	SourceAssertedValueSetHierarchyServicesImpl spyService;
	String excptMess = "There was a problem connecting to the persistence store for the resource:"
			+ " URI: http://ncicb.nci.nih.gov/xml/owl/EVS/owl4lexevs.owl, Version: 0.1.12."
			+ "\nPlease make sure this resource exists in the system.";
	String postInitMess = "Problem retrieving a production version for coding scheme " 
			+ "with uri: http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
	ValueSetHierarchyServiceImpl vss;

	@Before
	public void setUp() throws Exception {

		hservice = (SourceAssertedValueSetHierarchyServicesImpl) SourceAssertedValueSetHierarchyServicesImpl.defaultInstance();
		spyService = Mockito.spy(hservice);
		Mockito.doAnswer((Answer) x ->	{spyService.preprocessSourceHierarchyData("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5",
				"Concept_In_Subset", "Contributing_Source", "Publish_Value_Set", "C54453"); return null; }).when(spyService).preprocessSourceHierarchyData();
	}

	@Test
	public void testGetSerivceForWrongCSParameters() {
		try {
			//Bad request to a scheme that doesn't exist.  
			spyService.preprocessSourceHierarchyData("http://ncicb.nci.nih.gov/xml/owl/EVS/owl4lexevs.owl", "0.1.12",
					"Concept_In_Subset", "Contributing_Source", "Publish_Value_Set", "C54453");
		} catch (Exception e) {
            //Should fast fail with the values of the bad scheme gone				
			System.out.println("Fails as expected");
			assertEquals(excptMess, e.getMessage());
		}
		try {
			//Initializing against the default will fail
			//Since it's not a test scheme. We check message output
			//to insure it's default values
			hservice.preprocessSourceHierarchyData();
		} catch (Exception e) {
			System.out.println("Fails as expected");
			e.printStackTrace();
			assertEquals(postInitMess, e.getMessage());
		}		
//		try {
//			//Smoke test of the new init
//			HashMap<String, LexEVSTreeItem> map = spyService.getFullServiceValueSetTree();
//			assertTrue(map.size() > 0);
//		} catch (Exception e) {
//			fail(e.getMessage());
//			e.printStackTrace();
//		}
	}

}