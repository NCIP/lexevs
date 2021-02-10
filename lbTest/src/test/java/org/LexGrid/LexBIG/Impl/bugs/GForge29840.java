
package org.LexGrid.LexBIG.Impl.bugs;


import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;

public class GForge29840 extends LexBIGServiceTestCase {
    final static String testID = "GForge29840";
    
    @Override
    protected String getTestID() {
        return testID;
    }

/**
     * 
     * 
     * GForge #29840
     * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=29840&group_id=491&atid=1850
     * 
     * @throws Throwable
     */
public void testLanguageTags() throws Throwable {
    	LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
    	CodingSchemeVersionOrTag csvt = Constructors.createCodingSchemeVersionOrTagFromVersion(PIZZA_SCHEME_VERSION);
    	CodedNodeGraph cng = lbs.getNodeGraph(PIZZA_SCHEME_NAME, csvt, null);
    	
    	AssociatedConcept focus = new AssociatedConcept();
        focus.setCode("PizzaTopping");
        focus.setCodeNamespace("pizza.owl");

        ResolvedConceptReferenceList list = cng.resolveAsList(focus, true, false, 1, -1, null, null, null, null, -1);
        Entity entity = list.getResolvedConceptReference()[0].getEntity();
        for (Property p : entity.getAllProperties()) {
        	if (p.getPropertyName().equals("label"))
        		assertEquals(p.getLanguage(), "pt");
        }
        
    }
    
   
}