
package org.lexevs.dao.database.constants.classifier.mapping;

import java.util.Map;

import org.LexGrid.naming.URIMap;

/**
 * The Class ClassToStringMappingClassifier.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ClassToStringMappingClassifier extends AbstractMappingClassifier<Class<? extends URIMap>,String> {

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.constants.classifier.mapping.AbstractMappingClassifier#doClassify(java.lang.Object, java.util.Map)
	 */
	@Override
	protected String doClassify(Class<? extends URIMap> item, Map<Class<? extends URIMap>, String> mappings) {
		return mappings.get(item);
	}
}