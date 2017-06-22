package org.lexevs.dao.database.service.valuesets;

import java.util.HashMap;
import java.util.HashSet;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.valueSets.ValueSetDefinition;

import gov.nih.nci.evs.browser.utils.TreeItem;

public interface ValueSetHierarchyService {
	public  static final String ROOT = "<Root>";
    public final static String INVERSE_IS_A = "inverse_is_a";
	
	//TODO required
	HashMap<String, TreeItem> getCodingSchemeValueSetTree(String scheme, String version);
	
	void preprocessSourceHierarchyData();
	
	HashSet<String> getValueSetParticipationHashSet();
	
	void initializeCS2vsdURIs_map();
	
	HashMap<String, TreeItem> getSourceValueSetTree(String Scheme, String version);
	
	
	//TODO Useful to have?
	ResolvedConceptReferenceList getValueSetHierarchyRoots();

	TreeItem getVSDChildNodeBySource(ValueSetDefinition vsd);

	TreeItem getSourceValueSetTreeBranch(ValueSetDefinition vsd);

	TreeItem getCodingSchemeValueSetTreeBranch(String scheme, String code, String name);


}