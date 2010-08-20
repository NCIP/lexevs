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
package org.LexGrid.LexBIG.Impl.bugs;

import java.lang.reflect.Method;

import org.LexGrid.LexBIG.Impl.dataAccess.SQLImplementedMethods;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.lexevs.dao.database.connection.SQLInterface;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.ResourceManager;

/**
 * This class should be used as a place to write JUnit tests which show a bug,
 * and pass when the bug is fixed.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class GForge21923 extends LexBIGServiceTestCase {
    final static String testID = "GForge21923";
    
    @Override
    protected String getTestID() {
        return testID;
    }
    
   //TODO: This only is useful for backward compatibility tests.
   public void testMapNamespaceToCodingScheme() throws Exception {
       Method method = SQLImplementedMethods.class.getDeclaredMethod("mapToCodingSchemeName", new Class[]{SQLInterface.class, String.class, String.class});
       method.setAccessible(true);
       
       SQLInterface sqlInterface = ResourceManager.instance().getSQLInterface(AUTO_SCHEME, AUTO_VERSION);
       
       int maxConnectionsPerDb = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables().getMaxConnectionsPerDB();
       
       //This used to leak one connection per call -- we'll call it 3x the defined maxConnectionsPerDb, just to make sure.
       //If this reaches the end, the JUnit will have passed. If it blocks (i.e., the pool is exhausted) this will never complete.
       for(int i=0;i< maxConnectionsPerDb * 3;i++){
           method.invoke(new SQLImplementedMethods(), sqlInterface, AUTO_SCHEME, String.valueOf(i));
       }
       
       assertTrue(true);
   }
}