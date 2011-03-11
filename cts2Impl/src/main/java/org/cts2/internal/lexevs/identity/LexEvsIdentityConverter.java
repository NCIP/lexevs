package org.cts2.internal.lexevs.identity;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;

public interface LexEvsIdentityConverter {

	/**
	 * Name or uri to absolute coding scheme version reference.
	 *
	 * @param nameOrUri the name or uri
	 * @return the absolute coding scheme version reference
	 */
	public AbsoluteCodingSchemeVersionReference nameOrUriToAbsoluteCodingSchemeVersionReference(
			NameOrURI nameOrUri);
	
	public ConceptReference entityNameOrUriToConceptReference(
			EntityNameOrURI nameOrUri);

	/**
	 * Code system version name to coding scheme reference.
	 *
	 * @param codeSystemVersionName the code system version name
	 * @return the absolute coding scheme version reference
	 */
	public AbsoluteCodingSchemeVersionReference codeSystemVersionNameToCodingSchemeReference(
			String codeSystemVersionName);

	/**
	 * Coding scheme reference to code system version name.
	 *
	 * @param ref the ref
	 * @return the string
	 */
	public String codingSchemeReferenceToCodeSystemVersionName(
			AbsoluteCodingSchemeVersionReference ref);
	
	public String codingSchemeToCodeSystemVersionName(
			CodingScheme codingScheme);
	
	public String codingSchemeReferenceToCodeSystemVersionDocumentUri(
			AbsoluteCodingSchemeVersionReference ref);

	/**
	 * Code system version document uri to coding scheme reference.
	 *
	 * @param codeSystemVersionDocumentUri the code system version document uri
	 * @return the absolute coding scheme version reference
	 */
	public AbsoluteCodingSchemeVersionReference codeSystemVersionDocumentUriToCodingSchemeReference(
			String codeSystemVersionDocumentUri);
	
	public String codingSchemeToCodeSystemVersionDocumentUri(
			CodingScheme codingScheme);
	
	public String nsUriAndCodeToUri(String nsUri, String code);
}