
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import junit.framework.Test;
import junit.framework.TestSuite;

public class MrMapQueryTests {

		public static Test suite() {
			TestSuite suite = new TestSuite(
			"MrMap Loader supporting methods Test");
	//$JUnit-BEGIN$
	suite.addTestSuite(TestLoadMrMap2Mappings.class);
	suite.addTestSuite(TestMRMapResolveGraphOnly.class);
	suite.addTestSuite(TestManifestSetup.class);
	suite.addTestSuite(TestMrMapMappingResolution.class);
	suite.addTestSuite(TestRemoveMrMap2Loads.class);
	suite.addTestSuite(TestManifestBreakDown.class);
	//$JUnit-END$
	return suite;
		}
	}