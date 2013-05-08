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
package org.lexevs.dao.database.service.daocallback;

import org.lexevs.dao.database.access.DaoManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class DaoManagerDaoCallbackService.
 */
public class DaoManagerDaoCallbackService implements DaoCallbackService {

/** The dao manager. */
private DaoManager daoManager;
	
	/**
	 * Execute in dao layer.
	 * 
	 * @param daoCallback the dao callback
	 * 
	 * @return the t
	 */
	@Transactional
	public <T> T executeInDaoLayer(DaoCallback<T> daoCallback){
		return daoCallback.execute(daoManager);
	}

	/**
	 * Gets the dao manager.
	 * 
	 * @return the dao manager
	 */
	public DaoManager getDaoManager() {
		return daoManager;
	}

	/**
	 * Sets the dao manager.
	 * 
	 * @param daoManager the new dao manager
	 */
	public void setDaoManager(DaoManager daoManager) {
		this.daoManager = daoManager;
	}
}