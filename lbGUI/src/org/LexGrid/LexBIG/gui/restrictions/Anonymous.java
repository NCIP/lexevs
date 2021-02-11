
package org.LexGrid.LexBIG.gui.restrictions;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.AnonymousOption;

/**
 * Holder to store the RestrictToStatus restrictions.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class Anonymous extends Restriction {

	public AnonymousOption anonymousOption;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (anonymousOption != null) {
			sb.append(anonymousOption.toString() + ", ");
		}
		
		return "Anonymous '" + sb.toString() + "'";
	}
}