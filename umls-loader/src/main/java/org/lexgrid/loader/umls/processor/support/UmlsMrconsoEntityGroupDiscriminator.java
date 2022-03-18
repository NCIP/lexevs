
package org.lexgrid.loader.umls.processor.support;

import org.lexgrid.loader.reader.support.GroupDiscriminator;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class UmlsMrconsoEntityGroupDiscriminator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsMrconsoEntityGroupDiscriminator implements GroupDiscriminator<Mrconso>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.GroupDiscriminator#getDiscriminatingValue(java.lang.Object)
	 */
	public String getDiscriminatingValue(Mrconso item) {
		return item.getCode();
	}
}