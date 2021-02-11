
package org.LexGrid.LexBIG.Impl.Extensions.tree.dao.pathtoroot;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;

/**
 * The Interface PathToRootResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PathToRootResolver extends Serializable {

	/**
	 * Gets the path to root.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * 
	 * @return the path to root
	 */
	public List<LexEvsTreeNode> getPathToRoot(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code, String namespace);
}