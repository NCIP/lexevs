package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import static org.junit.Assert.*;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.index.service.search.SourceAssertedValueSetSearchIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

public class AssertedValueSetIndexCreation {
	SourceAssertedValueSetSearchIndexService svc;
	@Before
	public void setUp() throws Exception {
		 svc = LexEvsServiceLocator.getInstance().getIndexServiceManager().getAssertedValueSetIndexService();
	}

	@Test
	public void test() {
		AbsoluteCodingSchemeVersionReference ref = Constructors.
				createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5");
		svc.createIndex(ref);
		assertTrue(svc.doesIndexExist(ref));
	}

}
