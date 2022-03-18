
package org.lexgrid.loader.meta.integration.dataload;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Entity;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class DefinitionPropertyDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefinitionPropertyDataTestIT extends DataLoadTestBase {

	/** The test entity with definition. */
	private Entity testEntityWithDefinition;
	
	/** The test entity with definition. */
	private Entity testEntityWithTwoDefinitions;
	
	@Before
	public void buildTestEntity() throws Exception {
		cns.restrictToCodes(Constructors.createConceptReferenceList("C0000039"));
		ResolvedConceptReference rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		testEntityWithDefinition = rcr1.getEntity();
		
		cns = lbs.getCodingSchemeConcepts("NCI MetaThesaurus", null);
		cns.restrictToCodes(Constructors.createConceptReferenceList("C0000097"));
		ResolvedConceptReference rcr2 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		testEntityWithTwoDefinitions = rcr2.getEntity();
	}
	
	@Test
	public void testDefinitionNotNull() throws Exception {	
		assertNotNull(testEntityWithDefinition.getDefinition());
	}
	
	/**
	 * Test definition count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testDefinitionCount() throws Exception {	
		assertTrue(testEntityWithDefinition.getDefinition().length == 1);
	}
	
	@Test
	public void testTwoDefinitionsNotNull() throws Exception {	
		assertNotNull(testEntityWithDefinition.getDefinition());
	}
	
	/**
	 * Test definition count.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testTwoDefinitionsCount() throws Exception {	
		assertTrue(testEntityWithTwoDefinitions.getDefinition().length == 2);
	}
}