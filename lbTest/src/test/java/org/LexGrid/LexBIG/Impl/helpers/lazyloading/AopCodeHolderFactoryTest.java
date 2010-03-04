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
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import java.util.List;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.helpers.CodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;

/**
 * The Class LazyCodeHolderFactoryTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AopCodeHolderFactoryTest extends LexBIGServiceTestCase {

    /** The code holder. */
    private CodeHolder codeHolder;

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return "LazyCodeHolderFactory Tests";
    }
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp(){
        AopCodeHolderFactory factory = new AopCodeHolderFactory();

        try {
            codeHolder = factory.
            buildCodeHolder(super.AUTO_SCHEME,
                    super.AUTO_VERSION, null,null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Test create proxy.
     */
    public void testCreateProxy(){
        assertNotNull(codeHolder);
    }
    
    /**
     * Test get lazy loading code to return.
     */
    public void testGetLazyLoadingCodeToReturn(){
        List<CodeToReturn> codes = codeHolder.getAllCodes();
        assertTrue(codes.size() > 0);
    }
    
    /**
     * Test ceck code to return.
     */
    public void testCheckCodeToReturn(){
        List<CodeToReturn> codes = codeHolder.getAllCodes();
        CodeToReturn code = codes.get(0);
        
        assertTrue("Code: " + code.getCode(), code.getCode().equals("005"));
        assertTrue(code.getEntityDescription().equals("Domestic Auto Makers"));
        assertTrue(code.getUri(),
                code.getUri().equals(AUTO_URN));
        assertTrue(code.getVersion(),
                code.getVersion().equals(AUTO_VERSION));
        assertTrue(code.getEntityTypes().length == 1);
        assertTrue(code.getEntityTypes()[0].equals("concept"));
        assertNotNull(code.getScore());
    }
}
