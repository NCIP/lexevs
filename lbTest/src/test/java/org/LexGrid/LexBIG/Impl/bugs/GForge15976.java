
package org.LexGrid.LexBIG.Impl.bugs;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * Provides a check on Constructors that create a ConceptReference with a code only.
 * Should now return a value when used to retrieve CodedNodeSets and Graphs. 
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class GForge15976 extends LexBIGServiceTestCase {
    final static String testID = "GForge15976";

    private String code = "Body_Region";
    private String code1 = "Body_Fluid_or_Substance";
    private String code2 = "Body_Part";
    private String[] codes = {code, code1, code2};
    private CodedNodeGraph cng = null;
    private CodedNodeSet cns = null;
    private CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
    private ConceptReferenceList codeList = null;
    private ConceptReferenceList codeList2 = null;
    private ResolvedConceptReferenceList rcrl = null;
    private ResolvedConceptReferenceList rcr2 = null;
    LexBIGService lbsi;
    
    @Override
    protected String getTestID() {
        return testID;
    }

    /*
     * LexBIG Bug #19741 -
     * https://gforge.nci.nih.gov/tracker/index.php?func=detail&aid=19741&group_id=491&atid=1850
     */
    public void setUp(){
        try {
            lbsi = ServiceHolder.instance().getLexBIGService(); 
            csvt.setVersion(THES_VERSION);   
            cns = lbsi.getCodingSchemeConcepts(THES_SCHEME, csvt);
            cng = lbsi.getNodeGraph(THES_SCHEME, csvt, null); 
            
            codeList = Constructors.createConceptReferenceList(code);
            codeList2 = Constructors.createConceptReferenceList(codes);
         
            cns.restrictToCodes(codeList);
            rcrl = cns.resolveToList(null, null, null, 1);
            
            rcr2 = cng.resolveAsList(codeList2.getConceptReference(0), false, true, 0, 0, null, null, null, null, 1);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
 
    public void testCodedNodeSetResolveConceptReference() throws Exception {
            assertNotNull(rcrl);
            assertTrue(rcrl.getResolvedConceptReferenceCount() > 0);    
        }
   
    public void testCodedNodeGraphResolveConceptReference() throws Exception {
        assertNotNull(rcr2);
        assertTrue(rcr2.getResolvedConceptReferenceCount() > 0);
    }
       
}