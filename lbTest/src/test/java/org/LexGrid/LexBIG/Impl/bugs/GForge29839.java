
package org.LexGrid.LexBIG.Impl.bugs;


import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

public class GForge29839 extends LexBIGServiceTestCase {
    final static String testID = "GForge29839";
    
    @Override
    protected String getTestID() {
        return testID;
    }

/**
     * 
     * 
     * GForge #29839
     * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=29839&group_id=491&atid=1850
     * 
     * @throws Throwable
     */
public void testDuplicateInfoWithAnonymousClass() throws Throwable {
    	LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
    	CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(PIZZA_SCHEME_VERSION);
    	CodedNodeGraph cng = lbs.getNodeGraph(PIZZA_SCHEME_NAME, csvt, null);
    	
    	AssociatedConcept focus = new AssociatedConcept();
        focus.setCode("SpicyPizza");
        focus.setCodeNamespace("pizza.owl");

        ResolvedConceptReferenceList list = cng.resolveAsList(focus, true, false, 1, -1, null, null, null, null, -1);
        ResolvedConceptReference conRef = list.getResolvedConceptReference()[0];
        AssociationList assnList = conRef.getSourceOf();
        for (Association assn : assnList.getAssociation()) {
        	for (AssociatedConcept con : assn.getAssociatedConcepts().getAssociatedConcept()) {
        		assertEquals(con.getCode().equals("SpicyTopping"), false);
        	}
        }
        
    }
    
   
}