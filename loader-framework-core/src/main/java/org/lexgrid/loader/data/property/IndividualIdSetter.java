
package org.lexgrid.loader.data.property;

/**
 * The Interface IndividualIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface IndividualIdSetter<I> {

	/**
	 * Adds the id.
	 * 
	 * @param item the item
	 * 
	 * @return the string
	 */
	public String addId(I item);	
}