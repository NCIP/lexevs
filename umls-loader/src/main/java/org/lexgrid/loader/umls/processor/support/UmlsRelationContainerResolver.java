
package org.lexgrid.loader.umls.processor.support;

import org.lexgrid.loader.processor.support.RelationContainerResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

/**
 * The Class UmlsRelationContainerResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsRelationContainerResolver implements RelationContainerResolver {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationContainerResolver#getRelationContainer(java.lang.Object)
	 */
	public String getRelationContainer(Object item) {
		return RrfLoaderConstants.UMLS_RELATIONS_NAME;
	}

}