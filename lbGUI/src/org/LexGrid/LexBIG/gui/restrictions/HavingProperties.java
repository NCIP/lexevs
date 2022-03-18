
package org.LexGrid.LexBIG.gui.restrictions;

import java.util.ArrayList;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;

/**
 * Class to hold restrictToHavingProperties types of restrictions.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class HavingProperties extends Restriction {
	public ArrayList<String> properties;
	public ArrayList<PropertyType> propertyTypes;
	public ArrayList<String> sources;
	public ArrayList<String> usageContexts;
	public String propertyQualifier;
	public String propertyQualifierValue;

	@Override
	public String toString() {
		return "Having properties '...'";
		// TODO implement this to string...
	}
}