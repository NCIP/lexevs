package org.lexevs.dao.database.service.valuesets;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.access.association.model.DefinedNode;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.access.valuesets.ValueSetHierarchyDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.locator.LexEvsServiceLocator;


public class ValueSetHierarchyServiceImpl extends AbstractDatabaseService implements ValueSetHierarchyService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2755261228867112212L;
	String scheme = SCHEME;
	String version = null;
	String association = HIERARCHY;
	String sourceDesignation = SOURCE;
	String publishName = PUBLISH_DESIGNATION;
	String publishValue = CAN_PUBLISH;
	String root_code = ROOT_CODE;
	String schemeUID;
	String associationPredicateGuid;
	List<String> vsExternalURIs;
	public List<String> getVsExternalURIs() {
		return vsExternalURIs;
	}

	public void setVsExternalURIs(List<String> vsExternalURIs) {
		this.vsExternalURIs = vsExternalURIs;
	}

	ValueSetHierarchyDao vsDao;
	private ValueSetDefinitionService vsDef;

	public ValueSetHierarchyServiceImpl() {
	}

	public ValueSetHierarchyServiceImpl init(String scheme, String version, String association, String sourceDesignation, String publishName,
			String root_code) {
		this.scheme = scheme;
		this.version = version;
		schemeUID = this.getCodingSchemeUId(scheme, version);
		this.association = association;
		this.associationPredicateGuid = this.getPredicateUid();
		this.sourceDesignation = sourceDesignation;
		this.publishName = publishName;
		this.root_code = root_code;
		vsDao = getDaoManager().getCurrentValueSetHiearchyDao();
		vsDef = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetDefinitionService();
		vsExternalURIs = getExternallyDefinedValueSetsForAssertedSource(root_code);
		return this;
	}


	public ValueSetHierarchyServiceImpl init() {
		vsDao = getDaoManager().getCurrentValueSetHiearchyDao();
		schemeUID = this.getSchemeUid(scheme, version);
		this.associationPredicateGuid = this.getPredicateUid();
		vsDef = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetDefinitionService();
		vsExternalURIs = getExternallyDefinedValueSetsForAssertedSource(root_code);
		System.out.println("scheme: " + scheme);
		return this;
	}

	protected List<String> getExternallyDefinedValueSetsForAssertedSource(String root) {
		List<String> uris = new ArrayList<String>();
		 this.getRootCodes(root).stream().forEachOrdered(rootCode -> uris.
				 addAll(vsDef.getVSURIsForContextURI(rootCode)));
		 return uris;
	}
	
	protected List<String> getRootCodes(String root){
		List<String> roots = null;
		try {
			roots = this.getHierarchyValueSetRoots(root_code)
			.get(ROOT).
			_assocToChildMap.
			get(INVERSE_IS_A).stream().map(treeItem -> treeItem.get_code()).collect(Collectors.toList());
		} catch (LBException e) {
			throw new RuntimeException("There were problems getting hierarchy roots." + e);
		}
		return roots;
	}

	@Override
	public void preprocessSourceHierarchyData() {
		init();
	}
	
	@Override
	public void preprocessSourceHierarchyData(String scheme, String version, String association, String sourceDesignation, String publishName,
			String root_code) {
		init(scheme, version, association, sourceDesignation, publishName,
				root_code);
	}

	@Override
	public HashMap<String, LexEVSTreeItem> getSourceValueSetTree() throws LBException {
		HashMap<String, LexEVSTreeItem> roots = getHierarchyValueSetRoots(root_code);
		LexEVSTreeItem root = roots.get(ROOT);
		List<LexEVSTreeItem> nodes = root._assocToChildMap.get(INVERSE_IS_A);
		for (LexEVSTreeItem ti : nodes) {
			recurseFromRootsToUpdateMap(ti);
		}
		return roots;
	}

	protected void recurseFromRootsToUpdateMap(LexEVSTreeItem ti) {
		String reducedCode = reduceToCodeFromUri(ti.get_code());
		List<VSHierarchyNode> nodes = this.getFilteredNodeChildren(reducedCode);
		if(nodes != null) {nodes.addAll(getAnyExternallyDefinedNodes(reducedCode));}
		if(nodes != null && nodes.size() > 0){ ti._expandable= true;}
		sort(nodes);
		List<LexEVSTreeItem> items = new ArrayList<LexEVSTreeItem>();
		for (VSHierarchyNode node : nodes) {
			LexEVSTreeItem item = new LexEVSTreeItem(AssertedValueSetServices.
					createUri(VS_ROOT_URI, node.getSource(), node.getEntityCode()), 
					node.getDescription(), node.getNamespace(), null);
			items.add(item);
			this.getSourceValueSetTreeBranch(node, item);
		}
		ti.addAll(INVERSE_IS_A, items);
	}

	protected Collection<VSHierarchyNode> getAnyExternallyDefinedNodes(String reducedCode) {
		ValueSetDefinitionService vsDef =  LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetDefinitionService();
		List<String> uris = vsDef.getVSURIsForContextURI(
				AssertedValueSetServices.createUri(
						AssertedValueSetParameters.ROOT_URI, null, reducedCode));
		return uris.stream().map(uri -> transformUriToHeirarchyNode(uri)).collect(Collectors.toList());
	}

	protected String reduceToCodeFromUri(String get_code) {
		return get_code.substring(get_code.lastIndexOf('/') + 1, get_code.length());
	}

	@Override
	public List<VSHierarchyNode> getSourceValueSetTreeBranch(VSHierarchyNode topNode, LexEVSTreeItem ti) {
		List<VSHierarchyNode> nextBranch = this.getFilteredSortedIndividualizedNodeChildren(topNode.getEntityCode());
		if(nextBranch != null && nextBranch.size() > 0){ ti._expandable = true;}
		sort(nextBranch);
		List<LexEVSTreeItem> treeNodes = new ArrayList<LexEVSTreeItem>();
		for (VSHierarchyNode node : nextBranch) {
			LexEVSTreeItem newItem = new LexEVSTreeItem(AssertedValueSetServices.
					createUri(VS_ROOT_URI, node.getSource(), node.getEntityCode()), 
					node.getDescription(), node.getNamespace(), null);
			treeNodes.add(newItem);
			getSourceValueSetTreeBranch(node, newItem);
		}
		ti.addAll(INVERSE_IS_A, treeNodes);
		return nextBranch;
	}

	@Override
	public HashMap<String, LexEVSTreeItem> getHierarchyValueSetRoots(String code) throws LBException {		
		HashMap<String, LexEVSTreeItem> master = new HashMap<String, LexEVSTreeItem>();
		List<VSHierarchyNode> temps = getFilteredSortedIndividualizedNodeChildren(code);
		List<LexEVSTreeItem> subTrees = getSortedExpandedTreeItems(temps);
		LexEVSTreeItem super_root = new LexEVSTreeItem(ROOT, "Root node");
		super_root.addAll(INVERSE_IS_A, subTrees);
		super_root._expandable = true;
		master.put(ROOT, super_root);
		return master;
	}
	
	public List<LexEVSTreeItem> getSortedExpandedTreeItems(List<VSHierarchyNode> nodes){
		Map<String, List<VSHierarchyNode>> duplicateGrouping = groupByDescription(nodes);
		List<LexEVSTreeItem> subTrees = new ArrayList<LexEVSTreeItem>();
		Set<String> keys = duplicateGrouping.keySet();
		TreeSet<String> sortedKeys =  keys.stream().collect(
				Collectors.toCollection(TreeSet::new));
		for(String s : sortedKeys){
			if(duplicateGrouping.get(s).size() > 1){
				duplicateGrouping.get(s).stream().forEach(x -> subTrees.add(new LexEVSTreeItem(
						AssertedValueSetServices.createUri(VS_ROOT_URI, x.getSource(), x.getEntityCode()),
						x.getDescription() + 
						AssertedValueSetServices.createSuffixForSourceDefinedResolvedValueSet(x.getSource()),
						x.getNamespace(), null)));
			}else{
				duplicateGrouping.get(s).forEach(x ->subTrees.add(new LexEVSTreeItem(
						AssertedValueSetServices.createUri(VS_ROOT_URI, x.getSource(), x.getEntityCode()),
						x.getDescription(), x.getNamespace(), null)));
			}
		}
		return subTrees;
	}
	
	public Map<String, List<VSHierarchyNode>> groupByDescription(List<VSHierarchyNode> list){
		return list.stream().collect(Collectors.groupingBy(VSHierarchyNode::getDescription));
	}

	protected String getSchemeUid(String Uri, String version) {
		if (version == null) {
			version = getProductionVersionFromTargetScheme(Uri);
		}
		System.out.println("version: " + version);
		return this.getCodingSchemeUId(Uri, version);
	}

	protected String getProductionVersionFromTargetScheme(String uri) {

		try {
			return LexEvsServiceLocator.getInstance().getSystemResourceService().getInternalVersionStringForTag(uri,
					"PRODUCTION");
		} catch (LBParameterException e) {
			throw new RuntimeException("Problem retrieving a production version for coding scheme with uri: " + uri);
		}
	}

	protected String getPredicateUid() {
		return getDaoManager().getCurrentAssociationDao()
				.getAssociationPredicateUidsForAssociationName(schemeUID, null, association).get(0);
	}

	private void sort(List<VSHierarchyNode> temps) {
		Collections.sort(temps);
	}

	protected String getURIFromVSHeirarchyNode(VSHierarchyNode n) {
		return AssertedValueSetServices.createUri(AssertedValueSetServices.BASE, n.getSource(), n.getEntityCode());
	}

	public List<VSHierarchyNode> getFilteredNodeChildren(String code) {
		return collectReducedNodes(getUnfilteredNodes(code));
	}
	
	public List<VSHierarchyNode> getFilteredSortedIndividualizedNodeChildren(String code){
		List<VSHierarchyNode> nodes = collectReducedNodes(getUnfilteredNodes(code));
		Map<String, List<VSHierarchyNode>> groupedNodes = groupByDescription(nodes);
		List<VSHierarchyNode> sortedRenamedNodes = sortAndIndividuate(groupedNodes);		
		return sortedRenamedNodes;
	}

	private List<VSHierarchyNode> sortAndIndividuate(Map<String, List<VSHierarchyNode>> groupedNodes) {
		 List<VSHierarchyNode> finalNodes = new ArrayList<VSHierarchyNode>();
		Set<String> keys = groupedNodes.keySet();
		//effectively sorts on the description of each node
		TreeSet<String> sortedKeys =  keys.stream().collect(
				Collectors.toCollection(TreeSet::new));
		for(String s : sortedKeys){
			if(groupedNodes.get(s).size() > 1){
				groupedNodes.get(s).stream().forEach(x -> x.setDescription(x.getDescription() +
						AssertedValueSetServices.createSuffixForSourceDefinedResolvedValueSet(x.getSource())));
			}
			finalNodes.addAll(groupedNodes.get(s));
		}
		return finalNodes;
	}

	protected List<VSHierarchyNode> collectReducedNodes(List<VSHierarchyNode> nodes) {
		// Get all nodes with declared sources
		List<VSHierarchyNode> temps = nodes.stream().filter(x -> x.getSource() != null).collect(Collectors.toList());
		// Filter these from the remainder when there is a duplicate with a null
		// source
		List<VSHierarchyNode> complete = new ArrayList<VSHierarchyNode>();
		for (VSHierarchyNode n : nodes) {
			if (n.getSource() == null) {
				if (!temps.stream().anyMatch(x -> x.getEntityCode().equals(n.getEntityCode()))) {
					complete.add(n);
				}
			}
		}
		// add those with a source back in
		complete.addAll(temps);
		return complete;
	}

	protected List<VSHierarchyNode> getUnfilteredNodes(String code) {
		return vsDao.getAllVSTriplesTrOfVSNode(schemeUID, code, associationPredicateGuid, sourceDesignation,
				publishName, publishValue, 0, -1);
	}
	
	public List<DefinedNode> getAllValueSetNodesWithoutSource(String association, String publishName, String publishValue){
		return vsDao.getAllVSTriples(schemeUID, associationPredicateGuid, publishName, publishValue, 0, -1);
	}
	
    
    public VSHierarchyNode transformUriToHeirarchyNode(String uri) {
    	ValueSetDefinition vsd = null;
    	try {
			vsd = LexEvsServiceLocator.getInstance().
			getDatabaseServiceManager().getValueSetDefinitionService().
			getValueSetDefinitionByUri(new URI(uri));
		} catch (URISyntaxException e) {
			throw new RuntimeException("Problem URI for to transform "
					+ "value set definition to heirarchy node" + e);
		}
    	VSHierarchyNode node = new VSHierarchyNode();
    	node.setDescription(vsd.getValueSetDefinitionName());
    	//should be only one definition entry
    	node.setEntityCode(vsd.getDefinitionEntry()[0].getEntityReference().getEntityCode());
    	node.setNamespace(vsd.getDefinitionEntry()[0].getEntityReference().getEntityCodeNamespace());
    	//should have only one source since we are tying it to the source via the context
    	node.setSource(vsd.getSource()[0].getContent());
		return node;
        
    }
	

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 
	 * @return the scheme
	 */
	public String getScheme() {
		return this.scheme;
	}
	
	/**
	 * 
	 * @return the association
	 */
	public String getAssociation() {
		return this.association;
	}
}
