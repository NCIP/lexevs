
package org.lexgrid.loader.processor.support;

/**
 * The Interface PropertyResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PropertyResolver<T> {

	/**
	 * Gets the entity code.
	 * 
	 * @param item the item
	 * 
	 * @return the entity code
	 */
	public String getEntityCode(T item);
	
	public String getEntityCodeNamespace(T item);
	/**
	 * Gets the id.
	 * 
	 * @param item the item
	 * 
	 * @return the id
	 */
	public String getId(T item);
	
	/**
	 * Gets the property name.
	 * 
	 * @param item the item
	 * 
	 * @return the property name
	 */
	public String getPropertyName(T item);
	
	/**
	 * Gets the property type.
	 * 
	 * @param item the item
	 * 
	 * @return the property type
	 */
	public String getPropertyType(T item);
	
	/**
	 * Gets the degree of fidelity.
	 * 
	 * @param item the item
	 * 
	 * @return the degree of fidelity
	 */
	public String getDegreeOfFidelity(T item);
	
	/**
	 * Gets the format.
	 * 
	 * @param item the item
	 * 
	 * @return the format
	 */
	public String getFormat(T item);
	
	/**
	 * Gets the checks if is active.
	 * 
	 * @param item the item
	 * 
	 * @return the checks if is active
	 */
	public boolean getIsActive(T item);
	
	/**
	 * Gets the language.
	 * 
	 * @param item the item
	 * 
	 * @return the language
	 */
	public String getLanguage(T item);
	
	/**
	 * Gets the match if no context.
	 * 
	 * @param item the item
	 * 
	 * @return the match if no context
	 */
	public boolean getMatchIfNoContext(T item);
	
	/**
	 * Gets the property value.
	 * 
	 * @param item the item
	 * 
	 * @return the property value
	 */
	public String getPropertyValue(T item);
	
	/**
	 * Gets the representational form.
	 * 
	 * @param item the item
	 * 
	 * @return the representational form
	 */
	public String getRepresentationalForm(T item);
}