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
package org.LexGrid.persistence.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

// TODO: Auto-generated Javadoc
/**
 * The Interface LexEvsDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface LexEvsDao{

	/**
	 * Query.
	 * 
	 * @param SearchObject the search object
	 * @param criteria the criteria
	 * @param maxResults the max results
	 * 
	 * @return the list< t>
	 * 
	 * @throws Exception the exception
	 */
	public <T> List<T> query(Class SearchObject, DetachedCriteria criteria, int maxResults) throws Exception;
	
	/**
	 * Query.
	 * 
	 * @param searchObj the search obj
	 * 
	 * @return the list< t>
	 * 
	 * @throws Exception the exception
	 */
	public <T> List<T> query(T searchObj) throws Exception;
	
	/**
	 * Query.
	 * 
	 * @param SearchObject the search object
	 * @param hql the hql
	 * 
	 * @return the list< t>
	 * 
	 * @throws Exception the exception
	 */
	public <T> List<T> query(Class SearchObject, String hql) throws Exception;
	
	/**
	 * Find by id.
	 * 
	 * @param searchClass the search class
	 * @param id the id
	 * 
	 * @return the t
	 * 
	 * @throws Exception the exception
	 */
	public <T> T findById(Class<T> searchClass, Serializable id) throws Exception;
	
	/**
	 * Insert.
	 * 
	 * @param entry the entry
	 * 
	 * @throws Exception the exception
	 */
	public void insert(Object entry) throws Exception;
	
	/**
	 * Gets the unique.
	 * 
	 * @param searchObject the search object
	 * @param resultType the result type
	 * @param uniqueAttribute the unique attribute
	 * 
	 * @return the unique
	 */
	public <T> List<T> getUnique(Class searchObject, Class<T> resultType, String uniqueAttribute);
	
	public void flush();
}
