package org.lexevs.dao.index.model.compass.v20;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.index.model.IndexableResourceBuilder;

	public class IndexedEntityBuilder implements IndexableResourceBuilder<Entity,List<IndexedProperty>>{	
	
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

		public List<IndexedProperty> buildIndexableResource(Entity input) {
			Property[] allProps = input.getAllProperties();
			List<String> allContexts = getAllContexts(allProps);
			List<String> allQualifiers = getAllQualifiers(allProps);
			List<String> allSources = getAllSources(allProps);
			List<String> allPropertyTypes = getAllPropertyTypes(allProps);
			List<String> allPropertyNames = getAllPropertyNames(allProps);
			
			List<IndexedProperty> returnList = new ArrayList<IndexedProperty>();
			
			for(Property prop : input.getAllProperties()){
				IndexedProperty indexedProperty = new IndexedProperty();
				indexedProperty.setId(this.resolveId(input));
				
				indexedProperty.setEntityCode(input.getEntityCode());
				indexedProperty.setEntityCodeNamespace(input.getEntityCodeNamespace());
				
				if(input.getEntityDescription() != null){
					indexedProperty.setEntityDescription(input.getEntityDescription().getContent());
				}
				
				indexedProperty.setAllUsageContexts(allContexts);
				indexedProperty.setAllPropertyQualifiers(allQualifiers);
				indexedProperty.setAllSources(allSources);
				indexedProperty.setAllPropertyTypes(allPropertyTypes);
				indexedProperty.setAllPropertyNames(allPropertyNames);
				
				indexedProperty.setUsageContexts(
						getAllContexts(
								new Property[]{prop}));
				indexedProperty.setPropertyQualifiers(
						getAllQualifiers(
								new Property[]{prop}));
				indexedProperty.setSources(
						getAllSources(
								new Property[]{prop}));
				indexedProperty.setPropertyType(prop.getPropertyType());
				indexedProperty.setPropertyName(prop.getPropertyName());
				
				indexedProperty.setValue(prop.getValue().getContent());

				returnList.add(indexedProperty);
			}
			
			return returnList;
		}

		public String resolveId(Entity input) {
			return input.getEntityCode() + input.getEntityCodeNamespace() + new Random().nextInt();
		}
		
		

}
