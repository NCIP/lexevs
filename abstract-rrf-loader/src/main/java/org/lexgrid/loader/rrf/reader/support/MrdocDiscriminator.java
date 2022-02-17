
package org.lexgrid.loader.rrf.reader.support;

import org.lexgrid.loader.reader.support.GroupDiscriminator;
import org.lexgrid.loader.rrf.model.Mrdoc;

/**
 * The Class MrdocDiscriminator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdocDiscriminator implements GroupDiscriminator<Mrdoc> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.GroupDiscriminator#getDiscriminatingValue(java.lang.Object)
	 */
	public String getDiscriminatingValue(Mrdoc item) {
		return item.getValue();
	}
}