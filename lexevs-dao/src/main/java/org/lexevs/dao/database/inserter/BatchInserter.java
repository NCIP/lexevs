
package org.lexevs.dao.database.inserter;

/**
 * The Interface BatchInserter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface BatchInserter extends Inserter {

	/**
	 * Start batch.
	 */
	public void startBatch();
	
	/**
	 * Execute batch.
	 */
	public void executeBatch();
	
}