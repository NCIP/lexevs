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
package org.lexgrid.loader.meta.data.codingscheme;

import java.util.Map;
import java.util.Properties;

import org.lexgrid.loader.constants.LoaderConstants;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.rrf.data.codingscheme.MrsabUtility;
import org.springframework.beans.factory.InitializingBean;

/**
 * The Class MetaCodingSchemeIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaCodingSchemeIdSetter implements CodingSchemeIdSetter, InitializingBean  {

	private static String META_SAB = "NCIMTH";
	
	/** The coding scheme properties. */
	private Properties codingSchemeProperties;
	
	private MrsabUtility mrsabUtility;
	
	private String codingSchemeVersion;
	
	private String codingSchemeUri;
	
	private Map<String,String> isoMap;

	
	@Override
	public void afterPropertiesSet() throws Exception {
		codingSchemeVersion = mrsabUtility.getMrsabRowFromRsab(META_SAB).getImeta();
		codingSchemeUri = isoMap.get(codingSchemeProperties.getProperty(LoaderConstants.CODING_SCHEME_NAME_PROPERTY));
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter#getCodingSchemeName()
	 */
	public String getCodingSchemeName() {
		return codingSchemeProperties.getProperty(LoaderConstants.CODING_SCHEME_NAME_PROPERTY);
	}

	public String getCodingSchemeUri() {
		return codingSchemeUri;
	}

	public String getCodingSchemeVersion() {
		return this.codingSchemeVersion;
	}
	
	/**
	 * Gets the coding scheme properties.
	 * 
	 * @return the coding scheme properties
	 */
	public Properties getCodingSchemeProperties() {
		return codingSchemeProperties;
	}

	/**
	 * Sets the coding scheme properties.
	 * 
	 * @param codingSchemeProperties the new coding scheme properties
	 */
	public void setCodingSchemeProperties(Properties codingSchemeProperties) {
		this.codingSchemeProperties = codingSchemeProperties;
	}

	public MrsabUtility getMrsabUtility() {
		return mrsabUtility;
	}

	public void setMrsabUtility(MrsabUtility mrsabUtility) {
		this.mrsabUtility = mrsabUtility;
	}

	public Map<String, String> getIsoMap() {
		return isoMap;
	}

	public void setIsoMap(Map<String, String> isoMap) {
		this.isoMap = isoMap;
	}
}
