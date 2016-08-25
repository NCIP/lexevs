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

package org.lexevs.tree.dao.hierarchy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedHierarchy;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * The Class CachingHierarchyResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public class CachingHierarchyResolver implements HierarchyResolver{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/** The coding scheme cache. */
	private Map<String, CodingScheme> codingSchemeCache = Collections.synchronizedMap(new HashMap<String, CodingScheme>());
	
	private LexBIGService getLexBIGService() {
		return LexBIGServiceImpl.defaultInstance();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.tree.dao.hierarchy.HierarchyResolver#getHierarchies(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	public SupportedHierarchy[] getHierarchies(String codingScheme, CodingSchemeVersionOrTag versionOrTag){
			CodingScheme cs = getCodingScheme(codingScheme, versionOrTag);
			return cs.getMappings().getSupportedHierarchy();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.tree.dao.hierarchy.HierarchyResolver#getHierarchy(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String)
	 */
	public SupportedHierarchy getHierarchy(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String hierarchyId) {
		SupportedHierarchy[] hierarchies = getHierarchies(codingScheme, versionOrTag);
		for(SupportedHierarchy hier : hierarchies){
			if(hier.getLocalId().equals(hierarchyId)){
				return hier;
			}
		}
		throw new RuntimeException("Hierarchy Id not found: " + hierarchyId);
	}

	/**
	 * Gets the cache key.
	 * 
	 * @param obj the obj
	 * 
	 * @return the cache key
	 */
	protected String getCacheKey(Object... obj) {
		StringBuffer sb = new StringBuffer(256);
		for (Object o : obj){
			sb.append("::").append(o instanceof Object[] ? getCacheKey((Object[]) o) : ObjectToString.toString(o));
		}
		return DigestUtils.shaHex(sb.toString());
	}
	
	/**
	 * Gets the coding scheme.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * 
	 * @return the coding scheme
	 */
	protected CodingScheme getCodingScheme(String codingScheme, CodingSchemeVersionOrTag versionOrTag){
		Object key = getCacheKey(codingScheme, versionOrTag);
		if(codingSchemeCache.containsKey(key)){
			return codingSchemeCache.get(key);
		} else {
			try {
				CodingScheme cs = this.getLexBIGService().resolveCodingScheme(codingScheme, versionOrTag);
				codingSchemeCache.put(getCacheKey(codingScheme, versionOrTag), cs);
				return cs;
			} catch (LBException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
