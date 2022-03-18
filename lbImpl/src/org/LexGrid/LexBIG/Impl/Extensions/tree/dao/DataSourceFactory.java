
package org.LexGrid.LexBIG.Impl.Extensions.tree.dao;

import javax.sql.DataSource;

import org.LexGrid.LexBIG.Impl.Extensions.tree.service.ApplicationContextFactory;

/**
 * A factory for creating DataSource objects.
 */
public class DataSourceFactory {

	/** The instance. */
	private static DataSourceFactory instance;

	/**
	 * Gets the single instance of DataSourceFactory.
	 * 
	 * @return single instance of DataSourceFactory
	 */
	public static synchronized DataSourceFactory getInstance() {
		if (instance == null) {
			instance = new DataSourceFactory();
		}
		return instance;
	}
	
	/**
	 * Gets the lex evs tree dao.
	 * 
	 * @return the lex evs tree dao
	 */
	public DataSource getLexEvsTreeDao(){
		return (DataSource)ApplicationContextFactory.getInstance().getApplicationContext().getBean("dataSource");
	}
}