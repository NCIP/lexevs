/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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
