
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class RootsTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class RootsTest extends BaseCodedNodeGraphTest {

    /**
     * Test resolve to list associated concept count.
     * 
     * @throws Exception the exception
     */
	@Test
    public void testGetRoots() throws Exception {
    	ResolvedConceptReferenceList roots = 
    		this.cng.resolveAsList(null, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(2,roots.getResolvedConceptReferenceCount());
    	
    	ResolvedConceptReference ref1 = DataTestUtils.getConceptReference(roots.getResolvedConceptReference(), "005");
    	assertNotNull(ref1);
    	ResolvedConceptReference ref2 = DataTestUtils.getConceptReference(roots.getResolvedConceptReference(), "A0001");
    	assertNotNull(ref2);
    }
    
	@Test
    public void testGetRootsWithAssociationRestriction() throws Exception {
    	ResolvedConceptReferenceList roots = 
    		this.cng.restrictToAssociations(Constructors.createNameAndValueList("uses"), null).
    			resolveAsList(null, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(2,roots.getResolvedConceptReferenceCount());
    	
    	ResolvedConceptReference ref1 = DataTestUtils.getConceptReference(roots.getResolvedConceptReference(), "A0001");
    	assertNotNull(ref1);
    	ResolvedConceptReference ref2 = DataTestUtils.getConceptReference(roots.getResolvedConceptReference(), "Ford");
    	assertNotNull(ref2);
    }
    
	@Test
    public void testGetRootsWithAssociationAndQualifierRestriction() throws Exception {
    	ResolvedConceptReferenceList roots = 
    		this.cng.restrictToAssociations(Constructors.createNameAndValueList("hasSubtype"), Constructors.createNameAndValueList("hasEngine", "true")).
    			resolveAsList(null, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(1,roots.getResolvedConceptReferenceCount());
    	
    	ResolvedConceptReference ref1 = DataTestUtils.getConceptReference(roots.getResolvedConceptReference(), "A0001");
    	assertNotNull(ref1);
    }
    
	@Test
    public void testGetRootsWithAssociationAndWrongQualifierRestriction() throws Exception {
    	ResolvedConceptReferenceList roots = 
    		this.cng.restrictToAssociations(Constructors.createNameAndValueList("hasSubtype"), Constructors.createNameAndValueList("hasEngine", "false")).
    			resolveAsList(null, true, false, 0, -1, null, null, null, -1);
    	
    	assertEquals(0,roots.getResolvedConceptReferenceCount());
    }
}