
package org.LexGrid.LexBIG.Impl.Extensions.tree.dao.prefixresolver;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;

/**
 * The Interface PrefixResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PrefixResolver extends Serializable {

	/**
	 * Gets the prefix.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param tagOrVersion the tag or version
	 * 
	 * @return the prefix
	 * 
	 * @throws Exception the exception
	 */
	public String getPrefix(String codingSchemeName, CodingSchemeVersionOrTag tagOrVersion) throws Exception;
}