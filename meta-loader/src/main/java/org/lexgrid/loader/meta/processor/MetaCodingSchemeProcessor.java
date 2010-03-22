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
package org.lexgrid.loader.meta.processor;

import java.util.Map;
import java.util.Properties;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.constants.LoaderConstants;
import org.lexgrid.loader.dao.SupportedAttributeSupport;
import org.lexgrid.loader.rrf.model.Mrdoc;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class MetaCodingSchemeProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaCodingSchemeProcessor extends SupportedAttributeSupport implements ItemProcessor<Mrdoc, CodingScheme> {
	
	/** The iso map. */
	private Map<String,String> isoMap;
	
	/** The coding scheme properties. */
	private Properties codingSchemeProperties;
	
	/** The DOCKE y_ value. */
	private static String DOCKEY_VALUE = "RELEASE";
	
	/** The VALU e_ value. */
	private static String VALUE_VALUE = "umls.release.name";
	
	/** The TYP e_ value. */
	private static String TYPE_VALUE = "release_info";
		
	
	/**
	 * |DOCKEY |      VALUE      |    TYPE    | EXPL |
	 * |RELEASE|umls.release.name|release_info|200808|
	 * 
	 * @param mrdoc the mrdoc
	 * 
	 * @return the coding scheme
	 * 
	 * @throws Exception the exception
	 */
	public CodingScheme process(Mrdoc mrdoc) throws Exception {
		if(processLine(mrdoc)){
			CodingScheme cs = new CodingScheme();
			cs.setCodingSchemeName(codingSchemeProperties.getProperty(LoaderConstants.CODING_SCHEME_NAME_PROPERTY));
			cs.setRepresentsVersion(getVersion(mrdoc));
			cs.setCodingSchemeURI(isoMap.get(codingSchemeProperties.getProperty(LoaderConstants.CODING_SCHEME_NAME_PROPERTY)));
			cs.setFormalName(codingSchemeProperties.getProperty(LoaderConstants.FORMAL_NAME_PROPERTY));
			cs.setDefaultLanguage(codingSchemeProperties.getProperty(LoaderConstants.DEFAULT_LANGUAGE_PROPERTY));
			cs.setCopyright(DaoUtility.createText(codingSchemeProperties.getProperty(LoaderConstants.COPYRIGHT_PROPERTY)));
			
			EntityDescription ed = new EntityDescription();
			ed.setContent(codingSchemeProperties.getProperty(LoaderConstants.ENTITY_DESCRIPTION_PROPERTY));
			cs.setEntityDescription(ed);
			cs.setIsActive(true);
			
			return cs;
		} 
		return null;
	}
	
	/**
	 * Process line.
	 * 
	 * @param mrdoc the mrdoc
	 * 
	 * @return true, if successful
	 */
	protected boolean processLine(Mrdoc mrdoc){
		return mrdoc.getDockey().equals(DOCKEY_VALUE) &&
			mrdoc.getValue().equals(VALUE_VALUE) &&
			mrdoc.getType().equals(TYPE_VALUE);
	}
	
	/**
	 * Gets the version.
	 * 
	 * @param mrdoc the mrdoc
	 * 
	 * @return the version
	 */
	protected String getVersion(Mrdoc mrdoc){
		return mrdoc.getExpl();
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
}
