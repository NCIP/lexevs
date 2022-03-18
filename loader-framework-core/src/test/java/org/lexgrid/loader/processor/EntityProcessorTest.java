
package org.lexgrid.loader.processor;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexgrid.loader.processor.support.EntityResolver;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;

/**
 * The Class EntityProcessorTest.
 * 
 * @author <a href="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</a>
 */
public class EntityProcessorTest {
	/**
	 * Test get entity.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetEntityProcessed() throws Exception {
		TestEntityProcessor entityProcessor = new TestEntityProcessor();
		
		TestEntityResolver er = new TestEntityResolver();
		entityProcessor.setEntityResolver(er);
		
		CodingSchemeIdHolder<Entity> csh = entityProcessor.process(null);
		
		assertTrue(csh.getItem().getEntityCodeNamespace().equals("xyz"));
		assertTrue(csh.getItem().getEntityCode().equals("abc"));
		assertTrue(csh.getItem().getEntityDescription().getContent().equals("desc"));
		assertTrue((csh.getItem().getEntityType())[0].equals("concept"));
		assertTrue(csh.getItem().getIsActive());
		assertTrue(csh.getItem().getIsDefined());
		assertFalse(csh.getItem().getIsAnonymous());
	}

	/**
	 * The Class TestEntityProcessor.
	 * 
	 * @author <a href="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</a>
	 */
	private class TestEntityProcessor extends EntityProcessor{

		/**
		 * Process.
		 * 
		 * @param item the item
		 * 
		 * @return the object
		 * 
		 * @throws Exception the exception
		 */
		public CodingSchemeIdHolder<Entity> process(Object item) throws Exception {
			return super.process(item);
		}	
	}
	
	/**
	 * The Class TestEntityResolver.
	 * 
	 * @author <a href="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</a>
	 */
	private class TestEntityResolver implements EntityResolver{

		/**
		 * Process.
		 * 
		 * @param item the item
		 * 
		 * @return the object
		 * 
		 * @throws Exception the exception
		 */

		public String getEntityCode(Object item) {
			// TODO Auto-generated method stub
			return "abc";
		}

		public String getEntityCodeNamespace(Object item) {
			// TODO Auto-generated method stub
			return "xyz";
		}

		public String getEntityDescription(Object item) {
			// TODO Auto-generated method stub
			return "desc";
		}

		public String[] getEntityTypes(Object item) {
			// TODO Auto-generated method stub
			return (new String[]{"concept"});
		}

		public boolean getIsActive(Object item) {
			// TODO Auto-generated method stub
			return true;
		}

		public boolean getIsAnonymous(Object item) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean getIsDefined(Object item) {
			// TODO Auto-generated method stub
			return true;
		}	
	}
}