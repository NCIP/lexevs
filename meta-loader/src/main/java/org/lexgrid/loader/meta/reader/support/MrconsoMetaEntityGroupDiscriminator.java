
package org.lexgrid.loader.meta.reader.support;

import org.lexgrid.loader.reader.support.GroupDiscriminator;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class MrconsoMetaEntityGroupDiscriminator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrconsoMetaEntityGroupDiscriminator implements GroupDiscriminator<Mrconso>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.GroupDiscriminator#getDiscriminatingValue(java.lang.Object)
	 */
	public String getDiscriminatingValue(Mrconso item) {
		return item.getCui();
	}
}