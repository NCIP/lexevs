package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import static org.junit.Assert.*;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.valuesets.sourceasserted.impl.NCItSourceAssertedValueSetUpdateServiceImpl;

public class NCItSourceAssertedValueSetUpdateServiceTest {
	NCItSourceAssertedValueSetUpdateServiceImpl vsUpdate;
	@Before
	public void setUp() throws Exception {
		vsUpdate = new NCItSourceAssertedValueSetUpdateServiceImpl();
	}

	@Test
	public void getCodingSchemeNamespaceForURIandVersion() throws LBException {
		String namespace = vsUpdate.getCodingSchemeNamespaceForURIandVersion("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "17.07e");
		assertTrue(namespace != null);
		assertEquals(namespace, "Thesaurus");
	}

}
