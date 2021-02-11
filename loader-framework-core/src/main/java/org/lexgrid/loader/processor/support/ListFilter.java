
package org.lexgrid.loader.processor.support;

import java.util.List;

/**
 * The Interface ListFilter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface ListFilter<T> {

	/**
	 * Filter.
	 * 
	 * @param items the items
	 * 
	 * @return the list< t>
	 */
	public List<T> filter(List<T> items);
}