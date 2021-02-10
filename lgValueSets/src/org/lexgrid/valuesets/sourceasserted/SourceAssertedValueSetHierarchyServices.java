
package org.lexgrid.valuesets.sourceasserted;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.lexevs.dao.database.access.association.model.VSHierarchyNode;
import org.lexevs.dao.database.service.valuesets.LexEVSTreeItem;

/**
 * @author m029206
 *
 */
public interface SourceAssertedValueSetHierarchyServices extends Serializable{
	
	
	/**
	 *  Initializes the hierarchy service for default NCIt values
	 */
	public void preprocessSourceHierarchyData();	

	/**
	 * @param scheme: supported scheme name or uri
	 * @param version: coding scheme version
	 * @param association: the association name that defines the hierarchy
	 * @param sourceDesignation: property name of any sources that define this value set
	 * @param publishName: property name for publishing flag
	 * @param root_code: root entity code for the hierarchy tree
	 * 
	 * Initializes the hierarchy service for a set of values
	 */
	public void preprocessSourceHierarchyData(String scheme, String version, String association, String sourceDesignation,
			String publishName, String root_code);
	
	/**
	 * @return HashMap of the root tree node with it's defining relationship or root designation
	 * @throws LBException when it fails to resolve coding schemes or entities
	 * 
	 * Returns only source asserted value set hierarchy
	 */
	public HashMap<String, LexEVSTreeItem> getSourceValueSetTree() throws LBException;
	
	/**
	 * @return HashMap of the root tree node with it's defining relationship or root designation
	 * @throws LBException when it fails to resolve coding schemes or entities
	 * 
	 * Returns source asserted value set hierarchy with the external value sets all under the
	 * root node
	 */
	public HashMap<String, LexEVSTreeItem> getFullServiceValueSetTree() throws LBException;
	
	/**
	 * @return HashMap of the root tree node with it's defining relationship or root designation
	 * @throws LBException  LBException when it fails to resolve coding schemes or entities
	 * 
	 * Returns tree with root nodes defined by the source asserted value set coding scheme
	 * and any other schemes from which regular value sets are derived
	 */
	public HashMap<String, LexEVSTreeItem> getSourceDefinedTree() throws LBException;
	
	/**
	 * @param code: entity code for root of source asserted root
	 * @return HashMap of string defining association and source asserted value set roots
	 * @throws LBException
	 * 
	 * returns the roots of the source asserted value set hierarchy
	 */
	public HashMap<String, LexEVSTreeItem> getHierarchyValueSetRoots(String code) throws LBException;

	/**
	 * @param node: defines the root of the branch of the value set hierarchy to be returned
	 * @param ti: a tree item to add the branch to as children
	 * @return A list of nodes added to a given tree item. If there are nodes to add the tree item
	 * is set to expandable = true.
	 */
	public List<VSHierarchyNode> getSourceValueSetTreeBranch(VSHierarchyNode node, LexEVSTreeItem ti);
	


}