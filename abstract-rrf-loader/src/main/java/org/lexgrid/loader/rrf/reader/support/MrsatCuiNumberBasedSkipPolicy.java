
package org.lexgrid.loader.rrf.reader.support;

import org.lexgrid.loader.reader.support.SkipPolicy;
import org.lexgrid.loader.rrf.model.Mrrel;
import org.lexgrid.loader.rrf.model.Mrsat;
import org.lexgrid.loader.rrf.partition.MrrelFilePartitioner;

/**
 * The Class MrrelCuiNumberBasedSkipPolicy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrsatCuiNumberBasedSkipPolicy implements SkipPolicy<Mrsat> {
	
	/** The cui number. */
	private int cuiNumber;

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.SkipPolicy#toSkip(java.lang.Object)
	 */
	public boolean toSkip(Mrsat item) {
		int number = MrrelFilePartitioner.getLastNumberOfCui(item.getCui());
		if(number == cuiNumber){
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Gets the cui number.
	 * 
	 * @return the cui number
	 */
	public int getCuiNumber() {
		return cuiNumber;
	}

	/**
	 * Sets the cui number.
	 * 
	 * @param cuiNumber the new cui number
	 */
	public void setCuiNumber(int cuiNumber) {
		this.cuiNumber = cuiNumber;
	}	
}