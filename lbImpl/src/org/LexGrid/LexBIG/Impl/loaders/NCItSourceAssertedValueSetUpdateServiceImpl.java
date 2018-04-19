package org.LexGrid.LexBIG.Impl.loaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.NCItSourceAssertedValueSetUpdateService;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.History.UriBasedHistoryServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.SourceAssertedValueSetBatchLoader;
import org.LexGrid.LexBIG.Impl.loaders.SourceAssertedValueSetToSchemeBatchLoader;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.locator.LexEvsServiceLocator;

public class NCItSourceAssertedValueSetUpdateServiceImpl implements NCItSourceAssertedValueSetUpdateService {

	private LexBIGService lbs;
	private SourceAssertedValueSetToSchemeBatchLoader loader;
	private SourceAssertedValueSetBatchLoader vsDefLoader;
	private ValueSetDefinitionService vsService;

	private String codingScheme = "NCI_Thesaurus";
	private String version;
	private String association = "Concept_In_Subset";
	private String target = "true";
	private String uri = "http://evs.nci.nih.gov/valueset/";
	private String owner = "NCI";
	private String source = "Contributing_Source";
	private String conceptDomainIndicator = "Semantic_Type";
	private String schemeUri;
	private final static String PUBLISH_VALUE_SETS = "Publish_Value_Set";
	private final static String PUBLISH_VALUE_SETS_VALUE = "Yes";

	public NCItSourceAssertedValueSetUpdateServiceImpl() {
		try {
			loader = new SourceAssertedValueSetToSchemeBatchLoader(codingScheme, version, association,
					Boolean.parseBoolean(target), uri, owner, conceptDomainIndicator);
            AssertedValueSetParameters params = new AssertedValueSetParameters.Builder(version).
                    codingSchemeName("NCI_Thesaurus").
                    assertedDefaultHierarchyVSRelation("Concept_In_Subset").
                    baseValueSetURI("http://evs.nci.nih.gov/valueset/").
                    sourceName("Contributing_Source").
                    build();
            vsDefLoader = new SourceAssertedValueSetBatchLoader(params,
                     owner, conceptDomainIndicator);
			lbs = LexBIGServiceImpl.defaultInstance();
		} catch (LBParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public NCItSourceAssertedValueSetUpdateServiceImpl(String userDeterminedVersion) {
		try {
			loader = new SourceAssertedValueSetToSchemeBatchLoader(codingScheme, userDeterminedVersion, association,
					Boolean.parseBoolean(target), uri, owner, conceptDomainIndicator);
	         AssertedValueSetParameters params = new AssertedValueSetParameters.Builder(userDeterminedVersion).
	                    codingSchemeName("NCI_Thesaurus").
	                    assertedDefaultHierarchyVSRelation("Concept_In_Subset").
	                    baseValueSetURI("http://evs.nci.nih.gov/valueset/").
	                    sourceName("Contributing_Source").
	                    build();
	            vsDefLoader = new SourceAssertedValueSetBatchLoader(params,
	                     owner, conceptDomainIndicator);
			this.version = userDeterminedVersion;
			lbs = LexBIGServiceImpl.defaultInstance();
		} catch (LBParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public NCItSourceAssertedValueSetUpdateServiceImpl(String codingScheme, String version, String association,
			String target, String uri, String owner, String source, String conceptDomainIndicator, String schemeUri) {
		this.codingScheme = codingScheme;
		this.version = version;
		this.association = association;
		this.target = target;
		this.uri = uri;
		this.owner = owner;
		this.source = source;
		this.conceptDomainIndicator = conceptDomainIndicator;
		this.schemeUri = schemeUri;
		try {
			loader = new SourceAssertedValueSetToSchemeBatchLoader(codingScheme, version, association,
					Boolean.parseBoolean(target), uri, owner, conceptDomainIndicator);
	        AssertedValueSetParameters params = new AssertedValueSetParameters.Builder("17.02d").
	                codingSchemeName("NCI_Thesaurus").
	                assertedDefaultHierarchyVSRelation("Concept_In_Subset").
	                baseValueSetURI("http://evs.nci.nih.gov/valueset/").
	                sourceName("Contributing_Source").
	                build();
			vsDefLoader = new SourceAssertedValueSetBatchLoader(params,
					 owner, conceptDomainIndicator);
			lbs = LexBIGServiceImpl.defaultInstance();
		} catch (LBParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SourceAssertedValueSetToSchemeBatchLoader getSourceAssertedValueSetToSchemeBatchLoader() {
		return loader;
	}


	public HistoryService getNCItSourceHistoryService() throws LBException {
		return new UriBasedHistoryServiceImpl(getUri());
	}


	public LexBIGService getLexBIGService() {
		if (lbs != null) {
			return lbs;
		} else {
			return LexBIGServiceImpl.defaultInstance();
		}
	}


	public void setLexBIGService(LexBIGService lbs) {
		this.lbs = lbs;
	}


	public List<String> getReferencesForVersion(String version) throws LBException {
		return getNCItSourceHistoryService().getCodeListForVersion(version);
	}


	public List<String> getVersionsForDateRange(Date previousDate, Date currentDate)
			throws LBInvocationException, LBException {
		return getNCItSourceHistoryService().getVersionsForDateRange(previousDate, currentDate);
	}


	public void loadUpdatedValueSets(List<Node> refs) {

		try {
			synchronized (lbs) {
				loader.processEntitiesToCodingScheme(refs, source);
			}
		} catch (LBException | InterruptedException e) {
			throw new RuntimeException("Updating value sets FAILED: ", e);
		}

	}

	public static void main(String[] args) {
		NCItSourceAssertedValueSetUpdateServiceImpl service = // new
																// NCItSourceAssertedValueSetUpdateServiceImpl(
				// "owl2lexevs", "0.1.5.1", "Concept_In_Subset", "true",
				// "http://evs.nci.nih.gov/valueset/","NCI","Contributing_Source",
				// "Semantic_Type",
				// "http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl");
				new NCItSourceAssertedValueSetUpdateServiceImpl("17.08d");
		List<String> valueSetCodes = service.resolveUpdatedVSToReferences("17.07e", "17.08d");
		List<Node> finalNodes = null;
		try {
			List<Node> mappedNodes = service.mapSimpleReferencesToNodes(valueSetCodes);
			finalNodes = service.getNodeListForUpdate(mappedNodes);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		service.prepServiceForUpdate(finalNodes);

		service.loadUpdatedValueSets(finalNodes);

	}

	public List<Node> mapSimpleReferencesToNodes(List<String> valueSetCodes) throws LBException {
		final String namespace = this.getCodingSchemeNamespaceForURIandVersion(getUri(), version);
		return valueSetCodes.stream().map(x -> {
			Node node = new Node();
			node.setEntityCode(x);
			node.setEntityCodeNamespace(namespace != null ? namespace : "no namespace");
			return node;
		}).collect(Collectors.toList());
	}

	public ValueSetDefinitionService getVsService() {
		vsService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetDefinitionService();
		return vsService;
	}

	public Set<Node> getValueSetTopNodesForLeaves(List<Node> reducedNodes) {
		Set<Node> set = new HashSet<Node>();
		for (Node x : reducedNodes) {
			set.addAll(loader.getEntitiesForAssociationAndSourceEntity(x.getEntityCode(), x.getEntityCodeNamespace(),
					this.association, getUri(), this.version));
		}
		return set;
	}

	public List<Node> getNodeListForUpdate(List<Node> nodes) throws LBException {
		Set<Node> nodeHolder = getValueSetTopNodesForLeaves(nodes);
		for (Node node : nodes) {
			if (this.isValueSetTopNode(node)) {
				nodeHolder.add(node);
			}
		}
		return nodeHolder.stream().collect(Collectors.toList());
	}

	public boolean isValueSetTopNode(Node node) throws LBException {
		CodedNodeSet set = lbs.getCodingSchemeConcepts(this.codingScheme,
				Constructors.createCodingSchemeVersionOrTagFromVersion(this.version));
		set = set.restrictToCodes(
				Constructors.createConceptReferenceList(node.getEntityCode(), node.getEntityCodeNamespace()));
		ResolvedConceptReferencesIterator refs = set.resolve(null, null, null);
		if (refs.hasNext()) {
			ResolvedConceptReference ref = refs.next();
			boolean isTopNode = ref.getEntity().getPropertyAsReference().stream()
					.filter(x -> x.getPropertyName().equals(PUBLISH_VALUE_SETS))
					.anyMatch(x -> x.getValue().getContent().equals(PUBLISH_VALUE_SETS_VALUE));
			refs.release();
			return isTopNode;
		} else {
			return false;
		}
	}
	
	

	private boolean isTargetOfConceptInSubset(ResolvedConceptReference ref) {
		if(ref.getTargetOf() != null){
			return Arrays.asList(ref.getTargetOf().getAssociation()).stream().
					anyMatch(x-> x.getAssociationName().equals(association));
		}
		else{
			return false;
		}
			
	}

	public String getCodingSchemeNamespaceForURIandVersion(String uri, String version) throws LBException {
		// This complexity is thanks to some inconsistent namespace handling by
		// the OWL2 loader
		// We need to understand what the correct namespace format is and
		// correct the loader accordingly
		final String uriMod = normalizeUri(uri);
		CodingScheme scheme = getLexBIGService().resolveCodingScheme(uri,
				Constructors.createCodingSchemeVersionOrTagFromVersion(version));
		String schemeName = scheme.getMappings().getSupportedCodingSchemeAsReference().stream()
				.filter(supScheme -> supScheme.getUri().equals(uriMod) || supScheme.getUri().equals(uri)).findAny()
				.get().getLocalId();
		String schemeUri = scheme.getMappings().getSupportedCodingSchemeAsReference().stream()
				.filter(supScheme -> supScheme.getUri().equals(uriMod) || supScheme.getUri().equals(uri)).findAny()
				.get().getUri();
		String namespaceFromUri = scheme.getMappings().getSupportedNamespaceAsReference().stream()
				.filter(x -> x.getUri().equals(schemeUri) || x.getUri().equals(uri) || x.getUri().equals(uriMod))
				.findAny().get().getLocalId();
		if (namespaceFromUri != null) {
			return namespaceFromUri;
		}
		// Non standard namespace
		String namespaceFromEqCs = scheme.getMappings().getSupportedNamespaceAsReference().stream()
				.filter(x -> x.getEquivalentCodingScheme() != null && x.getEquivalentCodingScheme().equals(schemeName))
				.findAny().get().getLocalId();
		if (namespaceFromEqCs != null) {
			return namespaceFromEqCs;
		}
		return schemeName;
	}

	private String normalizeUri(String uri) {
		if (uri.endsWith("#")) {
			return uri;
		} else {
			return uri + "#";
		}
	}


	public Date getDateForVersion(String currentVersion) throws LBException {
		return getNCItSourceHistoryService().getDateForVersion(currentVersion);
	}


	public List<Node> getCurrentValueSetReferences() {
		return loader.getEntitiesForAssociation(association, getUri(), version);
	}

	public void prepServiceForUpdate(List<Node> nodes) {
		List<AbsoluteCodingSchemeVersionReference> refs = new ArrayList<AbsoluteCodingSchemeVersionReference>();
		try {
			for (Node node : nodes) {
				List<AbsoluteCodingSchemeVersionReference> subRefs = getVsService()
						.getValueSetDefinitionSchemeRefForTopNodeSourceCode(node);
				if (subRefs != null && subRefs.size() > 0) {
					refs.addAll(subRefs);
				} else { //If there is no value set -- we should see if we can create one.
				    
					List<AbsoluteCodingSchemeVersionReference> defRefs = getVsService()
							.getValueSetDefinitionDefRefForTopNodeSourceCode(node);
					if (defRefs == null || defRefs.size() == 0) {
						List<Node> temp = new ArrayList<Node>();
						temp.add(node);
						vsDefLoader.processEntitiesToDefinition(temp, source);
					} 
				}
			}
			for (AbsoluteCodingSchemeVersionReference abs : refs) {
				getLexBIGService().getServiceManager(null).deactivateCodingSchemeVersion(abs, null);
				getLexBIGService().getServiceManager(null).removeCodingSchemeVersion(abs);
			}
		} catch (LBException e) {
			throw new RuntimeException("Problem prepping service for update" + e);
		}
	}

	public List<String> resolveUpdatedVSToReferences(String previousVersion, String currentVersion) {
		try {
			if (previousVersion != null) {
				Date previousDate = getNCItSourceHistoryService().getDateForVersion(previousVersion);
				Date currentDate = getNCItSourceHistoryService().getDateForVersion(currentVersion);
				List<String> versions = getNCItSourceHistoryService().getVersionsForDateRange(previousDate,
						currentDate);
				List<String> list = new ArrayList<String>();
				for (String s : versions) {
					list.addAll(getNCItSourceHistoryService().getCodeListForVersion(s));
				}
				return list;

			} else {
				return getNCItSourceHistoryService().getCodeListForVersion(currentVersion);
			}
		} catch (LBException e) {
			throw new RuntimeException("Failed to retrieve Dates or Code Lists from history" + e);
		}
	}


	public List<String> resolveUpdatedVSToReferences(String currentVersion) {
		return resolveUpdatedVSToReferences(null, currentVersion);
	}

	private String getUri() {
		return schemeUri == null ? NCIT_URI : schemeUri;
	}

    public String getVersion() {
        return version;
    }

}
