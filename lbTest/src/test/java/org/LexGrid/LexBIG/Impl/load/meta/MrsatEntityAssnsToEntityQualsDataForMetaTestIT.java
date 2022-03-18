
package org.LexGrid.LexBIG.Impl.load.meta;

import junit.framework.JUnit4TestAdapter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.Mappings;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class EntityAssnsToEntityQualsDataTestIT.
 * 
 * @author <a href="mailto:bauer.scott@mayo.edu">Scott Bauer</a>
 */
public class MrsatEntityAssnsToEntityQualsDataForMetaTestIT{
	
	
	/** The graph focus. */
	private AssociatedConcept[] associatedConcept;
	private LexBIGService lbsvc;
	
	/**
	 * Builds the test entity.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		lbsvc = ServiceHolder.instance().getLexBIGService();
		CodedNodeGraph cngSM= lbsvc.getNodeGraph(LexBIGServiceTestCase.META_URN, Constructors.createCodingSchemeVersionOrTagFromVersion("200510.bvt"), null);
		cngSM = cngSM.restrictToAssociations(Constructors.createNameAndValueList("RO"), Constructors.createNameAndValueList("rela", "has_component"));
		associatedConcept = cngSM.resolveAsList(Constructors.createConceptReference("C0000039", 
				LexBIGServiceTestCase.META_URN), true, true, 1, 1, null, null, null, -1)
				.getResolvedConceptReference(0)
				.getTargetOf()
				.getAssociation()[0]
				.getAssociatedConcepts()
				.getAssociatedConcept();
	}
	
	@Test
	public void testQualsNotNull() throws Exception {	
		NameAndValueList quals = DataTestUtils.getConceptReference(associatedConcept, "CL234176").getAssociationQualifiers();
		assertNotNull(quals);
	}
	
	@Test
	public void testRelaQualMODID() throws Exception {	
		NameAndValueList quals = DataTestUtils.getConceptReference(associatedConcept, "CL234176").getAssociationQualifiers();
		assertTrue(DataTestUtils.isQualifierNameAndValuePresent("MODIFIER_ID", "900000000000451002", quals));
	}
	
	@Test
	public void testRelaQualCHARTYPEID() throws Exception {	
		NameAndValueList quals = DataTestUtils.getConceptReference(associatedConcept, "CL234176").getAssociationQualifiers();
		assertTrue(DataTestUtils.isQualifierNameAndValuePresent("CHARACTERISTIC_TYPE_ID", "900000000000011006", quals));
	}
	
	@Test
	public void testSupportedQualifierLoad() throws Exception {
		CodingScheme scheme = lbsvc.resolveCodingScheme(LexBIGServiceTestCase.META_URN, 
				Constructors.createCodingSchemeVersionOrTagFromVersion("200510.bvt"));
		Mappings mappings = scheme.getMappings();
		assertTrue(mappings.getSupportedAssociationQualifierAsReference().stream().
		anyMatch(x -> x.getContent().equals("MODIFIER_ID")));
		assertTrue(mappings.getSupportedAssociationQualifierAsReference().stream().
				anyMatch(x -> x.getContent().equals("CHARACTERISTIC_TYPE_ID")));
		
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(MrsatEntityAssnsToEntityQualsDataForMetaTestIT.class);  
	}   
}