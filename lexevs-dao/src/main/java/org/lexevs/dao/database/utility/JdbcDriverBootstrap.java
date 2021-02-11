
package org.lexevs.dao.database.utility;

import org.lexevs.logging.Logger;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.FactoryBean;

/**
 * The Class JdbcDriverBootstrap.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class JdbcDriverBootstrap implements FactoryBean{
	
	private static String MYSQL_DRIVER_URL = "https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.6/mysql-connector-java-5.1.6.jar";
	private static String ORACLE_DRIVER_URL = "http://www.oracle.com/technetwork/database/features/jdbc/index-091264.html";

	/** The driver class. */
	private String driverClass;
	
	private Logger logger;
	
	/** The class loader. */
	private ClassLoader classLoader;
	
	private SystemVariables systemVariables;

	/**
	 * Gets the driver class.
	 * 
	 * @return the driver class
	 */
	public String getDriverClass() {
		return driverClass;
	}

	/**
	 * Sets the driver class.
	 * 
	 * @param driverClass the new driver class
	 */
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		try {
			return Class.forName(driverClass, true, classLoader);
		} catch (ClassNotFoundException e) {
			logger.error(this.printError());
			
			System.exit(1);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return Class.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}
	
	protected String printError() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("\n");
		sb.append("=============Fatal-Database-Connection-Error===================");
		sb.append("\n");
		sb.append("An error occured while validating the Database Connection.");
		sb.append("\n");
		sb.append("\n");
		sb.append("The driver class: " + this.driverClass + " was not found.");
		sb.append("\n");
		sb.append("\n");
		sb.append("This most likely means that you do not have the appropriate");
		sb.append("\n");
		sb.append("JDBC driver jar available on the classpath.");
		sb.append("\n");
		sb.append(getDriverHint());
		sb.append("\n");
		sb.append("To make other changes to your Database connection parameters,\n");
		sb.append("edit the 'lbconfig.props' file at:\n");
		sb.append(this.systemVariables.getConfigFileLocation());
		sb.append("\n");
		sb.append("=============================================================");
		
		return sb.toString();
	}
	
	private String getDriverHint() {
		StringBuffer sb = new StringBuffer();
		
		String jdbcUrl = this.systemVariables.getAutoLoadDBURL().toLowerCase();
		String db = "UNKNOWN";
		String url = null;
		if(jdbcUrl.contains("mysql")) {
			db = "MYSQL";
			url = MYSQL_DRIVER_URL;
		} else if(jdbcUrl.contains("oracle")) {
			db = "ORACLE";
			url = ORACLE_DRIVER_URL;
		} else if(jdbcUrl.contains("db2")) {
			db = "DB2";
		} else if(jdbcUrl.contains("postgresql")) {
			db = "POSTGRESQL";
		} else if(jdbcUrl.contains("hsqldb")) {
			db = "HSQLDB";
		}

		sb.append("\n");
		sb.append("**RECOMMENDED SOLUTION**");
		sb.append("\n");
		sb.append(" * You are attempting to connect to a " + db + " datbase.");
		sb.append("\n");
		if(url != null) {
			sb.append(" * We recommend downloading a " + db + " driver from: ");
			sb.append("\n");
			sb.append(" * - " + url);
			sb.append("\n");
			sb.append(" * After obtaining the driver jar, place it in the /runtime/sqlDrivers directory of your LexEVS installation.");
		} else {
			sb.append(" * Please see your database vendor documentation for JDBC Driver locations.");
		}
		sb.append("\n");
		
		return sb.toString();
	}

	/**
	 * Sets the class loader.
	 * 
	 * @param classLoader the new class loader
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * Gets the class loader.
	 * 
	 * @return the class loader
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Logger getLogger() {
		return logger;
	}
}