
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.junit.Before;
import org.junit.Test;

public class MifVocabularyHierarchyRootsTestIT extends DataLoadTestBase {
	
	private ResolvedConceptReferenceList roots;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		LexBIGServiceConvenienceMethods lbscm = 
			(LexBIGServiceConvenienceMethods)lbs.getGenericExtension("LexBIGServiceConvenienceMethods");
		
		CodingSchemeVersionOrTag version = new CodingSchemeVersionOrTag();
		version.setVersion(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_VERSION);
		
				
		roots = lbscm.getHierarchyRoots(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_URN, version, null);
//		roots = lbscm.getHierarchyRoots(LexBIGServiceTestCase.HL7_MIF_VOCABULARY_SCHEME, version, null);
	}
	
	@Test
	public void testRootsNotNull(){
		assertNotNull(roots);
	}
	
	@Test
	public void testRootsLength(){
		int count = roots.getResolvedConceptReferenceCount();
		assertTrue("Roots: " + count, count == 424);
	}

}