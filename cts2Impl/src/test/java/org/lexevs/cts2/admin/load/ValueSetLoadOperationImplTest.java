/**
 * 
 */
package org.lexevs.cts2.admin.load;

import static org.junit.Assert.*;

import java.io.File;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.junit.Test;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexevs.cts2.LexEvsCTS2Impl;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * @author m004181
 *
 */
public class ValueSetLoadOperationImplTest {

	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.ValueSetLoadOperationImpl#load(java.net.URI, java.net.URI, java.lang.String, java.lang.Boolean)}.
	 * @throws LBException 
	 */
	@Test
	public void testLoadURIURIStringBoolean() throws LBException {
		LexEvsCTS2 cts2 = LexEvsCTS2Impl.defaultInstance();
		ValueSetLoadOperation vsLoadOp = cts2.getAdminOperation().getValueSetLoadOperation();
		URNVersionPair[] urns = vsLoadOp.load(new File(
				"../lbTest/resources/testData/valueDomain/vdTestData.xml").toURI(), null, "LexGrid_Loader", true);
		for(URNVersionPair urn : urns)
		{
			System.out.println("vsd loaded : " + urn);
		}
		
		urns = vsLoadOp.load(new File(
				"../lbTest/resources/testData/valueDomain/pickListTestData.xml").toURI(), null, "LexGrid_Loader", true);
		for(URNVersionPair urn : urns)
		{
			System.out.println("vsd loaded : " + urn);
		}
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.ValueSetLoadOperationImpl#load(org.LexGrid.valueSets.ValueSetDefinition, java.net.URI, java.lang.Boolean)}.
	 */
	@Test
	public void testLoadValueSetDefinitionURIBoolean() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.lexevs.cts2.admin.load.ValueSetLoadOperationImpl#load(org.LexGrid.valueSets.PickListDefinition, java.net.URI, java.lang.Boolean)}.
	 */
	@Test
	public void testLoadPickListDefinitionURIBoolean() {
		fail("Not yet implemented"); // TODO
	}

}
