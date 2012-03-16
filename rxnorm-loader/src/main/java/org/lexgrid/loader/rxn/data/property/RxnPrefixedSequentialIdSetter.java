package org.lexgrid.loader.rxn.data.property;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.lexgrid.loader.data.property.ListIdSetter;
import org.lexgrid.loader.wrappers.ParentIdHolder;

public class RxnPrefixedSequentialIdSetter implements ListIdSetter<ParentIdHolder<Property>> {

	/** The prefix. */
	private String prefix = "P";
	
	public void addIds(List<ParentIdHolder<Property>> items) {
		int counter = 1;
		
		for(ParentIdHolder<Property> idable : items){
			idable.getItem().setPropertyId(prefix + "-" + counter++);
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
