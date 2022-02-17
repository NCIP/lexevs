
package org.lexgrid.loader.umls.reader.support;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.lexgrid.loader.rrf.model.Mrhier;

/**
 * The Class UmlsMrhierHcdSabSkipPolicyTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsMrhierHcdSabSkipPolicyTest {

	/**
	 * Test skip.
	 */
	@Test
	public void testSkip(){
		Mrhier mrhier = new Mrhier();
		mrhier.setSab("NOT-THE-SAB");
		
		
		UmlsMrhierHcdSabSkipPolicy policy = new UmlsMrhierHcdSabSkipPolicy();
		policy.setSab("SAB");
		assertTrue(policy.toSkip(mrhier));
	}
	
	/**
	 * Test skip no hcd.
	 */
	@Test
	public void testSkipNoHcd(){
		Mrhier mrhier = new Mrhier();
		mrhier.setSab("SAB");
		
		
		UmlsMrhierHcdSabSkipPolicy policy = new UmlsMrhierHcdSabSkipPolicy();
		policy.setSab("SAB");
		assertTrue(policy.toSkip(mrhier));
	}
	
	/**
	 * Test skip hcd wrong sab.
	 */
	@Test
	public void testSkipHcdWrongSab(){
		Mrhier mrhier = new Mrhier();
		mrhier.setSab("NOT-THE-SAB");
		mrhier.setHcd("SOME-HCD");
		
		UmlsMrhierHcdSabSkipPolicy policy = new UmlsMrhierHcdSabSkipPolicy();
		policy.setSab("SAB");
		assertTrue(policy.toSkip(mrhier));
	}
	
	/**
	 * Test dont skip.
	 */
	@Test
	public void testDontSkip(){
		Mrhier mrhier = new Mrhier();
		mrhier.setSab("SAB");
		mrhier.setHcd("SOME-HCD");
		
		UmlsMrhierHcdSabSkipPolicy policy = new UmlsMrhierHcdSabSkipPolicy();
		policy.setSab("SAB");
		assertFalse(policy.toSkip(mrhier));
	}
}