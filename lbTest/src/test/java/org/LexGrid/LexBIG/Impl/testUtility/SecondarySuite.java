
package org.LexGrid.LexBIG.Impl.testUtility;

import org.LexGrid.LexBIG.Impl.helpers.ConfigureTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SecondarySuite {
	
	public static Test suite() throws Exception{
		
		TestSuite mainSuite = new TestSuite("ValueSet, XML export and MRMAP load tests");
		
		ServiceHolder.configureForSingleConfig();
		mainSuite.addTestSuite(ConfigureTest.class);
        //ValueSets tests
        mainSuite.addTest(org.LexGrid.valueset.test.VDAllTests.suite());
        
        //LexGrid XML Exporter tests
        mainSuite.addTest(org.LexGrid.LexBIG.Impl.export.xml.lgxml.AllTests.suite());
        
        //MRMAP RRF load tests
        mainSuite.addTest(edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MrMapAllTests.suite());
        return mainSuite;
	}

}