
package org.LexGrid.LexBIG.gui.codeSet;

import java.util.Comparator;

/**
 * Comparator for sorting strings.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class StringComparator implements Comparator<String> {

	public int compare(String o1, String o2) {
		return ((String) o1).compareToIgnoreCase(((String) o2));
	}

}