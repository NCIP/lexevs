
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

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

import junit.framework.TestCase;

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
		cns = lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_VERSION));
		cng = lbs.getNodeGraph(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_VERSION), null);
//		cns = lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_SCHEME, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_VERSION));
//		cng = lbs.getNodeGraph(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_SCHEME, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_VERSION), null);
	}
	
	public CodedNodeSet getCodedNodeSet() throws Exception {
		setUp();
//		return lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_SCHEME, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_VERSION));
		return lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_VERSION));
	}
	
	@Test
	public void testSetUp() throws Exception {
		Assert.noNullElements(new Object[] {lbs,cns,cng});
	}

}