
package org.LexGrid.LexBIG.Impl.bugs;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

/**
 * This class should be used as a place to write JUnit tests which show a bug,
 * and pass when the bug is fixed.
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class GForge19629 extends LexBIGServiceTestCase {
    final static String testID = "GForge196292";

    private LexBIGService lbs;
    
    public void setUp(){
        lbs = ServiceHolder.instance().getLexBIGService();    
    }
    
    @Override
    protected String getTestID() {
        return testID;
    }

    public void testResolveMismatchingCodingSchemeNamespace() throws LBException {     
        CodedNodeGraph cng = lbs.getNodeGraph(LexBIGServiceTestCase.AUTO_SCHEME, null, null);
        ConceptReference ref = ConvenienceMethods.createConceptReference("Ford", AUTO_SCHEME);
        
        ResolvedConceptReferenceList rcrl = cng.resolveAsList(ref, 
                true, 
                false, 
                10, 
                10, 
                null, 
                null, 
                null, 
                10);
        
        ResolvedConceptReference[] rcr = rcrl.getResolvedConceptReference();
        
        assertTrue(rcr.length == 1);
        
        ResolvedConceptReference ford = rcr[0];
        
        assertTrue(ford.getCode().equals("Ford"));
        assertTrue(ford.getCodingSchemeName().equals(AUTO_SCHEME));
        
        Association[] fordAssocs = ford.getSourceOf().getAssociation();
        
        //this is the source of three Associations
        assertTrue(fordAssocs.length == 3);
        
        //we want the 'uses' association
        Association assoc =  null;
        for(Association foundAssoc : fordAssocs){
            if(foundAssoc.getAssociationName().equals("uses")){
                assoc = foundAssoc;
            }
        }
        //verify we found it - and its correct
        assertTrue(assoc != null);
        assertTrue(assoc.getAssociationName().equals("uses"));
        
        AssociatedConcept[] associatedConcepts = assoc.getAssociatedConcepts().getAssociatedConcept();  
        assertTrue(associatedConcepts.length == 1); 
        AssociatedConcept r0001 = associatedConcepts[0];
        
        assertTrue(r0001.getCode().equals("R0001"));
        assertTrue(r0001.getCodingSchemeName().equals(LexBIGServiceTestCase.PARTS_SCHEME));
        assertTrue(r0001.getCodeNamespace().equals(LexBIGServiceTestCase.PARTS_NAMESPACE));
    }
}