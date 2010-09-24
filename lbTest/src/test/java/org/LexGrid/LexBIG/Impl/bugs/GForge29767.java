package org.LexGrid.LexBIG.Impl.bugs;

import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
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

public class GForge29767 extends LexBIGServiceTestCase {
    final static String testID = "GForge29767";
    
    @Override
    protected String getTestID() {
        return testID;
    }
    
    /**
     * 
     * 
     * GForge #29767
     * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=29767&group_id=491&atid=1850
     * 
     * @throws Throwable
     */
    public void testAnonymousClassOfAnonymousClass() throws Throwable {
       
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        
        CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(PIZZA_SCHEME_VERSION);   
        CodedNodeGraph cng = lbs.getNodeGraph(PIZZA_SCHEME_URI, csvt, null);
        
        AssociatedConcept focus = new AssociatedConcept();
        focus.setCode("NonVegetarianPizza");
        focus.setCodeNamespace("pizza.owl");
        
        ResolvedConceptReferenceList list = cng.resolveAsList(focus, true, false, 1, -1, null, null, null, null, -1);
        
        ResolvedConceptReference con = getAnonymousClass(list);
        if (con == null)
        	fail("the 1st anonymous class not found");
        
        list = cng.resolveAsList(con, true, false, 1, -1, null, null, null, null, -1);
        con = getAnonymousClass(list);
        if (con == null) {
        	fail("the anonymous class's anonymou class not found");
        }

    }
    
    private ResolvedConceptReference getAnonymousClass(ResolvedConceptReferenceList l) {
    	if (l.getResolvedConceptReferenceCount() != 1)
        	return null;
    	AssociationList al = l.getResolvedConceptReference(0).getSourceOf();
    	Iterator<? extends Association> ia = al.iterateAssociation();
    	
//        Iterator<? extends ResolvedConceptReference> i = l.getResolvedConceptReference()[0].iterateResolvedConceptReference();
//        while(i.hasNext()) {
//        	ResolvedConceptReference con = i.next();
//        	if (con.getCode().startsWith("@")) {
//        		return con;
//        	}
//        }
    	return null;
    }
   
}