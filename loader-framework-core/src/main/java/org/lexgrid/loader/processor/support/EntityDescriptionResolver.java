
package org.lexgrid.loader.processor.support;

/**
 * The Interface EntityDescriptionResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface EntityDescriptionResolver<T> {

	/**
	 * Gets the entity description.
	 * 
	 * @param item the item
	 * 
	 * @return the entity description
	 */
	public String getEntityDescription(T item);
}