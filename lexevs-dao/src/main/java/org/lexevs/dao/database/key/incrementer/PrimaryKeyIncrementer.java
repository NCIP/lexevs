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
package org.lexevs.dao.database.key.incrementer;

import org.lexevs.dao.database.type.DatabaseType;

/**
 * The Interface PrimaryKeyIncrementer.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PrimaryKeyIncrementer {
	
	/**
	 * The Enum KeyType.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public enum KeyType {
		INT,
		BIGINT,
		VARCHAR}
	
	/**
	 * Next key.
	 * 
	 * @return the string
	 */
	public String nextKey();
	
	/**
	 * Value of.
	 * 
	 * @param key the key
	 * 
	 * @return the object
	 */
	public Object valueOf(String key);
	
	/**
	 * Gets the key type.
	 * 
	 * @return the key type
	 */
	public KeyType getKeyType();
	
	/**
	 * String value.
	 * 
	 * @param key the key
	 * 
	 * @return the string
	 */
	public String stringValue(Object key);

	/**
	 * Gets the key length.
	 * 
	 * @return the key length
	 */
	public int getKeyLength();

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Initialize.
	 */
	public void initialize();
	
	/**
	 * Destroy.
	 */
	public void destroy();
	
	/**
	 * Supports databases.
	 * 
	 * @param databaseType the database type
	 * 
	 * @return true, if successful
	 */
	public boolean supportsDatabases(DatabaseType databaseType);
}