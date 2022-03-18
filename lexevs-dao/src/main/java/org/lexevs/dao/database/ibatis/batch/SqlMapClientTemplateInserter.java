
package org.lexevs.dao.database.ibatis.batch;

import org.lexevs.dao.database.inserter.Inserter;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * The Class SqlMapClientTemplateInserter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SqlMapClientTemplateInserter implements Inserter{

	/** The sql map client template. */
	private SqlMapClientTemplate sqlMapClientTemplate;
	
	/**
	 * Instantiates a new sql map client template inserter.
	 * 
	 * @param sqlMapClientTemplate the sql map client template
	 */
	public SqlMapClientTemplateInserter(SqlMapClientTemplate sqlMapClientTemplate){
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.ibatis.batch.Inserter#insert(java.lang.String, java.lang.Object)
	 */
	public void insert(String sql, Object parameter) {
		sqlMapClientTemplate.insert(sql, parameter);
	}

	/**
	 * Sets the sql map client template.
	 * 
	 * @param sqlMapClientTemplate the new sql map client template
	 */
	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	/**
	 * Gets the sql map client template.
	 * 
	 * @return the sql map client template
	 */
	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}

}