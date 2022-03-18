
package org.LexGrid.LexBIG.gui.restrictions;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;

/**
 * Holder for the restrictToMatchingDesignation types of restrictions.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class MatchingDesignation extends Restriction {
	public String matchText, matchAlgorithm, language;
	public SearchDesignationOption searchDesignationOption;

	@Override
	public String toString() {
		return "Designations Matching '" + matchText + "'";
	}

}