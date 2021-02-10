
package org.lexgrid.loader.umls.reader.support;

import org.apache.commons.lang.StringUtils;
import org.lexgrid.loader.rrf.data.property.MrsatUtility;
import org.lexgrid.loader.rrf.model.Mrsat;

/**
 * The Class MrsatSkipPolicy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrsatSkipPolicy extends AbstractSabSkippingPolicy<Mrsat> {

	/** The mrsat utility. */
	private MrsatUtility mrsatUtility;

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.umls.reader.support.AbstractSabSkippingPolicy#toSkip(java.lang.Object)
	 */
	public boolean toSkip(Mrsat item) {
		if(StringUtils.isBlank(item.getCode())) {
			return true;
		}
		
		if(super.toSkip(item)){
			return true;
		}
		return mrsatUtility.toSkip(item);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.umls.reader.support.AbstractSabSkippingPolicy#getSab(java.lang.Object)
	 */
	@Override
	public String getSab(Mrsat item) {
		return item.getSab();
	}

	/**
	 * Gets the mrsat utility.
	 * 
	 * @return the mrsat utility
	 */
	public MrsatUtility getMrsatUtility() {
		return mrsatUtility;
	}

	/**
	 * Sets the mrsat utility.
	 * 
	 * @param mrsatUtility the new mrsat utility
	 */
	public void setMrsatUtility(MrsatUtility mrsatUtility) {
		this.mrsatUtility = mrsatUtility;
	}

}