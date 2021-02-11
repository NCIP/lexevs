
package org.LexGrid.LexBIG.gui.restrictions;

import org.apache.commons.lang.StringUtils;

/**
 * Holder to store the DirectionalName restrictions.
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class DirectionalName extends Restriction {
	public String[] directionalNames;
	public String[] associationQualifiers;
	public String associationQualifierValue;

	@Override
	public String toString() {
		// Begin quoted value ...
		StringBuffer temp = new StringBuffer("Matching directionalNames '");

		// Start with directional names ...
		for (int i = 0; i < directionalNames.length; i++) {
			temp.append(directionalNames[i]);
			if (directionalNames.length != i + 1)
				temp.append(", ");
		}
		// Add association qualifiers ...
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