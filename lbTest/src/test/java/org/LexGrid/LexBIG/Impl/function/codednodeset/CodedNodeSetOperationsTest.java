
package org.LexGrid.LexBIG.Impl.function.codednodeset;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class CodedNodeSetOperationsTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class CodedNodeSetOperationsTest extends BaseCodedNodeSetTest {
    
    /** The unioned coded node set. */
    private CodedNodeSet unionedCodedNodeSet;
    
    /** The intersected coded node set. */
    private CodedNodeSet intersectedCodedNodeSet;
    
    /** The differenced coded node set. */
    private CodedNodeSet differencedCodedNodeSet;
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.codednodeset.BaseCodedNodeSetTest#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Test for mixing together CodedNodeSet-type operations.";
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.function.codednodeset.BaseCodedNodeSetTest#setUp()
     */
    @Override
    @Before
    public void setUp(){
        super.setUp();
        try {
            CodedNodeSet unionedCodedNodeSetPart1 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
            unionedCodedNodeSetPart1 = unionedCodedNodeSetPart1.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy" }, "Automobiles"));
            
            CodedNodeSet unionedCodedNodeSetPart2 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
            unionedCodedNodeSetPart2 = unionedCodedNodeSetPart2.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "GM" }, "Automobiles"));
            
            unionedCodedNodeSet = unionedCodedNodeSetPart1.union(unionedCodedNodeSetPart2);
            
            CodedNodeSet intersectedCodedNodeSetPart1 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
            intersectedCodedNodeSetPart1 = intersectedCodedNodeSetPart1.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy" }, "Automobiles"));
            
            CodedNodeSet intersectedCodedNodeSetPart2 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
            intersectedCodedNodeSetPart2 = intersectedCodedNodeSetPart2.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Chevy", "GM" }, "Automobiles"));
            
            intersectedCodedNodeSet = intersectedCodedNodeSetPart1.intersect(intersectedCodedNodeSetPart2);
            
            CodedNodeSet differencedCodedNodeSetPart1 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
            differencedCodedNodeSetPart1 = differencedCodedNodeSetPart1.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy" }, "Automobiles"));
            
            CodedNodeSet differencedCodedNodeSetPart2 = lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
            differencedCodedNodeSetPart2 = differencedCodedNodeSetPart2.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "Chevy", "GM" }, "Automobiles"));
            
            differencedCodedNodeSet = differencedCodedNodeSetPart1.difference(differencedCodedNodeSetPart2);
        } catch (Exception e) {
            System.err.print(e);
            fail(e.getMessage());
        } 
    }
    
  /**
   * Test intersect a union.
   * 
   * @throws Exception the exception
   */
  @Test
  public void testIntersectAUnion() throws Exception {

        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "GM" }, "Automobiles"));

        CodedNodeSet result = cns.intersect(unionedCodedNodeSet);

        ResolvedConceptReference[] rcr = result.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertTrue(rcr.length == 2);
        assertTrue(contains(rcr, "GM", "Automobiles"));
        assertTrue(contains(rcr, "005", "Automobiles"));
    }
    
    /**
     * Test union an intersection.
     * 
     * @throws Exception the exception
     */
  @Test
    public void testUnionAnIntersection() throws Exception {

        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy" }, "Automobiles"));

        CodedNodeSet result = cns.union(intersectedCodedNodeSet);

        ResolvedConceptReference[] rcr = result.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertTrue(rcr.length == 2);
        assertTrue(contains(rcr, "Chevy", "Automobiles"));
        assertTrue(contains(rcr, "005", "Automobiles"));
    }
    
 /**
  * Test intersect a difference.
  * 
  * @throws Exception the exception
  */
  @Test
 public void testIntersectADifference() throws Exception {
        
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "005", "Chevy" }, "Automobiles"));

        CodedNodeSet result = cns.intersect(differencedCodedNodeSet);

        ResolvedConceptReference[] rcr = result.resolveToList(null, null, null, 50).getResolvedConceptReference();

        assertTrue(rcr.length == 1);
        assertTrue(contains(rcr, "005", "Automobiles"));
    }
}