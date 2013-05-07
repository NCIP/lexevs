/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.index.model.compass.v20;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entity;
import org.compass.core.Compass;
import org.compass.core.Resource;
import org.compass.core.Property.Index;
import org.compass.core.Property.Store;
import org.lexevs.dao.index.model.IndexableResourceBuilder;

/**
	 * The Class IndexedEntityBuilder.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
public class IndexedEntityBuilder implements IndexableResourceBuilder<Entity,Resource>{	
		
		/** The compass. */
		private Compass compass;
	
		/**
		 * Gets the all qualifiers.
		 * 
		 * @param properties the properties
		 * 
		 * @return the all qualifiers
		 */
		protected List<String> getAllQualifiers(Property[] properties){
			List<String> returnList = new ArrayList<String>();
			for(Property prop : properties){
				for(PropertyQualifier qual : prop.getPropertyQualifier()){
							returnList.add(
									this.combineNameAndValue(qual.getPropertyQualifierName(), qual.getValue().getContent()));
				}
			}
			return returnList;
		}
		
		/**
		 * Gets the all contexts.
		 * 
		 * @param properties the properties
		 * 
		 * @return the all contexts
		 */
		protected List<String> getAllContexts(Property[] properties){
			List<String> returnList = new ArrayList<String>();
			for(Property prop : properties){
				for(String context : prop.getUsageContext()){
							returnList.add(
									context);
				}
			}
			return returnList;
		}
		
		/**
		 * Gets the all property names.
		 * 
		 * @param properties the properties
		 * 
		 * @return the all property names
		 */
		protected List<String> getAllPropertyNames(Property[] properties){
			List<String> returnList = new ArrayList<String>();
			for(Property prop : properties){
				returnList.add(prop.getPropertyName());
			}
			return returnList;
		}
		
		/**
		 * Gets the all property types.
		 * 
		 * @param properties the properties
		 * 
		 * @return the all property types
		 */
		protected List<String> getAllPropertyTypes(Property[] properties){
			List<String> returnList = new ArrayList<String>();
			for(Property prop : properties){
				returnList.add(prop.getPropertyType());
			}
			return returnList;
		}
		
		/**
		 * Gets the all sources.
		 * 
		 * @param properties the properties
		 * 
		 * @return the all sources
		 */
		protected List<String> getAllSources(Property[] properties){
			List<String> returnList = new ArrayList<String>();
			for(Property prop : properties){
				for(PropertyQualifier qual : prop.getPropertyQualifier()){
							returnList.add(
									this.combineNameAndValue(qual.getPropertyQualifierName(), qual.getValue().getContent()));
				}
			}
			return returnList;
		}
		
		/**
		 * Combine name and value.
		 * 
		 * @param name the name
		 * @param value the value
		 * 
		 * @return the string
		 */
		protected String combineNameAndValue(String name, String value){
			return name + ":" + value;
		}
		
	

		/* (non-Javadoc)
		 * @see org.lexevs.dao.index.model.IndexableResourceBuilder#buildIndexableResource(java.lang.Object)
		 */
		public Resource buildIndexableResource(Entity input) {
			Property[] allProps = input.getAllProperties();
			List<String> allContexts = getAllContexts(allProps);
			List<String> allQualifiers = getAllQualifiers(allProps);
			List<String> allSources = getAllSources(allProps);
			List<String> allPropertyTypes = getAllPropertyTypes(allProps);
			List<String> allPropertyNames = getAllPropertyNames(allProps);
			
			Resource resource = compass.getResourceFactory().createResource("Entity");
			
			for(int i=0;i<allProps.length;i++) {
				org.compass.core.Property prop = compass.
					getResourceFactory().
					createProperty("p"+String.valueOf(i), 
							allProps[i].getValue().getContent(), 
							Store.NO, Index.TOKENIZED);
				resource.addProperty(prop);
			}
			
			return resource;
		}

		/* (non-Javadoc)
		 * @see org.lexevs.dao.index.model.IndexableResourceBuilder#resolveId(java.lang.Object)
		 */
		public String resolveId(Entity input) {
			return input.getEntityCode() + input.getEntityCodeNamespace() + new Random().nextInt();
		}

		/**
		 * Sets the compass.
		 * 
		 * @param compass the new compass
		 */
		public void setCompass(Compass compass) {
			this.compass = compass;
		}

		/**
		 * Gets the compass.
		 * 
		 * @return the compass
		 */
		public Compass getCompass() {
			return compass;
		}
		
		

}