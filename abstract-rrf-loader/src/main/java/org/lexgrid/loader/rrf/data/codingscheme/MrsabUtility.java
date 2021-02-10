
package org.lexgrid.loader.rrf.data.codingscheme;

import org.lexgrid.loader.rrf.model.Mrsab;

/**
 * The Interface MrsabUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface MrsabUtility {

	/**
	 * Gets the coding scheme name from sab.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * 
	 * @return the coding scheme name from sab
	 */
	public String getCodingSchemeNameFromSab(String sab);
	
	/**
	 * Gets the mrsab row from rsab.
	 * 
	 * @param rsab the rsab
	 * 
	 * @return the mrsab row from rsab
	 */
	public Mrsab getMrsabRowFromRsab(String rsab);
	
	
	public String getCodingSchemeVersionFromSab(String sab);
}