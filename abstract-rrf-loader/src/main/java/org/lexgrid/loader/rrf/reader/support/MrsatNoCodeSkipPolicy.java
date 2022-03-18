
package org.lexgrid.loader.rrf.reader.support;

import org.apache.commons.lang.StringUtils;
import org.lexgrid.loader.reader.support.SkipPolicy;

import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MrSat;

/**
 * The Class MrsatNoCodeSkipPolicy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrsatNoCodeSkipPolicy implements SkipPolicy<MrSat> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.SkipPolicy#toSkip(java.lang.Object)
	 */
	public boolean toSkip(MrSat item) {
		return StringUtils.isBlank(item.getCode());
	}
}