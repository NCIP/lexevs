
package org.lexgrid.loader.data.property;

import java.util.List;

/**
 * The Interface ParameterizedListIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface ParameterizedListIdSetter<I> {

	/**
	 * Adds the ids.
	 * 
	 * @param idables the idables
	 * @param parameter the parameter
	 */
	public void addIds(List<I> idables, String parameter);	
}