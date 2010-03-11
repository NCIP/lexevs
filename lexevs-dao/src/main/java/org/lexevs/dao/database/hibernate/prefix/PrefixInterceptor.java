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
package org.lexevs.dao.database.hibernate.prefix;

import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.dao.database.prefix.PrefixResolver;

/**
 * Hibernate Interceptor used to modify the SQL query sent to the database.
 * This interceptor changes the prefix, and also places some extra constraints
 * on the query to ensure that critical queries always use DB table indexes.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PrefixInterceptor extends EmptyInterceptor {  

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1940273682945001115L;

	/** The PREFI x_ placeholder. */
	public static String PREFIX_PLACEHOLDER = DatabaseConstants.PREFIX_PLACEHOLDER;
	
	/** The log. */
	private static Logger log = Logger.getLogger(PrefixInterceptor.class.getName());
	
	/** The prefix. */
	private PrefixResolver prefixResolver;
	
	/**
	 * Instantiates a new prefix interceptor.
	 */
	public PrefixInterceptor(){
		super();
	}
	
	/**
	 * Instantiates a new prefix interceptor.
	 * 
	 * @param prefixResolver the prefix resolver
	 */
	public PrefixInterceptor(PrefixResolver prefixResolver){
		this.prefixResolver = prefixResolver;
	}
	
	/* (non-Javadoc)
	 * @see org.hibernate.EmptyInterceptor#onPrepareStatement(java.lang.String)
	 */
	public String onPrepareStatement(String sql) { 	
		log.debug("Adjusting table names to prefix: " + prefixResolver.resolveDefaultPrefix());
		sql = sql.replaceAll(PREFIX_PLACEHOLDER, prefixResolver.resolveDefaultPrefix());
	
		return sql;			
	}


	/**
	 * Sets the prefix resolver.
	 * 
	 * @param prefixResolver the new prefix resolver
	 */
	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}

	/**
	 * Gets the prefix resolver.
	 * 
	 * @return the prefix resolver
	 */
	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}
}  