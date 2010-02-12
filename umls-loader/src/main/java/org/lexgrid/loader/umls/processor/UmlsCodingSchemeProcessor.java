/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexgrid.loader.umls.processor;

import java.util.Map;

import org.LexGrid.persistence.model.CodingScheme;
import org.lexgrid.loader.dao.SupportedAttributeSupport;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.rrf.model.Mrsab;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class UmlsCodingSchemeProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsCodingSchemeProcessor extends SupportedAttributeSupport implements ItemProcessor<Mrsab,CodingScheme>{
	
	/** The iso map. */
	private Map<String,String> isoMap;
	
	/** The coding scheme name setter. */
	private CodingSchemeIdSetter codingSchemeIdSetter;
	
	/** The sab. */
	private String sab;
	
	/** The found. */
	boolean found = false;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public CodingScheme process(Mrsab mrsab) throws Exception {
		if(processRow(mrsab)){
			CodingScheme cs = new CodingScheme();
			cs.setCodingSchemeName(codingSchemeIdSetter.getCodingSchemeId());
			cs.setCodingSchemeUri(isoMap.get(sab));
			cs.setFormalName(mrsab.getSon());
			cs.setDefaultLanguage(mrsab.getLat());
			cs.setRepresentsVersion(mrsab.getSver());
			cs.setEntityDescription(mrsab.getScit());
			cs.setCopyright(mrsab.getScc());
			cs.setIsActive(true);

			getSupportedAttributeTemplate().addSupportedCodingScheme(cs.getCodingSchemeId(), 
					cs.getCodingSchemeId(), cs.getCodingSchemeUri(), cs.getFormalName(), false);

			return cs;
		} else {
			return null;
		}
	}
	
	/**
	 * Process row.
	 * 
	 * @param mrsab the mrsab
	 * 
	 * @return true, if successful
	 */
	protected boolean processRow(Mrsab mrsab){
		if(mrsab.getRsab().equals(sab) && !found){
			found = true;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the iso map.
	 * 
	 * @return the iso map
	 */
	public Map<String, String> getIsoMap() {
		return isoMap;
	}

	/**
	 * Sets the iso map.
	 * 
	 * @param isoMap the iso map
	 */
	public void setIsoMap(Map<String, String> isoMap) {
		this.isoMap = isoMap;
	}

	/**
	 * Gets the coding scheme name setter.
	 * 
	 * @return the coding scheme name setter
	 */
	public CodingSchemeIdSetter getCodingSchemeNameSetter() {
		return codingSchemeIdSetter;
	}

	/**
	 * Sets the coding scheme name setter.
	 * 
	 * @param codingSchemeIdSetter the new coding scheme name setter
	 */
	public void setCodingSchemeNameSetter(
			CodingSchemeIdSetter codingSchemeIdSetter) {
		this.codingSchemeIdSetter = codingSchemeIdSetter;
	}

	/**
	 * Gets the sab.
	 * 
	 * @return the sab
	 */
	public String getSab() {
		return sab;
	}

	/**
	 * Sets the sab.
	 * 
	 * @param sab the new sab
	 */
	public void setSab(String sab) {
		this.sab = sab;
	}
}