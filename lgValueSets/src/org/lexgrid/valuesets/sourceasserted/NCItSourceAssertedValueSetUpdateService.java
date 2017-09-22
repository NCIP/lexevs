package org.lexgrid.valuesets.sourceasserted;

import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.loaders.SourceAssertedValueSetToSchemeBatchLoader;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.lexevs.dao.database.access.association.model.Node;

public interface NCItSourceAssertedValueSetUpdateService {
	static final String NCIT_URI = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
	
	public SourceAssertedValueSetToSchemeBatchLoader getSourceAssertedValueSetToSchemeBatchLoader();
	public HistoryService getNCItSourceHistoryService() throws LBException;
	public LexBIGService getLexBIGService();
	public void setLexBIGService(LexBIGService lbs);
	public List<String> getReferencesForVersion(String version) throws LBException;
    public Date getDateForVersion(String currentVersion) throws LBException;
	public List<String> resolveUpdatedVSToReferences(String previousVersion, String currentVersion);
	public List<String> resolveUpdatedVSToReferences(String currentVersion);
	public List<Node> getCurrentValueSetReferences();
	public List<String> getVersionsForDateRange(Date previousDate, Date currentDate) throws LBInvocationException, LBException;
	public List<Node> getUpatedValueSetsForCurrentVersion(List<Node> references, List<String> valuesets);
	void loadUpdatedValueSets(List<Node> refs);
}
