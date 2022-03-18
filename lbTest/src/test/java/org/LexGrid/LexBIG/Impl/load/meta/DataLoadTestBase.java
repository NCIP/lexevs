
package org.LexGrid.LexBIG.Impl.load.meta;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;

/**
 * The Class DataLoadTestBase.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DataLoadTestBase extends TestCase {
	
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
		cns = lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.META_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.SAMPLE_META_VERSION));
		cng = lbs.getNodeGraph(LexBIGServiceTestCase.META_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.SAMPLE_META_VERSION), null);
	}
	
	public CodedNodeSet getCodedNodeSet() throws Exception {
		this.setUp();
		return lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.META_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.SAMPLE_META_VERSION));
	}
}