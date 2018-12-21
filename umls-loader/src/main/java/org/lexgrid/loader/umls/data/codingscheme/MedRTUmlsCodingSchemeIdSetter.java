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
package org.lexgrid.loader.umls.data.codingscheme;

import java.util.Map;

import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.rrf.data.codingscheme.MrsabUtility;
import org.springframework.beans.factory.InitializingBean;

/**
 * The Class UmlsCodingSchemeIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MedRTUmlsCodingSchemeIdSetter implements CodingSchemeIdSetter, InitializingBean {

	/** The mrsab utility. */
	private MrsabUtility mrsabUtility;
	
	/** The iso map. */
	private Map<String,String> isoMap;
	
	/** The sab. */
	private String sab;
	
	/** The coding scheme name. */
	private String codingSchemeName;
	
	private String codingSchemeUri;
	
	private String codingSchemeVersion;
	

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		//get the sab
		sab = getSab();
		codingSchemeName = mrsabUtility.getCodingSchemeNameFromSab(sab);
		codingSchemeUri = isoMap.get(sab);
		codingSchemeVersion = mrsabUtility.getCodingSchemeVersionFromSab(sab);
	}

	public String getCodingSchemeName() {
		return codingSchemeName;
	}

	public String getCodingSchemeUri() {
		return codingSchemeUri;
	}

	public String getCodingSchemeVersion() {
		return codingSchemeVersion;
	}

	/**
	 * Gets the mrsab utility.
	 * 
	 * @return the mrsab utility
	 */
	public MrsabUtility getMrsabUtility() {
		return mrsabUtility;
	}

	/**
	 * Sets the mrsab utility.
	 * 
	 * @param mrsabUtility the new mrsab utility
	 */
	public void setMrsabUtility(MrsabUtility mrsabUtility) {
		this.mrsabUtility = mrsabUtility;
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

	public Map<String, String> getIsoMap() {
		return isoMap;
	}

	public void setIsoMap(Map<String, String> isoMap) {
		this.isoMap = isoMap;
	}
	
}