
package org.LexGrid.LexBIG.Impl.function.codednodeset;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IncludeForDistributedTests.class)
public class RestrictToPropertiesTest extends BaseCodedNodeSetTest {

    @Override
    protected String getTestID() {
        return "RestrictToProperties Tests";
    }

    @Test
    public void testRestrictToProperty() throws LBException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
        cns = cns.restrictToProperties(Constructors.createLocalNameList("textualPresentation"), null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    @Test
    public void testRestrictToPropertyDefinition() throws LBException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
        cns = cns.restrictToProperties(Constructors.createLocalNameList("definition"), null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    @Test
    public void testRestrictToPropertyTwo() throws LBException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
        cns = cns.restrictToProperties(Constructors.createLocalNameList(new String[]{"definition", "textualPresentation"}), null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    @Test
    public void testRestrictToPropertyTwoSeperate() throws LBException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("A0001"));
        cns = cns.restrictToProperties(Constructors.createLocalNameList(new String[]{"definition"}), null);
        cns = cns.restrictToProperties(Constructors.createLocalNameList(new String[]{"textualPresentation"}), null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 1);
        
        ResolvedConceptReference ref = rcrl.getResolvedConceptReference(0);
        
        assertTrue(ref.getCode().equals("A0001"));
    }
    
    @Test
    public void testRestrictToWrongProperty() throws LBException{
        cns = cns.restrictToCodes(Constructors.createConceptReferenceList("73"));
        cns = cns.restrictToProperties(Constructors.createLocalNameList("definition"), null);
        
        ResolvedConceptReferenceList rcrl = cns.resolveToList(null, null, null, -1);

        assertTrue(rcrl.getResolvedConceptReferenceCount() == 0);  
    }
}