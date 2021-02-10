
package org.lexgrid.loader.umls.data.codingscheme;

import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;

/**
 * The Interface CodingSchemeSabNameSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodingSchemeSabNameSetter extends CodingSchemeIdSetter {
	
	/**
	 * Gets the sab.
	 * 
	 * @return the sab
	 */
	public String getSab();

}