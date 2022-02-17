
package org.lexgrid.loader.processor;

import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;

/**
 * The Class CodingSchemeIdAwareProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeIdAwareProcessor {
	
	/** The coding scheme name setter. */
	private CodingSchemeIdSetter codingSchemeIdSetter;

	/**
	 * Gets the coding scheme name setter.
	 * 
	 * @return the coding scheme name setter
	 */
	public CodingSchemeIdSetter getCodingSchemeIdSetter() {
		return codingSchemeIdSetter;
	}

	/**
	 * Sets the coding scheme name setter.
	 * 
	 * @param codingSchemeIdSetter the new coding scheme name setter
	 */
	public void setCodingSchemeIdSetter(
			CodingSchemeIdSetter codingSchemeIdSetter) {
		this.codingSchemeIdSetter = codingSchemeIdSetter;
	}
}