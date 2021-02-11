
package org.lexgrid.loader.dao.template;

import static org.junit.Assert.assertTrue;

import org.LexGrid.naming.SupportedCodingScheme;
import org.junit.Test;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class CachedSupportedAttributeSupportTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CachingSupportedAttribuiteTemplateTest extends LoaderFrameworkCoreTestBase {

	@Autowired
	private CachingSupportedAttribuiteTemplate cachingSupportedAttribuiteTemplate;
	/**
	 * Test insert cached.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertCached() throws Exception {
		SupportedCodingScheme cs = new SupportedCodingScheme();
		
		cs.setContent("content");
		cs.setIsImported(false);
		cs.setLocalId("locaId");
		
		cachingSupportedAttribuiteTemplate.insert("uri", "version", cs);
		
		assertTrue(cachingSupportedAttribuiteTemplate.getAttributeCache().size() == 1);
	}
	
	@Test
	public void testInsertTwoCached() throws Exception {
		SupportedCodingScheme cs = new SupportedCodingScheme();
		
		cs.setContent("content");
		cs.setIsImported(false);
		cs.setLocalId("locaId");
		
		SupportedCodingScheme cs2 = new SupportedCodingScheme();
		
		cs2.setContent("content");
		cs2.setIsImported(false);
		cs2.setLocalId("locaId");
		
		cachingSupportedAttribuiteTemplate.insert("uri", "version", cs);
		cachingSupportedAttribuiteTemplate.insert("uri", "version", cs2);
		
		assertTrue(cachingSupportedAttribuiteTemplate.getAttributeCache().size() == 1);
	}
}