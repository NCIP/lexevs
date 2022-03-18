
package edu.mayo.informatics.lexgrid.convert.directConversions.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.CodingScheme;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.Concept;

public class ConceptTest {
	public Concept concept1;
	public Concept concept1_1;
	public Concept concept2;
	
	public Concept conceptA;
	public Concept conceptB;
	
	@Before
	public void setUp() throws Exception {
		concept1 = new Concept("1", "name", "description", 0);
		concept1_1 = new Concept("1", "name112", "description1212", 1);
		concept2 = new Concept("2", "name", "description", 0);
	}

	@After
	public void tearDown() throws Exception {
		concept1 = null;
		concept1_1 = null;
		concept2 = null;
		conceptA = null;
		conceptB = null;
	}

	@Test
	public final void testConceptStringStringCodingScheme() {
		CodingScheme csA = new CodingScheme();
		CodingScheme csB = new CodingScheme();
	
		conceptA = new Concept("	Purple	The color purple", "\t", csA);
		conceptB = new Concept("1	Color	Holder of colors", "\t", csB);
		
		assertEquals("conceptA code: ", null, conceptA.code);
		assertEquals("conceptA name: ", "Purple", conceptA.name);
		assertEquals("conceptA desc: ", "The color purple", conceptA.description);
		assertEquals("conceptA depth: ", 1, conceptA.depth);
		assertEquals("indentity type a: ", false, csA.isTypeB);
		
		assertEquals("conceptB code: ", "1", conceptB.code);
		assertEquals("conceptB name: ", "Color", conceptB.name);
		assertEquals("conceptB desc: ", "Holder of colors", conceptB.description);
		assertEquals("conceptB depth: ", 0, conceptB.depth);
		assertEquals("indentity type B: ", true, csB.isTypeB);
	}

	@Test
	public void testEqualsObject() {
		assertTrue("test equals true: ", concept1.equals(concept1_1));
		assertFalse("test equals false: ", concept1.equals(concept2));
	}

}