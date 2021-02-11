
package org.lexgrid.loader.data.property;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class ParameterizedSequentialIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ParameterizedSequentialIdSetter implements ParameterizedListIdSetter<ParentIdHolder<Property>>{

	/** The prefix. */
	private String prefix = "P";
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.property.ParameterizedListIdSetter#addIds(java.util.List, java.lang.String)
	 */
	public void addIds(List<ParentIdHolder<Property>> props, String parameter) {
		int counter = 1;
		
		for(ParentIdHolder<Property> prop : props){
			prop.getItem().setPropertyId(prefix + "-" + parameter + "-" +  counter++);
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