
package org.LexGrid.LexBIG.Impl.load.meta;

import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.LexBIG.Utility.Constructors;
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
	public void setUp() throws Exception {
		super.setUp();
		lbsm = lbs.getServiceMetadata();
	}
	
	/**
	 * Test metadata load count.
	 * 
	 * @throws LBException the LB exception
	 */
	@Test
	public void testMetadataLoadCount() throws LBException{
		//TODO needs a replacement for ChainedFilter in AbstractLazyCodeHolderFactory;
		assertTrue(lbsm.listCodingSchemes().getAbsoluteCodingSchemeVersionReferenceCount() > 0);
	}
	
	/**
	 * Test check metadata entries for meta.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testCheckMetadataEntriesForMeta() throws Exception {
		AbsoluteCodingSchemeVersionReference ref = 
			Constructors.createAbsoluteCodingSchemeVersionReference(
					LexBIGServiceTestCase.META_URN, LexBIGServiceTestCase.SAMPLE_META_VERSION);
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
			Constructors.createAbsoluteCodingSchemeVersionReference(
					LexBIGServiceTestCase.META_URN, LexBIGServiceTestCase.SAMPLE_META_VERSION);
		lbsm.restrictToCodingScheme(ref);
		lbsm.restrictToProperties(new String[]{"rsab"});
		
		MetadataPropertyList mdpl = lbsm.resolve();
		assertTrue(mdpl.getMetadataPropertyCount() > 0);
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(MetadataLoadTestIT.class);  
	}  
}