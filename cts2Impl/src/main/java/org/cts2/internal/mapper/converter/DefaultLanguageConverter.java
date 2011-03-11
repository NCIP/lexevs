/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.internal.mapper.converter;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedLanguage;
import org.LexGrid.naming.URIMap;
import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.core.LanguageReference;
import org.dozer.DozerConverter;
import org.lexevs.dao.database.utility.DaoUtility;

/**
 * The Class DefinitionOperatorToSetOperatorConverter.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultLanguageConverter extends DozerConverter<CodingScheme,CodeSystemVersion> {

	public DefaultLanguageConverter() {
		super(CodingScheme.class, CodeSystemVersion.class);
	}

	@Override
	public CodingScheme convertFrom(CodeSystemVersion csv, CodingScheme cs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodeSystemVersion convertTo(CodingScheme cs, CodeSystemVersion csv) {
		String language = cs.getDefaultLanguage();
		
		if(language == null){
			return csv;
		}
		
		if(csv == null){
			csv = new CodeSystemVersion();
		}

		try {
			URIMap uriMap = 
				DaoUtility.getURIMap(cs, SupportedLanguage.class, language);

			if(uriMap == null){
				return csv;
			}

			LanguageReference languageRef = new LanguageReference();
			languageRef.setContent(uriMap.getLocalId());
			languageRef.setHref(uriMap.getUri());

			languageRef.setContent(language);

			csv.setDefaultLanguage(languageRef);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} 

		return csv;
	}
}
