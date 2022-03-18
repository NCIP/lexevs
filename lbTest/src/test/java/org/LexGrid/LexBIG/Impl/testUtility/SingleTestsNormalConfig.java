
package org.LexGrid.LexBIG.Impl.testUtility;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.LexBIGServiceConvenienceMethodsImplTest;
import org.LexGrid.LexBIG.Impl.function.query.TestHierarchyAPI;
import org.LexGrid.LexBIG.Impl.function.query.TestTransitiveClosure;

public class SingleTestsNormalConfig {

    public static Test suite() throws Exception {
        TestSuite mainSuite = new TestSuite("LexBIG validation tests");
        ServiceHolder.configureForSingleConfig();

         LoadTestDataTest loader= new  LoadTestDataTest();
         //loader.testLoadGenericOwl();
         //loader.testLoadOwl();
         //loader.testLoadOwlLoaderPreferences();
         loader.testLoadAutombiles();
        //mainSuite.addTestSuite(LoadTestDataTest.class);
        mainSuite.addTestSuite(LexBIGServiceConvenienceMethodsImplTest.class);
        //mainSuite.addTestSuite(TestCodedNodeGraphSqlGeneration.class);
//        CleanUpTest cleanup= new CleanUpTest();
//        cleanup.testRemoveAutombiles();
         //cleanup.testRemoveObo();
        //mainSuite.addTestSuite(CleanUpTest.class);

        // $JUnit-END$

        return mainSuite;
    }
}