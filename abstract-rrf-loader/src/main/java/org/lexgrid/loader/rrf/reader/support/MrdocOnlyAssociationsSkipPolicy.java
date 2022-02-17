
package org.lexgrid.loader.rrf.reader.support;

import org.lexgrid.loader.reader.support.SkipPolicy;
import org.lexgrid.loader.rrf.model.Mrdoc;

/**
 * The Class MrdocOnlyAssociationsSkipPolicy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdocOnlyAssociationsSkipPolicy implements SkipPolicy<Mrdoc>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.SkipPolicy#toSkip(java.lang.Object)
	 */
	public boolean toSkip(Mrdoc item) {
		return !(item.getDockey().equals("REL"));
	}
}