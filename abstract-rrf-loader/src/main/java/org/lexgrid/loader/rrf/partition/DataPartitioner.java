
package org.lexgrid.loader.rrf.partition;

/**
 * The Interface DataPartitioner.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface DataPartitioner<I> {

	/**
	 * Gets the partition number for input row.
	 * 
	 * @param item the item
	 * 
	 * @return the partition number for input row
	 */
	public int getPartitionNumberForInputRow(I item);
}