package org.lexevs.dao.indexer.utility;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.system.model.LocalCodingScheme;

public class ConcurrentMetaData {

	private static ConcurrentMetaData instance;
	private CopyOnWriteArrayList<CodingSchemeMetaData> list;
	
	private ConcurrentMetaData() {
		list = new CopyOnWriteArrayList<CodingSchemeMetaData>();
	}

	public static ConcurrentMetaData getInstance(){
		if(instance == null){
			instance = new ConcurrentMetaData();
			return instance;
		}
		return instance;
	}
	
	public List<CodingSchemeMetaData> getCodingSchemeList(){
		return list;
	}
	
	public boolean add(CodingSchemeMetaData scheme){
		return list.add(scheme);
	}
	
	public boolean remove(CodingSchemeMetaData scheme){
		return list.remove(scheme);
	}

	public void removeIndexMetaDataValue(String key) {
		for(CodingSchemeMetaData csm : list){
			if(key.equals(LocalCodingScheme.getLocalCodingScheme(
					csm.getCodingSchemeName(), csm.getCodingSchemeVersion()).getKey())){
				remove(csm);
			}
		}
	}
	
	public Iterator<CodingSchemeMetaData> refreshIterator(){
		return list.iterator();
	}

	public String getIndexMetaDataValue(String key) {
		for(CodingSchemeMetaData csmd: list){
			if(csmd.getNameVersionKey().equals(key)){
				if(csmd.getDirectory() != null){
				return csmd.getDirectory().getIndexName();
				}
			}
		}
		return null;
	}
	
	public CodingSchemeMetaData getIndexMetaData(String key){
		for(CodingSchemeMetaData csmd: list){
			if(csmd.getNameVersionKey().equals(key)){
				if(csmd.getDirectory() != null){
				return csmd;
				}
			}
		}
		return null;
	}
	
	public CodingSchemeMetaData getIndexMetaDataForFileName(String fileName){
		for(CodingSchemeMetaData csmd: list){
			if(csmd.getDirectory().getIndexName().equals(fileName)){
				return csmd;
			}
		}
		return null;
	}

	public String[] getIndexMetaDataKeys() {
		String[] keys = new String[list.size()];
		for(int i = 0; i < list.size(); i++){
			
			keys[i] = list.get(i).getNameVersionKey();
		}
		return keys;
		
	}


}
