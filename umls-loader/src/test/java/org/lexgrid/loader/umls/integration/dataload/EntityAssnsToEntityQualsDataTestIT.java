
package org.lexgrid.loader.umls.integration.dataload;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.test.util.DataTestUtils;

import util.integration.LoadUmlsForIntegration;

/**
 * The Class EntityAssnsToEntityQualsDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnsToEntityQualsDataTestIT extends DataLoadTestBase {
	
	/** The graph focus. */
	private AssociatedConcept[] associatedConcept;
	
	/**
	 * Builds the test entity.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void buildTestEntity() throws Exception {
		associatedConcept = cng.resolveAsList(Constructors.createConceptReference("ACRMG", 
				LoadUmlsForIntegration.UMLS_URN), true, true, 1, 1, null, null, null, -1)
				.getResolvedConceptReference(0)
				.getSourceOf()
				.getAssociation()[0]
				.getAssociatedConcepts()
				.getAssociatedConcept();
	}
	
	@Test
	public void testQualsNotNull() throws Exception {	
		NameAndValueList quals = DataTestUtils.getAssociatedConcept(associatedConcept, "U000010").getAssociationQualifiers();
		assertNotNull(quals);
	}
	
	@Test
	public void testRelaQual() throws Exception {	
		NameAndValueList quals = DataTestUtils.getAssociatedConcept(associatedConcept, "U000010").getAssociationQualifiers();
		assertTrue(DataTestUtils.isQualifierNameAndValuePresent(RrfLoaderConstants.RELA_QUALIFIER, "mapped_from", quals));
	}
}