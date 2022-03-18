
package org.lexgrid.loader.rrf.staging;

import java.util.List;

import org.lexgrid.loader.rrf.staging.model.CodeSabPair;
import org.lexgrid.loader.wrappers.CodeCodingSchemePair;

/**
 * The Interface MrconsoStagingDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface MrconsoStagingDao {

	/**
	 * Gets the code and sab.
	 * 
	 * @param cui the cui
	 * @param aui the aui
	 * 
	 * @return the code and sab
	 */
	public CodeSabPair getCodeAndSab(String cui, String aui);
	
	/**
	 * Gets the code and coding scheme.
	 * 
	 * @param cui the cui
	 * @param aui the aui
	 * 
	 * @return the code and coding scheme
	 */
	public CodeCodingSchemePair getCodeAndCodingScheme(String cui, String aui);
	
	/**
	 * Gets the codes.
	 * 
	 * @param cui the cui
	 * @param sab the sab
	 * 
	 * @return the codes
	 */
	public List<String> getCodes(String cui, String sab);
	
	/**
	 * Gets the code from aui.
	 * 
	 * @param aui the aui
	 * 
	 * @return the code from aui
	 */
	public String getCodeFromAui(String aui);
	
	/**
	 * Gets the cui from aui and sab.
	 * 
	 * @param aui the aui
	 * @param sab the sab
	 * 
	 * @return the cui from aui and sab
	 */
	public String getCuiFromAuiAndSab(String aui, String sab);
	
	public String getCuiFromAui(String aui);
	
	public List<String> getCuisFromCode(String code);
	
	public List<String> getCodesFromCui(String cui);
}