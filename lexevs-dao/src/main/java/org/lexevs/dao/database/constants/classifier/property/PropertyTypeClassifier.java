
package org.lexevs.dao.database.constants.classifier.property;

import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.springframework.classify.Classifier;

/**
 * The Class PropertyTypeClassifier.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PropertyTypeClassifier implements Classifier<PropertyType,String>{

	/* (non-Javadoc)
	 * @see org.springframework.batch.classify.Classifier#classify(java.lang.Object)
	 */
	public String classify(PropertyType type) {
		if(type == null) {return null;}
		
		if(type.equals(PropertyType.CODINGSCHEME)){
			return "codingScheme";
		}
		if(type.equals(PropertyType.ENTITY)){
			return "entity";
		}
		if(type.equals(PropertyType.RELATION)){
			return "relation";
		}
		else throw new RuntimeException("Class:" + type + " is not Classifiable.");
	}
	
	public static PropertyType getPropertyType(String propertyType) {
		
		if( propertyType == null )
			return null;
		
		if( propertyType.equals("codingScheme")){
			return PropertyType.CODINGSCHEME;
		} else if( propertyType.equals("entity")){
			return PropertyType.ENTITY;
		} else if( propertyType.equals("relation")){
			return PropertyType.RELATION;
		}
		
		return null;
	}
}