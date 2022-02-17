
package org.LexGrid.LexBIG.Impl.performance;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * Provides a check on Constructors that create a ConceptReference with a code only.
 * Should now return a value when used to retrieve CodedNodeSets and Graphs. 
 * 
 * @author <A HREF="mailto:kevin.peterson@mayo.edu">Kevin Peterson</A>
 */
public class ResolveConceptReferenceTest extends LexBIGServiceTestCase {
    final static String testID = "GForge15976";

    private LexBIGService lbs;
    
    @Override
    protected String getTestID() {
        return testID;
    }
    
    //main method for profiling outside of JUnit framework.
    public static void main(String[] args) throws Exception {
       ResolveConceptReferenceTest test = new ResolveConceptReferenceTest();
        test.setUp();
       test.testGetResolvedReferences();
    }

    //setup and do a dummy query to make sure everything is initialized.
    public void setUp(){
        try {
            System.setProperty("LG_CONFIG_FILE", "W:/user/m005256/LexEVSMetaTest/resources/config/lbconfig.props");
            lbs = LexBIGServiceImpl.defaultInstance();
            
            CodedNodeSet cns = lbs.getCodingSchemeConcepts("NCI MetaThesaurus", null);
         
            cns.restrictToCodes(Constructors.createConceptReferenceList("U000035"));
            
            cns.resolve(null, null, null);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
 
    //Baseline 5.0.1 : 790ms
    //June 24 2009 - Join EntryState on SQLImplementedMethods.buildCodedEntry : 650ms
    public void testGetResolvedReferences() throws Exception {
        CodedNodeSet cns = lbs.getCodingSchemeConcepts("NCI MetaThesaurus", null);
        cns.restrictToMatchingDesignations("heart", SearchDesignationOption.ALL, "RegExp", null);
        
        int runs = 1;
        
        long totaltime = 0;
        
        for(int i=0;i<runs;i++){
            Long time = System.currentTimeMillis();
            cns.resolveToList(null, null, null, 10);
            totaltime += (System.currentTimeMillis() - time);
        }
        
        System.out.println(totaltime/runs);
        
    }
   
    public void testGetUnresolvedReferences() throws Exception {
       // assertNotNull(rcr2);
        //assertTrue(rcr2.getResolvedConceptReferenceCount() > 0);
    }
       
}