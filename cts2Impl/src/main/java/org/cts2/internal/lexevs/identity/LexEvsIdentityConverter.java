package org.cts2.internal.lexevs.identity;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.cts2.service.core.NameOrURI;

public interface LexEvsIdentityConverter {

	/**
	 * Name or uri to absolute coding scheme version reference.
	 *
	 * @param nameOrUri the name or uri
	 * @return the absolute coding scheme version reference
	 */
	public abstract AbsoluteCodingSchemeVersionReference nameOrUriToAbsoluteCodingSchemeVersionReference(
			NameOrURI nameOrUri);

	/**
	 * Code system version name to coding scheme reference.
	 *
	 * @param codeSystemVersionName the code system version name
	 * @return the absolute coding scheme version reference
	 */
	public abstract AbsoluteCodingSchemeVersionReference codeSystemVersionNameToCodingSchemeReference(
			String codeSystemVersionName);

	/**
	 * Coding scheme reference to code system version name.
	 *
	 * @param ref the ref
	 * @return the string
	 */
	public abstract String codingSchemeReferenceToCodeSystemVersionName(
			AbsoluteCodingSchemeVersionReference ref);

	/**
	 * Code system version document uri to coding scheme reference.
	 *
	 * @param codeSystemVersionDocumentUri the code system version document uri
	 * @return the absolute coding scheme version reference
	 */
	public abstract AbsoluteCodingSchemeVersionReference codeSystemVersionDocumentUriToCodingSchemeReference(
			String codeSystemVersionDocumentUri);

}