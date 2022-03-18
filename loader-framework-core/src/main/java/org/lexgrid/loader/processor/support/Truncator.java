
package org.lexgrid.loader.processor.support;

/**
 * The Interface Truncator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface Truncator {

	/**
	 * Truncate.
	 * 
	 * @param item the item
	 * 
	 * @return the t
	 * 
	 * @throws Exception the exception
	 */
	public <T> T truncate(T item) throws Exception;
}