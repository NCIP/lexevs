
package org.lexgrid.loader.reader.support;

import java.util.List;

/**
 * Will iterate through a list of Skip Policies. If ANY of the Skip policies indicates that record should be skipped,
 * it will be.
 * 
 * @param <I>  * 
 * @author m005256
 */
public class CompoundSkipPolicy<I> implements SkipPolicy<I> {

	/** The skip policy list. */
	private List<SkipPolicy<I>> skipPolicyList;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.SkipPolicy#toSkip(java.lang.Object)
	 */
	public boolean toSkip(I item) {
		for(SkipPolicy<I> policy : skipPolicyList){
			if(policy.toSkip(item) == true){
				return true;
			}
		}
		return false;
	}

}