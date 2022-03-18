
package org.lexgrid.loader.data.property;

import java.util.List;

/**
 * The Interface ListIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface ListIdSetter<I> {

	/**
	 * Adds the ids.
	 * 
	 * @param idables the idables
	 */
	public void addIds(List<I> idables);	
}