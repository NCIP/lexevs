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
package org.lexgrid.loader.rxn.processor;

import java.util.Arrays;
import java.util.List;

import org.LexGrid.concepts.Entity;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.junit.Test;
import org.lexgrid.loader.rrf.model.Mrconso;
import org.lexgrid.loader.rxn.processor.EntityTypeAddingEntityProcessor;
import org.springframework.batch.item.ItemProcessor;

import static org.junit.Assert.*;

/**
 * The Class EntityTypeAddingEntityProcessorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityTypeAddingEntityProcessorTest {

	/**
	 * Test add entity type.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testAddEntityType() throws Exception {
		EntityTypeAddingEntityProcessor pr = new EntityTypeAddingEntityProcessor();
		pr.setEntityListProcessor(new ItemProcessor<List<Mrconso>,Entity>(){

			public Entity process(List<Mrconso> arg0) throws Exception {
				return new Entity();
			}	
		});

//		pr.setEntityTypeProcessor(new ItemProcessor<Entity,EntityTypes>(){
//
//			public EntityTypes process(Entity arg0) throws Exception {
//				return  arg0.getEntityType();
//			}	
//		});
		
		List<Object> list = pr.process(Arrays.asList(new Mrconso[]{new Mrconso()}));
	
		assertTrue(list.size() == 2);
		
		boolean foundEntity = false;
		boolean foundEntityType = false;
		
		for(Object obj : list){
			if(obj instanceof Entity){
				foundEntity = true;
			}
			if(obj instanceof EntityTypes){
				foundEntityType = true;
			}
		}
		
		assertTrue(foundEntity);
		assertTrue(foundEntityType);
	
	}
}

