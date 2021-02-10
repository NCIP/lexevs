
package org.LexGrid.LexBIG.gui.restrictions;

/**
 * Holder for the restrictionToMatchingProperties types of restrictions.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class MatchingProperties extends HavingProperties {
	public String matchText, matchAlgorithm, language;

	@Override
	public String toString() {
		return "Properties matching '" + matchText + "'";
	}
}