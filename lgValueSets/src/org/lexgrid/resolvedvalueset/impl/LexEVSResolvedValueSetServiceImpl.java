package org.lexgrid.resolvedvalueset.impl;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;

public class LexEVSResolvedValueSetServiceImpl implements LexEVSResolvedValueSetService {
	private transient LexBIGService lbs;
	
	@Override
	public List<CodingScheme> listAllResolvedValueSets() throws LBException {
		LexBIGService lbs= getLexBIGService();
		List<CodingScheme> resolvedValueSetList= new ArrayList<CodingScheme>();

		List<CodingSchemeRendering> candidateRenderingList= new ArrayList<CodingSchemeRendering>();
		CodingSchemeRenderingList schemes = lbs.getSupportedCodingSchemes();
        for (CodingSchemeRendering csr:  schemes.getCodingSchemeRendering()) {
        	CodingSchemeSummary css= csr.getCodingSchemeSummary();
        	String version = css.getRepresentsVersion();
        	//Resolved ValueSet versions are generated as 32 hex value of its MD5
        	if (version.length()==32) {
        		candidateRenderingList.add(csr);
        	}
        	
        }
        
        for (CodingSchemeRendering csr: candidateRenderingList) {
        	CodingScheme cs= getResolvedCodingScheme(csr);
        	if (isResolvedValueSetCodingScheme(cs) ) {
        	    resolvedValueSetList.add(cs);
        	}
        }
        
        return resolvedValueSetList;
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
