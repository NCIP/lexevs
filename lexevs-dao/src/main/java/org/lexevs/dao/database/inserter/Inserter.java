
package org.lexevs.dao.database.inserter;

/**
 * The Interface Inserter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface Inserter {

	/**
	 * Insert.
	 * 
	 * @param sql the sql
	 * @param parameter the parameter
	 */
	public void insert(String sql, Object parameter);
}