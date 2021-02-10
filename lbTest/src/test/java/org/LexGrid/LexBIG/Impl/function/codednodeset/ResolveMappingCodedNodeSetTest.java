
package org.LexGrid.LexBIG.Impl.function.codednodeset;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;

public class ResolveMappingCodedNodeSetTest extends BaseCodedNodeSetTest {

    @Override
    protected String getTestID() {
        return "ResolveMappingCodedNodeSet Tests";
    }


    public void testResolveMappingScheme() throws LBException{
    	CodedNodeSet cns = 
    		lbs.getNodeSet(MAPPING_SCHEME_URI, null, null);
    	
    	ResolvedConceptReferenceList list = cns.resolveToList(null, null, null, -1);
    	
    	assertEquals(9,list.getResolvedConceptReferenceCount());
    	
    	DataTestUtils.isConceptReferencePresent(list, "E0001");
    	DataTestUtils.isConceptReferencePresent(list, "Jaguar");
    	DataTestUtils.isConceptReferencePresent(list, "A0001");
    	DataTestUtils.isConceptReferencePresent(list, "R0001");
    	DataTestUtils.isConceptReferencePresent(list, "C0001");
    	DataTestUtils.isConceptReferencePresent(list, "C0002");
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
    	
    	assertEquals(6,list.getResolvedConceptReference().length);
    	
    	/*
    	"C0001"
    	"C0002"
    	"005"
    	"Ford"
    	"Jaguar"
    	"R0001"
    	*/
    	DataTestUtils.isConceptReferencePresent(list, "Jaguar");
    	DataTestUtils.isConceptReferencePresent(list, "R0001");
    	DataTestUtils.isConceptReferencePresent(list, "C0001");
    	DataTestUtils.isConceptReferencePresent(list, "C0002");
    	DataTestUtils.isConceptReferencePresent(list, "005");
    	DataTestUtils.isConceptReferencePresent(list, "Ford");	
    }
}