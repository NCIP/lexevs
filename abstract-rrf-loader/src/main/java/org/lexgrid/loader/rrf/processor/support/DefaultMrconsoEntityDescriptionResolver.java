
package org.lexgrid.loader.rrf.processor.support;

import org.lexgrid.loader.processor.support.EntityDescriptionResolver;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class DefaultMrconsoEntityDescriptionResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultMrconsoEntityDescriptionResolver implements EntityDescriptionResolver<Mrconso>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityDescriptionResolver#getEntityDescription(java.lang.Object)
	 */
	public String getEntityDescription(Mrconso item) {
		return item.getStr();
	}
}