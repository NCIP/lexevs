
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;



import junit.framework.Test;
import junit.framework.TestSuite;

public class MrMapAllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite(
		"MrMap Loader supporting methods Test");
//$JUnit-BEGIN$
suite.addTestSuite(AddTargetToExistingSourceTest.class);
suite.addTestSuite(CreateAssociationTargetTest.class);
suite.addTestSuite(CreateNewAssociationDataTest .class);
suite.addTestSuite(CreateNewAssociationSourceWithTargetTest.class);
suite.addTestSuite(GetAssociationQualifiersTest.class);
suite.addTestSuite(MrMapProcessorTest.class);
suite.addTestSuite(MrMapReaderTest.class);
suite.addTestSuite(ProcessMrSatToRelationsTest.class);
suite.addTestSuite(ProcessMrSatRowTest.class);
suite.addTestSuite(TestMapParse.class);
suite.addTest(MrMapQueryTests.suite());
//$JUnit-END$
return suite;
	}
}