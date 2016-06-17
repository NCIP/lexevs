/**
 * 
 */
package org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.AnonymousOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.IncludeForDistributedTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * @author m029206
 *
 */
@Category(IncludeForDistributedTests.class)
public class TestPlainLuceneQuery extends BaseSearchAlgorithmTest {
	
	String algorithm = "LuceneQuery";

	/* (non-Javadoc)
	 * @see org.LexGrid.LexBIG.Impl.function.query.lucene.searchAlgorithms.BaseSearchAlgorithmTest#setUp()
	 */
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void testMinimalParams() throws Exception {
        CodedNodeSet cns = super.getAutosCodedNodeSet();
		cns = cns.restrictToAnonymous(AnonymousOption.NON_ANONYMOUS_ONLY);
        ResolvedConceptReference[] rcrl = null;
        try{
        rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        }
        catch(Exception e){
        	fail("Failing to resolve when running with anon only designation :" + e);
        }

        assertTrue("Length: " + rcrl.length, rcrl.length > 0);

	}
	
	@Test
	public void testLuceneQuery() throws Exception{
        CodedNodeSet cns = super.getAutosCodedNodeSet();
		cns = cns.restrictToMatchingDesignations("Car", SearchDesignationOption.ALL, this.getAlgorithm(), null);
        ResolvedConceptReference[] rcrl = null;
        try{
        rcrl = cns.resolveToList(null, null, null, -1).getResolvedConceptReference();
        }
        catch(Exception e){
        	fail("Failing to resolve when running with anon only designation :" + e);
        }

        assertTrue("Length: " + rcrl.length, rcrl.length > 0);
        assertTrue(this.checkForMatch(rcrl, "C0001"));
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	

}
