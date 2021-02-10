
package org.lexgrid.loader.meta.reader.support;

import java.util.List;

import org.lexgrid.loader.reader.support.CompositeGroupComparator;
import org.lexgrid.loader.rrf.model.Mrhier;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class MrrelMrhierCompositeGroupComparator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrrelMrhierCompositeGroupComparator implements CompositeGroupComparator<Mrrel,Mrhier>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.CompositeGroupComparator#doGroupsMatch(java.util.List, java.util.List)
	 */
	public boolean doGroupsMatch(List<Mrrel> list1, List<Mrhier> list2) {
		if(list1 == null || list2 == null){
			return false;
		}
		if(list1.size() == 0 || list2.size() == 0){
			return false;
		}
		return list1.get(0).getCui1().equals(list2.get(0).getCui());
	}
}