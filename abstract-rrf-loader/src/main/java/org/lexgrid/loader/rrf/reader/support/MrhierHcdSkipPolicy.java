
package org.lexgrid.loader.rrf.reader.support;

import org.apache.commons.lang.StringUtils;
import org.lexgrid.loader.reader.support.SkipPolicy;
import org.lexgrid.loader.rrf.model.Mrhier;

/**
 * The Class MrhierHcdSkipPolicy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrhierHcdSkipPolicy implements SkipPolicy<Mrhier> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.SkipPolicy#toSkip(java.lang.Object)
	 */
	public boolean toSkip(Mrhier item) {
		return StringUtils.isBlank(item.getHcd());
	}
}