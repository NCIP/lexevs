
package org.LexGrid.LexBIG.Impl.function.mapping;

import java.util.HashSet;
import java.util.Set;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class MappingTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class MappingToNodeListTest extends LexBIGServiceTestCase {

	@Override
	protected String getTestID() {
		return this.getClass().getName();
	}
	
    @Test
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
    
    @Test
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