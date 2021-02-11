
package org.LexGrid.LexBIG.mapping;

import junit.framework.Test;
import junit.framework.TestSuite;

public class MappingAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Mapping Authoring Test");
		//$JUnit-BEGIN$
		suite.addTestSuite(LexEVSAssociationStatusUpdateTest.class);
		suite.addTestSuite(LexEVSAssociationPredicateCreationTest.class);
		suite.addTestSuite(LexEVSMappingCreationHelperMethodTest.class);
		suite.addTestSuite(LexEVSMappingCreationTest.class);
		suite.addTestSuite(LexEVSLoadedMappingsTest.class);
		//$JUnit-END$
		return suite;
	}
}