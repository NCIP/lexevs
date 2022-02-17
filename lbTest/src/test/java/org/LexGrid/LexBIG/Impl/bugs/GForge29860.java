
package org.LexGrid.LexBIG.Impl.bugs;


import java.util.ArrayList;
import java.util.List;

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

public class GForge29860 extends LexBIGServiceTestCase {
    final static String testID = "GForge29860";
    
    @Override
    protected String getTestID() {
        return testID;
    }

/**
     * 
     * 
     * GForge #29860
     * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=29860&group_id=491&atid=1850
     * 
     * @throws Throwable
     */
public void testDuplicateInfoWithAnonymousClass() throws Throwable {
    	LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
    	CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(PIZZA_SCHEME_VERSION);
    	CodedNodeGraph cng = lbs.getNodeGraph(PIZZA_SCHEME_NAME, csvt, null);
    	
    	// that what the bug missed
    	List<String> targetList = new ArrayList<String>();
    	targetList.add("FunctionalProperty");
    	targetList.add("ObjectProperty");
    	targetList.add("InverseFunctionalProperty");
    	
    	
    	
    	AssociatedConcept focus = new AssociatedConcept();
        focus.setCode("isBaseOf");
        focus.setCodeNamespace("pizza");

        List<String> loadedTargets = new ArrayList<String>();
        ResolvedConceptReferenceList list = cng.resolveAsList(focus, true, false, 1, -1, null, null, null, null, -1);
        ResolvedConceptReference conRef = list.getResolvedConceptReference()[0];
        AssociationList assnList = conRef.getSourceOf();
        for (Association assn : assnList.getAssociation()) {
        	for (AssociatedConcept con : assn.getAssociatedConcepts().getAssociatedConcept()) {
        		loadedTargets.add(con.getCode());
        	}
        }
        // check if the total is correct
        assertEquals(loadedTargets.size(), 1);
        
        // check if it gets what it missed before
//        for (String code: targetList){
//        	assertEquals(loadedTargets.contains(code), true);
//        }
        
    }
    
   
}