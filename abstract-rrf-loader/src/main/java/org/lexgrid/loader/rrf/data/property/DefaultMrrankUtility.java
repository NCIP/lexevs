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
package org.lexgrid.loader.rrf.data.property;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lexgrid.loader.rrf.model.Mrrank;

/**
 * The Class DefaultMrrankUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultMrrankUtility implements MrrankUtility {

	/** The mrrank list. */
	private List<Mrrank> mrrankList;
	
	private Map<String,Integer> mrrankCache = Collections.synchronizedMap(new HashMap<String,Integer>());
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.property.MrrankUtility#getRank(java.lang.String, java.lang.String)
	 */
	public int getRank(String sab, String tty) {
		String key = this.getKey(sab, tty);
		
		if(! mrrankCache.containsKey(key)){
			for(Mrrank mrrank : mrrankList){
				if(mrrank.getSab().equals(sab) && mrrank.getTty().equals(tty)){
					int rank = Integer.parseInt(mrrank.getRank());
					mrrankCache.put(key, rank);
				}
			}
		}
		return mrrankCache.get(key);
	}
	
	private String getKey(String sab, String tty){
		return Integer.toString(sab.hashCode() + tty.hashCode());
	}

	/**
	 * Gets the mrrank list.
	 * 
	 * @return the mrrank list
	 */
	public List<Mrrank> getMrrankList() {
		return mrrankList;
	}

	/**
	 * Sets the mrrank list.
	 * 
	 * @param mrrankList the new mrrank list
	 */
	public void setMrrankList(List<Mrrank> mrrankList) {
		this.mrrankList = mrrankList;
	}
}
