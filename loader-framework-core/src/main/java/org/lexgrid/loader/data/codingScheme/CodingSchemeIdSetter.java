
package org.lexgrid.loader.data.codingScheme;

/**
 * The Interface CodingSchemeIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodingSchemeIdSetter {

	/**
	 * Gets the coding scheme name.
	 * 
	 * @return the coding scheme name
	 */
	public String getCodingSchemeName();
	
	public String getCodingSchemeUri();
	
	public String getCodingSchemeVersion();
}