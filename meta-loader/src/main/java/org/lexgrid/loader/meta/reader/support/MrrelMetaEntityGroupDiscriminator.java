
package org.lexgrid.loader.meta.reader.support;

import org.lexgrid.loader.reader.support.GroupDiscriminator;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class MrrelMetaEntityGroupDiscriminator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrrelMetaEntityGroupDiscriminator implements GroupDiscriminator<Mrrel>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.GroupDiscriminator#getDiscriminatingValue(java.lang.Object)
	 */
	public String getDiscriminatingValue(Mrrel item) {
		return item.getCui1();
	}
}