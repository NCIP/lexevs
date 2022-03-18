
package org.lexgrid.loader.processor.support;

/**
 * The Interface EntityResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface EntityResolver<I> {

	/**
	 * Gets the entity code.
	 * 
	 * @param item the item
	 * 
	 * @return the entity code
	 */
	public String getEntityCode(I item);
	
	/**
	 * Gets the entity code namespace.
	 * 
	 * @param item the item
	 * 
	 * @return the entity code namespace
	 */
	public String getEntityCodeNamespace(I item);
	
	/**
	 * Gets the entity description.
	 * 
	 * @param item the item
	 * 
	 * @return the entity description
	 */
	public String getEntityDescription(I item);
	
	/**
	 * Gets the checks if is active.
	 * 
	 * @param item the item
	 * 
	 * @return the checks if is active
	 */
	public boolean getIsActive(I item);
	
	/**
	 * Gets the checks if is anonymous.
	 * 
	 * @param item the item
	 * 
	 * @return the checks if is anonymous
	 */
	public boolean getIsAnonymous(I item);
	
	/**
	 * Gets the checks if is defined.
	 * 
	 * @param item the item
	 * 
	 * @return the checks if is defined
	 */
	public boolean getIsDefined(I item);
	
	public String[] getEntityTypes(I item);
}