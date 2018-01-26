package org.lexevs.dao.database.service.valuesets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
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
	ValueSetHierarchyDao vsDao;

	public ValueSetHierarchyServiceImpl() {
	}

	public ValueSetHierarchyServiceImpl init(String scheme, String version, String association, String sourceDesignation, String publishName,
			String root_code) {
		this.scheme = scheme;
		this.version = version;
		this.association = association;
		this.sourceDesignation = sourceDesignation;
		this.publishName = publishName;
		this.root_code = root_code;
		vsDao = getDaoManager().getCurrentValueSetHiearchyDao();
		schemeUID = this.getCodingSchemeUId(scheme, version);
		this.associationPredicateGuid = this.getPredicateUid();
		return this;
	}


	public ValueSetHierarchyServiceImpl init() {
		vsDao = getDaoManager().getCurrentValueSetHiearchyDao();
		schemeUID = this.getSchemeUid(scheme, version);
		this.associationPredicateGuid = this.getPredicateUid();
		return this;
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
