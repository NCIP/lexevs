package org.LexGrid.valueset.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.valuesets.sourceasserted.SourceAssertedValueSetHierarchyServices;
import org.lexgrid.valuesets.sourceasserted.impl.SourceAssertedValueSetHierarchyServicesImpl;

public class TestAssertedValueSetInitialization{
	SourceAssertedValueSetHierarchyServices hservice;
	

	public TestAssertedValueSetInitialization() {

	}

	@Before
	public void setUp() throws Exception {
		hservice = SourceAssertedValueSetHierarchyServicesImpl.defaultInstance();
	}


	@Test
	public void testGetSerivceForWrongCSParameters() {
		try {
		hservice.preprocessSourceHierarchyData("http://ncicb.nci.nih.gov/xml/owl/EVS/owl4lexevs.owl",  
				"0.1.12", 
				"Concept_In_Subset", 
				"Contributing_Source",
				"Publish_Value_Set", 
				"C54453");}
		catch(Exception e) {
			System.out.println("Fails as expected");
			assertEquals("my expected message", e.getMessage());
		}
		try {
		hservice.preprocessSourceHierarchyData("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl",  
				"0.1.5", 
				"Concept_In_Subset", 
				"Contributing_Source",
				"Publish_Value_Set", 
				"C54453");
		}catch(Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown() throws Exception {
	}

}
