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
package org.lexgrid.loader.rxn.reader.support;

//import org.LexGrid.persistence.dao.LexEvsDao;
import org.LexGrid.concepts.Entity;
//import org.LexGrid.concepts.EntityId;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;

import org.lexgrid.loader.processor.support.EntityCodeResolver;
import org.lexgrid.loader.rrf.model.Mrsat;

/**
 * The Class AbstractSabSkippingPolicy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSabAndCodeSkippingPolicy<I> extends AbstractSabSkippingPolicy<I> {
	
//	private LexEvsDao lexEvsDao;
	/** The coding scheme name setter. */
	private CodingSchemeIdSetter codingSchemeNameSetter;
	private EntityCodeResolver<I> entityCodeResolver;





	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.SkipPolicy#toSkip(java.lang.Object)
	 */
	public boolean toSkip(I item) {
		if(super.toSkip(item)){
			return true;
		}
		if (doesEntityNotExist(item)) {
			return true;
		}
		return false;
	}
	
	
	private boolean doesEntityNotExist(I item)  {
		Entity entityId = new Entity();
		//entityId.setCodingSchemeName(codingSchemeNameSetter.getCodingSchemeName());
		entityId.setEntityCode(entityCodeResolver.getEntityCode(item));
		//entityId.setEntityCodeNamespace(codingSchemeNameSetter.getCodingSchemeName());
		Entity returnValue= null;
		try {
		 // returnValue = lexEvsDao.findById(Entity.class, entityId);
		} catch (Exception ex) {
			ex.printStackTrace();			
		}
		if (returnValue == null) {
			return true;
		} else {
			return false;
		}
	}	
	

	
//	public void setLexEvsDao(LexEvsDao lexEvsDao) {
//		this.lexEvsDao = lexEvsDao;
//	}
//
//	public LexEvsDao getLexEvsDao() {
//		return lexEvsDao;
//	}
	
	/**
	 * Gets the coding scheme name setter.
	 * 
	 * @return the coding scheme name setter
	 */
	public CodingSchemeIdSetter getCodingSchemeNameSetter() {
		return codingSchemeNameSetter;
	}

	/**
	 * Sets the coding scheme name setter.
	 * 
	 * @param codingSchemeNameSetter the new coding scheme name setter
	 */
	public void setCodingSchemeNameSetter(
			CodingSchemeIdSetter codingSchemeNameSetter) {
		this.codingSchemeNameSetter = codingSchemeNameSetter;
	}	
	
	public EntityCodeResolver<I> getEntityCodeResolver() {
		return entityCodeResolver;
	}


	public void setEntityCodeResolver(EntityCodeResolver<I> entityCodeResolver) {
		this.entityCodeResolver = entityCodeResolver;
	}	
}
