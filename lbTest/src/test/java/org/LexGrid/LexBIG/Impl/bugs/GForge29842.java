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


import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;

public class GForge29842 extends LexBIGServiceTestCase {
    final static String testID = "GForge29842";
    
    @Override
    protected String getTestID() {
        return testID;
    }

/**
     * 
     * 
     * GForge #29842
     * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=29842&group_id=491&atid=1850
     * 
     * @throws Throwable
     */
public void testDuplicateInfoWithAnonymousClass() throws Throwable {
    	LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
    	CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(PIZZA_SCHEME_VERSION);
    	CodedNodeGraph cng = lbs.getNodeGraph(PIZZA_SCHEME_NAME, csvt, null);
    	
    	AssociatedConcept focus = new AssociatedConcept();
    	
    	// entity isBaseOf
        focus.setCode("isBaseOf");
        focus.setCodeNamespace("pizza.owl");

        ResolvedConceptReferenceList list = cng.resolveAsList(focus, true, false, 1, -1, null, null, null, null, -1);
        Entity entity = list.getResolvedConceptReference()[0].getEntity();
        List<String> typeList = new ArrayList<String>();
        for (Property p : entity.getProperty()) {
        	if (p.getPropertyName().equals("type"))
        		typeList.add(p.getValue().getContent());
        }
        
        assertEquals(typeList.size(), 2);
        assertEquals(typeList.contains("InverseFunctional"), true);
        assertEquals(typeList.contains("Functional"), true);
        
        // entity hasSpiciness
        focus.setCode("hasSpiciness");
        list = cng.resolveAsList(focus, true, false, 1, -1, null, null, null, null, -1);
        entity = list.getResolvedConceptReference()[0].getEntity();
        typeList = new ArrayList<String>();
        for (Property p : entity.getProperty()) {
        	if (p.getPropertyName().equals("type"))
        		typeList.add(p.getValue().getContent());
        }
        
        assertEquals(typeList.size(), 1);
        assertEquals(typeList.contains("Functional"), true);
       // assertEquals(typeList.contains("ObjectProperty"), true);
        
    }
    
   
}