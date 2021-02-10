
package org.LexGrid.LexBIG.Impl.function.codednodeset;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class RestrictToMatchingDesignationsTest extends BaseCodedNodeSetTest {

    @Override
    protected String getTestID() {
        return "RestrictToMatchingDesignations Tests";
    }

    @Test
    public void testRestrictToMatchingDesignationsNodeSet() throws LBException{
    	CodedNodeSet cns = lbs.getNodeSet(AUTO_SCHEME, null, null);
        cns = cns.restrictToMatchingDesignations("General Motors", SearchDesignationOption.ALL, "contains", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("GM"));
    }
     
    @Test
    public void testRestrictToMatchingDesignationsALL() throws LBException{
        cns = cns.restrictToMatchingDesignations("Domestic", SearchDesignationOption.ALL, "contains", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("005"));
    }
    
    @Test
    public void testRestrictToMatchingDesignationsPreferredOnlyMatch() throws LBException{
        cns = cns.restrictToMatchingDesignations("Domestic", SearchDesignationOption.PREFERRED_ONLY, "contains", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("005"));
    }
    
    @Test
    public void testRestrictToMatchingDesignationsPreferredOnlyNoMatch() throws LBException{
        cns = cns.restrictToMatchingDesignations("American", SearchDesignationOption.PREFERRED_ONLY, "contains", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 0);
    }
    
    @Test
    public void testRestrictToMatchingDesignationsNonPreferredOnlyMatch() throws LBException{
        cns = cns.restrictToMatchingDesignations("American", SearchDesignationOption.NON_PREFERRED_ONLY, "contains", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("005"));
    }
    
    @Test
    public void testRestrictToMatchingDesignationsNonPreferredOnlyNoMatch() throws LBException{
        cns = cns.restrictToMatchingDesignations("Domestic", SearchDesignationOption.NON_PREFERRED_ONLY, "contains", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 0);
    }
    
    @Test
    public void testRestrictToMatchingDesignationsLanguage() throws LBException{
        cns = cns.restrictToMatchingDesignations("Truck", SearchDesignationOption.ALL, "contains", "en");
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("T0001"));
    }
}