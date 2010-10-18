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
package org.lexevs.cts2;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;

/**
 * The Class LexEvsBasedService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class LexEvsBasedService {

    /** The lbs_. */
    private transient LexBIGService lbs_ = null;

    /**
     * Gets the lex big service.
     * 
     * @return the lex big service
     */
    protected LexBIGService getLexBIGService(){
		if (lbs_ == null) {
			lbs_ = LexBIGServiceImpl.defaultInstance();
		}
		
		return lbs_;
	}
    
    /**
     * Gets the lex big service manager.
     * 
     * @return the lex big service manager
     * 
     * @throws LBException the LB exception
     */
    protected LexBIGServiceManager getLexBIGServiceManager() throws LBException{
		return this.getLexBIGService().getServiceManager(this.getLexBIGServiceManagerCredentials());
	}

	/**
	 * Gets the system resource service.
	 * 
	 * @return the system resource service
	 */
	protected SystemResourceService getSystemResourceService() {
		return this.getLexEvsServiceLocator().getSystemResourceService();
	}

	/**
	 * Gets the database service manager.
	 * 
	 * @return the database service manager
	 */
	protected DatabaseServiceManager getDatabaseServiceManager() {
		return this.getLexEvsServiceLocator().getDatabaseServiceManager();
	}

	/**
	 * Gets the index service manager.
	 * 
	 * @return the index service manager
	 */
	protected IndexServiceManager getIndexServiceManager() {
		return this.getLexEvsServiceLocator().getIndexServiceManager();
	}
	
    /**
     * Gets the lex evs service locator.
     * 
     * @return the lex evs service locator
     */
    private LexEvsServiceLocator getLexEvsServiceLocator() {
    	return LexEvsServiceLocator.getInstance();
    }

	/**
	 * Gets the lex big service manager credentials.
	 * 
	 * @return the lex big service manager credentials
	 */
	protected Object getLexBIGServiceManagerCredentials() {
		return null;
	}
}