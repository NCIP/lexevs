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
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.apache.commons.logging.LogFactory;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.sourceasserted.impl.SourceAssertedValueSetServiceImpl;

public class LexEVSResolvedValueSetServiceImpl implements LexEVSResolvedValueSetService {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5438158832122711604L;
	private transient LexBIGService lbs;
	private transient SourceAssertedValueSetServiceImpl vsSvc;
	private AssertedValueSetParameters params;
	
	public LexEVSResolvedValueSetServiceImpl(){
//		lbs = LexBIGServiceImpl.defaultInstance();
//		try {
//			vsSvc = (SourceAssertedValueSetServiceImpl) SourceAssertedValueSetServiceImpl.
//					getDefaultValueSetServiceForVersion(new AssertedValueSetParameters.Builder(
//							lbs.resolveCodingScheme(AssertedValueSetParameters.DEFAULT_CODINGSCHEME_URI, null).
//							getRepresentsVersion()).build());
//		} catch (LBException e) {
//			LogFactory.getLog(LexEVSResolvedValueSetServiceImpl.class).
//			warn("Could not find value sets for default asserted value set scheme: " + e);
//		}
	}
	
	public LexEVSResolvedValueSetServiceImpl(AssertedValueSetParameters  params){
		vsSvc = (SourceAssertedValueSetServiceImpl) SourceAssertedValueSetServiceImpl.
				getDefaultValueSetServiceForVersion(params);
	}
	
	public LexEVSResolvedValueSetServiceImpl(LexBIGService lbs){
		this.lbs = lbs;
		vsSvc = (SourceAssertedValueSetServiceImpl) SourceAssertedValueSetServiceImpl.
				getDefaultValueSetServiceForVersion(params);
	}
	
	public LexEVSResolvedValueSetServiceImpl(LexBIGService lbs, AssertedValueSetParameters params){
		this.lbs = lbs;
		vsSvc = (SourceAssertedValueSetServiceImpl) SourceAssertedValueSetServiceImpl.
				getDefaultValueSetServiceForVersion(params, lbs);
	}
	
	@Override
	public List<CodingScheme> listAllResolvedValueSets() throws LBException {
//		LexBIGService lbs= getLexBIGService();
		List<CodingScheme> minSchemeList = lbs.getMinimalResolvedVSCodingSchemes();
		List<CodingScheme> assertVSList = new ArrayList<CodingScheme>();
		if(vsSvc != null) {
		assertVSList = vsSvc.listAllSourceAssertedValueSets();}
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
		if(vsSvc == null)
        {return lbs.getMinimalResolvedVSCodingSchemes();}
		else {
		return vsSvc.getMinimalSourceAssertedValueSetSchemes();
		}
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
//		lbs = getLexBIGService();
		try {
			allRVSSchemes = listAllResolvedValueSets();
		} catch (LBException e) {
			throw new RuntimeException("There was a problem retrieving resolved values sets" + e);
		}

		for (CodingScheme cs : allRVSSchemes) {
			String uri = cs.getCodingSchemeURI();
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();

			try {
				CodingScheme scheme = null;
				if(vsSvc != null) {
				 scheme = vsSvc.getSourceAssertedValueSetForValueSetURI(new URI(uri));
				}

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
		SourceAssertedValueSetSearchExtension assertedValueSetSearch = (SourceAssertedValueSetSearchExtension) lbs.getGenericExtension("AssertedValueSetSearchExtension");
		Set<CodingSchemeReference> refs = getReferenceForSchemes(this.getMinimalResolvedValueSetSchemes());
		ResolvedConceptReferencesIterator itr = search.search(matchText, refs,matchType);
		ResolvedConceptReferencesIterator asVsItr = assertedValueSetSearch.search(matchText, matchType);
		while(asVsItr.hasNext()) {
		ResolvedConceptReference ref = asVsItr.next();
		if(vsSvc != null) {
			List<CodingScheme> schemes = vsSvc.getSourceAssertedValueSetsForConceptReference(ref);
			List<AbsoluteCodingSchemeVersionReference> mappedList = schemes.stream().map(toRef ->
					Constructors.createAbsoluteCodingSchemeVersionReference(
							toRef.getCodingSchemeURI(), toRef.getRepresentsVersion())).
				collect(Collectors.toList());
			list.addAll(mappedList);
			}
		}
		while(itr.hasNext()){
			ResolvedConceptReference ref = itr.next();
			list.add(Constructors.createAbsoluteCodingSchemeVersionReference(ref.getCodingSchemeURI(), 
					ref.getCodingSchemeVersion()));
		}
		//Clean list of duplicates and return
		return list.stream().map(
				refer-> refer.getCodingSchemeURN()).distinct().map(
				uri -> list.stream().filter(
				uriPick -> uriPick.getCodingSchemeURN() == uri ).
				findAny().get()).
				collect(Collectors.toList());
	}
	
	public List<AbsoluteCodingSchemeVersionReference> getResolvedValueSetsforEntityCode(String matchCode) throws LBException{
		
		List<AbsoluteCodingSchemeVersionReference> list = new ArrayList<AbsoluteCodingSchemeVersionReference>();
		//Set up search for regular code system based concept references
		SearchExtension search = (SearchExtension) lbs.getGenericExtension("SearchExtension");
		//Get a complete set of references for both kinds of value sets
		Set<CodingSchemeReference> refs = getReferenceForSchemes(this.getMinimalResolvedValueSetSchemes());
		//Searching on all references for code system type search
		ResolvedConceptReferencesIterator itr = search.search(matchCode, refs, MatchAlgorithm.CODE_EXACT);
		//Searching source asserted value set index and adding results to list
		List<CodingScheme> schemes = null;
			if(vsSvc != null) {
				schemes = vsSvc.getSourceAssertedValueSetforValueSetMemberEntityCode(matchCode);
				List<AbsoluteCodingSchemeVersionReference> tempList = schemes.stream().map(scheme -> 
					Constructors.createAbsoluteCodingSchemeVersionReference(
							scheme.getCodingSchemeURI(), scheme.getRepresentsVersion())).
						collect(Collectors.toList());
				list.addAll(tempList);}

			while(itr.hasNext()){
				ResolvedConceptReference ref = itr.next();
				list.add(Constructors.createAbsoluteCodingSchemeVersionReference(ref.getCodingSchemeURI(), 
						ref.getCodingSchemeVersion()));
			}
			//removing duplicates
			return list.stream().map(
					refer-> refer.getCodingSchemeURN()).distinct().map(
					uri -> list.stream().filter(
					uriPick -> uriPick.getCodingSchemeURN() == uri ).
					findAny().get()).
					collect(Collectors.toList());
	}
	
	public CodingScheme getResolvedValueSetForValueSetURI(URI uri){
//		LexBIGService lbs = getLexBIGService();
		CodingScheme scheme = null;
		try {
			if(vsSvc != null) {
			scheme = vsSvc.getSourceAssertedValueSetForValueSetURI(uri);
			}
			if(scheme == null) {
			scheme = lbs.resolveCodingScheme(uri.toString(), null);
			}
		} catch (LBException e) {
				throw new RuntimeException("There was a problem retrieving the designated Resolved Value Set: " + uri.toString() + e);
		}
		return scheme;
	}
	
	CodingScheme getResolvedCodingScheme(CodingSchemeRendering csr) throws LBException {
//		LexBIGService lbs= getLexBIGService();
		CodingSchemeSummary css= csr.getCodingSchemeSummary();
		String codingSchemeURI= css.getCodingSchemeURI();
    	String version = css.getRepresentsVersion();
    	CodingSchemeVersionOrTag csvt= Constructors.createCodingSchemeVersionOrTagFromVersion(version);
		CodingScheme cs= lbs.resolveCodingScheme(codingSchemeURI, csvt);
		return cs;
		
	}
	
	public ResolvedConceptReferenceList getValueSetEntitiesForURI(String uri){
//		LexBIGService lbs = getLexBIGService();
		ResolvedConceptReferenceList list = null;
		try {
			if(vsSvc != null) {
			list = vsSvc.getSourceAssertedValueSetEntitiesForURI(uri);
			}
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
//		LexBIGService lbs = getLexBIGService();
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
    Set<CodingSchemeReference> refs = schemes.parallelStream().map(x -> {
    		CodingSchemeReference ref = new CodingSchemeReference();
    		ref.setCodingScheme(x.getCodingSchemeURI());
    		ref.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromVersion(x.getRepresentsVersion()));
    		return ref;
    	}).collect(Collectors.toSet());
    Set<CodingSchemeReference> assertedRefs = getSourceAssertedReferenceForSchemes(schemes);
    return refs.stream().filter(scheme -> !assertedRefs.contains(scheme)).collect(Collectors.toSet());
    }
    
    private Set<CodingSchemeReference> getSourceAssertedReferenceForSchemes(List<CodingScheme> schemes){
    return schemes.stream().map(scheme -> {
    	CodingSchemeReference ref = new CodingSchemeReference();
		try {

			 CodingScheme cs = null;
			if(vsSvc != null) {
			cs = vsSvc.getSourceAssertedValueSetForValueSetURI(
					new URI(scheme.getCodingSchemeURI()));
			}
			if(cs == null) {return null;}
			ref.setCodingScheme(cs.getCodingSchemeURI());
			ref.setVersionOrTag(Constructors.
					createCodingSchemeVersionOrTagFromVersion(cs.getRepresentsVersion()));
		} catch (LBException | URISyntaxException e) {
			throw new RuntimeException("Cannot resolve references");
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
    
	/**
     * Return the associated LexBIGService instance; lazy initialized as
     * required.
     */
    @LgClientSideSafe
    public void setLexBIGService(LexBIGService lbsvc) {
        this.lbs = lbsvc;
    }
    
    
    public void initSourceAssertedValueSet(AssertedValueSetParameters params) {
    		this.params = params;
    		this.vsSvc = SourceAssertedValueSetServiceImpl.getDefaultValueSetServiceForVersion(params, lbs);
    }
    
    
    

}
