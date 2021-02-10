
package org.LexGrid.LexBIG.Impl.load.meta;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.junit.Test;

/**
 * The Class MetaVersionTestIT.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaVersionTestIT extends DataLoadTestBase {
	

	@Test
	public void testMetaVersion() throws Exception {	
		CodingScheme cs = 
			this.lbs.resolveCodingScheme(LexBIGServiceTestCase.META_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.SAMPLE_META_VERSION));
	
		assertEquals(LexBIGServiceTestCase.SAMPLE_META_VERSION, cs.getRepresentsVersion());
	}	
}