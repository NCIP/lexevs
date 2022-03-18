
package org.LexGrid.LexBIG.gui.restrictions;

import org.apache.commons.lang.StringUtils;

/**
 * Holder to store the Association restrictions.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class Association extends Restriction {
	public String[] associations;
	public String[] associationQualifiers;
	public String associationQualifierValue;

	@Override
	public String toString() {
		// Begin quoted value ...
		StringBuffer temp = new StringBuffer("Matching Associations '");

		// Start with association names ...
		for (int i = 0; i < associations.length; i++) {
			temp.append(associations[i]);
			if (associations.length != i + 1)
				temp.append(", ");
		}
		// Add association names ...
		for (int i = 0; i < associationQualifiers.length; i++) {
			if (i == 0)
				temp.append(": ");
			temp.append(associationQualifiers[i]);
			if (associationQualifiers.length != i + 1)
				temp.append(", ");
		}
		// Finally, append the qualifier value (if present)
		if (StringUtils.isNotBlank(associationQualifierValue))
			temp.append("(").append(associationQualifierValue).append(")");

		// End quoted value and return
		temp.append('\'');
		return StringUtils.abbreviate(temp.toString(), 64);
	}
}