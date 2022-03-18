
package org.lexgrid.loader.meta.reader.support;

import java.util.List;

import org.lexgrid.loader.reader.support.CompositeGroupComparator;
import org.lexgrid.loader.reader.support.CompositeReaderChunk;
import org.lexgrid.loader.rrf.model.Mrconso;
import org.lexgrid.loader.rrf.model.Mrdef;
import org.lexgrid.loader.rrf.model.Mrsat;
import org.lexgrid.loader.rrf.model.Mrsty;

/**
 * The Class MrconsoMrsatCompositeGroupComparator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class TotalEntityCompositeGroupComparator implements CompositeGroupComparator<CompositeReaderChunk<Mrconso,Mrsat>,CompositeReaderChunk<Mrsty,Mrdef>>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.CompositeGroupComparator#doGroupsMatch(java.util.List, java.util.List)
	 */
	public boolean doGroupsMatch(List<CompositeReaderChunk<Mrconso,Mrsat>> list1, List<CompositeReaderChunk<Mrsty,Mrdef>> list2) {
	
		if(list1 == null || list2 == null){
			return false;
		}
		if(list1.size() == 0 || list2.size() == 0){
			return false;
		}
		
		if(		list1.get(0) == null ||
				list2.get(0) == null ||
				list1.get(0).getItem1List() == null || 
				list2.get(0).getItem1List() == null

			){
			return false;
		}
		
		if(list1.get(0).getItem1List().size() == 0 && list2.get(0).getItem1List().size() == 0) {
			return false;
		}
		
		return list2.get(0).getItem1List().get(0).getCui().equals(list1.get(0).getItem1List().get(0).getCui());
	}
}