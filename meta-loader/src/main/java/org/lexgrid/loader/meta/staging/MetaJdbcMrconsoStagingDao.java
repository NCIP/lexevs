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
package org.lexgrid.loader.meta.staging;

import java.util.List;

import org.lexgrid.loader.meta.staging.processor.MetaMrconsoStagingDao;
import org.lexgrid.loader.rrf.staging.JdbcMrconsoStagingDao;
import org.springframework.dao.DataAccessException;

public class MetaJdbcMrconsoStagingDao extends JdbcMrconsoStagingDao implements MetaMrconsoStagingDao {

	protected String getRootCuis = "select CUI from " + PREFIX_PLACEHOLDER + TABLENAME_PLACEHOLDER + " where SAB = 'SRC' and TTY = 'RHT'";

	public List<String> getMetaRootCuis(){
		try {
			return (List<String>)getJdbcTemplate().queryForList(setPlaceholders(getRootCuis), String.class);
		} catch (DataAccessException e) {
			logger.error("Error getting Meta Root Cuis", e);	
			throw new RuntimeException(e);
		}
	}
}