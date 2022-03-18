
package org.lexgrid.loader.umls.reader.support;

import org.lexgrid.loader.rrf.model.Mrdef;

/**
 * The Class MrdefSabSkipPolicy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdefSabSkipPolicy extends AbstractSabSkippingPolicy<Mrdef>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.umls.reader.support.AbstractSabSkippingPolicy#getSab(java.lang.Object)
	 */
	@Override
	public String getSab(Mrdef item) {
		return item.getSab();
	}
}