package org.lexgrid.valuesets.sourceasserted.impl;

import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.SourceAssertedValueSetToSchemeBatchLoader;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.valuesets.sourceasserted.NCItSourceAssertedValueSetUpdateService;

public class NCItSourceAssertedValueSetUpdateServiceImpl implements NCItSourceAssertedValueSetUpdateService {

	private LexBIGService lbs;

	public NCItSourceAssertedValueSetUpdateServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public SourceAssertedValueSetToSchemeBatchLoader getSourceAssertedValueSetToSchemeBatchLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HistoryService getNCItSourceHistoryService() throws LBException {
		return getLexBIGService().getHistoryService(NCIT_URI);
	}

	@Override
	public LexBIGService getLexBIGService(){
		if(lbs != null){
			return lbs;
		}else{ return LexBIGServiceImpl.defaultInstance();}
	}

	@Override
	public void setLexBIGService(LexBIGService lbs) {
		this.lbs = lbs;
	}

	@Override
	public List<String> getReferencesForVersion(String version) throws LBException {
		return getNCItSourceHistoryService().getCodeListForVersion(version);
	}

	@Override
	public List<String> getVersionsForDateRange(String previousDate, String currentDate) throws LBInvocationException, LBException {
		return getNCItSourceHistoryService().getVersionsForDateRange(previousDate, currentDate);
	}

	@Override
	public void loadUpdatedValueSets(List<ConceptReference> refs) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public String getCodingSchemeNamespaceForURIandVersion(String uri, String version) throws LBException{
		CodingScheme scheme = getLexBIGService().resolveCodingScheme(uri, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(version));
		String schemeName = scheme.getMappings().getSupportedCodingSchemeAsReference().stream().filter(supScheme -> supScheme.getUri().equals(uri)).findAny().get().getLocalId();
		return scheme.getMappings().getSupportedNamespaceAsReference().stream().filter(
				x -> x.getEquivalentCodingScheme() != null && 
				x.getEquivalentCodingScheme().equals(schemeName)).
				findAny().get().getLocalId();
	}

	@Override
	public Date getDateForVersion(String currentVersion) throws LBException {
		return getNCItSourceHistoryService().getDateForVersion(currentVersion);
	}


}
