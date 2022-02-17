
package org.lexgrid.loader.meta.integration.dataload;

import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class MetadataLoadTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetadataLoadTestIT extends DataLoadTestBase {

	/** The lbsm. */
	private LexBIGServiceMetadata lbsm;
	
	/**
	 * Sets the up metadata service.
	 * 
	 * @throws LBException the LB exception
	 */
	@Before
	public void setUpMetadataService() throws LBException{
		lbsm = lbs.getServiceMetadata();
	}
	
	/**
	 * Test metadata load count.
	 * 
	 * @throws LBException the LB exception
	 */
	@Test
	public void testMetadataLoadCount() throws LBException{
		assertTrue(lbsm.listCodingSchemes().getAbsoluteCodingSchemeVersionReferenceCount() == 1);
	}
	
	/**
	 * Test check metadata entries for meta.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testCheckMetadataEntriesForMeta() throws Exception {
		AbsoluteCodingSchemeVersionReference ref = 
			lbsm.listCodingSchemes().getAbsoluteCodingSchemeVersionReference(0);
		lbsm.restrictToCodingScheme(ref);
		MetadataPropertyList mdpl = lbsm.resolve();
		assertTrue(mdpl.getMetadataPropertyCount() > 0);
	}
	
	/**
	 * Test metadata properties.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testMetadataProperties() throws Exception {
		AbsoluteCodingSchemeVersionReference ref = 
			lbsm.listCodingSchemes().getAbsoluteCodingSchemeVersionReference(0);
		lbsm.restrictToCodingScheme(ref);
		lbsm.restrictToProperties(new String[]{"rsab"});
		
		MetadataPropertyList mdpl = lbsm.resolve();
		assertTrue(mdpl.getMetadataPropertyCount() > 0);
	}
}