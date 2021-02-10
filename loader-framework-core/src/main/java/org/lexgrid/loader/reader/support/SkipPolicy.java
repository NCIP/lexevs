
package org.lexgrid.loader.reader.support;

/**
 * The Interface SkipPolicy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface SkipPolicy<I> {

	/**
	 * To skip.
	 * 
	 * @param item the item
	 * 
	 * @return true, if successful
	 */
	public boolean toSkip(I item);
}