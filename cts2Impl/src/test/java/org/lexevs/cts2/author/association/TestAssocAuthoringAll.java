package org.lexevs.cts2.author.association;
import junit.framework.Test;
import junit.framework.TestSuite;
public class TestAssocAuthoringAll {




		public static Test suite() {
			TestSuite suite = new TestSuite(
					"Mapping Authoring Test");
			//$JUnit-BEGIN$
			suite.addTestSuite(LoadAutoTestData.class);
			suite.addTestSuite(TestCTS2AssociationAuthoring.class);
			suite.addTestSuite(TestRemoveAutoTerms.class);
			suite.addTestSuite(TestRemoveCreatedMapping.class);
			suite.addTestSuite(TestRemoveRevisions.class);
			//$JUnit-END$
			return suite;
		}
}
