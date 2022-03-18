
package org.lexgrid.loader.meta.reader.support;

import org.apache.commons.lang.StringUtils;
import org.lexgrid.loader.reader.support.SkipPolicy;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrrel;

public class MetaMrrelRelationSkipPolicy implements SkipPolicy<Mrrel>{

	public boolean toSkip(Mrrel item) {
		String rel = item.getRel();
		return StringUtils.equals(rel, RrfLoaderConstants.SIB);
	}
}