/**
 * 
 */
package org.cts2.internal.profile.read;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Resource;

import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.test.BaseCts2UnitTest;
import org.cts2.valueset.ValueSetDefinition;
import org.cts2.valueset.ValueSetDefinitionEntry;
import org.junit.Test;
/**
 * 
 *	@author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * 	
 *
 */
public class LexEvsValueSetDefinitionReadServiceTest extends BaseCts2UnitTest {

	@Resource
	private LexEvsValueSetDefinitionReadService lexEvsValueSetDefinitionReadService;
	
	@Test
	public void testInit(){
		assertNotNull(lexEvsValueSetDefinitionReadService);
	}
	
	/**
	 * Test method for {@link org.cts2.internal.profile.read.LexEvsValueSetDefinitionReadService#exists(java.net.URI, org.cts2.service.core.ReadContext)}.
	 * @throws URISyntaxException 
	 */
	@Test
	public void testExists() throws URISyntaxException {
		assertTrue(lexEvsValueSetDefinitionReadService.exists(new URI("SRITEST:AUTO:Ford"), null));
	}

	/**
	 * Test method for {@link org.cts2.internal.profile.read.LexEvsValueSetDefinitionReadService#existsDefinitionForValueSet(org.cts2.core.NameOrURI, org.cts2.core.NameOrURI, org.cts2.service.core.ReadContext)}.
	 */
	@Test
	public void testExistsDefinitionForValueSet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.cts2.internal.profile.read.LexEvsValueSetDefinitionReadService#getDefinitionForValueSet(org.cts2.core.NameOrURI, org.cts2.core.NameOrURI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)}.
	 */
	@Test
	public void testGetDefinitionForValueSet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.cts2.internal.profile.read.LexEvsValueSetDefinitionReadService#read(java.net.URI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)}.
	 * @throws URISyntaxException 
	 */
	@Test
	public void testRead() throws URISyntaxException {
		QueryControl qc = new QueryControl();
		qc.setTimeLimit(10000L);
		qc.setMaxToReturn(null);
		NameOrURI nameOrURI = new NameOrURI();
		nameOrURI.setName("XML");
		qc.setFormat(nameOrURI);
		
		ValueSetDefinition vsd = this.lexEvsValueSetDefinitionReadService.read(new URI/*("SRITEST:AUTO:PropertyRefTest1") */("CTS2TESTVSD"), qc, null);
		
		System.out.println("vsd.about : " + vsd.getAbout());
		System.out.println("vsd.formal name : " + vsd.getFormalName());
		
		for (ValueSetDefinitionEntry de : vsd.getEntry())
		{
			System.out.println("ENTRY TYPE : " + de.getEntryType());
			System.out.println("OPERATOR : " + de.getOperator());
		}
	}

}
