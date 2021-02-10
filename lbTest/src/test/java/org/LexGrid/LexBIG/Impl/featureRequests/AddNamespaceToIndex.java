
package org.LexGrid.LexBIG.Impl.featureRequests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

/**
 * This class should be used as a place to write JUnit tests which demonstrate a Feature Request.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class AddNamespaceToIndex extends LexBIGServiceTestCase {
    final static String testID = "AddNamespaceToIndex";

    private LexBIGService lbs;
    
    public void setUp(){
        lbs = ServiceHolder.instance().getLexBIGService(); 
    }
    
    @Override
    protected String getTestID() {
        return testID;
    }

    /**
     * Try to pull out the Namespace from a non-resolved ConceptReference
     */
    public void testCheckForNamespaceInConceptReferenceNotNull() throws LBException {
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.AUTO_SCHEME, null);
        ResolvedConceptReferencesIterator rcri = cns.resolve(null, null, null, null, false);
        
        assertTrue(rcri.hasNext());
        while(rcri.hasNext()){
            assertTrue(rcri.next().getCodeNamespace() != null);
        }
    }
    
    /**
     * Try to pull out the Namespace from a non-resolved ConceptReference - and check that they are correct.
     */
    public void testCheckForCorrectNamespaceInConceptReference() throws LBException {
        CodedNodeSet cns = lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.PARTS_SCHEME, null);
        ResolvedConceptReferencesIterator rcri = cns.resolve(null, null, null, null, false);
        
        
        Map<String,String> codeNamespaceMap = new HashMap<String,String>();
        codeNamespaceMap.put("E0001", "GermanMadePartsNamespace");
        codeNamespaceMap.put("P0001", "GermanMadePartsNamespace");
        codeNamespaceMap.put("R0001", "GermanMadePartsNamespace");
        codeNamespaceMap.put("T0001", "GermanMadePartsNamespace");
        codeNamespaceMap.put("codeWithMultipleNs", "ns1");
        codeNamespaceMap.put("codeWithMultipleNs", "ns2");
        codeNamespaceMap.put("DifferentNamespaceEntity", "SomeOtherNamespace");
        
        int counter = 0;
        
        assertTrue(rcri.hasNext());
        
        Set<String> foundNamespaces = new HashSet<String>();
        
        while(rcri.hasNext()){
            counter++;
            ResolvedConceptReference ref = rcri.next();
            String entityCode = ref.getCode();
            String entityNamesapce = ref.getCodeNamespace();
            if(entityCode.equals("codeWithMultipleNs")) {
            	foundNamespaces.add(entityNamesapce);
            	continue;
            }
            assertTrue(codeNamespaceMap.get(entityCode).equals(entityNamesapce));
        }
        
        assertTrue(counter == 7);   
        assertEquals(2,foundNamespaces.size());
        assertTrue(foundNamespaces.contains("ns1"));
    	assertTrue(foundNamespaces.contains("ns2"));
    }
}