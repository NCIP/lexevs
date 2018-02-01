package org.lexgrid.valuesets.sourceasserted.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search.SourceAssertedValueSetSearchExtensionImpl;
import org.LexGrid.LexBIG.Impl.helpers.Transformer;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.service.entity.SourceAssertedValueSetEntityServiceImpl;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetService;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetServiceImpl;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.dao.database.service.valuesets.ValueSetHierarchyService;
import org.lexevs.dao.database.service.valuesets.VersionableEventValueSetDefinitionService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;
import org.lexgrid.valuesets.sourceasserted.SourceAssertedValueSetService;

import com.thoughtworks.xstream.mapper.Mapper;

public class SourceAssertedValueSetServiceImpl implements SourceAssertedValueSetService {
	
	LexBIGService svc;
	ValueSetDefinitionService vsds;
	AssertedValueSetService assVSSvc;
	AssertedValueSetParameters params;

	private SourceAssertedValueSetServiceImpl(AssertedValueSetParameters params) {
		this.params = params;
		assVSSvc = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAssertedValueSetService();
		assVSSvc.init(params);
		svc = LexBIGServiceImpl.defaultInstance();
		vsds = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetDefinitionService();
	}
	
	public static SourceAssertedValueSetService getDefaultValueSetServiceForVersion(AssertedValueSetParameters params){
		return new SourceAssertedValueSetServiceImpl(params);
	}

	@Override
	public List<CodingScheme> listAllSourceAssertedValueSets() throws LBException {
		List<String> list = ((SourceAssertedValueSetServiceImpl) SourceAssertedValueSetServiceImpl.
				getDefaultValueSetServiceForVersion(new AssertedValueSetParameters.Builder(params.getCodingSchemeVersion()).build())).
				getSourceAssertedValueSetTopNodesForRootCode(ValueSetHierarchyService.ROOT_CODE);
		return list.stream().map(code ->
			{CodingScheme scheme = null;
				try {
					scheme = getSourceAssertedValueSetforEntityCode(code).get(0);
				} catch (LBException e) {
					throw new RuntimeException("Mapping value set root code: " + code + " failed");
				}
				return scheme;
			}).collect(Collectors.toList());
	
	}

	@Override
	public List<CodingScheme> getMinimalSourceAssertedValueSetSchemes() throws LBException {
		return svc.getMinimalResolvedVSCodingSchemes();
	}

	@Override
	public List<CodingScheme> getSourceAssertedValueSetsForConceptReference(ConceptReference ref) {
		try {
			return getSourceAssertedValueSetforEntityCode(ref.getCode());
		} catch (LBException e) {
			throw new RuntimeException("Problem getting value set for code: " +  ref.getCode());
		}
	}
	
	@Override
	public List<String> getSourceAssertedValueSetTopNodesForRootCode(String rootCode){
		return assVSSvc.getAllValueSetTopNodeCodes(rootCode);	
	}

	@Override
	public CodingScheme getSourceAssertedValueSetForValueSetURI(URI uri) throws LBException {;
		return getSourceAssertedValueSetforEntityCode(
				AssertedValueSetServices.getConceptCodeForURI(uri)).get(0);
	}

	@Override
	public ResolvedConceptReferenceList getSourceAssertedValueSetEntitiesForURI(String uri) {
		
		List<Entity> entities = null;
		try {
			entities = assVSSvc.getSourceAssertedValueSetEntitiesForEntityCode(
					AssertedValueSetServices.getConceptCodeForURI(new URI(uri)));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResolvedConceptReferenceList referenceList = new ResolvedConceptReferenceList();
		entities.stream().forEach(x -> referenceList.addResolvedConceptReference(transformEntityToRCR(x, uri)));
		return referenceList;
	}

	private ResolvedConceptReference transformEntityToRCR(Entity x, String uri) {
		ResolvedConceptReference ref = new ResolvedConceptReference();
		ref.setCode(x.getEntityCode());
		ref.setCodeNamespace(x.getEntityCodeNamespace());
		ref.setCodingSchemeName(x.getEntityCodeNamespace());
		ref.setCodingSchemeURI(uri);
		ref.setCodingSchemeVersion(params.getCodingSchemeVersion());
		ref.setConceptCode(ref.getCode());
		ref.setCode(ref.getCode());
		ref.setEntity(x);
		ref.setEntityDescription(x.getEntityDescription());
		ref.setEntityType(x.getEntityType());
		return ref;
	}
	
	public String getEntityCodeFromValueSetDefinition(String uri) {
		ValueSetDefinition vsDef = null;
		try {
			vsDef = vsds.getValueSetDefinitionByUri(new URI(uri));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(vsDef == null || 
				vsDef.getDefinitionEntry(0) == null || 
				vsDef.getDefinitionEntry(0).getEntityReference() == null ||
				vsDef.getDefinitionEntry(0).getEntityReference().getEntityCode() == null
				) {
			throw new RuntimeException("ValueSet Definition does not contain adequate idenitfying information to resolve value set: "
					+ uri);
		}
		else {
		return vsDef.getDefinitionEntry(0).getEntityReference().getEntityCode();
		}
	}

	@Override
	public ResolvedConceptReferencesIterator getSourceAssertedValueSetIteratorForURI(String uri) {
		String code = getEntityCodeFromValueSetDefinition(uri);
		//TODO Implement with full query path to source asserted iterator.
//		return new AssertedValueSetResolvedConceptReferenceIterator(code, params);
		return null;
	}

	@Override
	public List<CodingScheme> getSourceAssertedValueSetforEntityCode(String matchCode)
			throws LBException {
		return assVSSvc.getSourceAssertedValueSetforEntityCode(matchCode);
	}

	@Override
	public List<AbsoluteCodingSchemeVersionReference> getSourceAssertedValueSetsforTextSearch(String matchText,
			MatchAlgorithm matchType) throws LBException {
		SourceAssertedValueSetSearchExtensionImpl saVSSearch =  (SourceAssertedValueSetSearchExtensionImpl) 
				svc.getGenericExtension("AssertedValueSetSearchService");
		ResolvedConceptReferencesIterator itr = saVSSearch.search(matchText, matchType);
		List<AbsoluteCodingSchemeVersionReference> list = new ArrayList<AbsoluteCodingSchemeVersionReference>();
		while(itr.hasNext()) {
			ResolvedConceptReference ref = itr.next();
			ref.getCodingSchemeURI();
			params.getCodingSchemeVersion();
			list.add(Constructors.
					createAbsoluteCodingSchemeVersionReference(
							ref.getCodingSchemeURI(), params.getCodingSchemeVersion()));
		}
		return list;
	}

	@Override
	public AbsoluteCodingSchemeVersionReferenceList getListOfCodingSchemeVersionsUsedInResolution(CodingScheme cs) {
		AbsoluteCodingSchemeVersionReferenceList list = new AbsoluteCodingSchemeVersionReferenceList();
		list.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(
				params.getCodingSchemeURI(), params.getCodingSchemeVersion()));
		return list;
	}
	
	@Override
	public List<Entity> getAllSourceAssertedValueSetEntities() {
		List<String> roots = this.getSourceAssertedValueSetTopNodesForRootCode(ValueSetHierarchyService.ROOT_CODE);
		Comparator<Entity> entityCompare = Comparator.comparing(Entity::getEntityCode); 
		Set<Entity> entitySet = new TreeSet<Entity>(entityCompare);
		for(String matchCode: roots) {
			List<Entity> temp = assVSSvc.getSourceAssertedValueSetEntitiesForEntityCode(matchCode);
			entitySet.addAll(temp);
		}
		return Arrays.asList((Entity[])entitySet.toArray());
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
		List<String> list = ((SourceAssertedValueSetServiceImpl) SourceAssertedValueSetServiceImpl.
				getDefaultValueSetServiceForVersion(new AssertedValueSetParameters.Builder("17.08d").build())).
				getSourceAssertedValueSetTopNodesForRootCode(ValueSetHierarchyService.ROOT_CODE);
		CodingScheme scheme = null;
		
		for(String s: list){
		try {
			scheme = SourceAssertedValueSetServiceImpl.getDefaultValueSetServiceForVersion(
					new AssertedValueSetParameters.Builder("17.08d").build()).
					getSourceAssertedValueSetForValueSetURI(new URI(AssertedValueSetParameters.ROOT_URI + s));
		} catch (LBException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("name :" + scheme.getCodingSchemeName());
		}
		scheme.getEntities().getEntityAsReference().stream().forEach(x-> System.out.println(x.getEntityCode() + " : " + x.getEntityDescription().getContent()));
	}


	

}
