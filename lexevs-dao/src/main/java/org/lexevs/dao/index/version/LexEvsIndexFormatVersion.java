
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