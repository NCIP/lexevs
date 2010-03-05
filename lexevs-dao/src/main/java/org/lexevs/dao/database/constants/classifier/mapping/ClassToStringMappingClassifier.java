package org.lexevs.dao.database.constants.classifier.mapping;

import java.util.Map;

import org.LexGrid.naming.URIMap;

public class ClassToStringMappingClassifier extends AbstractMappingClassifier<Class<? extends URIMap>,String> {

	@Override
	protected String doClassify(Class<? extends URIMap> item, Map<Class<? extends URIMap>, String> mappings) {
		return mappings.get(item);
	}
}
