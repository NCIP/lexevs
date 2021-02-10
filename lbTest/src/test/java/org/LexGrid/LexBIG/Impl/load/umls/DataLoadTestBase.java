
package org.LexGrid.LexBIG.Impl.load.umls;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

/**
 * The Class DataLoadTestBase.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DataLoadTestBase extends TestCase{
	
	/** The lbs. */
	protected LexBIGService lbs;
	protected CodedNodeSet cns;
	protected CodedNodeGraph cng;
	
	/**
	 * Sets the up lbs.
	 * @throws LBException 
	 */
	@Before
	public void setUp() throws Exception{
		lbs = ServiceHolder.instance().getLexBIGService();
		cns = lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.AIR_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.AIR_VERSION));
		cng = lbs.getNodeGraph(LexBIGServiceTestCase.AIR_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.AIR_VERSION), null);
	}
	
	public CodedNodeSet getCodedNodeSet() throws Exception {
		setUp();
		return lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.AIR_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.AIR_VERSION));
	}
	
	@Test
	public void testSetUp() throws Exception {
		Assert.noNullElements(new Object[] {lbs,cns,cng});
	}
}