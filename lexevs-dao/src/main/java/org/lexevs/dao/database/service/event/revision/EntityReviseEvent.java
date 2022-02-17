
package org.lexevs.dao.database.service.event.revision;

import org.LexGrid.concepts.Entity;

/**
 * The Class EntityReviseEvent.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityReviseEvent extends ReviseEvent<Entity>{
	
	/** The coding scheme uri. */
	private String codingSchemeUri;
	
	/** The coding scheme version. */
	private String codingSchemeVersion;
	
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
	 * Gets the coding scheme version.
	 * 
	 * @return the coding scheme version
	 */
	public String getCodingSchemeVersion() {
		return codingSchemeVersion;
	}
	
	/**
	 * Sets the coding scheme version.
	 * 
	 * @param codingSchemeVersion the new coding scheme version
	 */
	public void setCodingSchemeVersion(String codingSchemeVersion) {
		this.codingSchemeVersion = codingSchemeVersion;
	}
}