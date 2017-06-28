package org.lexevs.dao.database.service.valuesets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetServices;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.access.valuesets.ValueSetHierarchyDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import gov.nih.nci.evs.browser.utils.TreeItem;



public class ValueSetHierarchServiceImpl extends AbstractDatabaseService implements ValueSetHierarchyService {
	private LexBIGService LexBIGServiceImpl = null;
	TreeItem super_root = new TreeItem(ROOT, "Root node");
	
	@Override
	public HashMap<String, TreeItem> getCodingSchemeValueSetTree(String scheme, String version) {

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
		ValueSetHierarchyDao vsDao = getDaoManager().getCurrentValueSetHiearchyDao();
		//Get scheme UID from scheme, version 
		//Get associationPredicateGuid from association name
		String schemeUID = getDaoManager().getCurrentCodingSchemeDao().getCodingSchemeUIdByNameAndVersion(scheme, version);
		String associationPredicateGuid = getDaoManager().getCurrentAssociationDao().getAssociationPredicateUidsForAssociationName(schemeUID, null, association).get(0);
		
		List<VSHierarchyNode> nodes = vsDao.getAllVSTriplesTrOfVSNode(schemeUID, code, 
				associationPredicateGuid, sourceDesignation, publishName, 0, -1);
		//process nodes to remove duplicate description/code sets where source does not exist
		List<VSHierarchyNode> temps = collectReducedNodes( nodes);
		
		for(VSHierarchyNode n: nodes){			
			map.put(getURIFromVSHeirarchyNode(n), new TreeItem(n.getEntityCode(), 
					n.getDescription()));
		}
		
		return map;
	}
	
	private String getURIFromVSHeirarchyNode(VSHierarchyNode n) {
	       return AssertedValueSetServices.createUri(AssertedValueSetServices.BASE,
	    		   n.getSource(), n.getEntityCode());
	}
	
	protected List<VSHierarchyNode> collectReducedNodes(List<VSHierarchyNode> nodes){
		//Get all nodes with declared sources
		List<VSHierarchyNode> temps = nodes.stream().filter(x -> x.getSource() != null).collect(Collectors.toList());
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

	protected Entity getEntityForCode(String code, String codingScheme, String version) throws LBException{
		CodedNodeSet set = LexBIGServiceImpl.getCodingSchemeConcepts(codingScheme, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(version));
		set = set.restrictToCodes(Constructors.createConceptReferenceList(code));
		ResolvedConceptReferencesIterator it = set.resolve(null, null, null);
		return it.next().getEntity();
		
	}
	
	public static void main(String[] args){
		List<VSHierarchyNode> nodes = new ArrayList<VSHierarchyNode>();
		
		VSHierarchyNode node = new VSHierarchyNode();
		VSHierarchyNode node2 = new VSHierarchyNode();
		VSHierarchyNode node3 = new VSHierarchyNode();
		VSHierarchyNode node4 = new VSHierarchyNode();
		VSHierarchyNode node5 = new VSHierarchyNode();
		
		node.setDescription("apples");
		node2.setDescription("oranges");
		node3.setDescription("apples");
		node4.setDescription("oranges");
		node5.setDescription("banana");
		
		node.setEntityCode("C1");
		node2.setEntityCode("C2");
		node3.setEntityCode("C1");
		node4.setEntityCode("C2");
		node5.setEntityCode("C3");
		
		node.setSource("CDISC");
		node2.setSource("FDA");
		
		nodes.add(node);
		nodes.add(node2);
		nodes.add(node3);
		nodes.add(node4);
		nodes.add(node5);
		List<VSHierarchyNode> temps = nodes.stream().filter(x -> x.getSource() != null).collect(Collectors.toList());
		List<VSHierarchyNode> complete = new ValueSetHierarchServiceImpl().collectReducedNodes(nodes);
		
		complete.stream().forEach(x -> System.out.println(x.getDescription() + ": "+ x.getEntityCode() + ": " + x.getSource()));

	}

}
