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
package org.lexgrid.loader.rrf.dao;

import java.util.List;

import org.LexGrid.persistence.dao.hibernate.HibernateLexEvsDao;
import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.lexgrid.loader.data.codingScheme.CodingSchemeNameSetter;

/**
 * The Class HibernateRrfPostProcessingDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class HibernateRrfPostProcessingDao extends HibernateLexEvsDao implements RrfPostProcessingDao {

	/** The coding scheme name setter. */
	private CodingSchemeNameSetter codingSchemeNameSetter;
		
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.dao.RrfPostProcessingDao#doesRelationExistInEntityAssnToEntity(java.lang.String)
	 */
	public boolean doesRelationExistInEntityAssnToEntity(String relation) {
		EntityAssnsToEntity assoc = buildEntityAssnsToEntityForRelationName(relation);
		List returnList = getHibernateTemplate().findByExample(assoc, 0, 1);
		return returnList.size() > 0;
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.dao.RrfPostProcessingDao#getRelationContainers(java.lang.String)
	 */
	public List<String> getRelationContainers(String relation) {
		DetachedCriteria dc = DetachedCriteria.forClass(EntityAssnsToEntity.class);
		dc.add(Example.create(buildEntityAssnsToEntityForRelationName(relation)));	
		dc.setProjection(Projections.distinct(Projections.property("containerName")));

		return getHibernateTemplate().findByCriteria(dc);
	}
	
	/**
	 * Builds the entity assns to entity for relation name.
	 * 
	 * @param relation the relation
	 * 
	 * @return the entity assns to entity
	 */
	protected EntityAssnsToEntity buildEntityAssnsToEntityForRelationName(String relation){
		EntityAssnsToEntity assoc = new EntityAssnsToEntity();
		assoc.setCodingSchemeName(codingSchemeNameSetter.getCodingSchemeName());
		assoc.setEntityCode(relation);
		assoc.setEntityCodeNamespace(codingSchemeNameSetter.getCodingSchemeName());
		return assoc;
	}

	/**
	 * Gets the coding scheme name setter.
	 * 
	 * @return the coding scheme name setter
	 */
	public CodingSchemeNameSetter getCodingSchemeNameSetter() {
		return codingSchemeNameSetter;
	}

	/**
	 * Sets the coding scheme name setter.
	 * 
	 * @param codingSchemeNameSetter the new coding scheme name setter
	 */
	public void setCodingSchemeNameSetter(
			CodingSchemeNameSetter codingSchemeNameSetter) {
		this.codingSchemeNameSetter = codingSchemeNameSetter;
	}

}
