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

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedNamespace;
import org.apache.commons.lang.StringUtils;

/**
 * This class should be used as a place to write JUnit tests which show a bug,
 * and pass when the bug is fixed.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class GForge19628 extends LexBIGServiceTestCase {
    final static String testID = "GForge19628";
    
    private LexBIGService lbs;

    @Override
    protected String getTestID() {
        return testID;
    }

    /*
     * LexBIG Bug #19628 -
     * https://gforge.nci.nih.gov/tracker/?func=detail&aid=19628&group_id=491&atid=1850
     */
    //for an EMF-based load
    public void setUp(){
        lbs = ServiceHolder.instance().getLexBIGService();    
    }
    
    public void testDefaultSupportedNamespaceXML() throws Exception {
        CodingScheme cs = lbs.resolveCodingScheme(LexBIGServiceTestCase.AUTO_SCHEME, null);
        SupportedNamespace[] namespaces = cs.getMappings().getSupportedNamespace();
        
        boolean found = false;
        
        for(SupportedNamespace namespace : namespaces){
            if(namespace.getLocalId().equals(LexBIGServiceTestCase.AUTO_SCHEME)
                && StringUtils.equals(namespace.getEquivalentCodingScheme(), LexBIGServiceTestCase.AUTO_SCHEME)){
                found = true;
            }
        }
        
        assertTrue(found);       
    }
    
    //for a non UMLS load
    public void testDefaultSupportedNamespaceUMLS() throws Exception {
        CodingScheme cs = lbs.resolveCodingScheme(LexBIGServiceTestCase.AIR_SCHEME, null);
        SupportedNamespace[] namespaces = cs.getMappings().getSupportedNamespace();
        
        boolean found = false;
        
        for(SupportedNamespace namespace : namespaces){
            if(namespace.getLocalId().equals(LexBIGServiceTestCase.AIR_SCHEME)
                && StringUtils.equals(namespace.getEquivalentCodingScheme(),LexBIGServiceTestCase.AIR_SCHEME)){
                found = true;
            }
        }
        
        assertTrue(found);       
    }
    
    //for a Meta load
    public void testDefaultSupportedNamespaceMeta() throws Exception {
        CodingScheme cs = lbs.resolveCodingScheme(LexBIGServiceTestCase.META_SCHEME, null);
        SupportedNamespace[] namespaces = cs.getMappings().getSupportedNamespace();
        
        boolean found = false;
        
        for(SupportedNamespace namespace : namespaces){
            if(namespace.getLocalId().equals(LexBIGServiceTestCase.META_SCHEME)
                && StringUtils.equals(namespace.getEquivalentCodingScheme(),LexBIGServiceTestCase.META_SCHEME)){
                found = true;
            }
        }    
        assertTrue(found);       
    }
}