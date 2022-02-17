
package org.LexGrid.LexBIG.Impl.load.meta;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.meta.constants.MetaLoaderConstants;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;

/**
 * The Class EntityAssnsToEntityQualsDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnsToEntityQualsDataTestIT extends DataLoadTestBase {
	
	/** The graph focus. */
	private AssociatedConcept associatedConcept;
	
	/**
	 * Builds the test entity.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		associatedConcept = cng.resolveAsList(Constructors.createConceptReference("C0000005", 
				"NCI MetaThesaurus"), true, true, 1, 1, null, null, null, -1)
				.getResolvedConceptReference(0)
				.getTargetOf()
				.getAssociation()[0]
				.getAssociatedConcepts()
				.getAssociatedConcept(0);
	}
	
	@Test
	public void testQualsNotNull() throws Exception {	
		NameAndValueList quals = associatedConcept.getAssociationQualifiers();
		assertNotNull(quals);
	}
	
	@Test
	public void testSourceAuiQual() throws Exception {	
		NameAndValueList quals = associatedConcept.getAssociationQualifiers();
		assertTrue(
				DataTestUtils.isQualifierNameAndValuePresent(MetaLoaderConstants.SOURCE_AUI_QUALIFIER, "A3586555", quals));
	}
	
	@Test
	public void testTargetAuiQual() throws Exception {	
		NameAndValueList quals = associatedConcept.getAssociationQualifiers();
		assertTrue(
		DataTestUtils.isQualifierNameAndValuePresent(MetaLoaderConstants.TARGET_AUI_QUALIFIER, "A4345877", quals));
	}
	
	@Test
	public void testRelaQual() throws Exception {	
		NameAndValueList quals = associatedConcept.getAssociationQualifiers();
		assertTrue(
		DataTestUtils.isQualifierNameAndValuePresent(RrfLoaderConstants.RELA_QUALIFIER, "mapped_from", quals));
	}
	
	@Test
	public void testStype1Qual() throws Exception {	
		NameAndValueList quals = associatedConcept.getAssociationQualifiers();
		assertTrue(
		DataTestUtils.isQualifierNameAndValuePresent(RrfLoaderConstants.STYPE1_QUALIFIER, "AUI", quals));
	}
	
	@Test
	public void testStype2Qual() throws Exception {	
		NameAndValueList quals = associatedConcept.getAssociationQualifiers();
		assertTrue(
		DataTestUtils.isQualifierNameAndValuePresent(RrfLoaderConstants.STYPE2_QUALIFIER, "AUI", quals));
	}
	
	@Test
	public void testSruiQual() throws Exception {	
		NameAndValueList quals = associatedConcept.getAssociationQualifiers();
		assertTrue(
		DataTestUtils.isQualifierNameAndValuePresent(RrfLoaderConstants.SRUI_QUALIFIER, "testSRUI", quals));
	}
	
	@Test
	public void testRgQual() throws Exception {	
		NameAndValueList quals = associatedConcept.getAssociationQualifiers();
		assertTrue(
		DataTestUtils.isQualifierNameAndValuePresent(RrfLoaderConstants.RG_QUALIFIER, "testRG", quals));
	}
	
	@Test
	public void testSuppressQual() throws Exception {	
		//'N' values aren't loaded.
		NameAndValueList quals = associatedConcept.getAssociationQualifiers();
		assertFalse(
		DataTestUtils.isQualifierNameAndValuePresent(RrfLoaderConstants.SUPPRESS_QUALIFIER, "N", quals));
	}
	
	@Test
	public void testCVFQual() throws Exception {	
		NameAndValueList quals = associatedConcept.getAssociationQualifiers();
		assertTrue(
		DataTestUtils.isQualifierNameAndValuePresent(RrfLoaderConstants.CVF_QUALIFIER, "testCVF", quals));
	}
	
	@Test
	public void testRuiQual() throws Exception {	
		NameAndValueList quals = associatedConcept.getAssociationQualifiers();
		assertTrue(
		DataTestUtils.isQualifierNameAndValuePresent(RrfLoaderConstants.RUI_QUALIFIER, "R17427607", quals));
	}
	
	@Test
	public void testSourceQual() throws Exception {	
		NameAndValueList quals = associatedConcept.getAssociationQualifiers();
		assertTrue(
		DataTestUtils.isQualifierNameAndValuePresent(MetaLoaderConstants.SOURCE_QUALIFIER, "MSH", quals));
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(EntityAssnsToEntityQualsDataTestIT.class);  
	}  
}