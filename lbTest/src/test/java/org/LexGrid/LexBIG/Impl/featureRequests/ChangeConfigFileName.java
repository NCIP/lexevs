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
package org.LexGrid.LexBIG.Impl.featureRequests;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;

/**
 * This class should be used as a place to write JUnit tests which demonstrate a Feature Request.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class ChangeConfigFileName extends LexBIGServiceTestCase {
    final static String testID = "ChangeConfigFileName";
 
    @Override
    protected String getTestID() {
        return testID;
    }
    
    public void testLexEVSStart(){
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService(); 
        assert(lbs != null);
    }

    public void testCheckResourceManagerStart() throws LBException {
        ResourceManager rm = ResourceManager.instance();
        assert(rm != null);
    }
    
    public void testCheckResourceManagerReinit() throws LBException {
        ResourceManager.reInit(null);
    }
    
    public void testCheckForCorrectPropertyFileName() throws LBException {
       assertTrue(SystemVariables.CONFIG_FILE_NAME.equals("lbconfig.props"));
    }    
}