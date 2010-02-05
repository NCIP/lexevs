package org.lexevs.dao.database.constants.classifier.mapping;

import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.naming.URIMap;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.springframework.batch.classify.Classifier;

public class MappingClassifier implements Classifier<Class<? extends URIMap>,String>{

	public String classify(Class<? extends URIMap> clazz) {
		if(clazz == SupportedCodingScheme.class){
			return SQLTableConstants.TBLCOLVAL_SUPPTAG_CODINGSCHEME;
		}
		if(clazz == SupportedSource.class){
			return SQLTableConstants.TBLCOLVAL_SUPPTAG_SOURCE;
		}
		else throw new RuntimeException("Class:" + clazz.getName() + " is not Classifiable.");
	}
}
