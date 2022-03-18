
package org.LexGrid.LexBIG.Impl.bugs;

// LexBIG Test ID: GForge20875	TestRetrieveConceptandAttributesbyCode

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;

public class GForge20875 extends LexBIGServiceTestCase {
    final static String testID = "GForge20875";

    @Override
    protected String getTestID() {
        return testID;
    }

    public void testComplexPropertiesLoad() throws LBException {

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion("05.09.comp.prop.bvt");
        CodedNodeSet cns = ServiceHolder.instance().getLexBIGService().getCodingSchemeConcepts(THES_SCHEME, csvt);
        cns.restrictToCodes(Constructors.createConceptReferenceList(new String[] { "C55944" }, THES_SCHEME));
        ResolvedConceptReference[] rcr = cns.resolveToList(null, null,
                null, 0).getResolvedConceptReference();

        assertTrue(rcr.length == 1);
        assertTrue(rcr[0].getConceptCode().equals("C55944"));
        assertTrue(rcr[0].getEntity().getPropertyCount() == 7);
        Property[] properties = rcr[0].getEntity().getProperty();
        Boolean fail = true;
        for(Property prop: properties){
            if(prop.getValue().getContent().contains("CTCAE Gradé 2 Boñé Marrow Céllularity")){
               // assertTrue(prop.getSource()[0].getContent().equals("NCI"));
               // assertTrue(prop.getLanguage().equals("es"));
                fail = false;
            }
        }
        //if this fails then the property value does not match
        assertFalse(fail);

        fail = true;
        for(Property prop: properties){
        	System.out.println(prop.getValue().getContent());
            if(prop.getValue().getContent().contains("Modératély hypoçéllûlar or &gt;25 - &lt;=50% rédûçtion from normal çéllûlarity for agé")){
               // assertTrue(prop.getSource()[0].getContent().equals("CTCAE"));
               // assertTrue(prop.getLanguage().equals("fr"));
                fail = false;
            }
        }
        //if this fails then the property value does not match
        assertFalse(fail);

    }

}