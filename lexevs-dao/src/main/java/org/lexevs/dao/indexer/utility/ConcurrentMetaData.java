package org.lexevs.dao.indexer.utility;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentMetaData {

	private String path;
	 
	private ConcurrentMetaData() {
		list = new CopyOnWriteArrayList<CodingSchemeMetaData>();
	}
	
	private static ConcurrentMetaData instance;
	private CopyOnWriteArrayList<CodingSchemeMetaData> list;
	
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
