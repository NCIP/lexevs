
package org.lexgrid.loader.rrf.data.entity;

/**
 * The Interface NoCodeHandler.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface NoCodeHandler<I> {

	/**
	 * Handle no code.
	 * 
	 * @param item the item
	 * 
	 * @return the string
	 */
	public String handleNoCode(I item);
}