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
package org.lexgrid.loader.umls.processor.support;

import java.util.ArrayList;
import java.util.List;

import org.lexgrid.loader.rrf.processor.support.AbstractRrfRootNodeResolver;
import org.lexgrid.loader.rrf.staging.MrconsoStagingDao;

/**
 * The Class UmlsRootNodeResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsRootNodeResolver extends AbstractRrfRootNodeResolver {
	
	private MrconsoStagingDao mrconsoStagingDao;
	
	private static String ROOT_NODE_PREFIX = "V-";
	
	private List<String> cachedRootNodes;
	
	/** The sab. */
	private String sab;

	/**
	 * Construct sab root node.
	 * 
	 * @return the string
	 */
	protected boolean isSourceRootNode(String root){
		return getCachedRootCodes().contains(root);
	}
	
	protected List<String> getCachedRootCodes(){
		if(cachedRootNodes == null){
			cachedRootNodes = getRootCodes();
		}
		return cachedRootNodes;
	}
	
	protected List<String> getRootCodes(){
		List<String> returnList = new ArrayList<String>();
//		returnList.add(ROOT_NODE_PREFIX + sab);
		List<String> cuis = mrconsoStagingDao.getCuisFromCode(ROOT_NODE_PREFIX + sab);
		for(String cui : cuis){
			returnList.addAll(mrconsoStagingDao.getCodesFromCui(cui));
		}
		return returnList;
	}

	/**
	 * Gets the sab.
	 * 
	 * @return the sab
	 */
	public String getSab() {
		return sab;
	}

	/**
	 * Sets the sab.
	 * 
	 * @param sab the new sab
	 */
	public void setSab(String sab) {
		this.sab = sab;
	}

	public MrconsoStagingDao getMrconsoStagingDao() {
		return mrconsoStagingDao;
	}

	public void setMrconsoStagingDao(MrconsoStagingDao mrconsoStagingDao) {
		this.mrconsoStagingDao = mrconsoStagingDao;
	}
}