
package org.LexGrid.LexBIG.Impl.function.codednodegraph;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * The Class ToNodeListTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ToNodeListTest extends BaseCodedNodeGraphTest {

   
    /**
     * Test to node list no focus.
     * 
     * @throws Exception the exception
     */
    public void testToNodeListNoFocus() throws Exception {
        CodedNodeSet cns = cng.toNodeList(null, true, false, -1, -1);
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, null, -1).getResolvedConceptReference();
        assertTrue(refs.length > 0);
    }
    
    /**
     * Test to node list no focus zero levels.
     * 
     * @throws Exception the exception
     */
    public void testToNodeListNoFocusZeroLevels() throws Exception {
        CodedNodeSet cns = cng.toNodeList(null, true, false, 0, -1);
        ResolvedConceptReference[] refs = cns.resolveToList(null, null, null, null, -1).getResolvedConceptReference();
        assertEquals(2,refs.length);
    }
     
    /**
     * Test to node list with focus one level.
     * 
     * @throws Exception the exception
     */
    public void testToNodeListWithFocusOneLevel() throws Exception {
        CodedNodeSet cns = cng.toNodeList(Constructors.createConceptReference("005", AUTO_SCHEME), true, false, 1, -1);
        ResolvedConceptReference[] refs = cns.resolveToList(
                null,
                null, 
                null, 
                -1).getResolvedConceptReference();
        assertTrue("Length: " + refs.length, refs.length == 4);
    }
    
    public void testUnionToNodeList() throws Exception {
        CodedNodeSet cns1 = cng.toNodeList(Constructors.createConceptReference("005", AUTO_SCHEME), true, false, 1, -1);
        CodedNodeSet cns2 = cng.toNodeList(Constructors.createConceptReference("A0001", AUTO_SCHEME), true, false, 1, -1);
        
        CodedNodeSet union = cns1.union(cns2);
        ResolvedConceptReference[] refs = union.resolveToList(
                null,
                null, 
                null, 
                -1).getResolvedConceptReference();
        assertEquals(10,refs.length);
    }

    public void testUnionToNodeListNotInCodedNodeSet() throws Exception {
        CodedNodeSet cns1 = cng.toNodeList(Constructors.createConceptReference("Batteries", "ExpendableParts"), true, false, 1, -1);
        CodedNodeSet cns2 = cng.toNodeList(Constructors.createConceptReference("Tires", "ExpendableParts"), true, false, 1, -1);
        
        CodedNodeSet union = cns1.union(cns2);
        ResolvedConceptReference[] refs = union.resolveToList(
                null,
                null, 
                null, 
                -1).getResolvedConceptReference();
        assertEquals(2,refs.length);
    }
    
    public void testUnionToNodeListSomeInSomeNotInCodedNodeSet() throws Exception {
        CodedNodeSet cns1 = cng.toNodeList(Constructors.createConceptReference("A0001", AUTO_SCHEME), true, false, -1, -1);
        CodedNodeSet cns2 = cng.toNodeList(Constructors.createConceptReference("Tires", "ExpendableParts"), true, false, -1, -1);
        
        CodedNodeSet union = cns1.union(cns2);
        ResolvedConceptReference[] refs = union.resolveToList(
                null,
                null, 
                null, 
                -1).getResolvedConceptReference();
        assertEquals(7,refs.length);
        
        for(ConceptReference ref : refs) {
        	System.out.println(ref.getCode());
        }
    }
    
    public void testToNodeListSomeNotStoredEntities() throws Exception {

        CodedNodeSet cns2 = cng.toNodeList(Constructors.createConceptReference("Tires", "ExpendableParts"), true, false, -1, -1);
        ResolvedConceptReference[] refs = cns2.resolveToList(
                null,
                null, 
                null, 
                -1).getResolvedConceptReference();
        assertEquals(1,refs.length);
        for(ConceptReference ref : refs) {
        	System.out.println(ref.getCode());
        }
    }
    

}