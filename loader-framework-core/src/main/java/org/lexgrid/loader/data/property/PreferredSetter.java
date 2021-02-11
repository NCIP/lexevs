
package org.lexgrid.loader.data.property;

import java.util.List;

/**
 * The Interface PreferredSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PreferredSetter<I> {

	/**
	 * Sets the preferred.
	 * 
	 * @param properties the new preferred
	 */
	public void setPreferred(List<I> properties);
}