package org.LexGrid.LexBIG.Impl.function.codednodeset;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;

public class MultipeRestrictionsTest extends BaseCodedNodeSetTest {

    @Override
    protected String getTestID() {
        return "MultipeRestrictionsTest Tests";
    }

    public void testRestrictDesignationAndHasPropertyType() throws LBException{
        cns.restrictToMatchingDesignations("automobile", SearchDesignationOption.ALL, "contains", null);
        cns.restrictToProperties(
        		null, 
        		new PropertyType[]{PropertyType.DEFINITION}, 
        		null, 
        		null, 
        		null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    public void testRestrictDesignationAndDefinition() throws LBException{
        cns.restrictToMatchingDesignations("automobile", SearchDesignationOption.ALL, "exactMatch", null);
        cns.restrictToMatchingProperties(null, 
        		new PropertyType[]{PropertyType.DEFINITION}, 
        		"An automobile", 
        		"exactMatch", 
        		null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    public void testRestrictDesignationAndWrongDefinition() throws LBException{
        cns.restrictToMatchingDesignations("automobile", SearchDesignationOption.ALL, "contains", null);
        cns.restrictToMatchingProperties(null, 
        		new PropertyType[]{PropertyType.DEFINITION}, 
        		"An automobileWRONG", 
        		"exactMatch", 
        		null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 0); 
    }
    
    public void testRestrictDesignationAndSource() throws LBException{
        cns.restrictToMatchingDesignations("Domestic Auto Makers", SearchDesignationOption.ALL, "contains", null);
        cns.restrictToProperties(
        		null, 
        		new PropertyType[]{PropertyType.PRESENTATION}, 
        		Constructors.createLocalNameList("lexgrid.org"), 
        		null, 
        		null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);
        
        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("005"));
    }
     
    public void testRestrictDesignationAndSourceAndActive() throws LBException{
        cns.restrictToMatchingDesignations("Domestic Auto Makers", SearchDesignationOption.ALL, "contains", null);
        cns.restrictToProperties(
        		null, 
        		new PropertyType[]{PropertyType.PRESENTATION}, 
        		Constructors.createLocalNameList("lexgrid.org"), 
        		null, 
        		null);
        cns.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);
        
        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("005"));
    }
    
    public void testRestrictDesignationAndSourceAndActiveAndWrongPresentation() throws LBException{
        cns.restrictToMatchingDesignations("Domestic Auto Makers", SearchDesignationOption.ALL, "contains", null);
        cns.restrictToProperties(
        		null, 
        		new PropertyType[]{PropertyType.PRESENTATION}, 
        		Constructors.createLocalNameList("lexgrid.org"), 
        		null, 
        		null);
        cns.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);
        cns.restrictToMatchingDesignations("WRONG__DONT_MATCH_ANYTHING", SearchDesignationOption.ALL, "contains", null);
        
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);
        
        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 0);      
    }
    
    public void testRestrictDesignationAndSourceAndActiveAndCode() throws LBException{
        cns.restrictToMatchingDesignations("Domestic Auto Makers", SearchDesignationOption.ALL, "contains", null);
        cns.restrictToProperties(
        		null, 
        		new PropertyType[]{PropertyType.PRESENTATION}, 
        		Constructors.createLocalNameList("lexgrid.org"), 
        		null, 
        		null);
        cns.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);
        
        cns.restrictToCodes(Constructors.createConceptReferenceList("005"));
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);
        
        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("005"));
    }
    
    public void testRestrictDesignationAndSourceAndActiveAndWrongCode() throws LBException{
        cns.restrictToMatchingDesignations("Domestic Auto Makers", SearchDesignationOption.ALL, "contains", null);
        cns.restrictToProperties(
        		null, 
        		new PropertyType[]{PropertyType.PRESENTATION}, 
        		Constructors.createLocalNameList("lexgrid.org"), 
        		null, 
        		null);
        cns.restrictToStatus(ActiveOption.ACTIVE_ONLY, null);
        
        cns.restrictToCodes(Constructors.createConceptReferenceList("WRONG_CODE"));
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);
        
        assertTrue("Count: " + rcrl.getResolvedConceptReferenceCount(),
        		rcrl.getResolvedConceptReferenceCount() == 0);
    }
}
