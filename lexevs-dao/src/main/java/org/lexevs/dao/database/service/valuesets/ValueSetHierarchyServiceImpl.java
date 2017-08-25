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
		List<VSHierarchyNode> nodes = this.getFilteredNodeChildren(ti._code);
		if(nodes != null && nodes.size() > 0){ ti._expandable= true;}
		sort(nodes);
		List<LexEVSTreeItem> items = new ArrayList<LexEVSTreeItem>();
		for (VSHierarchyNode node : nodes) {
			LexEVSTreeItem item = new LexEVSTreeItem(node.getEntityCode(), node.getDescription());
			items.add(item);
			this.getSourceValueSetTreeBranch(node, item);
		}
		ti.addAll(INVERSE_IS_A, items);
	}

	@Override
	public List<VSHierarchyNode> getSourceValueSetTreeBranch(VSHierarchyNode topNode, LexEVSTreeItem ti) {
		List<VSHierarchyNode> nextBranch = this.getFilteredNodeChildren(topNode.getEntityCode());
		if(nextBranch != null && nextBranch.size() > 0){ ti._expandable = true;}
		sort(nextBranch);
		List<LexEVSTreeItem> treeNodes = new ArrayList<LexEVSTreeItem>();
		for (VSHierarchyNode node : nextBranch) {
			LexEVSTreeItem newItem = new LexEVSTreeItem(node.getEntityCode(), node.getDescription());
			treeNodes.add(newItem);
			getSourceValueSetTreeBranch(node, newItem);
		}
		ti.addAll(INVERSE_IS_A, treeNodes);
		return nextBranch;
	}

	@Override
	public HashMap<String, LexEVSTreeItem> getHierarchyValueSetRoots(String code) throws LBException {
		List<LexEVSTreeItem> subTrees = new ArrayList<LexEVSTreeItem>();
		HashMap<String, LexEVSTreeItem> master = new HashMap<String, LexEVSTreeItem>();
		List<VSHierarchyNode> temps = getFilteredNodeChildren(code);
		this.sort(temps);
		for (VSHierarchyNode n : temps) {
			subTrees.add(new LexEVSTreeItem(n.getEntityCode(), n.getDescription()));
		}
		LexEVSTreeItem super_root = new LexEVSTreeItem(ROOT, "Root node");
		super_root.addAll(INVERSE_IS_A, subTrees);
		super_root._expandable = true;
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
				publishName, publishValue, 0, -1);
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

}
