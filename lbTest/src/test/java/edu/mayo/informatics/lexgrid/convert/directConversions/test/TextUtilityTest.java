
package edu.mayo.informatics.lexgrid.convert.directConversions.test;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.lexevs.logging.LoggerFactory;

import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.Association;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.CodingScheme;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.Concept;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.TextUtility;

public class TextUtilityTest extends TestCase {
	public List<String> filesA;
	public List<String> filesB;
	public String token;
	public ArrayList<Concept> conceptsA, conceptsB;
	public ArrayList<Association> associationsA, associationsB;
	
	protected void setUp() throws Exception {
		super.setUp();
		token = "\t";

		//type A input 
		filesA = new ArrayList<String>();
		filesA.add(new File("resources/testData/textLoaderSamples/textLoad_A.txt").toURI().getPath());
		
		//type A result
		conceptsA = new ArrayList<Concept>();
		
		conceptsA.add(new Concept("1", "Purple", "The color purple", 1));
		conceptsA.add(new Concept("2", "Light purple", "The color purple", 2));
		conceptsA.add(new Concept("1", "Purple", "The color purple", 2));
		conceptsA.add(new Concept("3", "Color", "Holder of colors", 0));
		conceptsA.add(new Concept("4", "Red", null, 1));
		conceptsA.add(new Concept("5", "Green", "The color Green", 1));
		conceptsA.add(new Concept("6", "Light Green", "foobar", 2));
		conceptsA.add(new Concept("7", "Dark Green", "The color dark green", 2));
		conceptsA.add(new Concept("8", "Blue", null, 1));
		conceptsA.add(new Concept("9", "red", null, 1));
		conceptsA.add(new Concept("8", "Blue", null, 1));
		conceptsA.add(new Concept("4", "Red", null, 2));
		conceptsA.add(new Concept("5", "Green", "The color Green", 3));
		conceptsA.add(new Concept("10", "Black", null, 1));
		conceptsA.add(new Concept("8", "Blue", "The color blue", 1));
		conceptsA.add(new Concept("4", "Red", null, 2));
		
		associationsA = new ArrayList<Association>();
		Association a;
		a = new Association();
		a.setRelationName("PAR");
		a.setSourceConcept(new Concept("1", "Purple", "The color purple", 1));
		a.addTargetConcept(new Concept("2", "Light purple", "The color purple", 2));
		a.addTargetConcept(new Concept("1", "Purple", "The color purple", 2));
		associationsA.add(a);
		
		a = new Association();
		a.setRelationName("PAR");
		a.setSourceConcept(new Concept("5", "Green", "The color Green", 1));
		a.addTargetConcept(new Concept("6", "Light Green", "foobar", 2));
		a.addTargetConcept(new Concept("7", "Dark Green", "The color dark green", 2));
		associationsA.add(a);
		
		a = new Association();
		a.setRelationName("PAR");
		a.setSourceConcept(new Concept("8", "Blue", null, 1));
		a.addTargetConcept(new Concept("4", "Red", null, 2));
		associationsA.add(a);
		
		a = new Association();
		a.setRelationName("PAR");
		a.setSourceConcept(new Concept("4", "Red", null, 2));
		a.addTargetConcept(new Concept("5", "Green", "The color Green", 3));
		associationsA.add(a);
		
		a = new Association();
		a.setRelationName("PAR");
		a.setSourceConcept(new Concept("3", "Color", "Holder of colors", 0));
		a.addTargetConcept(new Concept("4", "Red", null, 1));
		a.addTargetConcept(new Concept("5", "Green", "The color Green", 1));
		a.addTargetConcept(new Concept("8", "Blue", null, 1));
		a.addTargetConcept(new Concept("9", "red", null, 1));
		a.addTargetConcept(new Concept("10", "black", null, 1));
		associationsA.add(a);
		
		// type B input
		filesB = new ArrayList<String>();
		filesB.add(new File("resources/testData/textLoaderSamples/textLoad_B.txt").toURI().getPath());
		// type B output
		Concept c1 = new Concept("1", "Color", "Holder of colors", 0);
		Concept c2 = new Concept("2", "Red", null, 3);
		Concept c3 = new Concept("3", "Green", "The color Green", 1);
		Concept c4 = new Concept("1", "Color", "Holder of colors", 0);
		Concept c5 = new Concept("2", "Red", null, 1);
		Concept c6 = new Concept("3", "Green", "The color Green", 1);
		Concept c7 = new Concept("4", "Shape", null, 0);

		conceptsB = new ArrayList<Concept>();
		conceptsB.add(c1);
		conceptsB.add(c2);
		conceptsB.add(c3);
		conceptsB.add(c4);
		conceptsB.add(c5);
		conceptsB.add(c6);
		conceptsB.add(c7);
		
		associationsB = new ArrayList<Association>();
		a = new Association();
		a.setRelationName("PAR");
		a.setSourceConcept(c1);
		a.addTargetConcept(c2);
		a.addTargetConcept(c3);
		
		associationsB.add(a);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		token = null;
		filesA = null;
		filesB = null;
		conceptsA = null;
		associationsA = null;
		conceptsB = null;
		associationsB = null;
	}

	public void testReadAndVerifyConceptsTypeA() {
		// type A
		for (String f : filesA){
			try {
				CodingScheme cs = TextUtility.readAndVerifyConcepts(new URI(f), LoggerFactory.getLogger(), token, false);
				
				//scheme
				assertEquals("name: ", "colorsA", cs.codingSchemeName);
				assertEquals("id: ", "1.2.3", cs.codingSchemeId);
				assertEquals("default lan: ", "en", cs.defaultLanguage);
				assertEquals("representsVer: ", "1.0", cs.representsVersion);
				assertEquals("formalName: ", "colors coding scheme", cs.formalName);
				assertEquals("source: ", "Dans Head", cs.source);
				assertEquals("entityDesc: ", "a simple example coding scheme using colors", cs.entityDescription);
				assertEquals("copy right: ", "This isn't worth copyrighting :)", cs.copyright);
				
				assertEquals("the concept size is not right", conceptsA.size(), cs.concepts.length);
				//concepts
				for (int i = 0; i < conceptsA.size(); i++){
					assertEquals("call concept equals method -- idx:" + Integer.toString(i), conceptsA.get(i), cs.concepts[i]);
					assertEquals("compare code -- idx:" + Integer.toString(i), conceptsA.get(i).code, cs.concepts[i].code);
					assertEquals("compare name -- idx:" + Integer.toString(i), conceptsA.get(i).name, cs.concepts[i].name);
					assertEquals("compare description -- idx:" + Integer.toString(i), conceptsA.get(i).description, cs.concepts[i].description);
					assertEquals("compare depth -- idx:" + Integer.toString(i), conceptsA.get(i).depth, cs.concepts[i].depth);
				}
				//associations
				assertEquals("the association size is not right", associationsA.size(), cs.associations.length);
				for (int i = 0; i < associationsA.size(); i++) {
					assertTrue("association equals -- index:" + Integer.toString(i), associationsA.contains(cs.associations[i]));
				}
				
			} catch (Exception e) {
				fail("open file error" + e.toString());
			}
		}
	}
	
	public void testReadAndVerifyConceptsTypeB() {
		// Type B
		for(String f : filesB) {
			try {
				CodingScheme cs = TextUtility.readAndVerifyConcepts(new URI(f), LoggerFactory.getLogger(), token, true);
				
				//scheme
				assertEquals("name: ", "colors2", cs.codingSchemeName);
				assertEquals("id: ", "1.2.4", cs.codingSchemeId);
				assertEquals("default lan: ", "en", cs.defaultLanguage);
				assertEquals("representsVer: ", "1.1", cs.representsVersion);
				assertEquals("formalName: ", "colors coding scheme", cs.formalName);
				assertEquals("source: ", "Dans Head", cs.source);
				assertEquals("entityDesc: ", "a simple example coding scheme using colors", cs.entityDescription);
				assertEquals("copy right: ", "This isn't worth copyrighting :)", cs.copyright);
				
				//concepts
				assertEquals("concept size is not correct: ", conceptsB.size(), cs.concepts.length);
				for (int i = 0; i < cs.concepts.length; i++) {
					assertTrue("concept equals - idx: " + Integer.toString(i), conceptsB.contains(cs.concepts[i]));
				}
				
				//associations
				assertEquals("association size is not correct: ", associationsB.size(), cs.associations.length);
				for (int i = 0; i < cs.associations.length; i++) {
					assertTrue("association equals - idx: " + Integer.toString(i), associationsB.contains(cs.associations[i]));
				}
				
			} catch (Exception e) {
				fail("open file error" + e.toString());
			}
		}		
	}
	
	public void testException(){
		try {
			CodingScheme cs = TextUtility.readAndVerifyConcepts(new URI("does/exist/nofile.txt"), LoggerFactory.getLogger(), token, false);
			fail("should raise a filenotfound exception");
		} catch (Exception success) {
		}
		try {
			CodingScheme cs = TextUtility.readAndVerifyConcepts(new URI("resources/testData/textLoaderSamples/textLoad_B_invalid_content.txt"), LoggerFactory.getLogger(), token, true);
			fail("should raise a filenotfound exception");
		} catch (Exception success) {
		}
	}
	
}