
package org.LexGrid.LexBIG.gui.restrictions;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;

/**
 * Holder to store the RestrictToStatus restrictions.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class Status extends Restriction {
	public String conceptStatus;
	public ActiveOption activeOption;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (activeOption != null) {
			sb.append(activeOption.toString() + ", ");
		}
		if (conceptStatus != null && conceptStatus.length() > 0) {
			sb.append(conceptStatus + ", ");

		}
		return "Status '" + sb.toString() + "'";
	}

}