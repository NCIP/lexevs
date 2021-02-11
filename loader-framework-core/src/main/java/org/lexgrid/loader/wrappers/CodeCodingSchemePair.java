
package org.lexgrid.loader.wrappers;

import java.io.Serializable;

/**
 * The Class CodeCodingSchemePair.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodeCodingSchemePair implements Serializable {
	
	/** The code. */
	private String code;
	
	/** The coding scheme. */
	private String codingScheme;
	
	public CodeCodingSchemePair(){}
	
	public CodeCodingSchemePair(String code, String codingScheme){
		this.code = code;
		this.codingScheme = codingScheme;
	}
	
	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Sets the code.
	 * 
	 * @param code the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * Gets the coding scheme.
	 * 
	 * @return the coding scheme
	 */
	public String getCodingScheme() {
		return codingScheme;
	}
	
	/**
	 * Sets the coding scheme.
	 * 
	 * @param codingScheme the new coding scheme
	 */
	public void setCodingScheme(String codingScheme) {
		this.codingScheme = codingScheme;
	}	
}