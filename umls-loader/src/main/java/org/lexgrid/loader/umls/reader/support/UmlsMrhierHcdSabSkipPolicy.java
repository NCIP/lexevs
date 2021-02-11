
package org.lexgrid.loader.umls.reader.support;

import org.lexgrid.loader.rrf.model.Mrhier;
import org.lexgrid.loader.rrf.reader.support.MrhierHcdSkipPolicy;

/**
 * The Class UmlsMrhierHcdSabSkipPolicy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsMrhierHcdSabSkipPolicy extends MrhierHcdSkipPolicy {

	/** The sab. */
	private String sab;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.reader.support.MrhierHcdSkipPolicy#toSkip(org.lexgrid.loader.rrf.model.Mrhier)
	 */
	@Override
	public boolean toSkip(Mrhier item) {
		if(!super.toSkip(item)){
			if(item.getSab().equals(sab)){
				return false;
			}
		}
		return true;
	}

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