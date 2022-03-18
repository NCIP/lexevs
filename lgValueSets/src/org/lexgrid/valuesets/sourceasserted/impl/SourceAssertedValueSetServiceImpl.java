
package org.lexgrid.valuesets.sourceasserted.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
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
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetService;
import org.lexevs.dao.database.service.valuesets.ValueSetHierarchyService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.valuesets.sourceasserted.SourceAssertedValueSetService;

public class SourceAssertedValueSetServiceImpl implements SourceAssertedValueSetService {
	
	private static final long serialVersionUID = 7172345965840590934L;
	private transient LexBIGService svc;
	AssertedValueSetParameters params;
	
	public SourceAssertedValueSetServiceImpl() {
		//Spring Required Constructor
	}

	private SourceAssertedValueSetServiceImpl(AssertedValueSetParameters params) {
		this.params = params;
		svc = LexBIGServiceImpl.defaultInstance();
	}
	
	private SourceAssertedValueSetServiceImpl(AssertedValueSetParameters params, LexBIGService lbsvc) {
		this.params = params;
		svc = lbsvc;
	}
	
	public void init(AssertedValueSetParameters params) {
		this.params = params;
	}
	
	public static SourceAssertedValueSetService getDefaultValueSetServiceForVersion(AssertedValueSetParameters params){
		return new SourceAssertedValueSetServiceImpl(params);
	}
	
	public static SourceAssertedValueSetServiceImpl getDefaultValueSetServiceForVersion(
			AssertedValueSetParameters params, LexBIGService lbs) {
		return new SourceAssertedValueSetServiceImpl(params, lbs);
	}

	@Override
	public List<CodingScheme> listAllSourceAssertedValueSets() throws LBException {
		List<String> list = getSourceAssertedValueSetTopNodesForRootCode(params.getRootConcept());
		return list.stream().map(code ->
			{List<CodingScheme> schemes = null;
				try {
					schemes = getSourceAssertedValueSetforTopNodeEntityCode(code);
				} catch (LBException e) {
					throw new RuntimeException("Mapping value set root code: " + code + " failed");
				}
				return schemes;
			}).collect(ArrayList::new, List::addAll, List::addAll);
	
	}

	@Override
	public List<CodingScheme> getMinimalSourceAssertedValueSetSchemes() throws LBException {
		List<CodingScheme> assertedVS = listAllSourceAssertedValueSets();
		assertedVS.addAll(getSvc().getMinimalResolvedVSCodingSchemes());
		return assertedVS;
	}

	@Override
	public List<CodingScheme> getSourceAssertedValueSetsForConceptReference(ConceptReference ref) {
		try {
			return getSourceAssertedValueSetforValueSetMemberEntityCode(ref.getCode());
		} catch (LBException e) {
			throw new RuntimeException("Problem getting value set for code: " +  ref.getCode());
		}
	}
	
	@Override
	public List<String> getSourceAssertedValueSetTopNodesForRootCode(String rootCode){
		return getAssertedValueSetService().getAllValidValueSetTopNodeCodes();	
	}

	@Override
	public CodingScheme getSourceAssertedValueSetForValueSetURI(URI uri) throws LBException {;
		
		List<CodingScheme> codingSchemes = getSourceAssertedValueSetforTopNodeEntityCode(
				AssertedValueSetServices.getConceptCodeForURI(uri));
		if (codingSchemes == null){
			return null;
		}
		CodingScheme codingSchemeMatch = null;
		
		for (Iterator iterator = codingSchemes.iterator(); iterator.hasNext();) {
			CodingScheme codingScheme = (CodingScheme) iterator.next();
			if (codingScheme.getCodingSchemeURI().equals(uri.toString())) {
				codingSchemeMatch = codingScheme;
				break;
			}
		}
		
		return codingSchemeMatch;
	}

	@Override
	public ResolvedConceptReferenceList getSourceAssertedValueSetEntitiesForURI(final String uri) {


		List<Entity> entities = null;
		try {
			entities = getAssertedValueSetService().getSourceAssertedValueSetEntitiesForEntityCode(
					AssertedValueSetServices.getConceptCodeForURI(new URI(uri)));
		} catch (URISyntaxException e) {
			throw new RuntimeException("Uri is not properly constructed: " + e);
		}
		
		ResolvedConceptReferenceList referenceList = new ResolvedConceptReferenceList();
		entities.stream().forEach(x -> {
			try {
				referenceList.addResolvedConceptReference(transformEntityToRCR(getTopNodeEntityFromURI(new URI(uri)), x));
			} catch (IndexOutOfBoundsException | LBException | URISyntaxException e) {
				throw new RuntimeException("Problems getting Asserted Value Set entities for URI: " + e);
			}
		});
		return referenceList;
	}

	private Entity getTopNodeEntityFromURI(URI uri) throws LBException {
		return getAssertedValueSetService()
				.getEntityforTopNodeEntityCode(AssertedValueSetServices.getConceptCodeForURI(uri));
	}

	private ResolvedConceptReference transformEntityToRCR(Entity topNode, Entity entity) {
		return AssertedValueSetServices.transformEntityToRCR(topNode, entity, params);
	}
	
	public String getEntityCodeFromValueSetDefinition(String uri) {
		ValueSetDefinition vsDef = null;
		try {
			vsDef = getDatabaseServiceManager().getValueSetDefinitionService().
					getValueSetDefinitionByUri(new URI(uri));
		} catch (URISyntaxException e) {
				throw new RuntimeException("URI not properly formatted: " + uri);
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
 		return new AssertedValueSetResolvedConceptReferenceIterator(code, params);
	}

	@Override
	public List<CodingScheme> getSourceAssertedValueSetforTopNodeEntityCode(String matchCode)
			throws LBException {
		return getAssertedValueSetService().getSourceAssertedValueSetforTopNodeEntityCode(matchCode);
	}
	
	@Override
	public CodingScheme listResolvedValueSetForDescription(String description) throws LBException {
		return getAssertedValueSetService().getSourceAssertedValueSetforDescription(description);
	}
	
	@Override
	public List<CodingScheme> getSourceAssertedValueSetforValueSetMemberEntityCode(String matchCode)
			throws LBException {
		return getAssertedValueSetService().getSourceAssertedValueSetforMemberEntityCode(matchCode);
	}

	@Override
	public List<AbsoluteCodingSchemeVersionReference> getSourceAssertedValueSetsforTextSearch(String matchText,
			MatchAlgorithm matchType) throws LBException {
		SourceAssertedValueSetSearchExtensionImpl saVSSearch =  (SourceAssertedValueSetSearchExtensionImpl) 
				getSvc().getGenericExtension("AssertedValueSetSearchExtension");
		ResolvedConceptReferencesIterator itr = saVSSearch.search(matchText, matchType);
		List<AbsoluteCodingSchemeVersionReference> list = new ArrayList<AbsoluteCodingSchemeVersionReference>();
		while(itr.hasNext()) {
			ResolvedConceptReference ref = itr.next();
			List<CodingScheme> schemes = getSourceAssertedValueSetforValueSetMemberEntityCode(ref.getCode());
			schemes.stream().map(scheme -> 
			Constructors.createAbsoluteCodingSchemeVersionReference(
					scheme.getCodingSchemeURI(), scheme.getRepresentsVersion())).forEachOrdered(list::add);
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
		List<String> roots = this.getSourceAssertedValueSetTopNodesForRootCode(params.getRootConcept());
		Comparator<Entity> entityCompare = Comparator.comparing(Entity::getEntityCode); 
		Set<Entity> entitySet = new TreeSet<Entity>(entityCompare);
		for(String matchCode: roots) {
			List<Entity> temp = getAssertedValueSetService().getSourceAssertedValueSetEntitiesForEntityCode(matchCode);
			entitySet.addAll(temp);
		}
		return entitySet.stream().collect(Collectors.toList());
	}

	public LexBIGService getSvc() {
		if(svc == null)
		{return LexBIGServiceImpl.defaultInstance();}
		else{return svc;}
	}

	public void setSvc(LexBIGService svc) {
		this.svc = svc;
	}
	
	
	private DatabaseServiceManager getDatabaseServiceManager() {
		return LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
	}
	
	private AssertedValueSetService getAssertedValueSetService() {
		AssertedValueSetService assVSDS = getDatabaseServiceManager().getAssertedValueSetService();
		assVSDS.init(this.params);
		return assVSDS;
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
			e.printStackTrace();
		}
		System.out.println("name :" + scheme.getCodingSchemeName());
		}
		scheme.getEntities().getEntityAsReference().stream().forEach(x-> System.out.println(x.getEntityCode() + " : " + x.getEntityDescription().getContent()));
	}


}