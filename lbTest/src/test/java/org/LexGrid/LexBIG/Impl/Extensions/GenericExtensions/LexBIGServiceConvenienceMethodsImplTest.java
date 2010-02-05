/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions;

import java.util.Map;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;

public class LexBIGServiceConvenienceMethodsImplTest extends LexBIGServiceTestCase {
    final static String testID = "LexBIGServiceConvenienceMethodsImplTest";

    private LexBIGService lbs;
    private LexBIGServiceConvenienceMethodsImpl lbscm;
    
    public void setUp(){
        lbs = ServiceHolder.instance().getLexBIGService(); 
        lbscm = new LexBIGServiceConvenienceMethodsImpl();
        lbscm.setLexBIGService(lbs);
    }
    
    @Override
    protected String getTestID() {
        return testID;
    }
    
    public void testThreadSafeCodingSchemeCaches() throws Throwable {
        Map cache = lbscm.getCache_CodingSchemes();
        runCacheThreadSaveTest(cache);       
    }
    
    public void testThreadSafeCopyRightsCaches() throws Throwable {
        Map cache = lbscm.getCache_CopyRights();
        runCacheThreadSaveTest(cache);        
    }
    
    public void testThreadSafeHIDCaches() throws Throwable {
        Map cache = lbscm.getCache_HIDs();
        runCacheThreadSaveTest(cache);      
    }
    
    public void testThreadSafeHPathToRootExistsCaches() throws Throwable {
        Map cache = lbscm.getCache_HPathToRootExists();
        runCacheThreadSaveTest(cache);      
    }
    
    public void testThreadSafeHRootCodesCaches() throws Throwable {
        Map cache = lbscm.getCache_HRootCodes();
        runCacheThreadSaveTest(cache);       
    }
    
    public void testThreadSafeHRootsCaches() throws Throwable {
        Map cache = lbscm.getCache_HRoots();
        runCacheThreadSaveTest(cache);      
    }
    
    /*
     * Using this test Association:
     * 
     * <lgRel:association associationName="differentEntityCodeAssoc" 
     * entityCode="A1" forwardName="differentEntityCodeAssocForward" 
     * isNavigable="true" isTransitive="false" 
     * reverseName="differentEntityCodeAssocReverse">
     */
    public void testGetAssociationNameFromAssociationCode() throws Exception {
    	String assocName = lbscm.getAssociationNameFromAssociationCode(AUTO_SCHEME, null, "A1");
    	assertTrue(assocName.equals("differentEntityCodeAssoc"));
    }
    
    public void testGetAssociationCodeFromAssociationName() throws Exception {
    	String assocCode = lbscm.getAssociationCodeFromAssociationName(AUTO_SCHEME, null, "differentEntityCodeAssoc");
    	assertTrue(assocCode.equals("A1"));
    }
    
    public void testGetAssociationNameFromAssociationCodeWrong() throws Exception {
    	try {
			lbscm.getAssociationNameFromAssociationCode(AUTO_SCHEME, null, "A1WRONG");
		} catch (LBParameterException e) {
			//this is good -- doesn't exist
			return;
		}
		fail("Should have thrown an exception.");
    }
    
    public void testGetAssociationCodeFromAssociationNameWrong() throws Exception {
    	try {
			lbscm.getAssociationCodeFromAssociationName(AUTO_SCHEME, null, "differentEntityCodeAssocWRONG");
		} catch (LBParameterException e) {
			//this is good -- doesn't exist
			return;
		}
		fail("Should have thrown an exception.");
    }
    
    public void testGetAssociationNameFromAssociationCodeSameCodeAndName() throws Exception {
    	String assocName = lbscm.getAssociationNameFromAssociationCode(AUTO_SCHEME, null, "hasSubtype");
    	assertTrue(assocName.equals("hasSubtype"));
    }
    
    public void testGetAssociationCodeFromAssociationNameSameCodeAndName() throws Exception {
    	String assocCode = lbscm.getAssociationCodeFromAssociationName(AUTO_SCHEME, null, "hasSubtype");
    	assertTrue(assocCode.equals("hasSubtype"));
    }
    
    protected void runCacheThreadSaveTest(Map cache) throws Throwable {
        TestRunnable[] runnables = {
                new TestCachePut(cache, 1000, 1),
                new TestCachePut(cache, 1000, 1)
        };

        MultiThreadedTestRunner runner = new MultiThreadedTestRunner(runnables);

        runner.runTestRunnables();   
    }
  
    
    class TestCachePut extends TestRunnable {
        private int count;
        private int sleepTime;
        private Map cache;

        public TestCachePut( Map cache, int count, int delay )
        {
            this.cache = cache;
            this.count = count;
            this.sleepTime = delay;
        }

        public void runTest() throws Throwable {
            for (int i = 0; i < this.count; ++i) {
                Thread.sleep( this.sleepTime );
                cache.put(i, i + i);
            }
        }
    }   
}
