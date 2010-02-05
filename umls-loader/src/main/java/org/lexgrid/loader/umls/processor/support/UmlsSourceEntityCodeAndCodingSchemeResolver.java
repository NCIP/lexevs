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
package org.lexgrid.loader.umls.processor.support;

import org.lexgrid.loader.processor.support.EntityCodeAndCodingSchemeResolver;
import org.lexgrid.loader.rrf.model.Mrrel;
import org.lexgrid.loader.rrf.staging.MrconsoStagingDao;
import org.lexgrid.loader.wrappers.CodeCodingSchemePair;

/**
 * The Class UmlsSourceEntityCodeAndCodingSchemeResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsSourceEntityCodeAndCodingSchemeResolver implements EntityCodeAndCodingSchemeResolver<Mrrel> {
	
	/** The mrconso staging dao. */
	private MrconsoStagingDao mrconsoStagingDao;

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityCodeAndCodingSchemeResolver#getEntityCodeAndCodingScheme(java.lang.Object)
	 */
	public CodeCodingSchemePair getEntityCodeAndCodingScheme(Mrrel item) {
		return mrconsoStagingDao.getCodeAndCodingScheme(item.getCui1(), item.getAui1());
	}

	/**
	 * Gets the mrconso staging dao.
	 * 
	 * @return the mrconso staging dao
	 */
	public MrconsoStagingDao getMrconsoStagingDao() {
		return mrconsoStagingDao;
	}

	/**
	 * Sets the mrconso staging dao.
	 * 
	 * @param mrconsoStagingDao the new mrconso staging dao
	 */
	public void setMrconsoStagingDao(MrconsoStagingDao mrconsoStagingDao) {
		this.mrconsoStagingDao = mrconsoStagingDao;
	}
}
