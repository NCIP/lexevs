
package org.LexGrid.valueset.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.LexGrid.LexBIG.Impl.testUtility.AllTestsNormalConfig;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.valueset.impl.LexEVSPickListServicesImplTest;
import org.junit.runners.model.InitializationError;

/**
 * Main test suite to test PickList.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class PickListAllTests {

	public static Test suite() throws InitializationError {
		TestSuite suite = new TestSuite(
				"Test for org.LexGrid.valueset.LexEVSPickListServices");
		ServiceHolder.configureForSingleConfig();
		
		//$JUnit-BEGIN$
		suite.addTest(AllTestsNormalConfig.orderedSuite(LoadTestDataTest.class));
		suite.addTestSuite(LexEVSPickListServicesImplTest.class);
		suite.addTestSuite(CleanUpTest.class);
		//$JUnit-END$
		return suite;
	}

}