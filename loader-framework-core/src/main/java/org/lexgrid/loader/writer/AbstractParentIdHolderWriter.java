package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.springframework.batch.item.ItemWriter;

public abstract class AbstractParentIdHolderWriter<T> extends AbstractDatabaseServiceWriter implements ItemWriter<ParentIdHolder<T>>{

	public void write(List<? extends ParentIdHolder<T>> list)
	throws Exception {
		Map<String, List<ParentIdHolder<T>>> map = groupByCodingSchemeId(list);
		for(String codingSchemeId : map.keySet()){
			doWrite(codingSchemeId, map.get(codingSchemeId));
		}
	}

	public abstract void doWrite(String codingSchemeId, List<ParentIdHolder<T>> items);

	public Map<String,List<ParentIdHolder<T>>> groupByCodingSchemeId(List<? extends ParentIdHolder<T>> list){
		Map<String,List<ParentIdHolder<T>>> returnMap = new HashMap<String,List<ParentIdHolder<T>>>();

		for(ParentIdHolder<T> holder : list){
			String csId = holder.getCodingSchemeIdSetter().getCodingSchemeName();
			if(! returnMap.containsKey(csId)){
				returnMap.put(csId, new ArrayList<ParentIdHolder<T>>());
			}
			returnMap.get(csId).add(holder);
		}
		return returnMap;
	}

}
