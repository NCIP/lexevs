package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;



import java.io.File;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

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
	
	@Test
	public void testResolveLabels() throws OWLOntologyCreationException{
		OwlApi2LG api = new OwlApi2LG(null, null, null, 1, null);
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		IRI iri = IRI.create(new File("resources/testData/owl2/owl2-special-cases-Defined-Annotated.owl")
				.toURI());
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(iri);
		api.setOntology(ontology);
		List<String> list = null;
		for (OWLAnnotationProperty prop : ontology.getAnnotationPropertiesInSignature()) {
			if(prop.getIRI().getFragment().equals("IAO_0000111")){
				list = api.resolveLabels(prop);
			}
		}
		assertNotNull(list);
		assertTrue(list.size() > 4);
		assertTrue(list.contains("editor preferred term~editor preferred label"));
		
	}
	@Test
	public void testProcessFormattedExpression() throws OWLOntologyCreationException{

		OwlApi2LG api = new OwlApi2LG(null, null, null, 1, null);
		String formattedExpression = "This has a\n return followed by a space";		
		String result = api.processEquivalentClassExp(formattedExpression);
		assertEquals(result, "This has a return followed by a space");
		
		String fexAlternateOne = "This has a\r return followed by a space";	
		String resultOne = api.processEquivalentClassExp(fexAlternateOne);
		assertEquals(resultOne, "This has a return followed by a space");
		
		String fexAlternateTwo = "This has a\r\n return followed by a space";	
		String resultTwo= api.processEquivalentClassExp(fexAlternateTwo);
		assertEquals(resultTwo, "This has a return followed by a space");
		
		String fexAlternateNoSpace = "This has a\nreturn with no space";	
		String resultNoSpace= api.processEquivalentClassExp(fexAlternateNoSpace);
		assertEquals(resultNoSpace, "This has a return with no space");
		
		String fexAlternateNoSpaceOne = "This has a\rreturn with no space";	
		String resultNoSpaceOne = api.processEquivalentClassExp(fexAlternateNoSpaceOne);
		assertEquals(resultNoSpaceOne, "This has a return with no space");
		
		String fexAlternateNoSpaceTwo = "This has a\r\nreturn with no space";	
		String resultNoSpaceTwo = api.processEquivalentClassExp(fexAlternateNoSpaceTwo);
		assertEquals(resultNoSpaceTwo, "This has a return with no space");
		
		String formattedExpressionTwoReturns = "This has a\n return followed\n by a space";		
		String result2Returns = api.processEquivalentClassExp(formattedExpressionTwoReturns);
		assertEquals(result2Returns, "This has a return followed by a space");
		
		String fexAlternateOne2Ret = "This has a\r return followed by\r a space";	
		String resultOne2Ret = api.processEquivalentClassExp(fexAlternateOne2Ret);
		assertEquals(resultOne2Ret, "This has a return followed by a space");
		
		String fexAlternateTwo2Ret = "This has a\r\n return followed\r\n by a space";	
		String resultTwo2Ret= api.processEquivalentClassExp(fexAlternateTwo2Ret);
		assertEquals(resultTwo2Ret, "This has a return followed by a space");
		
		String fexAlternateNoSpace2Ret = "This has a\nreturn with\nno space";	
		String resultNoSpace2Ret= api.processEquivalentClassExp(fexAlternateNoSpace2Ret);
		assertEquals(resultNoSpace2Ret, "This has a return with no space");
		
		String fexAlternateNoSpaceOne2Ret = "This has a\rreturn with\rno space";	
		String resultNoSpaceOne2Ret = api.processEquivalentClassExp(fexAlternateNoSpaceOne2Ret);
		assertEquals(resultNoSpaceOne2Ret, "This has a return with no space");
		
		String fexAlternateNoSpaceTwo2Ret = "This has a\r\nreturn with\r\nno space";	
		String resultNoSpaceTwo2Ret = api.processEquivalentClassExp(fexAlternateNoSpaceTwo2Ret);
		assertEquals(resultNoSpaceTwo2Ret, "This has a return with no space");
		
		String formattedExpressionPreSpace = "This has a \nreturn preceeded by a space";		
		String resultPreSpace = api.processEquivalentClassExp(formattedExpressionPreSpace);
		assertEquals(resultPreSpace, "This has a return preceeded by a space");
		
		String fexAlternateOnePreSpace = "This has a \rreturn preceeded by a space";	
		String resultOnePreSpace = api.processEquivalentClassExp(fexAlternateOnePreSpace);
		assertEquals(resultOnePreSpace, "This has a return preceeded by a space");
		
		String fexAlternateTwoPreSpace = "This has a \r\nreturn preceeded by a space";	
		String resultTwoPreSpace = api.processEquivalentClassExp(fexAlternateTwoPreSpace);
		assertEquals(resultTwoPreSpace, "This has a return preceeded by a space");
		
		String spex = "This has a lot      of   spaces";
		String spex1 = api.processEquivalentClassExp(spex);
		assertEquals(spex1, "This has a lot of spaces");
		
		String spex2 = "This has less   of those  spaces";
		String spex3 = api.processEquivalentClassExp(spex2);
		assertEquals(spex3, "This has less of those spaces");
		
	}

}
