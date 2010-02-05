package org.LexGrid.LexBIG.Impl.function.codednodeset;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;

public class RestrictToMatchingPropertiesTest extends BaseCodedNodeSetTest {

    @Override
    protected String getTestID() {
        return "RestrictToMatchingProperties Tests";
    }

    public void testRestrictToMatchingPropertiesError() throws LBInvocationException {

        try {
            cns.restrictToMatchingProperties(null, null, "Domestic", "contains", null);
            ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);
        } catch (LBParameterException e) {
            //this is good - pass the test.
            return;
        }

        fail("Didn't throw an Exception with no PropertyNames or PropertyTypes");
      
    }
    
    public void testRestrictToMatchingPropertiesPropertyNameMatch() throws LBException{
        cns.restrictToMatchingProperties(Constructors.createLocalNameList("definition"), null, "an automobile", "contains", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    public void testRestrictToMatchingPropertiesPropertyNameNoMatch() throws LBException{
        cns.restrictToMatchingProperties(Constructors.createLocalNameList("textualPresentation"), null, "An", "startsWith", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 0);
    }
    
    public void testRestrictToMatchingPropertiesPropertyTypeMatch() throws LBException{
        cns.restrictToMatchingProperties(null, new PropertyType[]{PropertyType.DEFINITION}, "An", "startsWith", null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    public void testRestrictToMatchingPropertiesPropertyTypeNoMatch() throws LBException{
        cns.restrictToMatchingProperties(null, new PropertyType[]{PropertyType.PRESENTATION}, "An", "startsWith", null);
          
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 0);
    }
    
    public void testRestrictToMatchingPropertiesPropertyGenericProperty() throws LBException{
        cns.restrictToMatchingProperties(null, new PropertyType[]{PropertyType.GENERIC}, "A Generic Property", "exactMatch", null);
          
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    public void testRestrictToMatchingPropertiesPropertySearchAllProperties() throws LBException{
    	PropertyType[] types = new PropertyType[]{PropertyType.GENERIC, PropertyType.COMMENT, PropertyType.DEFINITION, PropertyType.PRESENTATION};
    	
        cns.restrictToMatchingProperties(null, types, "A Generic Property", "exactMatch", null);
          
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
}
