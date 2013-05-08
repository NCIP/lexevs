/**
 * 
 */
package org.cts2.internal.profile.query;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.cts2.internal.profile.query.LexEvsCodeSystemVersionQueryService;
import org.cts2.profile.BaseService;
import org.junit.Test;

/**
 * 
 *	@author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * 	
 *
 */
public class LexEvsValueSetDefinitionQueryTest {

	@Resource
	private LexEvsCodeSystemVersionQueryService lexEvsCodeSystemVersionQuery;
	
	@Resource
	private BaseService baseService;
	
	/**
	 * Test method for {@link org.cts2.internal.profile.query.LexEvsValueSetDefinitionQueryService#resolve(org.cts2.uri.ValueSetDefinitionDirectoryURI, org.cts2.service.core.QueryControl, org.apache.commons.betwixt.io.read.ReadContext)}.
	 */
	@Test
	public void testResolve() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.cts2.internal.profile.query.LexEvsValueSetDefinitionQueryService#resolveAsList(org.cts2.uri.ValueSetDefinitionDirectoryURI, org.cts2.service.core.QueryControl, org.apache.commons.betwixt.io.read.ReadContext)}.
	 */
	@Test
	public void testResolveAsList() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.cts2.internal.profile.query.LexEvsValueSetDefinitionQueryService#restrictToEntities(org.cts2.uri.ValueSetDefinitionDirectoryURI, java.util.List)}.
	 */
	@Test
	public void testRestrictToEntities() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.cts2.internal.profile.query.AbstractBaseQueryService#count(org.cts2.uri.DirectoryURI, org.cts2.service.core.ReadContext)}.
	 */
	@Test
	public void testCount() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.cts2.internal.profile.query.AbstractBaseQueryService#restrict(org.cts2.uri.DirectoryURI, org.cts2.core.Filter)}.
	 */
	@Test
	public void testRestrict() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.cts2.internal.profile.query.AbstractBaseQueryService#setDirectoryURIFactory(org.cts2.internal.model.uri.factory.DirectoryURIFactory)}.
	 */
	@Test
	public void testSetDirectoryURIFactory() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.cts2.internal.profile.query.AbstractBaseQueryService#getDirectoryURIFactory()}.
	 */
	@Test
	public void testGetDirectoryURIFactory() {
		fail("Not yet implemented");
	}

}
