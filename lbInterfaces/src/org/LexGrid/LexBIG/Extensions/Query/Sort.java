
package org.LexGrid.LexBIG.Extensions.Query;
import java.util.Comparator;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Extendable;

/**
 * Allows for unique sorting of query results.
 */
public interface Sort extends Extendable {
	
	/**
	 * Gets the comparator for search class.
	 * 
	 * @param searchClass the search class
	 * 
	 * @return the comparator for search class
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public <T> Comparator<T> getComparatorForSearchClass(Class<T> searchClass) throws LBParameterException;
	
	/**
	 * Checks if this sort is valid for the class.
	 * 
	 * @param clazz the clazz
	 * 
	 * @return true, if this sort is valid for the class
	 */
	public boolean isSortValidForClass(Class<?> clazz);
}