
package org.LexGrid.valueset.test;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.LexGrid.LexBIG.Impl.testUtility.AllTestsNormalConfig;
import org.LexGrid.valueset.impl.AssertedVSHierarchyTest;
import org.LexGrid.valueset.impl.LexEVSPickListServicesImplTest;
import org.LexGrid.valueset.impl.LexEVSResolvedValueSetInvalidParamsTest;
import org.LexGrid.valueset.impl.LexEVSResolvedValueSetTest;
import org.LexGrid.valueset.impl.LexEVSValueSetDefServicesImplTest;
import org.lexevs.dao.database.ibatis.valuesets.IbatisSourceAssertedValueSetDaoTest;

import edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets.EntityToVSDTransFormerTest;
import edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets.ExternalResolvedValueSetIndexingTest;

/**
 * Main test suite to test Value Set and Pick List Definition.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class VDAllTests {

	public static Test suite() throws Exception {
		TestSuite suite = new TestSuite(
				"LG Value Set and Pick List Definition Test");
		//$JUnit-BEGIN$
		suite.addTest(AllTestsNormalConfig.orderedSuite(LoadTestDataTest.class));
		suite.addTest(new JUnit4TestAdapter(IbatisSourceAssertedValueSetDaoTest.class));
		suite.addTestSuite(LexEVSValueSetDefServicesImplTest.class);
		suite.addTestSuite(EntityToVSDTransFormerTest.class);
		suite.addTestSuite(LexEVSPickListServicesImplTest.class);
		suite.addTest(new JUnit4TestAdapter(LexEVSResolvedValueSetTest.class));
		suite.addTest(new JUnit4TestAdapter(LexEVSResolvedValueSetInvalidParamsTest.class));
		suite.addTest(new JUnit4TestAdapter(ExternalResolvedValueSetIndexingTest.class));
		suite.addTestSuite(AssertedVSHierarchyTest.class);
		suite.addTestSuite(CleanUpTest.class);
		//$JUnit-END$
		return suite;
	}

}