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
import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.dozer.DozerConverter;

/**
 * The Class DefinitionOperatorToSetOperatorConverter.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodeSystemVersionIdentityConverter extends DozerConverter<CodingScheme,CodeSystemVersion> {

	private LexEvsIdentityConverter lexEvsIdentityConverter;
	
	public CodeSystemVersionIdentityConverter() {
		super(CodingScheme.class, CodeSystemVersion.class);
	}

	@Override
	public CodingScheme convertFrom(CodeSystemVersion csv, CodingScheme cs) {
		//this.lexEvsIdentityConverter.codingSchemeToCodeSystemVersionName(cs);
		return null;
	}

	@Override
	public CodeSystemVersion convertTo(CodingScheme cs, CodeSystemVersion csv) {
		String codeSystemVersionName = 
			this.lexEvsIdentityConverter.codingSchemeToCodeSystemVersionName(cs);
		
		csv.setCodeSystemVersionName(codeSystemVersionName);
		
		return csv;
	}

	public LexEvsIdentityConverter getLexEvsIdentityConverter() {
		return lexEvsIdentityConverter;
	}

	public void setLexEvsIdentityConverter(
			LexEvsIdentityConverter lexEvsIdentityConverter) {
		this.lexEvsIdentityConverter = lexEvsIdentityConverter;
	}

}