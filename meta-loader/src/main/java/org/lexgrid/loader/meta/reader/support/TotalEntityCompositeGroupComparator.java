/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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