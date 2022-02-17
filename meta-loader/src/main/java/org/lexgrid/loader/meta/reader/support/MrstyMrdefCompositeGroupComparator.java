
package org.lexgrid.loader.meta.reader.support;

import java.util.List;

import org.lexgrid.loader.reader.support.CompositeGroupComparator;
import org.lexgrid.loader.rrf.model.Mrdef;
import org.lexgrid.loader.rrf.model.Mrsty;

/**
 * The Class MrconsoMrsatCompositeGroupComparator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrstyMrdefCompositeGroupComparator implements CompositeGroupComparator<Mrsty,Mrdef>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.CompositeGroupComparator#doGroupsMatch(java.util.List, java.util.List)
	 */
	public boolean doGroupsMatch(List<Mrsty> list1, List<Mrdef> list2) {

		if(list1 == null || list2 == null){
			return false;
		}
		if(list1.size() == 0 || list2.size() == 0){
			return false;
		}
		return list1.get(0).getCui().equals(list2.get(0).getCui());

	}
}