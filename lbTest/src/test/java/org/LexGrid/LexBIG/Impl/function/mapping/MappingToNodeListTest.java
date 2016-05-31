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
package org.LexGrid.LexBIG.Impl.function.mapping;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.RemoteApiSafeTest;

import java.util.HashSet;
import java.util.Set;

/**
 * The Class MappingTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RemoteApiSafeTest
public class MappingToNodeListTest extends LexBIGServiceTestCase {

	@Override
	protected String getTestID() {
		return this.getClass().getName();
	}
	
    public void testGetSourceCodesToNodeList() throws Exception {
    	LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        
    	CodedNodeGraph mappingGraph = 
            lbs.getNodeGraph(MAPPING_SCHEME_URI, null, null);
    	
    	CodedNodeSet cns = mappingGraph.restrictToSourceCodeSystem(AUTO_SCHEME).toNodeList(null, true, false, 0, -1);
    	
    	ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
  
    	int count = 0;
    	
    	Set<String> foundCodes = new HashSet<String>();
    	
    	while(itr.hasNext()) {
    		count++;
    		foundCodes.add(itr.next().getCode());
    	}
        
    	assertEquals(6,count);
    	assertEquals(6,foundCodes.size());
    	
    	assertTrue(foundCodes.contains("Jaguar"));
    	assertTrue(foundCodes.contains("A0001"));
    	assertTrue(foundCodes.contains("C0001"));
    	assertTrue(foundCodes.contains("005"));
    	assertTrue(foundCodes.contains("Ford"));
    	assertTrue(foundCodes.contains("C0002"));
    } 
    
    public void testGetTargetCodesToNodeList() throws Exception {
    	LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        
    	CodedNodeGraph mappingGraph = 
            lbs.getNodeGraph(MAPPING_SCHEME_URI, null, null);
    	
    	CodedNodeSet cns = mappingGraph.restrictToTargetCodeSystem(PARTS_SCHEME).toNodeList(null, false, true, 0, -1);
    	
    	ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
  
    	int count = 0;
    	
    	Set<String> foundCodes = new HashSet<String>();
    	
    	while(itr.hasNext()) {
    		count++;
    		foundCodes.add(itr.next().getCode());
    	}
     
    	assertEquals(3,foundCodes.size());
    	
    	assertTrue(foundCodes.contains("R0001"));
    	assertTrue(foundCodes.contains("E0001"));
    	assertTrue(foundCodes.contains("P0001"));
    } 
}