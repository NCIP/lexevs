
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * The Class SortGraphTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Category(IncludeForDistributedTests.class)
public class SortGraphTest extends BaseCodedNodeGraphTest {

    /**
     * Test resolve to list associated concept count.
     * 
     * @throws Exception the exception
     */
	@Test
    public void testSortGNodeAscending() throws Exception {
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("GM", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    Constructors.createSortOptionList(new String[]{"code"}, new Boolean[]{true}), 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        assertTrue("Length: " + rcr[0].getSourceOf().getAssociation(), 
                rcr[0].getSourceOf().getAssociation().length == 1);
        
        assertTrue("Count: " + rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount(), 
                rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount() == 2);
          
        AssociatedConcept[] assocCons = rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept();
        assertTrue(assocCons[0].getCode().equals("73"));
        assertTrue(assocCons[1].getCode().equals("Chevy"));
    }
    
	@Test
    public void testSortGNodeDescending() throws Exception {
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("GM", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    Constructors.createSortOptionList(new String[]{"code"}, new Boolean[]{false}), 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        assertTrue("Length: " + rcr[0].getSourceOf().getAssociation(), 
                rcr[0].getSourceOf().getAssociation().length == 1);
        
        assertTrue("Count: " + rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount(), 
                rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConceptCount() == 2);
      
        AssociatedConcept[] assocCons = rcr[0].getSourceOf().getAssociation()[0].getAssociatedConcepts().getAssociatedConcept();
        assertTrue(assocCons[0].getCode().equals("Chevy"));
        assertTrue(assocCons[1].getCode().equals("73"));
    }
    
	@Test
    public void testSortGAssociationAscending() throws Exception {
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("A0001", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    Constructors.createSortOptionList(new String[]{"associationChildCount"}, new Boolean[]{true}), 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        Association[] assocs = rcr[0].getSourceOf().getAssociation();
        
        assertTrue(assocs.length == 2);
        
        assertTrue("Found Association: " + assocs[0].getAssociationName(),
        		assocs[0].getAssociationName().equals("hasSubtype"));
        
        assertTrue("Found Association: " + assocs[1].getAssociationName(),
                assocs[1].getAssociationName().equals("uses"));
        
    }
    
	@Test
    public void testSortGAssociationDescending() throws Exception {
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("A0001", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    Constructors.createSortOptionList(new String[]{"associationChildCount"}, new Boolean[]{false}), 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        Association[] assocs = rcr[0].getSourceOf().getAssociation();
        
        assertTrue(assocs.length == 2);
        
        assertTrue("Found Association: " + assocs[0].getAssociationName(),
                assocs[0].getAssociationName().equals("uses"));
        
        assertTrue("Found Association: " + assocs[1].getAssociationName(),
                assocs[1].getAssociationName().equals("hasSubtype"));
        
    }
    
	@Test
    public void testTwoSortsAscending() throws Exception {
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("A0001", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    Constructors.createSortOptionList(new String[]{"associationChildCount", "code"}), 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        Association[] assocs = rcr[0].getSourceOf().getAssociation();
        
        assertTrue(assocs.length == 2);
        
        assertTrue("Found Association: " + assocs[0].getAssociationName(),
                assocs[0].getAssociationName().equals("hasSubtype"));
        
        assertTrue("Found Association: " + assocs[1].getAssociationName(),
                assocs[1].getAssociationName().equals("uses"));
        
        assertTrue("Found Code: " + assocs[0].getAssociatedConcepts()
                .getAssociatedConcept(0).getCode(),
                assocs[0].getAssociatedConcepts()
                .getAssociatedConcept(0).getCode().equals("C0001"));
        
        assertTrue(assocs[1].getAssociatedConcepts()
                .getAssociatedConcept(0).getCode().equals("Batteries"));      
    }
    
	@Test
    public void testTwoSortsDescending() throws Exception {
        ResolvedConceptReference[] rcr = 
            cng.resolveAsList(Constructors.createConceptReference("A0001", AUTO_SCHEME), 
                    true, 
                    false, 
                    -1, 
                    -1, 
                    null, 
                    null, 
                    Constructors.createSortOptionList(new String[]{"associationChildCount", "code"}, new Boolean[]{false, false}), 
                    -1).getResolvedConceptReference();
        
        assertTrue("Length: " + rcr.length, 
                rcr.length == 1);
        
        Association[] assocs = rcr[0].getSourceOf().getAssociation();
        
        assertTrue(assocs.length == 2);
        
        assertTrue("Found Association: " + assocs[0].getAssociationName(),
                assocs[0].getAssociationName().equals("uses"));
        
        assertTrue("Found Association: " + assocs[1].getAssociationName(),
                assocs[1].getAssociationName().equals("hasSubtype"));
        
        assertTrue("Found Code: " + assocs[0].getAssociatedConcepts()
                .getAssociatedConcept(0).getCode(),
                assocs[0].getAssociatedConcepts()
                .getAssociatedConcept(0).getCode().equals("Tires"));
        
        assertTrue(assocs[1].getAssociatedConcepts()
                .getAssociatedConcept(0).getCode().equals("T0001"));      
    }
}