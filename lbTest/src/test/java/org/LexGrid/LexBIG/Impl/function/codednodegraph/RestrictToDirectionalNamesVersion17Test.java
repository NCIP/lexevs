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
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import java.lang.reflect.Field;

import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;
import org.LexGrid.LexBIG.Impl.dataAccess.SQLInterface;

/**
 * The Class RestrictToDirectionalNamesVersion17Test.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RestrictToDirectionalNamesVersion17Test extends RestrictToDirectionalNamesTest {

    /** The table version for test. */
    private String tableVersionForTest = "1.7";
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.codednodegraph.BaseCodedNodeGraphTest#getTestID()
     */
    @Override
    protected String getTestID() {
       return "RestrictToDirectionalNames Table Version 1.7 Tests";
    } 
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.codednodegraph.BaseCodedNodeGraphTest#setUp()
     */
    public void setUp() throws Exception {
        SQLInterface si = ResourceManager.instance().getSQLInterface(AUTO_SCHEME, AUTO_VERSION);
        assertTrue(si.getSQLTableConstants().getVersion().equals("1.8"));
        this.setTableVersion(si, tableVersionForTest);
        assertTrue(
                ResourceManager.instance().getSQLInterface(AUTO_SCHEME, AUTO_VERSION).
                getSQLTableConstants().
                getVersion().equals(tableVersionForTest));
        super.setUp();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
        ResourceManager.reInit(null);
        SQLInterface si = ResourceManager.instance().getSQLInterface(AUTO_SCHEME, AUTO_VERSION);
        assertTrue(si.getSQLTableConstants().getVersion().equals("1.8"));
    }  
    
    /**
     * Sets the table version.
     * 
     * @param si the si
     * @param version the version
     * 
     * @throws Exception the exception
     */
    private void setTableVersion(SQLInterface si, String version) throws Exception {
        Field field = si.getSQLTableConstants().getClass().getDeclaredField("version_");
        field.setAccessible(true);
        field.set(si.getSQLTableConstants(), version);
    }  
}
