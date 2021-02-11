
package org.LexGrid.LexBIG.Impl.Extensions.tree.service;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Impl.Extensions.tree.evstree.EvsTreeConverter;
import org.LexGrid.LexBIG.Impl.Extensions.tree.json.JsonConverter;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTree;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;

/**
 * The Interface TreeService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
/**
 * @author bauerhs
 *
 */
public interface TreeService extends GenericExtension {

	/**
	 * Gets the tree.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * @param hierarchyId the hierarchy id
	 * 
	 * @return the tree
	 */
	public LexEvsTree getTree(
			String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code,
			String namespace,
			String hierarchyId);
	
	/**
	 * Gets the tree.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * 
	 * @return the tree
	 */
	public LexEvsTree getTree(
			String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code,
			String namespace);
	
	/**
	 * Gets the tree.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * 
	 * @return the tree
	 */
	public LexEvsTree getTree(
			String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code);
	
	/**
	 * Gets the sub concepts.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * @param hierarchyId the hierarchy id
	 * 
	 * @return the sub concepts
	 */
	public LexEvsTreeNode getSubConcepts(
			String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code,
			String namespace,
			String hierarchyId);
	
	/**
	 * Gets the sub concepts.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * 
	 * @return the sub concepts
	 */
	public LexEvsTreeNode getSubConcepts(
			String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code,
			String namespace);
	
	/**
	 * Gets the sub concepts.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * 
	 * @return the sub concepts
	 */
	public LexEvsTreeNode getSubConcepts(
			String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code);
	
	/**
	 * Gets the json converter.
	 * 
	 * @return the json converter
	 */
	public JsonConverter getJsonConverter();
	
	/**
	 * Gets the evsTree converter.
	 * 
	 * @return the evsTree converter
	 */
	public EvsTreeConverter getEvsTreeConverter();
	

    /**
     * Gets the evsTree converter
     * @param maxChildren
     * @return the evsTree converter
     */
    public JsonConverter getJsonConverter(int maxChildren);
    
 
    /**
     * Gets the json converter
     * @param maxChildren
     * @return the json converter
     */
    public EvsTreeConverter getEvsTreeConverter(int maxChildren);
}