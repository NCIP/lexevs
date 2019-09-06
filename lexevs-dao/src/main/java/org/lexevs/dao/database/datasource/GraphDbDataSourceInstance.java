package org.lexevs.dao.database.datasource;

import org.lexevs.logging.Logger;
import org.lexevs.system.constants.SystemVariables;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;

public class GraphDbDataSourceInstance {
	
	private String GRAPH_DB_USER;
	private String GRAPH_DB_PWD;
	private String GRAPH_DB_URL;
	private String GRAPH_DB_PORT;
	private String GRAPH_DB_MAX_CONNECTIONS;
	private String GRAPH_DB_CONNECTION_TIMEOUT_LENGTH;
	private String GRAPH_DB_NAME;
	private ArangoDB arangoDb;
	private ArangoDatabase dbInstance;
	
	public GraphDbDataSourceInstance(String DB_NAME){
		this.GRAPH_DB_NAME = DB_NAME;
		init();
	}

	public GraphDbDataSourceInstance(String DB_USER,
			String DB_PWD,
			String DB_URL,
			String DB_PORT,
			String DB_MAX_CONNECTIONS,
			String DB_CONNECTION_TIMEOUT_LENGTH, 
			String DB_NAME){
		this.GRAPH_DB_USER = DB_USER;
		this.GRAPH_DB_PWD = DB_PWD;
		this.GRAPH_DB_URL = DB_URL;
		this.GRAPH_DB_PORT = DB_PORT;
		this.GRAPH_DB_MAX_CONNECTIONS = DB_MAX_CONNECTIONS;
		this.GRAPH_DB_CONNECTION_TIMEOUT_LENGTH = DB_CONNECTION_TIMEOUT_LENGTH;	
		this.GRAPH_DB_NAME = DB_NAME;
		initArangoDB();
	}
	
	
	private void init() {
		SystemVariables vars = null;
		try {
			vars = new SystemVariables(new Logger());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.GRAPH_DB_USER = vars.getGraphdbUser();
		this.GRAPH_DB_PWD = vars.getGraphdbpwd();
		this.GRAPH_DB_URL = vars.getGraphdbUrl();
		this.GRAPH_DB_PORT = vars.getGraphdbPort();
		this.GRAPH_DB_MAX_CONNECTIONS = vars.getGraphdbMaxConnections();
		this.GRAPH_DB_CONNECTION_TIMEOUT_LENGTH = vars.getGraphdbConnectTimeOutLength();	
		initArangoDB();
	}
	
	private void initArangoDB(){
		if(GRAPH_DB_URL == null || 
				GRAPH_DB_USER == null || 
				GRAPH_DB_PORT == null|| 
				GRAPH_DB_PWD == null ||
				GRAPH_DB_MAX_CONNECTIONS == null ||
				GRAPH_DB_CONNECTION_TIMEOUT_LENGTH == null
				){
			throw new RuntimeException("Some graphdb configurations could not be found."
					+ "  Check configuration file and restart");
		}
		this.arangoDb = new ArangoDB
				.Builder()
				.host(GRAPH_DB_URL, 
						Integer.valueOf(GRAPH_DB_PORT).intValue())
				.user(GRAPH_DB_USER)
				.password(GRAPH_DB_PWD)
				.maxConnections(
						Integer.valueOf(GRAPH_DB_MAX_CONNECTIONS).intValue())
				.connectionTtl(
						Integer.valueOf(GRAPH_DB_CONNECTION_TIMEOUT_LENGTH).longValue())
				.build();
		if(arangoDb.getDatabases().contains(GRAPH_DB_NAME)){
			this.dbInstance = arangoDb.db(GRAPH_DB_NAME);
			}
		else {
			try {
				arangoDb.createDatabase(GRAPH_DB_NAME);
				this.dbInstance = arangoDb.db(GRAPH_DB_NAME);
			} catch (ArangoDBException e) {
				System.out.println("A database by this name already exists" + 
			" no duplicate connection will be made:  "
						+ GRAPH_DB_NAME);
				return;
			}

		}
	}

	/**
	 * @return the arangoDb
	 */
	public ArangoDB getArangoDb() {
		return arangoDb;
	}

	/**
	 * @return the dbInstance
	 */
	public ArangoDatabase getDbInstance() {
		return dbInstance;
	}


}
