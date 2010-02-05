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
package org.LexGrid.persistence.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.persistence.dao.LexEvsDao;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

// TODO: Auto-generated Javadoc
/**
 * The Class HibernateLexEvsDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class HibernateLexEvsDao extends HibernateDaoSupport implements LexEvsDao {
	
	private static final String ID_FIELD = "id";

	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.dao.LexEvsDao#query(java.lang.Class, org.hibernate.criterion.DetachedCriteria, int)
	 */
	public <T> List<T> query(Class searchObj, DetachedCriteria criteria, int maxResults)
			throws Exception {
		return this.getHibernateTemplate().findByCriteria(criteria, 0, maxResults);
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.dao.LexEvsDao#query(java.lang.Object)
	 */
	public <T> List<T> query(T searchObj) throws Exception {
		if(doesObjectHaveIdField(searchObj)) {
			DetachedCriteria dc = DetachedCriteria.forClass(searchObj.getClass());
			dc.add(buildIdCriterion(searchObj));
			dc.add(Example.create(searchObj));
			return this.getHibernateTemplate().findByCriteria(dc);
		} else {
			return this.getHibernateTemplate().findByExample(searchObj);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.dao.LexEvsDao#findById(java.lang.Class, java.io.Serializable)
	 */
	public <T> T findById(Class<T> searchClass, Serializable id) throws Exception {
		return (T)this.getHibernateTemplate().get(searchClass, id);
	}
	
	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.dao.LexEvsDao#insert(java.lang.Object)
	 */
	public void insert(Object entry) throws Exception {
		this.getHibernateTemplate().saveOrUpdate(entry);
	}
	
	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.dao.LexEvsDao#getUnique(java.lang.Class, java.lang.Class, java.lang.String)
	 */
	public <T> List<T> getUnique(Class searchObj, Class<T> resultType, String uniqueAttribute) {
		 DetachedCriteria dc = DetachedCriteria.forClass(searchObj);
		 dc.setProjection(Projections.distinct(Projections.property(uniqueAttribute)));
		 return this.getHibernateTemplate().findByCriteria(dc);
	 }
	
	public void flush() {
		 this.getHibernateTemplate().flush();
	 }

	/**
	 * Builds the id criterion.
	 * 
	 * @param searchObj the search obj
	 * 
	 * @return the criterion
	 * 
	 * @throws Exception the exception
	 */
	protected Criterion buildIdCriterion(Object searchObj) throws Exception {
		Map<String,Object> idValueMap = new HashMap<String,Object>();
		Field idField = searchObj.getClass().getDeclaredField(ID_FIELD);
		idField.setAccessible(true);
		Object id = idField.get(searchObj);
		Field[] idFields = id.getClass().getDeclaredFields();
		for(Field field : idFields){
			field.setAccessible(true);
			Object fieldObj = field.get(id);
			if(fieldObj != null){
				idValueMap.put(ID_FIELD + "." + field.getName(), fieldObj);
			}
		}	
		return Restrictions.allEq(idValueMap);
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.dao.LexEvsDao#query(java.lang.Class, java.lang.String)
	 */
	public <T> List<T> query(Class SearchObject, String hql) throws Exception {
		return this.getHibernateTemplate().find(hql);	
	}	
	
	protected boolean doesObjectHaveIdField(Object searchObj) {
		for(Field field : searchObj.getClass().getDeclaredFields()) {
			if(field.getName().equals(ID_FIELD)) {
				return true;
			}
		}
		return false;
	}	
}
