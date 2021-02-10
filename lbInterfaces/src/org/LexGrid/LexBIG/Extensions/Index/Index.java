
package org.LexGrid.LexBIG.Extensions.Index;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Extendable;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;

/**
 * Identifies expected behavior and an associated loader to build and maintain a
 * named index. Note that a single loader may be used to maintain multiple named
 * indexes.
 */
public interface Index extends Extendable {

	/**
	 * Returns an object used to administer and maintain the Index.
	 * 
	 * @return IndexLoader
	 */
	public IndexLoader getLoader();

	/**
	 * Return a coded node set containing all of the nodes that match the
	 * supplied criteria.
	 * 
	 * @param baseSet
	 *            Set of nodes to be searched.
	 * @param matchText
	 *            Filter string
	 * @param preferredOnly
	 *            True means match only preferred designations, false means all.
	 * @param language
	 *            Filter language, where appropriate
	 * @throws LBException
	 */
	public CodedNodeSet locateMatchingDesignations(CodedNodeSet baseSet,
			String matchText, boolean preferredOnly, String language)
			throws LBException;

	/**
	 * Return a coded node set containing all of the nodes that match the
	 * supplied criteria.
	 * 
	 * @param baseSet
	 *            Starting set to search
	 * @param propertyList
	 *            List of properties to include in the match. Absent means all
	 *            properties.
	 * @param matchText
	 *            Text String to use in the match
	 * @param language
	 *            Language code to match agains
	 * @throws LBException
	 */
	public CodedNodeSet locateMatchingProperties(CodedNodeSet baseSet,
			LocalNameList propertyList, String matchText, String language)
			throws LBException;

}