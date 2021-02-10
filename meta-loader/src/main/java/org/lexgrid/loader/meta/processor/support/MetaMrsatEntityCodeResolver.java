
package org.lexgrid.loader.meta.processor.support;

import org.lexgrid.loader.processor.support.EntityCodeResolver;
import org.lexgrid.loader.rrf.model.Mrsat;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 */
public class MetaMrsatEntityCodeResolver implements EntityCodeResolver<Mrsat> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lexgrid.loader.processor.support.EntityCodeResolver#getEntityCode
	 * (java.lang.Object)
	 */
	public String getEntityCode(Mrsat item) {
		return item.getCui();
	}

}