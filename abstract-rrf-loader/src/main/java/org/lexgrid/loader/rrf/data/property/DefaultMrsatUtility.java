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
 * The Class DefaultMrsatUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultMrsatUtility implements MrsatUtility {
	
	/** The mrsat map. */
	private Map<String,MrsatPropertyTypes> mrsatMap;

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.property.MrsatUtility#getPropertyName(org.lexgrid.loader.rrf.model.Mrsat)
	 */
	public String getPropertyName(Mrsat mrsat){
		String type =  mrsat.getAtn();
		if(type.equals(MrsatPropertyTypes.PRESENTATION)){
			return SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION;
		} 
		if(type.equals(MrsatPropertyTypes.COMMENT)){
			return SQLTableConstants.TBLCOLVAL_COMMENT;
		}
		return type;
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.property.MrsatUtility#getPropertyType(org.lexgrid.loader.rrf.model.Mrsat)
	 */
	public String getPropertyType(Mrsat mrsat){
		String type =  mrsat.getAtn();
		if(type.equals(MrsatPropertyTypes.PRESENTATION)){
			return SQLTableConstants.TBLCOLVAL_PRESENTATION;
		} 
		if(type.equals(MrsatPropertyTypes.COMMENT)){
			return SQLTableConstants.TBLCOLVAL_COMMENT;
		}
		return SQLTableConstants.TBLCOLVAL_PROPERTY;
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.property.MrsatUtility#toSkip(org.lexgrid.loader.rrf.model.Mrsat)
	 */
	public boolean toSkip(Mrsat mrsat) {
		String prop = mrsat.getAtn();
		if(mrsatMap.containsKey(prop)){
			if(mrsatMap.get(prop).equals(MrsatPropertyTypes.SKIP)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the mrsat map.
	 * 
	 * @return the mrsat map
	 */
	public Map<String, MrsatPropertyTypes> getMrsatMap() {
		return mrsatMap;
	}

	/**
	 * Sets the mrsat map.
	 * 
	 * @param mrsatMap the mrsat map
	 */
	public void setMrsatMap(Map<String, MrsatPropertyTypes> mrsatMap) {
		this.mrsatMap = mrsatMap;
	}
}