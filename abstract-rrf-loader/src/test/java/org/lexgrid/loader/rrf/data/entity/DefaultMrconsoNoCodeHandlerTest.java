
package org.lexgrid.loader.rrf.data.entity;

import org.junit.Test;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrconso;
import static org.junit.Assert.*;

/**
 * The Class DefaultMrconsoNoCodeHandlerTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultMrconsoNoCodeHandlerTest {

	/**
	 * Test handle valid code.
	 */
	@Test
	public void testHandleValidCode(){
		Mrconso item = new Mrconso();
		item.setCode("C1111");
		
		DefaultMrconsoNoCodeHandler handler = new DefaultMrconsoNoCodeHandler();
		assertTrue(handler.handleNoCode(item).equals("C1111"));
	}
	
	/**
	 * Test handle no code.
	 */
	@Test
	public void testHandleNoCode(){
		Mrconso item = new Mrconso();
		item.setCode(RrfLoaderConstants.NO_CODE);
		item.setAui("aui");
		item.setCui("cui");
		
		DefaultMrconsoNoCodeHandler handler = new DefaultMrconsoNoCodeHandler();
		assertTrue(handler.handleNoCode(item).equals("cui:aui"));
	}
}