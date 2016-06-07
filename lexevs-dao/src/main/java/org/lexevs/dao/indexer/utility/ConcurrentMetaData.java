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
package org.lexevs.dao.indexer.utility;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.system.model.LocalCodingScheme;

/**
 * A replacement for the MetaData class 
 * 
 * @author <A HREF="mailto:bauer.scott@mayo.edu">Scott Bauer </A>
 *
 */
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
	
	public Set<AbsoluteCodingSchemeVersionReference> getReferenceSet(){
		Set<AbsoluteCodingSchemeVersionReference> refToReturn = new HashSet<AbsoluteCodingSchemeVersionReference>();
		
		for(CodingSchemeMetaData csm: getCodingSchemeList()){
			refToReturn.add(csm.getRef());
		}
		
		return refToReturn;
	}
	
	public CodingSchemeMetaData getCodingSchemeMetaDataForNameAndVersion(String name, String version){
		for(CodingSchemeMetaData csm:  getCodingSchemeList()){
			if(csm.getCodingSchemeName().equals(name) && csm.getCodingSchemeVersion().equals(version)){
				return csm;
			}
		}
		
		return null;
	}
	
	public CodingSchemeMetaData getCodingSchemeMetaDataForUriAndVersion(String uri, String version){
		for(CodingSchemeMetaData csm:  getCodingSchemeList()){
			if(csm.getCodingSchemeUri().equals(uri) && csm.getCodingSchemeVersion().equals(version)){
				return csm;
			}
		}		
		return null;
	}


}
