package org.lexgrid.valuesets.sourceasserted.impl;

import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexgrid.valuesets.sourceasserted.SourceAssertedValueSetService;

public class SourceAssertedValuesSetServiceImpl implements SourceAssertedValueSetService {
	
	LexBIGService svc;

	public SourceAssertedValuesSetServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<CodingScheme> listAllSourceAssertedValueSets() throws LBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CodingScheme> getMinimalSourceAssertedValueSetSchemes() throws LBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CodingScheme> getSourceAssertedValueSetsForConceptReference(ConceptReference ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodingScheme getSourceAssertedValueSetForValueSetURI(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResolvedConceptReferenceList getSourceAssertedValueSetEntitiesForURI(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResolvedConceptReferencesIterator getSourceAssertedValueSetIteratorForURI(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CodingScheme> getSourceAssertedValueSetforEntityCode(String matchCode, String assertedRelation)
			throws LBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AbsoluteCodingSchemeVersionReference> getSourceAssertedValueSetsforTextSearch(String matchText,
			MatchAlgorithm matchType) throws LBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbsoluteCodingSchemeVersionReferenceList getListOfCodingSchemeVersionsUsedInResolution(CodingScheme cs) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the svc
	 */
	public LexBIGService getSvc() {
		if(svc == null)
		{return LexBIGServiceImpl.defaultInstance();}
		else{return svc;}
	}

	/**
	 * @param svc the svc to set
	 */
	public void setSvc(LexBIGService svc) {
		this.svc = svc;
	}
	

}
