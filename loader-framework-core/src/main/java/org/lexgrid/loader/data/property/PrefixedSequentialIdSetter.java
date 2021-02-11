
package org.lexgrid.loader.data.property;

import java.util.List;

import org.LexGrid.commonTypes.Property;

/**
 * The Class PrefixedSequentialIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PrefixedSequentialIdSetter implements ListIdSetter<Property> {

	/** The prefix. */
	private String prefix = "P";

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.property.ListIdSetter#addIds(java.util.List)
	 */
	public void addIds(List<Property> items) {
		int counter = 1;
		
		for(Property idable : items){
			idable.setPropertyId(prefix + "-" + counter++);
		}
	}

	/**
	 * Gets the prefix.
	 * 
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets the prefix.
	 * 
	 * @param prefix the new prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}