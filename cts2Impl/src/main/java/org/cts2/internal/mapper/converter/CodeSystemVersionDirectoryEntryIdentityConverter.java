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

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.cts2.codesystemversion.CodeSystemVersionDirectoryEntry;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.dozer.DozerConverter;

/**
 * The Class DefinitionOperatorToSetOperatorConverter.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodeSystemVersionDirectoryEntryIdentityConverter extends DozerConverter<CodingSchemeRendering,CodeSystemVersionDirectoryEntry> {

	private LexEvsIdentityConverter lexEvsIdentityConverter;
	
	public CodeSystemVersionDirectoryEntryIdentityConverter() {
		super(CodingSchemeRendering.class, CodeSystemVersionDirectoryEntry.class);
	}

	@Override
	public CodingSchemeRendering convertFrom(CodeSystemVersionDirectoryEntry codeSystemVersionDirectoryEntry,
			CodingSchemeRendering codingSchemeRendering) {
		return null;
	}

	@Override
	public CodeSystemVersionDirectoryEntry convertTo(CodingSchemeRendering codingSchemeRendering,
			CodeSystemVersionDirectoryEntry codeSystemVersionDirectoryEntry) {
		
		String codeSystemVersionDirectoryEntryName = this.lexEvsIdentityConverter.
			codingSchemeReferenceToCodeSystemVersionName(
					Constructors.createAbsoluteCodingSchemeVersionReference(codingSchemeRendering.getCodingSchemeSummary()));
		
		String codeSystemVersionDocumentUri = this.lexEvsIdentityConverter.
			codingSchemeReferenceToCodeSystemVersionDocumentUri(
					Constructors.createAbsoluteCodingSchemeVersionReference(codingSchemeRendering.getCodingSchemeSummary()));
		
		codeSystemVersionDirectoryEntry.setCodeSystemVersionName(codeSystemVersionDirectoryEntryName);
		codeSystemVersionDirectoryEntry.setResourceName(codeSystemVersionDirectoryEntryName);
		codeSystemVersionDirectoryEntry.setDocumentURI(codeSystemVersionDocumentUri);
		codeSystemVersionDirectoryEntry.setResourceID(codeSystemVersionDocumentUri);
		
		return codeSystemVersionDirectoryEntry;
	}

	public LexEvsIdentityConverter getLexEvsIdentityConverter() {
		return lexEvsIdentityConverter;
	}

	public void setLexEvsIdentityConverter(
			LexEvsIdentityConverter lexEvsIdentityConverter) {
		this.lexEvsIdentityConverter = lexEvsIdentityConverter;
	}
}