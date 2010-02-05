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
