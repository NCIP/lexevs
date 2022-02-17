
package org.lexgrid.loader.umls.reader.support;

import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class MrrelSabSkipPolicy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrrelSabSkipPolicy extends AbstractSabSkippingPolicy<Mrrel> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.umls.reader.support.AbstractSabSkippingPolicy#getSab(java.lang.Object)
	 */
	@Override
	public String getSab(Mrrel item) {
		return item.getSab();
	}
}