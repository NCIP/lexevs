package org.lexevs.dao.database.constants.classifier.property;

import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.springframework.batch.classify.Classifier;


public class PropertyTypeClassifier implements Classifier<PropertyType,String>{

	//TODO: Figure out which constants to use here...
	public String classify(PropertyType type) {
		if(type.equals(PropertyType.CODINGSCHEME)){
			return "codingSceme";
		}
		if(type.equals(PropertyType.ENTITY)){
			return "entity";
		}
		else throw new RuntimeException("Class:" + type + " is not Classifiable.");
	}
}
