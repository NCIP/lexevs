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
package org.lexevs.dao.database.constants.classifier.property;

import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.springframework.batch.classify.Classifier;


/**
 * The Class PropertyTypeClassifier.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PropertyTypeClassifier implements Classifier<PropertyType,String>{

	//TODO: Figure out which constants to use here...
	/* (non-Javadoc)
	 * @see org.springframework.batch.classify.Classifier#classify(java.lang.Object)
	 */
	public String classify(PropertyType type) {
		if(type.equals(PropertyType.CODINGSCHEME)){
			return "codingScheme";
		}
		if(type.equals(PropertyType.ENTITY)){
			return "entity";
		}
		else throw new RuntimeException("Class:" + type + " is not Classifiable.");
	}
}
