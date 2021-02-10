
package org.LexGrid.LexBIG.Impl.bugs;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * Test for GForge Item #26741
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class GForge26741 extends LexBIGServiceTestCase {
    final static String testID = "GForge26741";
    
    @Override
    protected String getTestID() {
        return testID;
    }
    
    /**
     * Test to see if there is any instance data being loaded. The bug occured when the instance
     * data was only numbers (no NCNAMELeading char)
     * 
     * GForge #26741
     * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=26741&group_id=491&atid=1850
     * 
     * @throws Throwable
     */
    public void testOwlInstancePresence() throws Throwable {
        String code = "111333002";
        //String code = "Cyst__disorder_";
       
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(OVARIANMASS_SCHEME_VERSION);   
        CodedNodeSet cns = lbs.getNodeSet(OVARIANMASS_SCHEME_URN, csvt, null);
        cns.restrictToCodes(Constructors.createConceptReferenceList(code));
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, 1);
        ResolvedConceptReference[] rcr = rcrl.getResolvedConceptReference();
        assertTrue(rcr.length == 1);


    }
    
    
    /**
     * Test to see if there there are associations between instance data
     * 
     * GForge #26741
     * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=26741&group_id=491&atid=1850
     * 
     * @throws Throwable
     */
// No longer valid to load skos structures using current OWL api
//    public void testOwlInstanceToInstanceAssociation() throws Throwable {
//        String code = "111333002";
//        String relatedCode="300479008";
//       
//        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
//        
//        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(OVARIANMASS_SCHEME_VERSION);   
//        CodedNodeGraph cng = lbs.getNodeGraph(OVARIANMASS_SCHEME_URN, csvt, null);
//        cng.restrictToAssociations(Constructors.createNameAndValueList("broader"), null);
//
//        ResolvedConceptReferenceList rcrl = cng.resolveAsList(Constructors.createConceptReference(code, OVARIANMASS_SCHEME_URN), true, true, 1, 1, null, null, null, null, -1);
//        ResolvedConceptReference[] rcr = rcrl.getResolvedConceptReference();
//        Association[] assocs = rcr[0].getSourceOf().getAssociation();
//
//        assertTrue(assocs.length == 1);
//
//        Association assoc = assocs[0];
//
//        AssociatedConcept[] assocConcepts = assoc.getAssociatedConcepts().getAssociatedConcept();
//
//        assertTrue(searchAssociatedConceptsForCode(assocConcepts, relatedCode));
//
//        
//
//
//    }    
    public boolean searchAssociatedConceptsForCode(AssociatedConcept[] assocConcepts, String code){
        boolean found = false;
        for(AssociatedConcept concept : assocConcepts){
            if(concept.getCode().equals(code)){
                found = true;
            }
        }
        return found;
    }
}