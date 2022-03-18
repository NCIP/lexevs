
package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.lexgrid.loader.wrappers.CodingSchemeUriVersionPair;
import org.springframework.batch.item.ItemWriter;

public abstract class AbstractCodingSchemeIdHolderWriter<T> extends AbstractDatabaseServiceWriter implements ItemWriter<CodingSchemeIdHolder<T>>{

	public void write(List<? extends CodingSchemeIdHolder<T>> list)
			throws Exception {
		Map<CodingSchemeUriVersionPair, List<T>> map = groupByCodingSchemeId(list);
		for(CodingSchemeUriVersionPair codingSchemeId : map.keySet()){
			doWrite(codingSchemeId, map.get(codingSchemeId));
		}
	}

	public abstract void doWrite(CodingSchemeUriVersionPair codingSchemeId, List<T> items);
	
	public Map<CodingSchemeUriVersionPair,List<T>> groupByCodingSchemeId(List<? extends CodingSchemeIdHolder<T>> list){
		Map<CodingSchemeUriVersionPair,List<T>> returnMap = new HashMap<CodingSchemeUriVersionPair,List<T>>();
		
		for(CodingSchemeIdHolder<T> holder : list){
			CodingSchemeUriVersionPair csId = 
				new CodingSchemeUriVersionPair(
						holder.getCodingSchemeIdSetter().getCodingSchemeUri(),
						holder.getCodingSchemeIdSetter().getCodingSchemeVersion());
			if(! returnMap.containsKey(csId)){
				returnMap.put(csId, new ArrayList<T>());
			}
			returnMap.get(csId).add(holder.getItem());
		}
		return returnMap;
	}
	
}