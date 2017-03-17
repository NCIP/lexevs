package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;



import java.io.File;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

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

}
