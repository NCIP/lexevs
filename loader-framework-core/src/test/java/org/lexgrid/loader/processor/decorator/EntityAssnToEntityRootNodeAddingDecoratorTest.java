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

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.easymock.EasyMock;
import org.junit.Test;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.data.codingScheme.CodingSchemeNameSetter;
import org.lexgrid.loader.processor.support.RootNodeResolver;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class EntityAssnToEntityRootNodeAddingDecoratorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnToEntityRootNodeAddingDecoratorTest {

	/**
	 * Test no root nodes.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testNoRootNodes() throws Exception {
		
		EntityAssnToEntityRootNodeAddingDecorator<String> decorator = new EntityAssnToEntityRootNodeAddingDecorator<String>(
				new ItemProcessor<String, EntityAssnsToEntity>(){

					public EntityAssnsToEntity process(String arg0)
					throws Exception {

							EntityAssnsToEntity assoc = new EntityAssnsToEntity();
							assoc.setEntityCode(arg0);
							assoc.setSourceEntityCode("source");
							assoc.setTargetEntityCode("target");
							return assoc;	
						
					}				
				});

		decorator.setRootNodeResolver(new RootNodeResolver(){
			public boolean isRootNode(Object item) {
				return false;
			}

			public Object process(Object arg0) throws Exception {
				return arg0;
			}
		});
		
		List<EntityAssnsToEntity> resultList = decorator.process("test");
		
		assertTrue(resultList.size() == 1);
		
	}
	
	/**
	 * Test add root nodes.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testAddRootNodes() throws Exception {

		EntityAssnToEntityRootNodeAddingDecorator<String> decorator = new EntityAssnToEntityRootNodeAddingDecorator<String>(
				new ItemProcessor<String, EntityAssnsToEntity>(){

					public EntityAssnsToEntity process(String arg0)
					throws Exception {

						EntityAssnsToEntity assoc = new EntityAssnsToEntity();
						assoc.setEntityCode(arg0);
						assoc.setSourceEntityCode("@");
						return assoc;	
					}				
				});
		
		decorator.setRootNodeResolver(new RootNodeResolver(){
			public boolean isRootNode(Object item) {
				return true;
			}

			public Object process(Object arg0) throws Exception {
				return arg0;
			}
		});
		
		decorator.setReplaceRelation(false);
		
		decorator.setSupportedAttributeTemplate(EasyMock.createMock(SupportedAttributeTemplate.class));
		
		List<EntityAssnsToEntity> resultList = decorator.process("test1");
		
		assertTrue(resultList.size() == 2);
		
	}
	
	/**
	 * Test add replace nodes.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testAddReplaceNodes() throws Exception {

		EntityAssnToEntityRootNodeAddingDecorator<String> decorator = new EntityAssnToEntityRootNodeAddingDecorator<String>(
				new ItemProcessor<String,EntityAssnsToEntity>(){

					public EntityAssnsToEntity process(String arg0)
					throws Exception {

						EntityAssnsToEntity assoc = new EntityAssnsToEntity();
						assoc.setEntityCode(arg0);
						assoc.setSourceEntityCode("@");
						return assoc;	
					}				
				});
		
		decorator.setRootNodeResolver(new RootNodeResolver<EntityAssnsToEntity>(){
			public boolean isRootNode(EntityAssnsToEntity item) {
				return true;
			}

			public EntityAssnsToEntity process(EntityAssnsToEntity arg0) throws Exception {
				return arg0;
			}
		});
		
		decorator.setReplaceRelation(true);
		
		decorator.setSupportedAttributeTemplate(EasyMock.createMock(SupportedAttributeTemplate.class));
		
		List<EntityAssnsToEntity> resultList = decorator.process("test");
		
		assertTrue(resultList.size() == 1);
		
	}
}
