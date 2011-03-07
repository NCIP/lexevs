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
package org.cts2.internal.lexevs.identity;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.apache.commons.lang.StringUtils;
import org.cts2.service.core.NameOrURI;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.CodingSchemeAliasHolder;
import org.lexevs.system.service.SystemResourceService.CodingSchemeMatcher;

/**
 * The Class LexEvsIdentityConverter.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultLexEvsIdentityConverter implements LexEvsIdentityConverter {
	
	/** The DEFAUL t_ nam e_ conca t_ string. */
	public static String DEFAULT_NAME_CONCAT_STRING = "_";
	
	/** The DEFAUL t_ ur i_ conca t_ string. */
	public static String DEFAULT_URI_CONCAT_STRING = "<::>";
	
	/** The name concat string. */
	private String nameConcatString = DEFAULT_NAME_CONCAT_STRING;
	
	/** The uri concat string. */
	private String uriConcatString = DEFAULT_URI_CONCAT_STRING;
	
	/** The lex big service. */
	public LexBIGService lexBigService;

	
	/* (non-Javadoc)
	 * @see org.cts2.internal.lexevs.identity.LexEvsIdentityConverter#nameOrUriToAbsoluteCodingSchemeVersionReference(org.cts2.service.core.NameOrURI)
	 */
	@Override
	public AbsoluteCodingSchemeVersionReference nameOrUriToAbsoluteCodingSchemeVersionReference(NameOrURI nameOrUri){
		if(StringUtils.isNotBlank(nameOrUri.getName())){
			return this.codeSystemVersionNameToCodingSchemeReference(nameOrUri.getName());
		} else {
			return this.codeSystemVersionDocumentUriToCodingSchemeReference(nameOrUri.getUri());
		}
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.lexevs.identity.LexEvsIdentityConverter#codeSystemVersionNameToCodingSchemeReference(java.lang.String)
	 */
	@Override
	public AbsoluteCodingSchemeVersionReference
		codeSystemVersionNameToCodingSchemeReference(String codeSystemVersionName){
		
		return this.getCodeSystemVersionNameMatch(codeSystemVersionName);
	}
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.lexevs.identity.LexEvsIdentityConverter#codingSchemeReferenceToCodeSystemVersionName(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
	 */
	@Override
	public String
		codingSchemeReferenceToCodeSystemVersionName(AbsoluteCodingSchemeVersionReference ref){
		String codingSchemeName;
		try {
			codingSchemeName = ServiceUtility.getCodingSchemeName(ref.getCodingSchemeURN(), ref.getCodingSchemeVersion());
		} catch (LBParameterException e) {
			throw new RuntimeException(e);
		}
		return this.constructCodeSystemVersionName(codingSchemeName, ref.getCodingSchemeVersion());
	}
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.lexevs.identity.LexEvsIdentityConverter#codeSystemVersionDocumentUriToCodingSchemeReference(java.lang.String)
	 */
	@Override
	public AbsoluteCodingSchemeVersionReference 
		codeSystemVersionDocumentUriToCodingSchemeReference(String codeSystemVersionDocumentUri){
		
		return this.getCodeSystemVersionDocumentUriMatch(codeSystemVersionDocumentUri);
	}
	
	/**
	 * Gets the code system version name match.
	 *
	 * @param codeSystemVersionLocalName the code system version local name
	 * @return the code system version name match
	 */
	protected AbsoluteCodingSchemeVersionReference getCodeSystemVersionNameMatch(final String codeSystemVersionLocalName){
		List<AbsoluteCodingSchemeVersionReference> result = 
			LexEvsServiceLocator.getInstance().
				getSystemResourceService().
					getMatchingCodingSchemeResources(new CodingSchemeMatcher(){

			@Override
			public boolean isMatch(CodingSchemeAliasHolder aliasHolder) {
				return StringUtils.equals(codeSystemVersionLocalName, 
						constructCodeSystemVersionName(
								aliasHolder.getCodingSchemeName(), 
								aliasHolder.getRepresentsVersion()));
			}
		});
		
		if(result.size() != 1){
			throw new RuntimeException("Error -- waiting for real CTS2 exceptions...");
		}
		
		return result.get(0);
	}
	
	/**
	 * Gets the code system version document uri match.
	 *
	 * @param documentUri the document uri
	 * @return the code system version document uri match
	 */
	protected AbsoluteCodingSchemeVersionReference getCodeSystemVersionDocumentUriMatch(final String documentUri){
		List<AbsoluteCodingSchemeVersionReference> result = 
			LexEvsServiceLocator.getInstance().
				getSystemResourceService().
					getMatchingCodingSchemeResources(new CodingSchemeMatcher(){

			@Override
			public boolean isMatch(CodingSchemeAliasHolder aliasHolder) {
				return StringUtils.equals(documentUri, 
						constructCodeSystemVersionDocumentUri(
								aliasHolder.getCodingSchemeName(), 
								aliasHolder.getRepresentsVersion()));
			}
		});
		
		if(result.size() != 1){
			throw new RuntimeException("Error -- waiting for real CTS2 exceptions...");
		}
		
		return result.get(0);
	}
	
	/**
	 * Construct code system version name.
	 *
	 * @param codingSchemeName the coding scheme name
	 * @param version the version
	 * @return the string
	 */
	protected String constructCodeSystemVersionName(String codingSchemeName, String version){
		return codingSchemeName + nameConcatString + version;
	}
	
	/**
	 * Construct code system version document uri.
	 *
	 * @param uri the uri
	 * @param version the version
	 * @return the string
	 */
	protected String constructCodeSystemVersionDocumentUri(String uri, String version){
		return uri + uriConcatString + version;
	}

	/**
	 * Gets the name concat string.
	 *
	 * @return the name concat string
	 */
	public String getNameConcatString() {
		return nameConcatString;
	}

	/**
	 * Sets the name concat string.
	 *
	 * @param nameConcatString the new name concat string
	 */
	public void setNameConcatString(String nameConcatString) {
		this.nameConcatString = nameConcatString;
	}

	/**
	 * Gets the uri concat string.
	 *
	 * @return the uri concat string
	 */
	public String getUriConcatString() {
		return uriConcatString;
	}

	/**
	 * Sets the uri concat string.
	 *
	 * @param uriConcatString the new uri concat string
	 */
	public void setUriConcatString(String uriConcatString) {
		this.uriConcatString = uriConcatString;
	}

	/**
	 * Gets the lex big service.
	 *
	 * @return the lex big service
	 */
	public LexBIGService getLexBigService() {
		return lexBigService;
	}

	/**
	 * Sets the lex big service.
	 *
	 * @param lexBigService the new lex big service
	 */
	public void setLexBigService(LexBIGService lexBigService) {
		this.lexBigService = lexBigService;
	}
}
