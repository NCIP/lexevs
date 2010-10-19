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
package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lexgrid.loader.wrappers.CodingSchemeUriVersionPair;
import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.springframework.batch.item.ItemWriter;

public abstract class AbstractParentIdHolderWriter<T> extends AbstractDatabaseServiceWriter implements ItemWriter<ParentIdHolder<T>>{

	public void write(List<? extends ParentIdHolder<T>> list)
	throws Exception {
		Map<CodingSchemeUriVersionPair,List<ParentIdHolder<T>>> map = groupByCodingSchemeId(list);
		for(CodingSchemeUriVersionPair codingSchemeId : map.keySet()){
			doWrite(codingSchemeId, map.get(codingSchemeId));
		}
	}

	public abstract void doWrite(CodingSchemeUriVersionPair codingSchemeId, List<ParentIdHolder<T>> items);

	public Map<CodingSchemeUriVersionPair,List<ParentIdHolder<T>>> groupByCodingSchemeId(List<? extends ParentIdHolder<T>> list){
		Map<CodingSchemeUriVersionPair,List<ParentIdHolder<T>>> returnMap = new HashMap<CodingSchemeUriVersionPair,List<ParentIdHolder<T>>>();
		
		for(ParentIdHolder<T> holder : list){
			CodingSchemeUriVersionPair csId = 
				new CodingSchemeUriVersionPair(
						holder.getCodingSchemeIdSetter().getCodingSchemeUri(),
						holder.getCodingSchemeIdSetter().getCodingSchemeVersion());
			if(! returnMap.containsKey(csId)){
				returnMap.put(csId, new ArrayList<ParentIdHolder<T>>());
			}
			returnMap.get(csId).add(holder);
		}
		return returnMap;
	}

}