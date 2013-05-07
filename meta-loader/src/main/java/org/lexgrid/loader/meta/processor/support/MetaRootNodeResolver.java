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
package org.lexgrid.loader.meta.processor.support;

import java.util.List;

import org.lexgrid.loader.meta.staging.processor.MetaMrconsoStagingDao;
import org.lexgrid.loader.rrf.processor.support.AbstractRrfRootNodeResolver;

/**
 * The Class UmlsRootNodeResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaRootNodeResolver extends AbstractRrfRootNodeResolver {
	
	private MetaMrconsoStagingDao metaMrconsoStagingDao;
	
	private List<String> rootCuis;
	/**
	 * Construct root node.
	 * 
	 * @return the string
	 */
	protected boolean isSourceRootNode(String root){
		//We can't populate rootCuis on bean init, because the staging tables
		//aren't loaded yet. Here, we just do it on the first call. On restarts,
		//this will repopulate itself as needed.
		if(rootCuis == null){
			rootCuis = metaMrconsoStagingDao.getMetaRootCuis();
		}
		return rootCuis.contains(root);
	}
	
	public MetaMrconsoStagingDao getMetaMrconsoStagingDao() {
		return metaMrconsoStagingDao;
	}
	public void setMetaMrconsoStagingDao(MetaMrconsoStagingDao metaMrconsoStagingDao) {
		this.metaMrconsoStagingDao = metaMrconsoStagingDao;
	}
}