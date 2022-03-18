
package org.LexGrid.LexBIG.gui.restrictions;

/**
 * Holder to store the MatchToCode restrictions.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class MatchingCode extends Restriction {
	public String codes;

	@Override
	public String toString() {
		return "Matching Codes '" + codes + "'";
	}

}