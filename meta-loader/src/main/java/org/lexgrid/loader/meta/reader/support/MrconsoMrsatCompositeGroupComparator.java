
package org.lexgrid.loader.meta.reader.support;

import java.util.List;

import org.lexgrid.loader.reader.support.CompositeGroupComparator;
import org.lexgrid.loader.rrf.model.Mrconso;
import org.lexgrid.loader.rrf.model.Mrsat;

/**
 * The Class MrrelMrhierCompositeGroupComparator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrconsoMrsatCompositeGroupComparator implements CompositeGroupComparator<Mrconso,Mrsat>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.CompositeGroupComparator#doGroupsMatch(java.util.List, java.util.List)
	 */
	public boolean doGroupsMatch(List<Mrconso> list1, List<Mrsat> list2) {
		if(list1 == null || list2 == null){
			return false;
		}
		if(list1.size() == 0 || list2.size() == 0){
			return false;
		}
		return list1.get(0).getCui().equals(list2.get(0).getCui());
	}
}