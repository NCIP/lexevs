
package edu.mayo.informatics.lexgrid.convert.directConversions.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.Association;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon.Concept;

public class AssociationTest {
	public Concept srcConcept1, srcConcept2, tgtConcept1a, tgtConcept2a,  tgtConcept1b, tgtConcept2b;
	public Association a1, a2;

	@Before
	public void setUp() throws Exception {
		a1 = new Association();
		
		srcConcept1 = new Concept("1", "Color", "Color desc", 0);
		tgtConcept1a = new Concept("2", "Green", null, 1);
		tgtConcept1b = new Concept("3", "Red", null, 1);
		
	
		a2 = new Association();		
		srcConcept2 = new Concept("1", "Color2", "Color desc2", 0);
		tgtConcept2a = new Concept("2", "Green2", null, 4);
		tgtConcept2b = new Concept("3", "Red", null, 1);
	}

	@After
	public void tearDown() throws Exception {
		a1 = null;
		a2 = null;
		srcConcept1 = null;
		srcConcept2 = null;
		tgtConcept1a = null;
		tgtConcept2a = null;
		tgtConcept1b = null;
		tgtConcept2b = null;
	}

	@Test
	public final void testRelationName() {
		assertEquals("get relation name right after init null", null, a1.getRelationName());
		a1.setRelationName("PAR");
		assertEquals("get relation name", "PAR", a1.getRelationName());
	}

	@Test
	public final void testSourceConcept() {
		assertEquals("source concept null", null, a1.getSourceConcept());
		a1.setSourceConcept(srcConcept1);
		assertEquals("source concept", srcConcept1, a1.getSourceConcept());
	}

	
	@Test
	public final void testGetTargetConceptSet() {
		assertEquals("get target concept set", 0, a1.getTargetConceptSet().size());
	}

	@Test
	public final void testAddTargetConcept() {
		a1.addTargetConcept(tgtConcept1a);
		assertEquals("add target concept size ", 1, a1.getTargetConceptSet().size());
		assertEquals("add target concept equals", tgtConcept1a, a1.getTargetConceptSet().get(0));
		a1.addTargetConcept(tgtConcept1a);
		assertEquals("add target concept size ", 1, a1.getTargetConceptSet().size());
	}

	@Test
	public final void testEqualsObject() {
		a1 = new Association();
		a2 = new Association();
		assertTrue("equals ",a1.equals(a2));
		a1.setRelationName("PAR");
		assertFalse("equals ",a1.equals(a2));
		a2.setRelationName("PAR");
		assertTrue("equals ",a1.equals(a2));
		a1.setSourceConcept(srcConcept1);
		assertFalse("equals ",a1.equals(a2));
		a2.setSourceConcept(srcConcept2);
		assertTrue("equals src",a1.equals(a2));
		a1.addTargetConcept(tgtConcept1a);
		assertFalse("equals ",a1.equals(a2));
		a2.addTargetConcept(tgtConcept2b);
		assertFalse("equals ",a1.equals(a2));
		a2.addTargetConcept(tgtConcept2a);
		assertFalse("equals ",a1.equals(a2));
		a1.addTargetConcept(tgtConcept1b);
		assertTrue("equals src",a1.equals(a2));
	}

}