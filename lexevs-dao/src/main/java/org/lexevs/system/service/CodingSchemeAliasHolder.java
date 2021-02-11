
package org.lexevs.system.service;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class CodingSchemeAliasHolder.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeAliasHolder {
	
	/** The coding scheme name. */
	private String codingSchemeName;
	
	/** The coding scheme uri. */
	private String codingSchemeUri;
	
	/** The represents version. */
	private String representsVersion;
	
	/** The formal name. */
	private String formalName;
	
	/** The local names. */
	private List<String> localNames = new ArrayList<String>();
	
	/**
	 * Gets the coding scheme name.
	 * 
	 * @return the coding scheme name
	 */
	public String getCodingSchemeName() {
		return codingSchemeName;
	}
	
	/**
	 * Sets the coding scheme name.
	 * 
	 * @param codingSchemeName the new coding scheme name
	 */
	public void setCodingSchemeName(String codingSchemeName) {
		this.codingSchemeName = codingSchemeName;
	}
	
	/**
	 * Gets the coding scheme uri.
	 * 
	 * @return the coding scheme uri
	 */
	public String getCodingSchemeUri() {
		return codingSchemeUri;
	}
	
	/**
	 * Sets the coding scheme uri.
	 * 
	 * @param codingSchemeUri the new coding scheme uri
	 */
	public void setCodingSchemeUri(String codingSchemeUri) {
		this.codingSchemeUri = codingSchemeUri;
	}
	
	/**
	 * Gets the represents version.
	 * 
	 * @return the represents version
	 */
	public String getRepresentsVersion() {
		return representsVersion;
	}
	
	/**
	 * Sets the represents version.
	 * 
	 * @param representsVersion the new represents version
	 */
	public void setRepresentsVersion(String representsVersion) {
		this.representsVersion = representsVersion;
	}
	
	/**
	 * Gets the formal name.
	 * 
	 * @return the formal name
	 */
	public String getFormalName() {
		return formalName;
	}
	
	/**
	 * Sets the formal name.
	 * 
	 * @param formalName the new formal name
	 */
	public void setFormalName(String formalName) {
		this.formalName = formalName;
	}
	
	/**
	 * Gets the local names.
	 * 
	 * @return the local names
	 */
	public List<String> getLocalNames() {
		return localNames;
	}
	
	/**
	 * Sets the local names.
	 * 
	 * @param localNames the new local names
	 */
	public void setLocalNames(List<String> localNames) {
		this.localNames = localNames;
	}
}