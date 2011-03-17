package org.cts2.test;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.codingSchemes.CodingScheme;
import org.cts2.internal.lexevs.identity.LexEvsIdentityConverter;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;

public class LexEvsIdentityConverterNoOp implements LexEvsIdentityConverter {

	@Override
	public AbsoluteCodingSchemeVersionReference nameOrUriToAbsoluteCodingSchemeVersionReference(
			NameOrURI nameOrUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String codingSchemeSummaryToCodeSystemVersionName(
			CodingSchemeSummary codingSchemeSummary) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ConceptReference entityNameOrUriToConceptReference(
			EntityNameOrURI nameOrUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbsoluteCodingSchemeVersionReference codeSystemVersionNameToCodingSchemeReference(
			String codeSystemVersionName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String codingSchemeReferenceToCodeSystemVersionName(
			AbsoluteCodingSchemeVersionReference ref) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String codingSchemeToCodeSystemVersionName(CodingScheme codingScheme) {
		// TODO Auto-generated method stub
		return null;
	}

	public String codingSchemeToCodeSystemVersionDocumentUri(
			CodingScheme codingScheme) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbsoluteCodingSchemeVersionReference codeSystemVersionDocumentUriToCodingSchemeReference(
			String codeSystemVersionDocumentUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String codingSchemeReferenceToCodeSystemVersionDocumentUri(
			AbsoluteCodingSchemeVersionReference ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String nsUriAndCodeToUri(String nsUri, String code) {
		// TODO Auto-generated method stub
		return null;
	}


}
