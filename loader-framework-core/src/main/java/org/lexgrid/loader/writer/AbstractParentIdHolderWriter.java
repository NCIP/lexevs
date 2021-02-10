
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