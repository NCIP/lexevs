
package org.lexgrid.loader.umls.reader.support;

import org.lexgrid.loader.reader.support.SkipPolicy;

/**
 * The Class AbstractSabSkippingPolicy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSabSkippingPolicy<I> implements SkipPolicy<I> {
	
	/** The sab. */
	private String sab;

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.SkipPolicy#toSkip(java.lang.Object)
	 */
	public boolean toSkip(I item) {
		if(getSab(item).equals(sab)){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Gets the sab.
	 * 
	 * @param item the item
	 * 
	 * @return the sab
	 */
	public abstract String getSab(I item);

	/**
	 * Gets the sab.
	 * 
	 * @return the sab
	 */
	public String getSab() {
		return sab;
	}

	/**
	 * Sets the sab.
	 * 
	 * @param sab the new sab
	 */
	public void setSab(String sab) {
		this.sab = sab;
	}
}