
package org.lexgrid.loader.reader.support;

/**
 * The Interface GroupDiscriminator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface GroupDiscriminator<T> {

	/**
	 * Returning Object MUST implement 'equals()'.
	 * 
	 * @param item the item
	 * 
	 * @return the discriminating value
	 */
	public Object getDiscriminatingValue(T item);
}