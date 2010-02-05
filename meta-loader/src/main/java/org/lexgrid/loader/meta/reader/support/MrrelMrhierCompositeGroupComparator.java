/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
