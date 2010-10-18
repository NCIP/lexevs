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
package org.lexevs.dao.database.jdbc;

import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;

public class MysqlJdbcUrlAppendingFactory implements FactoryBean{

	private String urlAppendString = "?useCursorFetch=true";
	
	private SystemVariables systemVariables;
	
	private static String MYSQL_URL_MATCH = "mysql";
	
	@Override
	public Object getObject() throws Exception {
		String url = this.systemVariables.getAutoLoadDBURL();
		
		if(isMysqlUrl(url)) {
			
			if(! url.contains(urlAppendString)) {
			
				return url + urlAppendString;
			}
		}
		
		return url;
	}

	protected boolean isMysqlUrl(String url) {
		return url.equalsIgnoreCase(MYSQL_URL_MATCH);
	}

	@Override
	public Class getObjectType() {
		return String.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setUrlAppendString(String urlAppendString) {
		this.urlAppendString = urlAppendString;
	}

	public String getUrlAppendString() {
		return urlAppendString;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}	
}