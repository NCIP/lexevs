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

	public class IndexedEntityBuilder implements IndexableResourceBuilder<Entity,Resource>{	
		
		private Compass compass;
	
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
		
		protected List<String> getAllPropertyNames(Property[] properties){
			List<String> returnList = new ArrayList<String>();
			for(Property prop : properties){
				returnList.add(prop.getPropertyName());
			}
			return returnList;
		}
		
		protected List<String> getAllPropertyTypes(Property[] properties){
			List<String> returnList = new ArrayList<String>();
			for(Property prop : properties){
				returnList.add(prop.getPropertyType());
			}
			return returnList;
		}
		
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
		
		protected String combineNameAndValue(String name, String value){
			return name + ":" + value;
		}
		
	

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

		public String resolveId(Entity input) {
			return input.getEntityCode() + input.getEntityCodeNamespace() + new Random().nextInt();
		}

		public void setCompass(Compass compass) {
			this.compass = compass;
		}

		public Compass getCompass() {
			return compass;
		}
		
		

}
