package org.lexgrid.resolvedvalueset.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.codingSchemes.CodingScheme;
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
		List<CodingScheme> resolvedValueSetList= new ArrayList<CodingScheme>();
		CodingSchemeRenderingList schemes = lbs.getSupportedCodingSchemes();
        
        for (CodingSchemeRendering csr: schemes.getCodingSchemeRendering()) {
        	CodingScheme cs= getResolvedCodingScheme(csr);
        	if (isResolvedValueSetCodingScheme(cs) ) {
        	    resolvedValueSetList.add(cs);
        	}
        }
        
        return resolvedValueSetList;
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
