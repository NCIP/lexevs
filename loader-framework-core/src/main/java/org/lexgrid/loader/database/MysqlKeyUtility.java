package org.lexgrid.loader.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.LexGrid.persistence.dao.LexEvsDao;
import org.LexGrid.persistence.model.CodingScheme;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.util.sql.lgTables.SQLTableUtilities;
import org.lexgrid.loader.connection.LoaderConnectionManager;
import org.lexgrid.loader.connection.impl.LexEVSConnectionManager;
import org.lexgrid.loader.data.codingScheme.CodingSchemeNameSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class MysqlKeyUtility extends JdbcDaoSupport {
	
	/** The connection manager. */
	private LoaderConnectionManager connectionManager = new LexEVSConnectionManager();
	
	/** The lex evs dao. */
	private LexEvsDao lexEvsDao;
	
	/** The coding scheme name setter. */
	private CodingSchemeNameSetter codingSchemeNameSetter;
	
	protected enum KEYACTION {ENABLE, DISABLE};
	
	public void disableKeys() throws Exception{
		List<String> tableNames  = getTableNames();
		for(String tableName : tableNames){
			alterTables(tableName, KEYACTION.DISABLE);
		}	
	}
	
	public void enableKeys() throws Exception{
		List<String> tableNames  = getTableNames();
		for(String tableName : tableNames){
			alterTables(tableName, KEYACTION.ENABLE);
		}
	}
	
	protected void alterTables(String tableName, KEYACTION action){
		getJdbcTemplate().execute(
				"alter table " + tableName + " " + action.toString() + " keys"
				);
	}
	
	protected List<String> getTableNames() throws Exception {
		List<String> returnList = new ArrayList<String>();
		connectionManager
		.getExistingConnectionInfo(getCurrentCodingSchemeUri(), getCurrentCodingSchemeVersion());
		
		SQLTableUtilities utilities = connectionManager
			.getSQLTableUtilities(getCurrentCodingSchemeUri(), 
					getCurrentCodingSchemeVersion());
		
		SQLTableConstants constants = connectionManager
		.getSQLTableConstants(getCurrentCodingSchemeUri(), 
				getCurrentCodingSchemeVersion());
		
		Set<String> tableKeys = utilities.getDefaultTableKeys();
		for(String key : tableKeys){
			returnList.add(constants.getTableName(key));
		}
		return returnList;
	}
	

	/**
	 * Gets the current coding scheme uri.
	 * 
	 * @return the current coding scheme uri
	 * 
	 * @throws Exception the exception
	 */
	protected String getCurrentCodingSchemeUri() throws Exception {	
		return getCurrentCodingScheme().getCodingSchemeUri();
	}
	
	/**
	 * Gets the current coding scheme version.
	 * 
	 * @return the current coding scheme version
	 * 
	 * @throws Exception the exception
	 */
	protected String getCurrentCodingSchemeVersion() throws Exception {
		return getCurrentCodingScheme().getRepresentsVersion();
	}
	
	
	/**
	 * Gets the current coding scheme.
	 * 
	 * @return the current coding scheme
	 * 
	 * @throws Exception the exception
	 */
	protected CodingScheme getCurrentCodingScheme() throws Exception {
		return lexEvsDao.findById(CodingScheme.class, codingSchemeNameSetter.getCodingSchemeName());
	}

	public LoaderConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public void setConnectionManager(LoaderConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	public LexEvsDao getLexEvsDao() {
		return lexEvsDao;
	}

	public void setLexEvsDao(LexEvsDao lexEvsDao) {
		this.lexEvsDao = lexEvsDao;
	}

	public CodingSchemeNameSetter getCodingSchemeNameSetter() {
		return codingSchemeNameSetter;
	}

	public void setCodingSchemeNameSetter(
			CodingSchemeNameSetter codingSchemeNameSetter) {
		this.codingSchemeNameSetter = codingSchemeNameSetter;
	}	
}
