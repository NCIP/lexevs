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

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.util.PrintUtility;

public class ResolveMappingCodedNodeSetTest extends BaseCodedNodeSetTest {

    @Override
    protected String getTestID() {
        return "ResolveMappingCodedNodeSet Tests";
    }


    public void testResolveMappingScheme() throws LBException{
    	CodedNodeSet cns = 
    		lbs.getNodeSet(MAPPING_SCHEME_URI, null, null);
    	
    	ResolvedConceptReferenceList list = cns.resolveToList(null, null, null, -1);
    	
    	PrintUtility.print(list);
    	
    	assertEquals(10,list.getResolvedConceptReferenceCount());
    	
    	DataTestUtils.isConceptReferencePresent(list, "E0001");
    	DataTestUtils.isConceptReferencePresent(list, "Jaguar");
    	DataTestUtils.isConceptReferencePresent(list, "A0001");
    	DataTestUtils.isConceptReferencePresent(list, "R0001");
    	DataTestUtils.isConceptReferencePresent(list, "C0001");
    	DataTestUtils.isConceptReferencePresent(list, "T0001");
    	DataTestUtils.isConceptReferencePresent(list, "005");
    	DataTestUtils.isConceptReferencePresent(list, "P0001");
    	DataTestUtils.isConceptReferencePresent(list, "Ford");
    }
    
    public void testSearchMapping() throws LBException{
    	CodedNodeSet cns = 
    		lbs.getNodeSet(MAPPING_SCHEME_URI, null, null);
 
    	cns = cns.restrictToMatchingDesignations("Jaguar", SearchDesignationOption.ALL, "LuceneQuery", null);
    	
    	ResolvedConceptReferenceList list = cns.resolveToList(null, null, null, -1);
    	
    	assertEquals(1,list.getResolvedConceptReference().length);
    	
    	assertEquals("Jaguar",list.getResolvedConceptReference()[0].getCode());
    }
    
    public void testSearchMappingBothCodingSchemes() throws LBException{
    	CodedNodeSet cns = 
    		lbs.getNodeSet(MAPPING_SCHEME_URI, null, null);
 
    	cns = cns.restrictToMatchingDesignations("*r*", SearchDesignationOption.ALL, "LuceneQuery", null);
    	
    	ResolvedConceptReferenceList list = cns.resolveToList(null, null, null, -1);
    	
    	PrintUtility.print(list);
    	
    	assertEquals(7,list.getResolvedConceptReference().length);
    	
    	/*
    	"C0001"
    	"005"
    	"Ford"
    	"T0001"
    	"Jaguar"
    	"R0001"
    	*/
    	DataTestUtils.isConceptReferencePresent(list, "Jaguar");
    	DataTestUtils.isConceptReferencePresent(list, "R0001");
    	DataTestUtils.isConceptReferencePresent(list, "C0001");
    	DataTestUtils.isConceptReferencePresent(list, "T0001");
    	DataTestUtils.isConceptReferencePresent(list, "005");
    	DataTestUtils.isConceptReferencePresent(list, "Ford");	
    }
}