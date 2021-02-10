
package org.lexgrid.loader.processor.support;

import org.LexGrid.commonTypes.Text;

/**
 * The Interface QualifierResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface QualifierResolver<T> {

	/**
	 * Gets the qualifier value.
	 * 
	 * @param item the item
	 * 
	 * @return the qualifier value
	 */
	public Text getQualifierValue(T item);
	
	/**
	 * Gets the qualifier name.
	 * 
	 * @return the qualifier name
	 */
	public String getQualifierName(T item);	
}