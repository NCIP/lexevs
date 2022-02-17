
package org.lexgrid.loader.rrf.processor.support;

import org.LexGrid.commonTypes.Property;
import org.lexgrid.loader.processor.support.EntityDescriptionResolver;

/**
 * The Class EntityPropertyEntityDescriptionResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyEntityDescriptionResolver implements EntityDescriptionResolver<Property>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityDescriptionResolver#getEntityDescription(java.lang.Object)
	 */
	public String getEntityDescription(Property item) {
		return item.getValue().getContent();
	}
}