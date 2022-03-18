
package org.LexGrid.LexBIG.Impl.load.meta;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.function.codednodegraph.BaseCodedNodeGraphTest;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

import org.LexGrid.LexBIG.Impl.testUtility.DataTestUtils;

public class MrhierAssocQualifierTestIT extends DataLoadTestBase {
	
	private ResolvedConceptReference refC0000005;
	private ResolvedConceptReference refC0036775;
	private ResolvedConceptReference refC0001555;
	private ResolvedConceptReference refCL385598;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		refC0000005 = cng.resolveAsList(Constructors.createConceptReference("C0000005", LexBIGServiceTestCase.META_URN), true, false, 0, 1, null, null, null, -1).getResolvedConceptReference()[0];	
		refC0036775 = cng.resolveAsList(Constructors.createConceptReference("C0036775", LexBIGServiceTestCase.META_URN), true, false, 0, 1, null, null, null, -1).getResolvedConceptReference()[0];
		refC0001555 = cng.resolveAsList(Constructors.createConceptReference("C0001555", LexBIGServiceTestCase.META_URN), false, true, 0, 1, null, null, null, -1).getResolvedConceptReference()[0];
		refCL385598 = cng.resolveAsList(Constructors.createConceptReference("CL385598", LexBIGServiceTestCase.META_URN), true, false, 0, 1, null, null, null, -1).getResolvedConceptReference()[0];
	}
	
	@Test
	public void testNotNull() throws Exception {	
		assertNotNull(refC0000005);
		assertNotNull(refC0036775);
		assertNotNull(refC0001555);
		assertNotNull(refCL385598);
	}

	@Test
	public void testIsC0000005SourceOfC0036775() throws Exception {	
		Association[] assocs = refC0000005.getSourceOf().getAssociation();
		assertTrue(assocs.length == 1);
				
		AssociatedConcept[] acons = assocs[0].getAssociatedConcepts().getAssociatedConcept();
		
		assertTrue(acons.length == 1);
		
		assertTrue(acons[0].getCode().equals("C0036775"));
	}
	
	@Test
	public void testIsrefC0036775SourceOfC0001555() throws Exception {	
		Association[] assocs = refC0036775.getSourceOf().getAssociation();
				
		AssociatedConcept[] acons = assocs[0].getAssociatedConcepts().getAssociatedConcept();

		BaseCodedNodeGraphTest.associatedConceptListContains(acons, "C0001555");
	}
	
	@Test
	public void testIsrefC0001555TargetOfCL385598() throws Exception {	
		Association[] assocs = refC0001555.getTargetOf().getAssociation();

		Association assoc = DataTestUtils.getAssociation(assocs, "PAR");
		
		AssociatedConcept[] acons = assoc.getAssociatedConcepts().getAssociatedConcept();
		
		assertTrue(BaseCodedNodeGraphTest.associatedConceptListContains(acons, "CL385598"));
	}
	
	public static junit.framework.Test suite() {  
		return new JUnit4TestAdapter(MrhierAssocQualifierTestIT.class);  
	}  
}