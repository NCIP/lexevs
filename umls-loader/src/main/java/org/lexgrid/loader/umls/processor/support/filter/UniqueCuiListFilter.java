
package org.lexgrid.loader.umls.processor.support.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lexgrid.loader.processor.support.ListFilter;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class UniqueCuiListFilter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UniqueCuiListFilter implements ListFilter<Mrconso>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.ListFilter#filter(java.util.List)
	 */
	public List<Mrconso> filter(List<Mrconso> items) {
		Map<String,Mrconso> unique = new HashMap<String,Mrconso>();
		for(Mrconso item : items){
			unique.put(item.getCui(), item);
		}
		return new ArrayList<Mrconso>(unique.values());	
	}
}