
package edu.mayo.informatics.lexgrid.convert.directConversions.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for edu.mayo.informatics.lexgrid.convert.directConversions.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(TextUtilityTest.class);
		//$JUnit-END$
		return suite;
	}

}