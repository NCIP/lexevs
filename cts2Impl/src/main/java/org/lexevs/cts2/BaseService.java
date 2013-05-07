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

import org.lexevs.dao.database.key.Java5UUIDKeyGenerator;

/**
 * The Class BaseService.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public abstract class BaseService extends LexEvsBasedService {

    private transient LexEvsCTS2 lexEvsCTS2;
    private ServiceInfo serviceInfo_ = null;
 
	public static enum SortableProperties {
        matchToQuery, code, codeSystem, entityDescription, conceptStatus, isActive
    };

    public static enum KnownTags {
        PRODUCTION
    };
    
    public ServiceInfo getServiceInfo(){
    	if (serviceInfo_ == null)
    		serviceInfo_ = new ServiceInfo();
    	
    	return serviceInfo_;
    }

	protected LexEvsCTS2 getLexEvsCTS2() {
		if(this.lexEvsCTS2 == null) {
			this.lexEvsCTS2 = LexEvsCTS2Impl.defaultInstance();
		}
		return this.lexEvsCTS2;
	}
	
	/**
	 * Creates the unique id.
	 * 
	 * @return the string
	 */
	protected String createUniqueId(){
		return Java5UUIDKeyGenerator.getRandomUUID().toString();
	}
}