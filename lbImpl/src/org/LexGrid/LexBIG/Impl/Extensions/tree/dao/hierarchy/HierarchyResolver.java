
package org.LexGrid.LexBIG.Impl.Extensions.tree.dao.hierarchy;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.naming.SupportedHierarchy;

/**
 * The Interface HierarchyResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface HierarchyResolver extends Serializable {

	/**
	 * Gets the hierarchy.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param hierarchyId the hierarchy id
	 * 
	 * @return the hierarchy
	 */
	public SupportedHierarchy getHierarchy(
			String codingScheme, 
			CodingSchemeVersionOrTag versionOrTag,
			String hierarchyId);
	
	/**
	 * Gets the hierarchies.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * 
	 * @return the hierarchies
	 */
	public SupportedHierarchy[] getHierarchies(String codingScheme, CodingSchemeVersionOrTag versionOrTag);
}