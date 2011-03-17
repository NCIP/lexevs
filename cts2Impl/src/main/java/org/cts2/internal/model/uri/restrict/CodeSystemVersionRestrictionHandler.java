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
package org.cts2.internal.model.uri.restrict;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.cts2.internal.match.AttributeResolver;
import org.cts2.internal.match.ResolvableModelAttributeReference;

/**
 * The Class CodeSystemVersionRestrictionHandler.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodeSystemVersionRestrictionHandler extends AbstractIterableLexEvsBackedRestrictionHandler<CodingSchemeRendering>{
	
	/** The lex evs identity converter. */
	private LexEvsIdentityConverter lexEvsIdentityConverter;

	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.AbstractIterableLexEvsBackedRestrictionHandler#registerSupportedModelAttributes()
	 */
	@Override
	public List<ResolvableModelAttributeReference<CodingSchemeRendering>> registerSupportedModelAttributes() {
		List<ResolvableModelAttributeReference<CodingSchemeRendering>> returnList = 
			new ArrayList<ResolvableModelAttributeReference<CodingSchemeRendering>>();
		
		ResolvableModelAttributeReference<CodingSchemeRendering> codeSystmeVersionName = 
			new ResolvableModelAttributeReference<CodingSchemeRendering>(new CodeSystemVersionNameAttributeResolver());
		
		returnList.add(codeSystmeVersionName);
		
		return returnList;
	}
	
	/**
	 * The Class CodeSystemVersionNameAttributeResolver.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class CodeSystemVersionNameAttributeResolver implements AttributeResolver<CodingSchemeRendering> {

		/* (non-Javadoc)
		 * @see org.cts2.internal.match.AttributeResolver#resolveAttribute(java.lang.Object)
		 */
		@Override
		public String resolveAttribute(
				CodingSchemeRendering modelObject) {
			
			return lexEvsIdentityConverter.codingSchemeSummaryToCodeSystemVersionName(modelObject.getCodingSchemeSummary());
		}
	}
}
