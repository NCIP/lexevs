package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import junit.framework.Test;
import junit.framework.TestSuite;

public class MrMapQueryTests {

		public static Test suite() {
			TestSuite suite = new TestSuite(
			"MrMap Loader supporting methods Test");
	//$JUnit-BEGIN$
	suite.addTestSuite(TestLoadMrMapOneMapping.class);
	suite.addTestSuite(TestManifestSetup.class);
	suite.addTestSuite(TestMrMapMappingResolution.class);
	suite.addTestSuite(TestRemoveMrMapOneLoad .class);
	suite.addTestSuite(TestManifestBreakDown.class);
	//$JUnit-END$
	return suite;
		}
	}
