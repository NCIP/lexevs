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
package org.LexGrid.LexBIG.Impl.helpers;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.dataAccess.CleanUpUtility;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.lexevs.system.ResourceManager;

/**
 * this test reconfigured us to single db mode. Call this before rerunning the
 * tests.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class ConfigureTest extends TestCase {
    public void testConfigureLexBig() throws LBException {
        ServiceHolder.instance().configureNext();

        // make sure there aren't any old database tables / databases floating
        // around
        // that shouldn't be here.
        // Clean-up utility is disabled now for Multi-database mode.
        if(ResourceManager.instance().getSystemVariables().getAutoLoadSingleDBMode()){
            CleanUpUtility.removeAllUnusedResources();
        }
    }
}