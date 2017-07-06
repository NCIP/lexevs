package org.lexevs.dao.database.service.valuesets;

import java.util.HashMap;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;


public interface ValueSetHierarchyService {
	public static final String SCHEME = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
	public static final String HIERARCHY = "subClassOf";
	public static final String SOURCE = "Contributing_Source";
	public static final String PUBLISH_DESIGNATION = "Publish_Value_Set";
	public static final String CAN_PUBLISH = "Yes";
	public static final String ROOT_CODE = "C54443";
	public  static final String ROOT = "<Root>";
    public final static String INVERSE_IS_A = "inverse_is_a";
	
	void preprocessSourceHierarchyData();
	
	HashMap<String, LexEVSTreeItem> getSourceValueSetTree(String Scheme, String version) throws LBException;

	public HashMap<String, LexEVSTreeItem> getHierarchyValueSetRoots(
			String scheme, 
			String version, 
			String association,
			String sourceDesignation,
			String publishName,
			String code) throws LBException;

	public List<VSHierarchyNode> getSourceValueSetTreeBranch(VSHierarchyNode node, LexEVSTreeItem ti);

}