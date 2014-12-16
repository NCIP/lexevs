package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;



import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import edu.mayo.informatics.lexgrid.convert.directConversions.owlapi.OwlApi2LG;

public class OWL2UnitTests extends TestCase {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testValueParsingMethod() {
		String value = "\"http://some.kind.of.IRI#Thing\"^^rdf:thingy";
		String expected = "http://some.kind.of.IRI#Thing";
		OwlApi2LG owl2 = new OwlApi2LG(null, null, null, 0, null);
		assertEquals(expected, owl2.stripDataType(value));
	}

}
