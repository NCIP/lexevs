
package org.lexgrid.loader.reader.support;

import java.util.List;

/**
 * The Class CompositeReaderChunk.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CompositeReaderChunk<I1,I2> {

	/** The item1 list. */
	private List<I1> item1List;
	
	/** The item2 list. */
	private List<I2> item2List;
	
	/**
	 * Gets the item1 list.
	 * 
	 * @return the item1 list
	 */
	public List<I1> getItem1List() {
		return item1List;
	}
	
	/**
	 * Sets the item1 list.
	 * 
	 * @param item1List the new item1 list
	 */
	public void setItem1List(List<I1> item1List) {
		this.item1List = item1List;
	}
	
	/**
	 * Gets the item2 list.
	 * 
	 * @return the item2 list
	 */
	public List<I2> getItem2List() {
		return item2List;
	}
	
	/**
	 * Sets the item2 list.
	 * 
	 * @param item2List the new item2 list
	 */
	public void setItem2List(List<I2> item2List) {
		this.item2List = item2List;
	}	
}