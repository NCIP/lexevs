
package org.lexgrid.loader.meta.processor.support;

import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.model.Mrrel;

import static org.junit.Assert.*;

/**
 * The Class SelfReferencingAssocQualResolverTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SelfReferencingAssocQualResolverTest {

	/** The resolver. */
	private SelfReferencingAssocQualResolver resolver;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		resolver = new SelfReferencingAssocQualResolver();
	}
	
	/**
	 * Test not null.
	 */
	@Test
	public void testNotNull() {
		assertNotNull(resolver);
	}
	
	/**
	 * Test process.
	 */
	@Test
	public void testProcess() {
		Mrrel mrrel = new Mrrel();
		mrrel.setCui1("cui");
		mrrel.setCui2("cui");
		
		assertTrue(resolver.toProcess(mrrel));
	}
	
	/**
	 * Test dont process.
	 */
	@Test
	public void testDontProcess() {
		Mrrel mrrel = new Mrrel();
		mrrel.setCui1("cui1");
		mrrel.setCui2("cui2");
		
		assertFalse(resolver.toProcess(mrrel));
	}	
}