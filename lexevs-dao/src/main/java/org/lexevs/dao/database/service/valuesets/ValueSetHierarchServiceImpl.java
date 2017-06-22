package org.lexevs.dao.database.service.valuesets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.locator.LexEvsServiceLocator;

import gov.nih.nci.evs.browser.utils.TreeItem;



public class ValueSetHierarchServiceImpl extends AbstractDatabaseService implements ValueSetHierarchyService {
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
	protected HashMap<String, TreeItem>  getHierarchyValueSetRoots(String Scheme, String version, String association, String code){
		HashMap<String, TreeItem> map = new HashMap<String,TreeItem>();
		
		return map;
	}

}
