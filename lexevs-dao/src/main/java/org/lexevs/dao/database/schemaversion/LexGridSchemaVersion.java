
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