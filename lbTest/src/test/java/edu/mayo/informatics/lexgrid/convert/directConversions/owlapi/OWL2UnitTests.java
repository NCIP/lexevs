package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;



import java.util.Date;

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
	
	@Test
	public void testIRIParsingFromRendererResultsLEXEVS_684(){
		String value = "PrognosisGood>";
		String expected = "PrognosisGood";
		OwlApi2LG owl2 = new OwlApi2LG(null, null, null, 0, null);
		assertEquals(expected, owl2.getFromLastIndexOfColonOrHash(value));
	}
	
	@Test
	public void testDateParsingMethod(){
		String value = "august 8, 2014";
		Date date = null;
		OwlApi2LG owl2 = new OwlApi2LG(null, null, null, 0, null);
		try{
		date = owl2.parseEffectiveDate(value);
		}
		catch(Exception e){
			fail("Cannot Parse date");
		}
		assertTrue(date != null);
	}
	
	@Test
	public void testDateQuoteParse(){
		String value = "\"august 8, 2014\"";
		String expected = "august 8, 2014";
		OwlApi2LG owl2 = new OwlApi2LG(null, null, null, 0, null);
		assertTrue(owl2.stripQuotes(value).equals(expected));

	}

}
