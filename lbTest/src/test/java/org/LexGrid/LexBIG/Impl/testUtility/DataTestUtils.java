
package org.LexGrid.LexBIG.Impl.testUtility;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entity;

/**
 * The Class DataTestUtils.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DataTestUtils {
	
	/**
	 * Gets the properties from entity.
	 * 
	 * @param entity the entity
	 * @param propertyName the property name
	 * 
	 * @return the properties from entity
	 */
	public static List<Property> getPropertiesFromEntity(Entity entity, String propertyName){
		List<Property> returnList = new ArrayList<Property>();
		
		Property[] props = entity.getAllProperties();
		
		for(Property prop : props){
			if(prop.getPropertyName().equals(propertyName)){
				returnList.add(prop);
			}
		}
		
		return returnList;
	}
	
	public static Property getPropertyWithValue(Property[] properties, String value) {
		for(Property prop : properties){
			if(prop.getValue().getContent().equals(value)){
				return prop;
			}
		}
		
		throw new RuntimeException("Property Not Found.");
	}
	
	public static Property getPropertyWithId(Property[] properties, String id) {
		for(Property prop : properties){
			if(prop.getPropertyId().equals(id)){
				return prop;
			}
		}
		
		throw new RuntimeException("Property Not Found.");
	}
	
	public static Property getPropertyWithValue(Entity entity, String value) {
		return getPropertyWithValue(entity.getAllProperties(), value);
	}
	
	/**
	 * Gets the property qualifiers from property.
	 * 
	 * @param prop the prop
	 * @param qualifierName the qualifier name
	 * 
	 * @return the property qualifiers from property
	 */
	public static List<PropertyQualifier> getPropertyQualifiersFromProperty(Property prop, String qualifierName){
		List<PropertyQualifier> returnList = new ArrayList<PropertyQualifier>();
		
		PropertyQualifier[] quals = prop.getPropertyQualifier();
		
		for(PropertyQualifier qual : quals){
			if(qual.getPropertyQualifierName().equals(qualifierName)){
				returnList.add(qual);
			}
		}
		
		return returnList;
	}

	/**
	 * Gets the association.
	 * 
	 * @param assocs the assocs
	 * @param associationName the association name
	 * 
	 * @return the association
	 */
	public static Association getAssociation(Association[] assocs, String associationName){
		for(Association assoc : assocs){
			if(assoc.getAssociationName().equals(associationName)){
				return assoc;
			}
		}
		
		throw new RuntimeException("Association Not Found.");
	}
	
	/**
	 * Gets the associated concept.
	 * 
	 * @param assocConcepts the assoc concepts
	 * @param code the code
	 * 
	 * @return the associated concept
	 */
	public static <T extends ConceptReference> T getConceptReference(T[] concepts, String code){
		for(T concept : concepts){
			if(concept.getCode().equals(code)){
				return concept;
			}
		}
		
		throw new RuntimeException("AssociatedConcept Not Found.");
	}
	
	public static boolean isPropertyWithValuePresent(Entity entity, String propertyName, String propertyValue) {
		for(Property prop : entity.getAllProperties()){
			if(prop.getPropertyName().equals(propertyName) &&
			prop.getValue().getContent().equals(propertyValue)){
				return true;
			}
		}
		
		throw new RuntimeException("Property Not Found.");
	}
	
	public static boolean isAssociatedConceptPresent(AssociatedConcept[] assocConcepts, String code){
		for(AssociatedConcept concept : assocConcepts){
			if(concept.getCode().equals(code)){
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isConceptReferencePresent(List<? extends ConceptReference> refs, String code){
		for(ConceptReference concept : refs){
			if(concept.getCode().equals(code)){
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isConceptReferencePresent(ResolvedConceptReferenceList refs, String code){
		List<ConceptReference> list = new ArrayList<ConceptReference>();
		
		for(ResolvedConceptReference ref : refs.getResolvedConceptReference()) {
			list.add(ref);
		}
		
		return isConceptReferencePresent(list, code);
	}
	
	/**
	 * Checks if is qualifier name and value present.
	 * 
	 * @param qualifierName the qualifier name
	 * @param qualifierValue the qualifier value
	 * @param list the list
	 * 
	 * @return true, if is qualifier name and value present
	 */
	public static boolean isQualifierNameAndValuePresent(String qualifierName, String qualifierValue, NameAndValueList list) {
		for(NameAndValue nameAndValue : list.getNameAndValue()) {
			if(nameAndValue.getName().equals(qualifierName) && 
					nameAndValue.getContent().equals(qualifierValue)){
				return true;
			}
		}
		return false;	
	}
	
	public static boolean isQualifierNameAndValuePresentInProperty(String qualifierName, String qualifierValue, Property property) {
		for(PropertyQualifier qual : property.getPropertyQualifier()) {
			if(qual.getPropertyQualifierName().equals(qualifierName) && qual.getValue().getContent().equals(qualifierValue)) {
				return true;
			}
		}
		return false;
	}
}