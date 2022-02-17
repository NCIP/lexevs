
package org.LexGrid.LexBIG.Impl.featureRequests;

import java.util.ArrayList;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

public class GForge17019 extends LexBIGServiceTestCase {

    final static String testID = "GForge17019";

    private CodedNodeGraph cng;

    @Override
    protected String getTestID() {
        return testID;
    }
    
    
    public void testRoleGroupInMeta() throws Exception {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService(); 

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion(SAMPLE_META_VERSION);
        cng = lbs.getNodeGraph(META_SCHEME, csvt, null);  
        cng.restrictToAssociations(Constructors.createNameAndValueList("RB", null), 
                Constructors.createNameAndValueList("RG", "testRG"));
        ConceptReference cr = Constructors.createConceptReference("C0036775", META_SCHEME);
        ResolvedConceptReferenceList rcrl = cng.resolveAsList(cr, true, true, 1, 1, null, null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount()>0);   
        ResolvedConceptReference[] rcrList = rcrl.getResolvedConceptReference();
        ArrayList<String> al = new ArrayList<String>();

        for(ResolvedConceptReference rcr: rcrList){
            AssociationList source = rcr.getSourceOf();
            Association[] associations = source.getAssociation();
            for (int i = 0; i < associations.length; i++) {
                Association assoc = associations[i];
                AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
                for (int j = 0; j < acl.length; j++) {
                    AssociatedConcept ac = acl[j];
                    al.add(ac.getCode());
                }
            }
        }
        assertTrue(al.contains("C0000005"));
    }
    
    public void testBadRoleGroupInMeta() throws Exception {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService(); 

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        csvt.setVersion(SAMPLE_META_VERSION);
        cng = lbs.getNodeGraph(META_SCHEME, csvt, null);  
        cng.restrictToAssociations(Constructors.createNameAndValueList("RB", null), 
                Constructors.createNameAndValueList("RG", "Bad_Role_Group"));
        ConceptReference cr = Constructors.createConceptReference("C0000005", META_SCHEME);
        ResolvedConceptReferenceList rcrl = cng.resolveAsList(cr, true, true, 1, 1, null, null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount()>0);   
        ResolvedConceptReference[] rcrList = rcrl.getResolvedConceptReference();
        
        assertTrue(rcrList.length == 1);
        
        assertNull(rcrList[0].getSourceOf());
    }
}