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
package org.LexGrid.LexBIG.Impl.function.codednodeset;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

/**
 * The Class ExtensionCodedNodeSetTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
public class ExtensionCodedNodeSetTest extends BaseCodedNodeSetTest {
    
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.codednodeset.BaseCodedNodeSetTest#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Test Coding Scheme Extensions.";
    }
   
    public void testExtensionResolveCount() throws Exception {
        
    	int autosEntities = lbs.getNodeSet(AUTO_URN, null, null).resolve(null, null, null).numberRemaining();
    	
    	int autosExtensionEntities = lbs.getNodeSet(AUTO_EXTENSION_URN, null, null).resolve(null, null, null).numberRemaining();
    	
    	assertEquals(2, autosExtensionEntities - autosEntities);
    }
    
    
    public void testSearchExtension() throws Exception {

    	int foundEntities = lbs.getNodeSet(AUTO_EXTENSION_URN, null, null).
    		restrictToCodes(Constructors.createConceptReferenceList("DeVille")).
    			resolve(null, null, null).numberRemaining();

    	assertEquals(1, foundEntities);
    }
    
    public void testSearchParent() throws Exception {

    	int foundEntities = lbs.getNodeSet(AUTO_EXTENSION_URN, null, null).
    		restrictToCodes(Constructors.createConceptReferenceList("GM")).
    			resolve(null, null, null).numberRemaining();

    	assertEquals(1, foundEntities);
    }
    
    public void testSearchBoth() throws Exception {

    	int foundEntities = lbs.getNodeSet(AUTO_EXTENSION_URN, null, null).
    		restrictToCodes(Constructors.createConceptReferenceList(new String[]{"GM", "DeVille"})).
    			resolve(null, null, null).numberRemaining();

    	assertEquals(2, foundEntities);
    }
    
}