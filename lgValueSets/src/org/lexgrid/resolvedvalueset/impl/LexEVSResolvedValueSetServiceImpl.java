package org.lexgrid.resolvedvalueset.impl;

import java.net.URI;
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
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;

public class LexEVSResolvedValueSetServiceImpl implements LexEVSResolvedValueSetService {
	private transient LexBIGService lbs;
	
	public LexEVSResolvedValueSetServiceImpl(){
		//local constructor
	}
	
	public LexEVSResolvedValueSetServiceImpl(LexBIGService lbs){
		this.lbs = lbs;
	}
	
	@Override
	public List<CodingScheme> listAllResolvedValueSets() throws LBException {
		LexBIGService lbs= getLexBIGService();
		List<CodingScheme> resolvedValueSetList = new ArrayList<CodingScheme>();
		List<CodingScheme> minSchemeList = lbs.getMinimalResolvedCodingSchemes();
		minSchemeList.stream().forEach(x -> {
			try {
				resolvedValueSetList.add(
						lbs.resolveCodingScheme(x.getCodingSchemeURI(), 
								Constructors.createCodingSchemeVersionOrTagFromVersion(x.getRepresentsVersion())));
			} catch (LBException e) {
				throw new RuntimeException("Problem resolving a Value Set Coding Scheme", e);
			}
		});
        return resolvedValueSetList;
	}

	@Override
	public List<CodingScheme> getMinimalResolvedValueSetSchemes() throws LBException {    
        return lbs.getMinimalResolvedCodingSchemes() ;
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
		List<CodingScheme> allRVSSchemes;
		try {
			allRVSSchemes = listAllResolvedValueSets();
		} catch (LBException e) {
			throw new RuntimeException("There was a problem retrieving resolved values sets" + e);
		}
		
		
		for(CodingScheme cs : allRVSSchemes){
			String codingScheme = cs.getCodingSchemeURI();
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			lbs = getLexBIGService();
			try {
				CodedNodeSet cns = lbs.getCodingSchemeConcepts(codingScheme, versionOrTag);
				if(cns.isCodeInSet(ref)){
					filteredSchemes.add(cs);
				}
			} catch (LBException e) {
				throw new RuntimeException("There was a problem retreiving entries from this resolved value set"
				+ cs.getCodingSchemeName() + " " + cs.getRepresentsVersion() + e);
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
		SearchExtension search = (SearchExtension) lbs.getGenericExtension("SearchExtension");
		Set<CodingSchemeReference> refs = getReferenceForSchemes(this.getMinimalResolvedValueSetSchemes());
		ResolvedConceptReferencesIterator itr = search.search(matchCode, refs, MatchAlgorithm.CODE_EXACT);
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
			scheme = lbs.resolveCodingScheme(uri.toString(), null);
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
			CodedNodeSet set = lbs.getCodingSchemeConcepts(uri, null);
			list = set.resolveToList(null, null, null, -1);
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
			if (prop.getPropertyName().equalsIgnoreCase(LexEVSValueSetDefinitionServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION)) {
				return true;
			}

		}
		return false;
	}
	
    private boolean isValueSet(ResolvedConceptReference ref) throws LBException
    {
        CodingScheme scheme = lbs.resolveCodingScheme(ref.getCodingSchemeURI(), 
                Constructors.createCodingSchemeVersionOrTagFromVersion(ref.getCodingSchemeVersion()));
        Properties props = scheme.getProperties();
        return props.getPropertyAsReference().stream().filter(x -> x.getPropertyName().equals
        		("ontologyFormat")).filter(x->x.getValue().getContent().equals("RESOLVEDVALUESET")).
        		findAny().isPresent();
        
    }
    
    public Set<CodingSchemeReference> getReferenceForSchemes(List<CodingScheme> schemes){
    	return schemes.parallelStream().map(x -> {
    		CodingSchemeReference ref = new CodingSchemeReference();
    		ref.setCodingScheme(x.getCodingSchemeURI());
    		ref.setVersionOrTag(Constructors.createCodingSchemeVersionOrTagFromVersion(x.getRepresentsVersion()));
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
