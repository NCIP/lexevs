package org.lexevs.dao.indexer.utility;

import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentMetaData {

	private String path;
	private static ConcurrentMetaData instance;
	private CopyOnWriteArrayList<CodingSchemeMetaData> list;
	
	private ConcurrentMetaData() {
		list = new CopyOnWriteArrayList<CodingSchemeMetaData>();
	}

	public static ConcurrentMetaData getInstance(String fileLocation){
		if(instance == null){
			return new ConcurrentMetaData();
		}
		return instance;
	}
	
	public CopyOnWriteArrayList<CodingSchemeMetaData> getCodingSchemeList(){
		return list;
	}
	
	public boolean add(CodingSchemeMetaData scheme){
		return list.add(scheme);
	}
	
	public boolean remove(CodingSchemeMetaData scheme){
		return list.remove(scheme);
	}
	

}
