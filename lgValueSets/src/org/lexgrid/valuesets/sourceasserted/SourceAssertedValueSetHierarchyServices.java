package org.lexgrid.valuesets.sourceasserted;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.service.valuesets.LexEVSTreeItem;

public interface SourceAssertedValueSetHierarchyServices extends Serializable{
	
	public void preprocessSourceHierarchyData();	

	public void preprocessSourceHierarchyData(String scheme, String version, String association, String sourceDesignation,
			String publishName, String root_code);
	
	public HashMap<String, LexEVSTreeItem> getSourceValueSetTree() throws LBException;
	
	public HashMap<String, LexEVSTreeItem> getFullServiceValueSetTree() throws LBException;
	
	public HashMap<String, LexEVSTreeItem> getHierarchyValueSetRoots(String code) throws LBException;

	public List<VSHierarchyNode> getSourceValueSetTreeBranch(VSHierarchyNode node, LexEVSTreeItem ti);
	
	public VSHierarchyNode addNodeToRoot(String URI, VSHierarchyNode root);


}
