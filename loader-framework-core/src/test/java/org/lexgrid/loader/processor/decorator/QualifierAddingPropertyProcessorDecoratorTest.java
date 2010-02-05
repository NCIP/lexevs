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
package org.lexgrid.loader.processor.decorator;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.persistence.model.EntityProperty;
import org.LexGrid.persistence.model.EntityPropertyId;
import org.LexGrid.persistence.model.EntityPropertyMultiAttrib;
import org.LexGrid.persistence.model.EntityPropertyMultiAttribId;
import org.junit.Test;
import org.springframework.batch.item.ItemProcessor;
import static org.junit.Assert.*;

/**
 * The Class QualifierAddingPropertyProcessorDecoratorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class QualifierAddingPropertyProcessorDecoratorTest {

	/**
	 * Test construct decorator.
	 */
	@Test
	public void testConstructDecorator(){
		QualifierAddingPropertyProcessorDecorator<Integer> decorator =
			new QualifierAddingPropertyProcessorDecorator<Integer>(new ItemProcessor<Integer,EntityProperty>(){

				public EntityProperty process(Integer arg0) throws Exception {
					EntityProperty prop = new EntityProperty(new EntityPropertyId(), null, null);
					prop.getId().setEntityCode(arg0.toString());
					return prop;
				}
			});
		
		assertTrue(decorator != null);
	}
	
	/**
	 * Test process.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testProcess() throws Exception{
		QualifierAddingPropertyProcessorDecorator<Integer> decorator =
			new QualifierAddingPropertyProcessorDecorator<Integer>(new ItemProcessor<Integer,EntityProperty>(){

				public EntityProperty process(Integer arg0) throws Exception {
					EntityProperty prop = new EntityProperty(new EntityPropertyId(), null, null);
					prop.getId().setEntityCode(arg0.toString());
					return prop;
				}
			});
		
		EntityProperty result = decorator.process(1);
		
		assertTrue(result.getId().getEntityCode().equals("1"));
	}
}
