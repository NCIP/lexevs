/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.internal.model.resource.factory;

import java.sql.Timestamp;
import java.util.Date;

import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * A factory for creating AbstractResource objects.
 */
public abstract class AbstractResourceFactory {
	
	/** The lex evs service locator. */
	private LexEvsServiceLocator lexEvsServiceLocator;
	
	/**
	 * Gets the revision id by date.
	 *
	 * @param date the date
	 * @return the revision id by date
	 */
	protected String getRevisionIdByDate(final Date date){
		return this.lexEvsServiceLocator.
			getDatabaseServiceManager().
				getDaoCallbackService().
					executeInDaoLayer(new DaoCallback<String>(){

			@Override
			public String execute(DaoManager daoManager) {
				return daoManager.getRevisionDao().getRevisionIdForDate(new Timestamp(date.getTime()));
			}
		});
	}

	/**
	 * Sets the lex evs service locator.
	 *
	 * @param lexEvsServiceLocator the new lex evs service locator
	 */
	public void setLexEvsServiceLocator(LexEvsServiceLocator lexEvsServiceLocator) {
		this.lexEvsServiceLocator = lexEvsServiceLocator;
	}

	/**
	 * Gets the lex evs service locator.
	 *
	 * @return the lex evs service locator
	 */
	public LexEvsServiceLocator getLexEvsServiceLocator() {
		return lexEvsServiceLocator;
	}
}
