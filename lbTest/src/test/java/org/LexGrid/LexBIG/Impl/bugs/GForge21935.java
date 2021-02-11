
package org.LexGrid.LexBIG.Impl.bugs;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * Test for GForge Item #21935
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class GForge21935 extends LexBIGServiceTestCase {
    final static String testID = "GForge21935";
    
    @Override
    protected String getTestID() {
        return testID;
    }
    
    /**
     * Test to check if OWL Restriction Associations are being populated with the correct
     * concept code.
     * 
     * GForge #21935
     * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=21935&group_id=491&atid=1850
     * 
     * @throws Throwable
     */
    public void testOwlRestrictionAssociations() throws Throwable {
        String code = "A0001";
        String relatedCode = "C0001";

        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        
        CodedNodeGraph cng = lbs.getNodeGraph(AUTO_SCHEME, null, null);
        cng.restrictToAssociations(Constructors.createNameAndValueList("hasSubtype"), null);
        ResolvedConceptReferenceList rcrl = cng.resolveAsList(Constructors.createConceptReference(code, AUTO_SCHEME), true, true, 1, 1, null, null, null, null, -1);
        ResolvedConceptReference[] rcr = rcrl.getResolvedConceptReference();

        assertTrue(rcr.length == 1);

        ResolvedConceptReference ref = rcr[0];

        assertTrue(ref.getCode().equals(code));

        Association[] assocs = ref.getSourceOf().getAssociation();

        assertTrue(assocs.length == 1);

        Association assoc = assocs[0];

        AssociatedConcept[] assocConcepts = assoc.getAssociatedConcepts().getAssociatedConcept();

        assertTrue(searchAssociatedConceptsForCode(assocConcepts, relatedCode));
    }
    
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