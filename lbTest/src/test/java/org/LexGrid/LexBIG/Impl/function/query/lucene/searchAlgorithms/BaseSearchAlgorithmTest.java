
package org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;

/**
 * The Class BaseSearchAlgorithmTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BaseSearchAlgorithmTest extends LexBIGServiceTestCase {

    /** The lbs. */
    protected LexBIGService lbs;

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase#getTestID()
     */
    @Override
    protected String getTestID() {
        return "Base Search Algorithm Test";
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() {
        lbs = ServiceHolder.instance().getLexBIGService();
    }

    /**
     * Gets the autos coded node set.
     * 
     * @return the autos coded node set
     * 
     * @throws Exception the exception
     */
    protected CodedNodeSet getOWLSchemeCodedNodeSet() throws Exception {
        return lbs.getCodingSchemeConcepts(OWL2_SNIPPET_INDIVIDUAL_URN, 
        		Constructors.createCodingSchemeVersionOrTagFromVersion(
        				OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION));
    }
    
    /**
     * Gets the autos coded node set.
     * 
     * @return the autos coded node set
     * 
     * @throws Exception the exception
     */
    protected CodedNodeSet getAutosCodedNodeSet() throws Exception {
        return lbs.getCodingSchemeConcepts(AUTO_SCHEME, null);
    }


    /**
     * Check for match.
     * 
     * @param rcrl the rcrl
     * @param code the code
     * 
     * @return true, if successful
     */
    protected boolean checkForMatch(ResolvedConceptReference[] rcrl, String code) {
        return checkForMatch(rcrl, Arrays.asList(code));
    }

    /**
     * Check for match.
     * 
     * @param rcrl the rcrl
     * @param codes the codes
     * 
     * @return true, if successful
     */
    protected boolean checkForMatch(ResolvedConceptReference[] rcrl, List<String> codes) {
        Map<String, Boolean> matches = new HashMap<String, Boolean>();
        for (String code : codes) {
            matches.put(code, false);
        }

        for (ResolvedConceptReference ref : rcrl) {
            String code = ref.getCode();
            matches.put(code, true);
        }

        return !matches.containsValue(false);
    }

    /**
     * Test set up.
     */
    public void testSetUp() {
        assertNotNull(lbs);
    }
}