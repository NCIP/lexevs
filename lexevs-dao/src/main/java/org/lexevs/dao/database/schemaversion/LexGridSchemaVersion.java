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
package org.lexevs.dao.database.schemaversion;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * The Class LexGridSchemaVersion.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexGridSchemaVersion {

	/** The major version. */
	private int majorVersion;
	
	/** The minor version. */
	private int minorVersion;
	
	/**
	 * Sets the major version.
	 * 
	 * @param majorVersion the new major version
	 */
	public void setMajorVersion(int majorVersion) {
		this.majorVersion = majorVersion;
	}
	
	/**
	 * Gets the major version.
	 * 
	 * @return the major version
	 */
	public int getMajorVersion() {
		return majorVersion;
	}
	
	/**
	 * Sets the minor version.
	 * 
	 * @param minorVersion the new minor version
	 */
	public void setMinorVersion(int minorVersion) {
		this.minorVersion = minorVersion;
	}
	
	/**
	 * Gets the minor version.
	 * 
	 * @return the minor version
	 */
	public int getMinorVersion() {
		return minorVersion;
	}
	
	/**
	 * Parses the string to version.
	 * 
	 * Assumes a String formatted as "'major-version'.'minor-version'".
	 * 
	 * @param version the version
	 * 
	 * @return the database version
	 */
	public static LexGridSchemaVersion parseStringToVersion(String version){
		Assert.hasLength(version, "Version must not be blank.");
		
		String[] parsedVersion = StringUtils.split(version, ".");
		
		LexGridSchemaVersion dbVersion = new LexGridSchemaVersion();
		dbVersion.setMajorVersion(Integer.valueOf(parsedVersion[0]));
		dbVersion.setMinorVersion(Integer.valueOf(parsedVersion[1]));
		
		return dbVersion;
	}
	
	/**
	 * Checks if is equal version.
	 * 
	 * @param otherVersion the other version
	 * 
	 * @return true, if is equal version
	 */
	public boolean isEqualVersion(LexGridSchemaVersion otherVersion){
		return this.getMajorVersion() == otherVersion.getMajorVersion() &&
			this.getMinorVersion() == otherVersion.getMinorVersion();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj){
		if(obj != null && obj instanceof LexGridSchemaVersion){
			return isEqualVersion((LexGridSchemaVersion) obj);
		} else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "Major Version: " + this.majorVersion + " Minor Version: " + this.minorVersion;
	}
}