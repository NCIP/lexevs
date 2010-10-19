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

import java.sql.Timestamp;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;

public class GForge21211 extends TestCase {

    public void testPropertyEntryState() throws Exception {
        
    	LexBIGService service = ServiceHolder.instance().getLexBIGService();

    	CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
    	versionOrTag.setVersion("1.0");

    	CodedNodeSet nodeSet = service.getCodingSchemeConcepts("Automobiles", versionOrTag);

    	nodeSet.restrictToCodes(Constructors.createConceptReferenceList("Ford"));

    	ResolvedConceptReferenceList resolvedCodeList = nodeSet.resolveToList(null, null, null, 1);

    	ResolvedConceptReference resolvedCode = resolvedCodeList.getResolvedConceptReference()[0];

    	assertNotNull(resolvedCode);

    	Entity ford = resolvedCode.getEntity();

    	assertNotNull(ford);

    	Property[] properties = ford.getAllProperties();

    	assertTrue(properties.length == 2);

    	for (int i = 0; i < properties.length; i++) {

    		String propId = properties[i].getPropertyId();

    		if( "p1".equals(propId)) {

    			assertEquals(properties[i].getEffectiveDate().getTime(), 
    					Timestamp.valueOf("2006-05-04 18:13:51").getTime());

    			assertEquals(properties[i].getExpirationDate().getTime(), 
    					Timestamp.valueOf("2006-06-04 18:13:51").getTime());

    			assertEquals(properties[i].getStatus(), "Active");

    		} else if( "p2".equals(propId)) {

    			assertNull(properties[i].getEntryState());

    			assertNull(properties[i].getEffectiveDate());

    			assertNull(properties[i].getExpirationDate());

    			assertNull(properties[i].getStatus());
    		}
    	}

    }
}