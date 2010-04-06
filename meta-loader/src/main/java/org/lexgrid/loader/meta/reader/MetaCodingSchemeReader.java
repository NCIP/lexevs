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
package org.lexgrid.loader.meta.reader;

import java.util.Map;
import java.util.Properties;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * NOT THREAD SAFE!.
 */
public class MetaCodingSchemeReader implements ItemReader<CodingScheme>{
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(MetaCodingSchemeReader.class);

	/** The iso map. */
	private Map<String,String> isoMap;
	
	/** The coding scheme properties. */
	private Properties codingSchemeProperties;
	
	/** The CODIN g_ schem e_ name. */
	private static String CODING_SCHEME_NAME = "CodingSchemeName";
	
	/** The FORMA l_ name. */
	private static String FORMAL_NAME = "FormalName";
	
	/** The DEFAUL t_ language. */
	private static String DEFAULT_LANGUAGE = "DefaultLanguage";
	
	/** The COPYRIGHT. */
	private static String COPYRIGHT = "Copyright";
	
	/** The ENTIT y_ description. */
	private static String ENTITY_DESCRIPTION = "EntityDescription";
	
	/** The has been read. */
	private boolean hasBeenRead = false;

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	public CodingScheme read() throws Exception, UnexpectedInputException,
			ParseException {
		if(hasBeenRead == true){
			return null;
		}
		
		CodingScheme cs = new CodingScheme();
		cs.setCodingSchemeName(codingSchemeProperties.getProperty(CODING_SCHEME_NAME));
		cs.setFormalName(codingSchemeProperties.getProperty(FORMAL_NAME));
		cs.setCodingSchemeURI(isoMap.get(codingSchemeProperties.getProperty(CODING_SCHEME_NAME)));
		cs.setDefaultLanguage(codingSchemeProperties.getProperty(DEFAULT_LANGUAGE));
		cs.setCopyright(DaoUtility.createText(codingSchemeProperties.getProperty(COPYRIGHT)));
		
		EntityDescription ed = new EntityDescription();
		ed.setContent(codingSchemeProperties.getProperty(ENTITY_DESCRIPTION));
		cs.setEntityDescription(ed);
		hasBeenRead = true;
		return cs;
	}	
}