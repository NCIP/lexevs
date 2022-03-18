
package org.LexGrid.LexBIG.Impl.bugs;

import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

public class GForge29772 extends LexBIGServiceTestCase {
    final static String testID = "GForge29772";
    
    @Override
    protected String getTestID() {
        return testID;
    }

/**
     * 
     * 
     * GForge #29772
     * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=29772&group_id=491&atid=1850
     * 
     * @throws Throwable
     */
public void testAnonymousClassOfAnonymousClass() throws Throwable {
       
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(PIZZA_SCHEME_VERSION);   
        CodedNodeGraph cng = lbs.getNodeGraph(PIZZA_SCHEME_NAME, csvt, null);
        
        AssociatedConcept focus = new AssociatedConcept();
        focus.setCode("American");
        focus.setCodeNamespace("pizza");
        
        ResolvedConceptReferenceList list = cng.resolveAsList(focus, true, false, 1, -1, null, null, null, null, -1);
        
        boolean t = existAnonymousClassSubClass(list);
        assertEquals(t, true);

    }
    
    private boolean existAnonymousClassSubClass(ResolvedConceptReferenceList l) {
    	if (l.getResolvedConceptReferenceCount() != 1)
        	return false;
    	AssociationList al = l.getResolvedConceptReference(0).getSourceOf();
    	Iterator<? extends Association> ia = al.iterateAssociation();
    	while(ia.hasNext()) {
    		Association a = ia.next();
    		if (a.getAssociationName().equalsIgnoreCase("subClassOf") == false)
    			continue;
    		AssociatedConceptList cl = a.getAssociatedConcepts();
    		Iterator<? extends AssociatedConcept> iCon = cl.iterateAssociatedConcept();
    		while(iCon.hasNext()) {
    			AssociatedConcept con = iCon.next();
    			if (con.getCode().startsWith("@")) {
            		return true;
            	}
    		}
    	}
    	return false;
    }
   
}