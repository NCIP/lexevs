package org.LexGrid.LexBIG.Impl.testUtility;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.LexGrid.LexBIG.Impl.helpers.ConfigureTest;

public class LoadSuite {
	
	public Test suite(){
		TestSuite mainSuite = new TestSuite("Load Tests");
        mainSuite.addTestSuite(ConfigureTest.class);
        mainSuite.addTestSuite(LoadTestDataTest.class);
        return mainSuite;
	}
}
