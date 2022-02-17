
package org.lexgrid.loader.rrf.data.association;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class MrrelRuiKeyResolverTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrrelRuiKeyResolverTest {

	/** The resolver. */
	private MrrelRuiAssociationInstanceIdResolver resolver;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		resolver = new MrrelRuiAssociationInstanceIdResolver();
	}
	
	/**
	 * Test not null.
	 */
	@Test
	public void testNotNull() {
		assertNotNull(resolver);
	}
	
	/**
	 * Test resolve.
	 */
	@Test
	public void testResolve() {
		Mrrel mrrel = new Mrrel();
		mrrel.setRui("testRui");
		
		assertTrue(resolver.resolveAssociationInstanceId(mrrel).equals("testRui"));
	}
}