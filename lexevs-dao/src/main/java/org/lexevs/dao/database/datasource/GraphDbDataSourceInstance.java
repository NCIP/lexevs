package org.lexevs.dao.database.datasource;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.LogManager;
import org.lexevs.logging.Logger;
import org.lexevs.system.constants.SystemVariables;

public class GraphDbDataSourceInstance {
	
	private String GRAPH_DB_USER;
	private String GRAPH_DB_PWD;
	private String GRAPH_DB_URL;
	private String GRAPH_DB_PORT;
	private String GRAPH_DB_MAX_CONNECTIONS;
	private String GRAPH_DB_CONNECTION_TIMEOUT_LENGTH;
	private String GRAPH_DB_NAME;
	
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
		
	}


}
