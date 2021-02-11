
package org.lexgrid.loader.reader.support;

import java.util.List;

/**
 * The Interface CompositeGroupComparator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CompositeGroupComparator<I1,I2> {

	/**
	 * Do groups match.
	 * 
	 * @param list1 the list1
	 * @param list2 the list2
	 * 
	 * @return true, if successful
	 */
	public boolean doGroupsMatch(List<I1> list1, List<I2> list2);
}