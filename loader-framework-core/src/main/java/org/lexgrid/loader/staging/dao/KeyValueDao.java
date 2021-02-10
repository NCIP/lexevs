
package org.lexgrid.loader.staging.dao;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * The Class KeyValueDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class KeyValueDao extends JdbcDaoSupport {
	
	/** The table name. */
	private String tableName = "stagingkeyvalue";
	
	/** The sql. */
	final String sql = "SELECT value from " + tableName +" where key = ?";

	/**
	 * Gets the value.
	 * 
	 * @param key the key
	 * 
	 * @return the value
	 */
	public String getValue(String key) {
		return (String)getJdbcTemplate().queryForObject(sql, new Object[] { key }, String.class);
	}
}