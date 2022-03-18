
package org.LexGrid.LexBIG.Impl.testUtility;


import org.LexGrid.LexBIG.LexBIGService.LexBIGService;

public interface LexBIGServiceTestFactory {

    public final static String LBS_TEST_FACTORY_ENV = "LBS_TEST_FACTORY";

    LexBIGService getLbs();

}