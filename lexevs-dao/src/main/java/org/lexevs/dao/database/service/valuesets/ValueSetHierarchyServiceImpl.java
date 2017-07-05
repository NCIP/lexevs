package org.lexevs.dao.database.service.valuesets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.access.valuesets.ValueSetHierarchyDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.locator.LexEvsServiceLocator;

import gov.nih.nci.evs.browser.utils.TreeItem;

public class ValueSetHierarchyServiceImpl extends AbstractDatabaseService implements ValueSetHierarchyService {

	TreeItem super_root = new TreeItem(ROOT, "Root node");
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
	HashMap<String, TreeItem> master = new HashMap<String, TreeItem>();

	public ValueSetHierarchyServiceImpl() {
	}

	public void init(String scheme, String version, String association, String sourceDesignation, String publishName,
			String root_code) {
		this.scheme = scheme;
		this.version = version;
		this.association = association;
		this.sourceDesignation = sourceDesignation;
		this.publishName = publishName;
		this.root_code = root_code;
		vsDao = getDaoManager().getCurrentValueSetHiearchyDao();
		super_root._expandable = true;
		schemeUID = this.getCodingSchemeUId(scheme, version);
		this.associationPredicateGuid = this.getPredicateUid();
	}

	public void init() {
		vsDao = getDaoManager().getCurrentValueSetHiearchyDao();
		super_root._expandable = true;
		schemeUID = this.getSchemeUid(scheme, version);
		this.associationPredicateGuid = this.getPredicateUid();
	}

	@Override
	public void preprocessSourceHierarchyData() {
		init();
	}

	@Override
	public HashMap<String, TreeItem> getSourceValueSetTree(String Scheme, String version) throws LBException {
		HashMap<String, TreeItem> roots = getHierarchyValueSetRoots(scheme, version, association, sourceDesignation,
				publishName, root_code);
		TreeItem root = roots.get(ROOT);
		List<TreeItem> nodes = root._assocToChildMap.get(INVERSE_IS_A);
		for (TreeItem ti : nodes) {
			recurseFromRootsToUpdateMap(ti);
		}
		return master;
	}

	protected void recurseFromRootsToUpdateMap(TreeItem ti) {
		List<VSHierarchyNode> nodes = this.getFilteredNodeChildren(ti._code);
		sort(nodes);
		List<TreeItem> items = new ArrayList<TreeItem>();
		for (VSHierarchyNode node : nodes) {
			TreeItem item = new TreeItem(this.getURIFromVSHeirarchyNode(node), node.getDescription());
			items.add(item);
			this.getSourceValueSetTreeBranch(node, item);
		}
		ti.addAll(INVERSE_IS_A, items);
	}

	@Override
	public List<VSHierarchyNode> getSourceValueSetTreeBranch(VSHierarchyNode topNode, TreeItem ti) {
		List<VSHierarchyNode> nextBranch = this.getFilteredNodeChildren(topNode.getEntityCode());
		sort(nextBranch);
		List<TreeItem> treeNodes = new ArrayList<TreeItem>();
		for (VSHierarchyNode node : nextBranch) {
			TreeItem newItem = new TreeItem(this.getURIFromVSHeirarchyNode(node), node.getDescription());
			treeNodes.add(newItem);
			getSourceValueSetTreeBranch(node, newItem);
		}
		ti.addAll(INVERSE_IS_A, treeNodes);
		return nextBranch;
	}

	@Override
	public HashMap<String, TreeItem> getHierarchyValueSetRoots(String scheme, String version, String association,
			String sourceDesignation, String publishName, String code) throws LBException {
		List<TreeItem> subTrees = new ArrayList<TreeItem>();

		List<VSHierarchyNode> nodes = this.getUnfilteredNodes(code);
		// process nodes to remove duplicate description/code sets where source
		// does not exist
		List<VSHierarchyNode> temps = collectReducedNodes(nodes);
		this.sort(temps);
		for (VSHierarchyNode n : temps) {
			subTrees.add(new TreeItem(n.getEntityCode(), n.getDescription()));
		}
		super_root.addAll(INVERSE_IS_A, subTrees);
		master.put(ROOT, super_root);
		return master;
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
				publishName, 0, -1);
	}

}
