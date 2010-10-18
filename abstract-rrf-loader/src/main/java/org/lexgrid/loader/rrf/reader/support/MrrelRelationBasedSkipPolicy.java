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
package org.lexgrid.loader.rrf.reader.support;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.lexgrid.loader.reader.support.SkipPolicy;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrrel;

public class MrrelRelationBasedSkipPolicy implements SkipPolicy<Mrrel>{

/** The forward name list. */
private List<String> forwardNameList;
	
	public boolean toSkip(Mrrel item) {	
		String dirFlag = item.getDir();
		if(StringUtils.isNotEmpty(dirFlag)){
			if(dirFlag.equals(RrfLoaderConstants.ASSOC_DIR_ASSERTED_TRUE)){
				return false;
			} else if(dirFlag.equals(RrfLoaderConstants.ASSOC_DIR_ASSERTED_FALSE)){
				return true;
			}
		}
		return !processRelation(item.getRel());
	}
	
	/**
	 * Process relation.
	 * 
	 * @param relationName the relation name
	 * 
	 * @return true, if successful
	 */
	protected boolean processRelation(String relationName){
		if(StringUtils.isEmpty(relationName)){
			return false;
		}
		return forwardNameList.contains(relationName);
	}

	public List<String> getForwardNameList() {
		return forwardNameList;
	}

	public void setForwardNameList(List<String> forwardNameList) {
		this.forwardNameList = forwardNameList;
	}
}