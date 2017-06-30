package org.lexevs.dao.database.service.valuesets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.access.valuesets.ValueSetHierarchyDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.locator.LexEvsServiceLocator;

import gov.nih.nci.evs.browser.utils.TreeItem;



public class ValueSetHierarchyServiceImpl extends AbstractDatabaseService implements ValueSetHierarchyService {

	private LexBIGService lexBIGServiceImpl = null;
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
	
	public ValueSetHierarchyServiceImpl(){	
	}
	
	public void init(String scheme, 
			String version, 
			String association,
			String sourceDesignation,
			String publishName,
			String root_code){
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
	
	public void init(){
		vsDao = getDaoManager().getCurrentValueSetHiearchyDao();
		super_root._expandable = true;
		schemeUID = this.getSchemeUid(scheme, version);
		this.associationPredicateGuid = this.getPredicateUid();
	}
	
	@Override
	public HashMap<String, TreeItem> getCodingSchemeValueSetTree(String scheme, String version) throws LBException {
		HashMap<String, TreeItem> roots = getHierarchyValueSetRoots(scheme, version, association, sourceDesignation, publishName, root_code);
		TreeItem root = roots.get(ROOT);
		List<TreeItem> nodes = root._assocToChildMap.get(INVERSE_IS_A);
		for(TreeItem ti: nodes){
			List<VSHierarchyNode> children = this.getUnfilteredNodes(ti._code);
		}
		return null;
	}

	@Override
	public void preprocessSourceHierarchyData() {
		// TODO Auto-generated method stub

	}

	@Override
	public HashSet<String> getValueSetParticipationHashSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initializeCS2vsdURIs_map() {
		// TODO Auto-generated method stub

	}

	@Override
	public HashMap<String, TreeItem> getSourceValueSetTree(String Scheme, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResolvedConceptReferenceList getValueSetHierarchyRoots() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeItem getVSDChildNodeBySource(ValueSetDefinition vsd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeItem getSourceValueSetTreeBranch(ValueSetDefinition vsd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeItem getCodingSchemeValueSetTreeBranch(String scheme, String code, String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	//Util
	protected HashMap<String, TreeItem> getHierarchyValueSetRoots(
			String scheme, 
			String version, 
			String association,
			String sourceDesignation,
			String publishName,
			String code) throws LBException{
		HashMap<String, TreeItem> map = new HashMap<String,TreeItem>();
		List<TreeItem> subTrees = new ArrayList<TreeItem>();
		
		
		List<VSHierarchyNode> nodes = this.getUnfilteredNodes(code);
		//process nodes to remove duplicate description/code sets where source does not exist
		List<VSHierarchyNode> temps = collectReducedNodes( nodes);
		this.sort(temps);
		TreeItem root = super_root;
		for(VSHierarchyNode n: temps){			
			subTrees.add( new TreeItem(n.getEntityCode(), 
					n.getDescription()));
		}
		//Need to sort subtrees by some method at some point
		root.addAll(INVERSE_IS_A , subTrees);
		map.put(ROOT, root);
		return map;
	}
	
	protected String getSchemeUid(String Uri, String version){
		if(version == null){
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
	
	private String getURIFromVSHeirarchyNode(VSHierarchyNode n) {
	       return AssertedValueSetServices.createUri(AssertedValueSetServices.BASE,
	    		   n.getSource(), n.getEntityCode());
	}
	
	public List<VSHierarchyNode> getFilteredNodeChildren(String code){
		return collectReducedNodes(getUnfilteredNodes(code));
	}
	
	protected List<VSHierarchyNode> collectReducedNodes(List<VSHierarchyNode> nodes){
		//Get all nodes with declared sources
		List<VSHierarchyNode> temps = nodes.stream().filter(x -> x.getSource() != null).
				collect(Collectors.toList());
		//Filter these from the remainder when there is a duplicate with a null source
		List<VSHierarchyNode> complete = new ArrayList<VSHierarchyNode>();
		for(VSHierarchyNode n: nodes){
			if(n.getSource() == null){
			if(!temps.stream().anyMatch(x->x.getEntityCode().equals(n.getEntityCode()))){
			complete.add(n);
			}
		}
		}
		// add those with a source back in
		complete.addAll(temps);
		return complete;
	}

//	protected Entity getEntityForCode(String code, String codingScheme, String version) throws LBException{
//		CodedNodeSet set = lexBIGServiceImpl.getCodingSchemeConcepts(codingScheme, 
//				Constructors.createCodingSchemeVersionOrTagFromVersion(version));
//		set = set.restrictToCodes(Constructors.createConceptReferenceList(code));
//		ResolvedConceptReferencesIterator it = set.resolve(null, null, null);
//		return it.next().getEntity();
//		
//	}
	
	protected List<VSHierarchyNode> getUnfilteredNodes(String code){
		return vsDao.getAllVSTriplesTrOfVSNode(schemeUID, code, 
				associationPredicateGuid, sourceDesignation, publishName, 0, -1);
	}
	
	public static void main(String[] args){
		

	}

}
