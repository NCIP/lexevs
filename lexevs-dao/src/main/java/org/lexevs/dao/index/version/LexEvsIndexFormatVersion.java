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
package org.lexevs.dao.index.version;

import org.springframework.util.Assert;

/**
 * The Class LexEvsIndexFormatVersion.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsIndexFormatVersion {

	/** The model format version. */
	private String modelFormatVersion;

	/**
	 * Parses the string to version.
	 * 
	 * Assumes a String formatted as "'major-version'.'minor-version'".
	 * 
	 * @param version the version
	 * 
	 * @return the database version
	 */
	public static LexEvsIndexFormatVersion parseStringToVersion(String version){
		Assert.hasLength(version, "Version must not be blank.");
		
		LexEvsIndexFormatVersion lexEvsIndexFormatVersion = new LexEvsIndexFormatVersion();
		lexEvsIndexFormatVersion.setModelFormatVersion(version);
		
		return lexEvsIndexFormatVersion;
	}
	
	/**
	 * Checks if is equal version.
	 * 
	 * @param otherVersion the other version
	 * 
	 * @return true, if is equal version
	 */
	public boolean isEqualVersion(LexEvsIndexFormatVersion otherVersion){
		return this.getModelFormatVersion().equals(otherVersion.getModelFormatVersion());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj){
		if(obj != null && obj instanceof LexEvsIndexFormatVersion){
			return isEqualVersion((LexEvsIndexFormatVersion) obj);
		} else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "Index format version: " + this.modelFormatVersion;
	}

	/**
	 * Gets the model format version.
	 * 
	 * @return the model format version
	 */
	public String getModelFormatVersion() {
		return modelFormatVersion;
	}

	/**
	 * Sets the model format version.
	 * 
	 * @param modelFormatVersion the new model format version
	 */
	public void setModelFormatVersion(String modelFormatVersion) {
		this.modelFormatVersion = modelFormatVersion;
	}
}