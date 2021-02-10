
package org.LexGrid.LexBIG.Extensions.Load;

import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;

public interface NCItSourceAssertedValueSetUpdateService {
	static final String NCIT_URI = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
	

	public HistoryService getNCItSourceHistoryService() throws LBException;
	public LexBIGService getLexBIGService();
	public void setLexBIGService(LexBIGService lbs);
	public List<String> getReferencesForVersion(String version) throws LBException;
    public Date getDateForVersion(String currentVersion) throws LBException;
	public List<String> resolveUpdatedVSToReferences(String previousVersion, String currentVersion);
	public List<String> resolveUpdatedVSToReferences(String currentVersion);
}