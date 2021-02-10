
package org.lexevs.dao.database.constants.classifier.mapping;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.LexGrid.naming.URIMap;

/**
 * The Class StringToClassMappingClassifier.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class StringToClassMappingClassifier extends AbstractMappingClassifier<String,Class<? extends URIMap>> {

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.constants.classifier.mapping.AbstractMappingClassifier#doClassify(java.lang.Object, java.util.Map)
	 */
	@Override
	protected Class<? extends URIMap> doClassify(String item,
			Map<Class<? extends URIMap>, String> mappings) {
		Set<Entry<Class<? extends URIMap>, String>> entrySet = mappings.entrySet();
		for(Entry<Class<? extends URIMap>, String> entry : entrySet) {
			if(entry.getValue().equals(item)) {
				return entry.getKey();
			}
		}
		throw super.getException(item);
	}
}