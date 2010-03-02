package org.lexevs.dao.index.version;

import org.springframework.util.Assert;

public class LexEvsIndexFormatVersion {

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
	
	public boolean isEqualVersion(LexEvsIndexFormatVersion otherVersion){
		return this.getModelFormatVersion().equals(otherVersion.getModelFormatVersion());
	}
	
	public boolean equals(Object obj){
		if(obj != null && obj instanceof LexEvsIndexFormatVersion){
			return isEqualVersion((LexEvsIndexFormatVersion) obj);
		} else {
			return false;
		}
	}
	
	public String toString(){
		return "Index format version: " + this.modelFormatVersion;
	}

	public String getModelFormatVersion() {
		return modelFormatVersion;
	}

	public void setModelFormatVersion(String modelFormatVersion) {
		this.modelFormatVersion = modelFormatVersion;
	}
}
