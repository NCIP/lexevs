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
package org.lexevs.dao.database.constants.classifier.property;

import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.springframework.batch.classify.Classifier;

public class PropertyMultiAttributeClassifier implements Classifier<Class<?>,String>{

	@Override
	public String classify(Class<?> clazz) {
		if(clazz == Source.class) {
			return SQLTableConstants.TBLCOLVAL_SOURCE;
		} else if(clazz == String.class) {
			return SQLTableConstants.TBLCOLVAL_USAGECONTEXT;
		} else if(clazz == PropertyQualifier.class) {
			return SQLTableConstants.TBLCOLVAL_QUALIFIER;
		}	
		throw new RuntimeException("Class:" + clazz + " is not Classifiable.");
	}
}