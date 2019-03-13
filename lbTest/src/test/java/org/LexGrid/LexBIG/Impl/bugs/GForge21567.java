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

import java.util.Arrays;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.lexevs.system.ResourceManager;

public class GForge21567 extends LexBIGServiceTestCase {
    final static String testID = "GForge21567";
    
    @Override
    protected String getTestID() {
        return testID;
    }

public void testResourceManagerThreadInitialization(){
	ResourceManager mgr = new ResourceManager();
	boolean memberThreadExists = Arrays.asList(mgr.getClass().getDeclaredFields()).stream().
	 filter(x -> x.getName().equals("deactivatorThread_")).findFirst().isPresent();
	assertFalse(memberThreadExists);
	boolean runnableClassExists = Arrays.asList(mgr.getClass().getDeclaredClasses()).stream().filter(x -> x.getName().equals("FutureDeactivatorThread")).findFirst().isPresent();
	assertFalse(runnableClassExists);
}

}