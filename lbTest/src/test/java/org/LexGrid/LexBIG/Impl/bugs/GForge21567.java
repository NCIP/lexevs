/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.bugs;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import java.util.Arrays;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.aspectj.util.Reflection;
import org.lexevs.system.ResourceManager;

public class GForge21567 extends LexBIGServiceTestCase {
    final static String testID = "GForge21567";
    
    @Override
    protected String getTestID() {
        return testID;
    }

/**
     * Test putting values in the SystemResourceService cache in a multithreaded environment.
     * 
     * @throws Throwable
     */
public void testThreadSafeResourceManagerCache() throws Throwable {

        ResourceManager rm = ResourceManager.instance();

        TestRunnable[] runnables = {
                new TestResourceManagerPut(rm, 1000, 1),
                new TestResourceManagerPut(rm, 1000, 1)
        };

        MultiThreadedTestRunner runner = new MultiThreadedTestRunner(runnables);

        runner.runTestRunnables();
    }

public void testResourceManagerThreadInitialization(){
	ResourceManager mgr = new ResourceManager();
	boolean memberThreadExists = Arrays.asList(mgr.getClass().getDeclaredFields()).stream().
	 filter(x -> x.getName().equals("deactivatorThread_")).findFirst().isPresent();
	assertFalse(memberThreadExists);
	boolean runnableClassExists = Arrays.asList(mgr.getClass().getDeclaredClasses()).stream().filter(x -> x.getName().equals("FutureDeactivatorThread")).findFirst().isPresent();
	assertFalse(runnableClassExists);
}

    class TestResourceManagerPut extends TestRunnable {
        private int count;
        private int sleepTime;
        private ResourceManager rm;

        public TestResourceManagerPut( ResourceManager rm, int count, int delay )
        {
            this.rm = rm;
            this.count = count;
            this.sleepTime = delay;
        }

        public void runTest() throws Throwable {
            for (int i = 0; i < this.count; ++i) {
                Thread.sleep( this.sleepTime );
                rm.getCache().put(i, i + i);
            }
        }
    }   
}