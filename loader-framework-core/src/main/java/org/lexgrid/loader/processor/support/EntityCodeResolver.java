
package org.lexgrid.loader.processor.support;

/**
 * The Interface EntityCodeResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface EntityCodeResolver<T> {

	/**
	 * Gets the entity code.
	 * 
	 * @param item the item
	 * 
	 * @return the entity code
	 */
	public String getEntityCode(T item);
}