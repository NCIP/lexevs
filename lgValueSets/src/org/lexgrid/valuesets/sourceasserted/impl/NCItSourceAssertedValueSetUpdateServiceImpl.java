package org.lexgrid.valuesets.sourceasserted.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.valuesets.sourceasserted.NCItSourceAssertedValueSetUpdateService;

public class NCItSourceAssertedValueSetUpdateServiceImpl implements NCItSourceAssertedValueSetUpdateService {

	private LexBIGService lbs;
    private SourceAssertedValueSetToSchemeBatchLoader loader;
	
    private String codingScheme = "NCI_Thesaurus";
    private String version;
    private String association = "Concept_In_Subset";
    private String target = "true";
    private String uri = "http://evs.nci.nih.gov/valueset/";
    private String owner="NCI";
    private String source = "Contributing_Source";
    private String conceptDomainIndicator = "Semantic_Type";

	public NCItSourceAssertedValueSetUpdateServiceImpl() {
	       try {
	           loader = new SourceAssertedValueSetToSchemeBatchLoader(
	                       codingScheme, 
	                       version, 
	                       association, 
	                       Boolean.parseBoolean(target), 
	                       uri, 
	                       owner, 
	                       conceptDomainIndicator);
	           lbs = LexBIGServiceImpl.defaultInstance();
	       } catch (LBParameterException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	       }
	}
	
	public NCItSourceAssertedValueSetUpdateServiceImpl(String userDeterminedVersion) {
	       try {
	           loader = new SourceAssertedValueSetToSchemeBatchLoader(
	                       codingScheme, 
	                       userDeterminedVersion, 
	                       association, 
	                       Boolean.parseBoolean(target), 
	                       uri, 
	                       owner, 
	                       conceptDomainIndicator);
	           this.version = userDeterminedVersion;
	           lbs = LexBIGServiceImpl.defaultInstance();
	       } catch (LBParameterException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	       }
	}
	
	public NCItSourceAssertedValueSetUpdateServiceImpl(String codingScheme,
    String version,
    String association,
    String target,
    String uri,
    String owner,
    String source,
    String conceptDomainIndicator) {
	    this.codingScheme = codingScheme;
	    this.version = version;
	    this.association = association;
	    this.target = target;
	    this.uri = uri;
	    this.owner= owner;
	    this.source = source;
	    this.conceptDomainIndicator = conceptDomainIndicator;
	    
	       try {
	           loader = new SourceAssertedValueSetToSchemeBatchLoader(
	                       codingScheme, 
	                       version, 
	                       association, 
	                       Boolean.parseBoolean(target), 
	                       uri, 
	                       owner, 
	                       conceptDomainIndicator);
	           lbs = LexBIGServiceImpl.defaultInstance();
	       } catch (LBParameterException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	       }
	}

	@Override
	public SourceAssertedValueSetToSchemeBatchLoader getSourceAssertedValueSetToSchemeBatchLoader() {
		return loader;
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
	public List<String> getVersionsForDateRange(Date previousDate, Date currentDate) throws LBInvocationException, LBException {
		return getNCItSourceHistoryService().getVersionsForDateRange(previousDate, currentDate);
	}

	@Override
	public void loadUpdatedValueSets(List<Node> refs) {
		
      try {
    	synchronized(lbs){
		loader.processEntitiesToCodingScheme(refs, source);
    	}
	} catch (LBException | InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}

	public static void main(String[] args) {
		NCItSourceAssertedValueSetUpdateServiceImpl service = new NCItSourceAssertedValueSetUpdateServiceImpl("17.07e");
		List<String> valueSetCodes = service.resolveUpdatedVSToReferences("17.07e");
		List<Node> nodes = service.getCurrentValueSetReferences();
		List<Node> finalNodes = service.getUpatedValueSetsForCurrentVersion(nodes, valueSetCodes);
		service.loadUpdatedValueSets(finalNodes);

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

	@Override
	public List<Node> getCurrentValueSetReferences() {
	 return loader.getEntitiesForAssociation(association, NCIT_URI, version);
	}

	@Override
	public List<String> resolveUpdatedVSToReferences(String previousVersion, String currentVersion) {
		try {
			if (previousVersion != null) {
				Date previousDate = getNCItSourceHistoryService().getDateForVersion(previousVersion);
				Date currentDate = getNCItSourceHistoryService().getDateForVersion(currentVersion);
				return getNCItSourceHistoryService().getVersionsForDateRange(previousDate, currentDate);
			} else {
				return getNCItSourceHistoryService().getCodeListForVersion(currentVersion);
			}
		} catch (LBException e) {
			throw new RuntimeException("Failed to retrieve Dates or Code Lists from history" + e);
		}
	}

	@Override
	public List<String> resolveUpdatedVSToReferences(String currentVersion) {
		return resolveUpdatedVSToReferences(null,  currentVersion);
	}
	
	@Override
	public List<Node> getUpatedValueSetsForCurrentVersion(List<Node> references,
			List<String> valuesets) {
		return references.stream().filter(x -> valuesets.contains(x.getEntityCode())).collect(Collectors.toList());
	}

}
