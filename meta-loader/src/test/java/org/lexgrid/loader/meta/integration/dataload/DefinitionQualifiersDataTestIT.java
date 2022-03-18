
package org.lexgrid.loader.meta.integration.dataload;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.concepts.Definition;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.test.util.DataTestUtils;

/**
 * The Class DefinitionQualifiersDataTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefinitionQualifiersDataTestIT extends DataLoadTestBase {

	/** The test entity with definition. */
	private Definition definition;

	
	@Before
	public void buildTestEntity() throws Exception {
		cns.restrictToCodes(Constructors.createConceptReferenceList("C0000039"));
		ResolvedConceptReference rcr1 = cns.resolveToList(null, null, null, -1).getResolvedConceptReference()[0];
	
		Definition[] defs = rcr1.getEntity().getDefinition();
		
		assertTrue(defs.length == 1);
		
		definition = defs[0];
	}
	
	@Test
	public void testDefinitionNotNull() throws Exception {	
		assertNotNull(definition);
	}
	
	/*
	 * gforge: 21723 mcturk 8/17/2009
	 * ATUI, SUPPRESS, CVF, SATAUI, column values will 
	 * be loaded as property qualifiers on the 
	 * Definition type property derived from MRDEF column.
	 */
	@Test
	public void testDefinitionWithPropertyQualifierAui() throws Exception {	
		assertTrue(DataTestUtils.isQualifierNameAndValuePresentInProperty(RrfLoaderConstants.AUI_QUALIFIER, "A4222344", definition));
	}
	
	@Test
	public void testDefinitionWithPropertyQualifierAtui() throws Exception {	
		assertTrue(DataTestUtils.isQualifierNameAndValuePresentInProperty(RrfLoaderConstants.ATUI_QUALIFIER, "AT22533916", definition));
	}
	
	@Test
	public void testDefinitionWithPropertyQualifierSuppress() throws Exception {	
		assertTrue(DataTestUtils.isQualifierNameAndValuePresentInProperty(RrfLoaderConstants.SUPPRESS_QUALIFIER, "N", definition));
	}
	
	@Test
	public void testDefinitionWithPropertyQualifierCvf() throws Exception {
		// AT22533916	
		assertTrue(DataTestUtils.isQualifierNameAndValuePresentInProperty(RrfLoaderConstants.CVF_QUALIFIER, "cvfVal", definition));
	}
	
	public void testDefinitionWithPropertyQualifierSataui() throws Exception {
		// AT22533916	
		assertTrue(DataTestUtils.isQualifierNameAndValuePresentInProperty(RrfLoaderConstants.SATUI_QUALIFIER, "satauiVal", definition));
	}
}