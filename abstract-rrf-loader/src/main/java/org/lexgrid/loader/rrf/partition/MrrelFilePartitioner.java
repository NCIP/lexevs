
package org.lexgrid.loader.rrf.partition;

import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class MrrelFilePartitioner.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrrelFilePartitioner implements DataPartitioner<Mrrel> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.partition.DataPartitioner#getPartitionNumberForInputRow(java.lang.Object)
	 */
	public int getPartitionNumberForInputRow(Mrrel item) {
		String cui = item.getCui1();
		return getLastNumberOfCui(cui);
	}
	
	/**
	 * Gets the last number of cui.
	 * 
	 * @param cui the cui
	 * 
	 * @return the last number of cui
	 */
	public static int getLastNumberOfCui(String cui){
		String lastLetterOfCui = cui.substring(cui.length()-1);
		return Integer.valueOf(lastLetterOfCui);
	}
}