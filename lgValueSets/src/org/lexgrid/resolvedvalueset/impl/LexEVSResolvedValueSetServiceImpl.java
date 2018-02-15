package org.lexgrid.resolvedvalueset.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.Extensions.Generic.SourceAssertedValueSetSearchExtension;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.apache.commons.logging.LogFactory;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.sourceasserted.impl.SourceAssertedValueSetServiceImpl;

public class LexEVSResolvedValueSetServiceImpl implements LexEVSResolvedValueSetService {
	private transient LexBIGService lbs;
	private SourceAssertedValueSetServiceImpl vsSvc;
	
	public LexEVSResolvedValueSetServiceImpl(){
		//local constructor
	}
	
	public LexEVSResolvedValueSetServiceImpl(LexBIGService lbs){
		this.lbs = lbs;
		try {
			vsSvc = (SourceAssertedValueSetServiceImpl) SourceAssertedValueSetServiceImpl.
					getDefaultValueSetServiceForVersion(new AssertedValueSetParameters.Builder(
							lbs.resolveCodingScheme(AssertedValueSetParameters.DEFAULT_CODINGSCHEME_URI, null).
							getRepresentsVersion()).build());
		} catch (LBException e) {
			LogFactory.getLog(LexEVSResolvedValueSetServiceImpl.class).
			warn("Could not find value sets for default asserted value set scheme: " + e);
		}
	}
	
	public LexEVSResolvedValueSetServiceImpl(LexBIGService lbs, AssertedValueSetParameters params){
		this.lbs = lbs;
		vsSvc = (SourceAssertedValueSetServiceImpl) SourceAssertedValueSetServiceImpl.
				getDefaultValueSetServiceForVersion(params);
	}
	
	@Override
	public List<CodingScheme> listAllResolvedValueSets() throws LBException {
		LexBIGService lbs= getLexBIGService();
		List<CodingScheme> minSchemeList = lbs.getMinimalResolvedVSCodingSchemes();
		List<CodingScheme> assertVSList = vsSvc.listAllSourceAssertedValueSets();
		assertVSList.addAll(minSchemeList.stream().map(x -> 
						{ CodingScheme cs = null;
							try {
								cs = lbs.resolveCodingScheme(x.getCodingSchemeURI(), 
										Constructors.createCodingSchemeVersionOrTagFromVersion(x.getRepresentsVersion()));
							} catch (LBException e) {
								throw new RuntimeException("Cannot resolve coding scheme " + x.getCodingSchemeURI(), e);
							}
							return cs;
						}).collect(Collectors.toList()));
		return assertVSList;
	}

	@Override
	public List<CodingScheme> getMinimalResolvedValueSetSchemes() throws LBException {    
        //return lbs.getMinimalResolvedVSCodingSchemes() ;
		return vsSvc.getMinimalSourceAssertedValueSetSchemes();
	}
	
	/**
	 * Return a list of AbsoluteCodingSchemeVersionReference that was used for resolving the resolvedValueSet
	 * 
	 * @param codingScheme- The resolvedValueSet CodingScheme
	 * @return AbsoluteCodingSchemeVersionReferenceList list of codingScheme and
	 *         version used for the resolution of the resolvedValueSet
	 * @throws LBException
	 */
	public AbsoluteCodingSchemeVersionReferenceList getListOfCodingSchemeVersionsUsedInResolution(CodingScheme cs) {
		
			AbsoluteCodingSchemeVersionReferenceList acsvrList = new AbsoluteCodingSchemeVersionReferenceList();
			for (Property prop: cs.getProperties().getProperty()) {
				if (prop.getPropertyName() != null && prop.getPropertyName().equalsIgnoreCase(LexEVSValueSetDefinitionServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION) && prop.getValue() != null) {
					AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
					
					   acsvr.setCodingSchemeURN(prop.getValue().getContent());
					
					for (PropertyQualifier pq: prop.getPropertyQualifier()) {
						if (pq.getPropertyQualifierName() != null && pq.getPropertyQualifierName().equalsIgnoreCase(LexEVSValueSetDefinitionServices.VERSION) && pq.getValue() != null) {
							acsvr.setCodingSchemeVersion(pq.getValue().getContent());
						}
					}
					
					acsvrList.addAbsoluteCodingSchemeVersionReference(acsvr);
				}

			}
			
			return acsvrList;
					
	}
	
	
	public List<CodingScheme> getResolvedValueSetsForConceptReference(ConceptReference ref) {
		List<CodingScheme> filteredSchemes = new ArrayList<CodingScheme>();
		List<CodingScheme> allRVSSchemes = null;
		lbs = getLexBIGService();
		try {
			allRVSSchemes = listAllResolvedValueSets();
		} catch (LBException e) {
			throw new RuntimeException("There was a problem retrieving resolved values sets" + e);
		}

		for (CodingScheme cs : allRVSSchemes) {
			String uri = cs.getCodingSchemeURI();
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();

			try {
				CodingScheme scheme = vsSvc.getSourceAssertedValueSetForValueSetURI(new URI(uri));

				if (scheme != null) {
					if (scheme.getEntities().getEntityAsReference().stream()
							.anyMatch(entity -> entity.getEntityCode().equals(ref.getCode()))) {
						filteredSchemes.add(scheme);
					}
				} else {
					CodedNodeSet cns = lbs.getCodingSchemeConcepts(uri, versionOrTag);
					if (cns.isCodeInSet(ref)) {
						filteredSchemes.add(cs);
					}
				}
			} catch (LBException e) {
				throw new RuntimeException("There was a problem retreiving entries from this resolved value set"
						+ cs.getCodingSchemeName() + " " + cs.getRepresentsVersion() + e);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return filteredSchemes;
	}
	
	public List<AbsoluteCodingSchemeVersionReference> getResolvedValueSetsforTextSearch(String matchText, MatchAlgorithm matchType) throws LBException{
		List<AbsoluteCodingSchemeVersionReference> list = new ArrayList<AbsoluteCodingSchemeVersionReference>();
		SearchExtension search = (SearchExtension) lbs.getGenericExtension("SearchExtension");
		Set<CodingSchemeReference> refs = getReferenceForSchemes(this.getMinimalResolvedValueSetSchemes());
		ResolvedConceptReferencesIterator itr = search.search(matchText, refs,matchType);
		while(itr.hasNext()){
			ResolvedConceptReference ref = itr.next();
			list.add(Constructors.createAbsoluteCodingSchemeVersionReference(ref.getCodingSchemeURI(), 
					ref.getCodingSchemeVersion()));
		}
		return list;
	}
	
	public List<AbsoluteCodingSchemeVersionReference> getResolvedValueSetsforEntityCode(String matchCode) throws LBException{
		List<AbsoluteCodingSchemeVersionReference> list = new ArrayList<AbsoluteCodingSchemeVersionReference>();
		SourceAssertedValueSetSearchExtension assertedValueSetSearch = (SourceAssertedValueSetSearchExtension) lbs.getGenericExtension("AssertedValueSetSearchExtension");
		SearchExtension search = (SearchExtension) lbs.getGenericExtension("SearchExtension");
		
		ResolvedConceptReferencesIterator asVsItr = assertedValueSetSearch.search(matchCode, MatchAlgorithm.CODE_EXACT);
		Set<CodingSchemeReference> refs = getReferenceForSchemes(this.getMinimalResolvedValueSetSchemes());
		ResolvedConceptReferencesIterator itr = search.search(matchCode, refs, MatchAlgorithm.CODE_EXACT);
		while(asVsItr.hasNext()) {
			ResolvedConceptReference ref = itr.next();
			list.add(Constructors.createAbsoluteCodingSchemeVersionReference(ref.getCodingSchemeURI(), 
					ref.getCodingSchemeVersion()));
		}
		while(itr.hasNext()){
		ResolvedConceptReference ref = itr.next();
		list.add(Constructors.createAbsoluteCodingSchemeVersionReference(ref.getCodingSchemeURI(), 
				ref.getCodingSchemeVersion()));
		}
		return list;
	}
	
	public CodingScheme getResolvedValueSetForValueSetURI(URI uri){
		LexBIGService lbs = getLexBIGService();
		CodingScheme scheme;
		try {
			scheme = vsSvc.getSourceAssertedValueSetForValueSetURI(uri);
			if(scheme == null) {
			scheme = lbs.resolveCodingScheme(uri.toString(), null);
			}
		} catch (LBException e) {
				throw new RuntimeException("There was a problem retrieving the designated Resolved Value Set: " + uri.toString() + e);
		}
		return scheme;
	}
	
	CodingScheme getResolvedCodingScheme(CodingSchemeRendering csr) throws LBException {
		LexBIGService lbs= getLexBIGService();
		CodingSchemeSummary css= csr.getCodingSchemeSummary();
		String codingSchemeURI= css.getCodingSchemeURI();
    	String version = css.getRepresentsVersion();
    	CodingSchemeVersionOrTag csvt= Constructors.createCodingSchemeVersionOrTagFromVersion(version);
		CodingScheme cs= lbs.resolveCodingScheme(codingSchemeURI, csvt);
		return cs;
		
	}
	
	public ResolvedConceptReferenceList getValueSetEntitiesForURI(String uri){
		LexBIGService lbs = getLexBIGService();
		ResolvedConceptReferenceList list;
		try {
			list = vsSvc.getSourceAssertedValueSetEntitiesForURI(uri);
			if(list == null || list.getResolvedConceptReferenceCount() == 0) {		
			CodedNodeSet set = lbs.getCodingSchemeConcepts(uri, null);
			list = set.resolveToList(null, null, null, -1);
			}
		} catch (LBException e) {
			throw new RuntimeException("There was problem retrieving the entities for resolved value set: " + uri);
		}
		
		return list;
	}
	
	public ResolvedConceptReferencesIterator getValueSetIteratorForURI(String uri){
		LexBIGService lbs = getLexBIGService();
		ResolvedConceptReferencesIterator list;
		try {
			CodedNodeSet set = lbs.getCodingSchemeConcepts(uri, null);
			list = set.resolve(null, null, null);
		} catch (LBException e) {
			throw new RuntimeException("There was problem retrieving the entities for resolved value set: " + uri);
		}
		
		return list;
	}
		
	
	boolean isResolvedValueSetCodingScheme(CodingScheme cs) {
		for (Property prop: cs.getProperties().getProperty()) {
			if (prop.getPropertyName().equalsIgnoreCase(LexEVSValueSetDefinitionServices.
					RESOLVED_AGAINST_CODING_SCHEME_VERSION)) {
				return true;
			}

		}
		return false;
	}
    
    public Set<CodingSchemeReference> getReferenceForSchemes(List<CodingScheme> schemes){
    	
    	return schemes.parallelStream().map(x -> {
    		CodingSchemeReference ref = new CodingSchemeReference();
    		ref.setCodingScheme(x.getCodingSchemeURI());
    		ref.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromVersion(x.getRepresentsVersion()));
    		return ref;
    	}).collect(Collectors.toSet());
    	
    }
    
    private Set<CodingSchemeReference> getSourceAssertedReferenceForSchemes(List<CodingScheme> schemes){
    return schemes.stream().map(scheme -> {
    	CodingSchemeReference ref = null;
		try {
			  ref = new CodingSchemeReference();
			CodingScheme cs = vsSvc.getSourceAssertedValueSetForValueSetURI(
					new URI(scheme.getCodingSchemeURI()));
			ref.setCodingScheme(cs.getCodingSchemeURI());
			ref.setVersionOrTag(Constructors.
					createCodingSchemeVersionOrTagFromVersion(cs.getRepresentsVersion()));
		} catch (LBException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ref;
	}).collect(Collectors.toSet());
    }
	
	/**
     * Return the associated LexBIGService instance; lazy initialized as
     * required.
     */
    @LgClientSideSafe
    public LexBIGService getLexBIGService() {
        if (lbs == null)
            lbs = LexBIGServiceImpl.defaultInstance();
        return lbs;
    }
    
    
    

}
