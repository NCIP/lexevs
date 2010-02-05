package org.LexGrid.LexBIG42.Impl.function.query;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG42.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * This testcase checks if the manifest implementation works when using either a
 * manifest file or a manifest object. It also checks if the same ontology can
 * be loaded twice while using the manifest to adjust the registered name of the
 * ontology.
 * 
 * @author Pradip Kanjamala
 * 
 */
public class TestHierarchyAPI extends LexBIGServiceTestCase {

    private static LoadStatus status_ = null;
    private static MessageDirector md_ = new MessageDirector("TestPostLoadManifest", status_);
    final static String testID = "T1_FNC_50";

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
    public void testFindRootConceptOBO() throws InterruptedException, LBException {
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

        ResolvedConceptReferenceList rcrl = lbscm.getHierarchyRoots(CELL_URN, csvt, hierarchyId);
        ResolvedConceptReference rcr[]= rcrl.getResolvedConceptReference();
        assertTrue(rcr[0].getConceptCode().equals("CL:0000000"));

        
    }
}
