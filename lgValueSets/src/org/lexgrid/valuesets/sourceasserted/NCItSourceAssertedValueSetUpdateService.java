package org.lexgrid.valuesets.sourceasserted;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.loaders.SourceAssertedValueSetToSchemeBatchLoader;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;

public interface NCItSourceAssertedValueSetUpdateService {
	static final String NCIT_URI = "";
	
	public SourceAssertedValueSetToSchemeBatchLoader getSourceAssertedValueSetToSchemeBatchLoader();
	public HistoryService getNCItSourceHistoryService() throws LBException;
	public LexBIGService getLexBIGService();
	public void setLexBIGService(LexBIGService lbs);
	public List<String> getReferencesForVersion(String version);
	public void loadUpdatedValueSets(List<ConceptReference> refs);
	public List<ConceptReference> getUpdateListFromHistoryService(String currentVersion, String previousVersion) throws LBInvocationException, LBException;
}
