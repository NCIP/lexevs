
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