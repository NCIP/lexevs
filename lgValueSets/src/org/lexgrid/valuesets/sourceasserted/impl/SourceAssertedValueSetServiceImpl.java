package org.lexgrid.valuesets.sourceasserted.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetService;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetServiceImpl;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.valuesets.sourceasserted.SourceAssertedValueSetService;

public class SourceAssertedValueSetServiceImpl implements SourceAssertedValueSetService {
	
	LexBIGService svc;
	AssertedValueSetService assVSSvc;
	AssertedValueSetParameters params;

	private SourceAssertedValueSetServiceImpl(String version, 
			String codingSchemeURI, 
			String association, 
			String publishName,
			String publishValue) {
		params = new AssertedValueSetParameters.Builder(version).codingSchemeURI(codingSchemeURI).
				assertedValueSetRelation(association).publishName(publishName).publishValue(publishValue).build();
		svc = getSvc();
		assVSSvc = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAssertedValueSetService();
		((AssertedValueSetServiceImpl) assVSSvc).init();
	}
	
	public static SourceAssertedValueSetService getDefaultValueSetServiceForVersion(String version){
		return new SourceAssertedValueSetServiceImpl(version, 
				AssertedValueSetParameters.DEFAULT_CODINGSCHEME_URI,
				AssertedValueSetParameters.ASSERTED_VALUESET_RELATION,
				AssertedValueSetParameters.DEFAULT_DO_PUBLISH_NAME,
				AssertedValueSetParameters.DEFAULT_DO_PUBLISH_VALUE);
	}
	
	public SourceAssertedValueSetService getDefaultValueSetServiceForVersionAndRelation(String version, 
			String association){
	return new SourceAssertedValueSetServiceImpl(version, 
			AssertedValueSetParameters.DEFAULT_CODINGSCHEME_URI,
			association,
			AssertedValueSetParameters.DEFAULT_DO_PUBLISH_NAME,
			AssertedValueSetParameters.DEFAULT_DO_PUBLISH_VALUE);
	}
	
	public SourceAssertedValueSetService getDefaultValueSetServiceForCodingSchemeAndRelation(String version, 
			String codingSchemeURI,
			String association){
	return new SourceAssertedValueSetServiceImpl(version, 
			codingSchemeURI,
			association,
			AssertedValueSetParameters.DEFAULT_DO_PUBLISH_NAME,
			AssertedValueSetParameters.DEFAULT_DO_PUBLISH_VALUE);
	}
	
	public SourceAssertedValueSetService getDefaultValueSetServiceForCodingSchemeWithRelationAndPublishValues(
			String version, 
			String codingSchemeURI,
			String association,
			String publishName,
			String publishValue){
	return new SourceAssertedValueSetServiceImpl(version, 
			codingSchemeURI,
			association,
			publishName,
			publishValue);
	}
	
	

	@Override
	public List<CodingScheme> listAllSourceAssertedValueSets() throws LBException {
		return null;
	}

	@Override
	public List<CodingScheme> getMinimalSourceAssertedValueSetSchemes() throws LBException {
		// return svc.getSourceAssertedResolvedVSCodingSchemes(); nope
		return null;
	}

	@Override
	public List<CodingScheme> getSourceAssertedValueSetsForConceptReference(ConceptReference ref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodingScheme getSourceAssertedValueSetForValueSetURI(URI uri) throws LBException {
		getSourceAssertedValueSetforEntityCode(AssertedValueSetServices.getConceptCodeForURI(uri), 
				params.getAssertedValueSetRelation());
		return getSourceAssertedValueSetforEntityCode(
				AssertedValueSetServices.getConceptCodeForURI(uri), 
				params.getAssertedValueSetRelation()).get(0);
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
		return assVSSvc.getSourceAssertedValueSetforEntityCode(matchCode, assertedRelation, 
				params.getPublishName(), params.getPublishValue(),
				params.getCodingSchemeVersion(), params.getCodingSchemeURI());
	}

	@Override
	public List<AbsoluteCodingSchemeVersionReference> getSourceAssertedValueSetsforTextSearch(String matchText,
			MatchAlgorithm matchType) throws LBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbsoluteCodingSchemeVersionReferenceList getListOfCodingSchemeVersionsUsedInResolution(CodingScheme cs) {
		AbsoluteCodingSchemeVersionReferenceList list = new AbsoluteCodingSchemeVersionReferenceList();
		list.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(
				params.getCodingSchemeURI(), params.getCodingSchemeVersion()));
		return list;
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
	
	public static void main(String[] args){
		CodingScheme scheme = null;
		try {
			scheme = SourceAssertedValueSetServiceImpl.getDefaultValueSetServiceForVersion("17.08d").getSourceAssertedValueSetForValueSetURI(new URI(AssertedValueSetParameters.ROOT_URI + "C128784"));
		} catch (LBException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("name :" + scheme.getCodingSchemeName());
		scheme.getEntities().getEntityAsReference().stream().forEach(x-> System.out.println(x.getEntityCode() + " : " + x.getEntityDescription().getContent()));
	}
	

}
