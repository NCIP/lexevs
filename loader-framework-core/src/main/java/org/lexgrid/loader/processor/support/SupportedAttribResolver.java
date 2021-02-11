
package org.lexgrid.loader.processor.support;

/**
 * The Interface SupportedAttribResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface SupportedAttribResolver<T> {
	
	/**
	 * Gets the supported attribute tag.
	 * 
	 * @return the supported attribute tag
	 */
	public String getSupportedAttributeTag();
	
	/**
	 * Gets the id.
	 * 
	 * @param item the item
	 * 
	 * @return the id
	 */
	public String getId(T item);
	
	/**
	 * Gets the uri.
	 * 
	 * @param item the item
	 * 
	 * @return the uri
	 */
	public String getUri(T item);
	
	/**
	 * Gets the id val.
	 * 
	 * @param item the item
	 * 
	 * @return the id val
	 */
	public String getIdVal(T item);
	
	/**
	 * Gets the val1.
	 * 
	 * @param item the item
	 * 
	 * @return the val1
	 */
	public String getVal1(T item);
	
	/**
	 * Gets the val2.
	 * 
	 * @param item the item
	 * 
	 * @return the val2
	 */
	public String getVal2(T item);
}