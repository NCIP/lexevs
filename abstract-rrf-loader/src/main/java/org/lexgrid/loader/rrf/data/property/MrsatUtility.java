
package org.lexgrid.loader.rrf.data.property;

import java.util.Map;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexgrid.loader.rrf.factory.MrsatUsageFactory.MrsatPropertyTypes;
import org.lexgrid.loader.rrf.model.Mrsat;

/**
 * The Interface MrsatUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface MrsatUtility {


	/**
	 * Gets the property name.
	 * 
	 * @param mrsat the mrsat
	 * 
	 * @return the property name
	 */
	public String getPropertyName(Mrsat mrsat);
	
	/**
	 * Gets the property type.
	 * 
	 * @param mrsat the mrsat
	 * 
	 * @return the property type
	 */
	public String getPropertyType(Mrsat mrsat);
	
	/**
	 * To skip.
	 * 
	 * @param mrsat the mrsat
	 * 
	 * @return true, if successful
	 */
	public boolean toSkip(Mrsat mrsat);
}