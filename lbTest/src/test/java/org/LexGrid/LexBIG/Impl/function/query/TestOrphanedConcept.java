
package org.LexGrid.LexBIG.Impl.function.query;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * This testcase checks that the hierarchy api works as desired.
 * 
 * @author Pradip Kanjamala
 * 
 */
public class TestOrphanedConcept extends LexBIGServiceTestCase {

    final static String testID = "T1_FNC_150";

    @Override
    protected String getTestID() {
        return testID;
    }

    /**
     * Test getting the root concept of an OBO ontology
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testOrphanedConceptOBO() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(CELL_VERSION);
        String[] hierarchyIDs = lbscm.getHierarchyIDs(CELL_URN, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("IS_A"))
                hierarchyId = hierarchy;
        }

        ResolvedConceptReferenceList rcrl = lbscm.getHierarchyOrphanedConcepts(CELL_URN, csvt, hierarchyId);
        assertTrue(findMatchingConcept("CL:0000070", rcrl));

    }

    /**
     * Test getting the root concept of an OBO ontology
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testOrphanedConceptUMLS() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(AIR_VERSION);
        String[] hierarchyIDs = lbscm.getHierarchyIDs(AIR_SCHEME, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("IS_A"))
                hierarchyId = hierarchy;
        }

        ResolvedConceptReferenceList rcrl = lbscm.getHierarchyOrphanedConcepts(AIR_SCHEME, csvt, hierarchyId);
        ResolvedConceptReference rcr[] = rcrl.getResolvedConceptReference();
        assertTrue(rcr.length > 0);

    }

    /**
     * Test getting the root concept of when using the generic owl loader
     * 
     * @throws InterruptedException
     * @throws LBException
     */
    public void testOrphanedConceptGenericOwl() throws InterruptedException, LBException {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceConvenienceMethods lbscm = (LexBIGServiceConvenienceMethods) lbs
                .getGenericExtension("LexBIGServiceConvenienceMethods");

        // Iterate through all hierarchies ...
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(AMINOACID_VERSION);
        String[] hierarchyIDs = lbscm.getHierarchyIDs(AMINOACID_SCHEME, csvt);
        String hierarchyId = (hierarchyIDs.length > 0) ? hierarchyIDs[0] : null;

        for (String hierarchy : hierarchyIDs) {
            if (hierarchy.equalsIgnoreCase("IS_A"))
                hierarchyId = hierarchy;
        }

        ResolvedConceptReferenceList rcrl = lbscm.getHierarchyOrphanedConcepts(AMINOACID_SCHEME, csvt, hierarchyId);
        ResolvedConceptReference rcr[] = rcrl.getResolvedConceptReference();
        assertTrue(rcr.length > 0);

    }
    

    

    
    
    boolean findMatchingConcept(String code, ResolvedConceptReferenceList rcrl) {        
        for (ResolvedConceptReference rcr: rcrl.getResolvedConceptReference()) {
            if (code.equals(rcr.getConceptCode())) {                
                return true;
            }
        }
        return false;
    }
    
    
    
}