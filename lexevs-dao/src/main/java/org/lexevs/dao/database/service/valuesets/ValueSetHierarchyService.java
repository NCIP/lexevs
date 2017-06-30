package org.lexevs.dao.database.service.valuesets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;

import gov.nih.nci.evs.browser.utils.TreeItem;

public interface ValueSetHierarchyService {
	public static final String SCHEME = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
	public static final String HIERARCHY = "subClassOf";
	public static final String SOURCE = "Contributing_Source";
	public static final String PUBLISH_DESIGNATION = "Publish_Value_Set";
	public static final String CAN_PUBLISH = "Yes";
	public static final String ROOT_CODE = "C54443";
	public  static final String ROOT = "<Root>";
    public final static String INVERSE_IS_A = "inverse_is_a";
	
	//TODO required
	HashMap<String, TreeItem> getCodingSchemeValueSetTree(String scheme, String version) throws LBException;
	
	void preprocessSourceHierarchyData();
	
	HashSet<String> getValueSetParticipationHashSet();
	
	void initializeCS2vsdURIs_map();
	
	HashMap<String, TreeItem> getSourceValueSetTree(String Scheme, String version) throws LBException;
	
	
	//TODO Useful to have?
	ResolvedConceptReferenceList getValueSetHierarchyRoots();

	TreeItem getVSDChildNodeBySource(ValueSetDefinition vsd);

	TreeItem getSourceValueSetTreeBranch(ValueSetDefinition vsd);
	

	public List<VSHierarchyNode> getSourceValueSetTreeBranch(VSHierarchyNode node, TreeItem ti);


	public TreeItem getSourceValueSetTreeBranch(TreeItem ti);

	TreeItem getCodingSchemeValueSetTreeBranch(String scheme, String code, String name);


}