
package org.lexgrid.loader.meta.reader.support;

import org.lexgrid.loader.reader.support.GroupDiscriminator;
import org.lexgrid.loader.rrf.model.Mrsat;

/**
 * The Class MrconsoMetaEntityGroupDiscriminator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrsatMetaEntityGroupDiscriminator implements GroupDiscriminator<Mrsat>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.GroupDiscriminator#getDiscriminatingValue(java.lang.Object)
	 */
	public String getDiscriminatingValue(Mrsat item) {
		return item.getCui();
	}
}