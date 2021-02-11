
package org.lexgrid.loader.processor.support;

/**
 * The Interface PropertyLinkResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PropertyLinkResolver<T> {

	/**
	 * Gets the entity code.
	 * 
	 * @param item the item
	 * 
	 * @return the entity code
	 */
	public String getEntityCode(T item);
	
	/**
	 * Gets the link.
	 * 
	 * @param item the item
	 * 
	 * @return the link
	 */
	public String getLink(T item);
	
	/**
	 * Gets the source id.
	 * 
	 * @param item the item
	 * 
	 * @return the source id
	 */
	public String getSourceId(T item);
	
	/**
	 * Gets the target id.
	 * 
	 * @param item the item
	 * 
	 * @return the target id
	 */
	public String getTargetId(T item);
}