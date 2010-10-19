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
package org.lexgrid.loader.umls.processor;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.lexevs.dao.database.utility.DaoUtility;
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
	
	/** The coding scheme name setter. */
	private CodingSchemeIdSetter codingSchemeIdSetter;
	
	private String prefix;
	
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
			cs.setCodingSchemeName(codingSchemeIdSetter.getCodingSchemeName());
			cs.setCodingSchemeURI(codingSchemeIdSetter.getCodingSchemeUri());
			cs.setFormalName(mrsab.getSon());
			cs.setDefaultLanguage(mrsab.getLat());
			cs.setRepresentsVersion(codingSchemeIdSetter.getCodingSchemeVersion());
			
			EntityDescription ed = new EntityDescription();
			ed.setContent(mrsab.getScit());
			cs.setEntityDescription(ed);
			cs.setCopyright(DaoUtility.createText(mrsab.getScc()));
			cs.setIsActive(true);
			
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

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
}