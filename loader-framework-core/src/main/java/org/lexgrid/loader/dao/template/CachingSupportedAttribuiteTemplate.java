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
package org.lexgrid.loader.dao.template;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.persistence.dao.LexEvsDao;
import org.LexGrid.persistence.model.CodingSchemeSupportedAttrib;
import org.LexGrid.persistence.model.CodingSchemeSupportedAttribId;

/**
 * The Class CachingSupportedAttribuiteTemplate.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CachingSupportedAttribuiteTemplate extends AbstractSupportedAttributeTemplate{

	/** The attribute cache. */
	private List<CodingSchemeSupportedAttribId> attributeCache = new ArrayList<CodingSchemeSupportedAttribId>();
	
	/** The lex evs dao. */
	private LexEvsDao lexEvsDao;
	
	/** The max cache size. */
	private int maxCacheSize = 100;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.AbstractSupportedAttributeTemplate#insert(org.LexGrid.persistence.model.CodingSchemeSupportedAttrib)
	 */
	@Override
	protected synchronized void insert(CodingSchemeSupportedAttrib attrib){
		CodingSchemeSupportedAttribId id = attrib.getId();
		if(!attributeCache.contains(id)){
			try {
				lexEvsDao.insert(attrib);
			} catch (Exception e) {
				this.getLogger().info("Same Supported Attribute inserted twice -- skipping.");
			}
			attributeCache.add(id);
		}
		if(attributeCache.size() >= maxCacheSize){
			getLogger().info("Dumping CodingSchemeSupportedAttrib cache.");
			attributeCache.clear();
		}
	}
	
	/**
	 * Gets the cache.
	 * 
	 * @return the cache
	 */
	protected synchronized List<CodingSchemeSupportedAttribId> getCache(){
		return attributeCache;
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.AbstractSupportedAttributeTemplate#getLexEvsDao()
	 */
	public LexEvsDao getLexEvsDao() {
		return lexEvsDao;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.AbstractSupportedAttributeTemplate#setLexEvsDao(org.LexGrid.persistence.dao.LexEvsDao)
	 */
	public void setLexEvsDao(LexEvsDao lexEvsDao) {
		this.lexEvsDao = lexEvsDao;
	}

	/**
	 * Gets the max cache size.
	 * 
	 * @return the max cache size
	 */
	public int getMaxCacheSize() {
		return maxCacheSize;
	}

	/**
	 * Sets the max cache size.
	 * 
	 * @param maxCacheSize the new max cache size
	 */
	public void setMaxCacheSize(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
	}
}
