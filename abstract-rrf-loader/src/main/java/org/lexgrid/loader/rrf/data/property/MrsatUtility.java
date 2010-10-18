/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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