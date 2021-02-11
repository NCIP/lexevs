
package org.LexGrid.LexBIG.Extensions.Generic;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;

/**
 * A grouping of Coding Scheme Supplement related functionality.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface SupplementExtension extends GenericExtension {

    /**
     * Checks if a given coding scheme is a supplement.
     * 
     * @param codingScheme the coding scheme
     * @param tagOrVersion the tag or version
     * 
     * @return true, if is supplement
     * 
     * @throws LBParameterException the LB parameter exception
     */
	public boolean isSupplement(String codingScheme,
			CodingSchemeVersionOrTag tagOrVersion) throws LBParameterException;

    /**
     * Gets the parent of a supplement.
     * 
     * @param codingScheme the coding scheme
     * @param tagOrVersion the tag or version
     * 
     * @return the parent of supplement
     * 
     * @throws LBParameterException the LB parameter exception
     */
	public AbsoluteCodingSchemeVersionReference getParentOfSupplement(
			String codingScheme, CodingSchemeVersionOrTag tagOrVersion)
			throws LBParameterException;
}