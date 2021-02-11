
package org.lexgrid.loader.processor.support;

/**
 * The Interface EntityNamespaceResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface EntityNamespaceResolver<T> {

	/**
	 * Gets the entity namespace.
	 * 
	 * @param item the item
	 * 
	 * @return the entity namespace
	 */
	public String getEntityNamespace(T item);
}