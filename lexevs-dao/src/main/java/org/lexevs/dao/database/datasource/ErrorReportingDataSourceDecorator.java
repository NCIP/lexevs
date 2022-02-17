
package org.lexevs.dao.database.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.commons.lang.StringUtils;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

public class ErrorReportingDataSourceDecorator implements DataSource, InitializingBean {

	private DataSource decoratoredDataSource;
	
	private SystemVariables systemVariables;
	
	private LgLoggerIF logger;
	
	public ErrorReportingDataSourceDecorator(DataSource decoratoredDataSource) {
		super();
		this.decoratoredDataSource = decoratoredDataSource;
	}

	/*
	 * This is for Java 7 - but we need to support Java 6 as well.
	 * 
	 * (non-Javadoc)
	 * @see javax.sql.CommonDataSource#getParentLogger()
	 */
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.decoratoredDataSource);
		
		Connection connection = null;
		try {
			connection = this.decoratoredDataSource.getConnection();

			JdbcUtils.closeConnection(connection);
		} catch (Exception e) {
			JdbcUtils.closeConnection(connection);
			logger.fatal(
					this.printError(e));
			System.exit(1);
		} finally {
			JdbcUtils.closeConnection(connection);
		}
	}
	
	protected String printError(Exception e) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("\n");
		sb.append("=============Fatal-Database-Connection-Error===================");
		sb.append("\n");
		sb.append("An error occured while validating the Database Connection.");
		sb.append("\n");
		
		if(StringUtils.isNotBlank(e.getMessage())) {
			sb.append("The Database reported an error message of:");
			sb.append("\n");
			sb.append(" * " + e.getMessage());
		} else {
			sb.append("The Database reported an error \n");
			sb.append("but did not provide an error message.");
			sb.append("\n");
			sb.append("The Exception Class was:");
			sb.append("\n");
			sb.append(e.getClass().getName());
		}
		sb.append("\n");
		sb.append("\n");
		sb.append("Your connection parameters are:");
		sb.append("\n");
		sb.append(" * Database URL: " + this.systemVariables.getAutoLoadDBURL());
		sb.append("\n");
		sb.append(" * Database Driver Class: " + this.systemVariables.getAutoLoadDBDriver());
		sb.append("\n");
		sb.append("User Name: " + this.systemVariables.getAutoLoadDBUsername());
		sb.append("\n");
		sb.append("\n");
		sb.append("Please check that the Database exists, and the Username\n");
		sb.append("and Password are correct.\n");
		sb.append("\n");
		sb.append("To make changes to your Database connection parameters,\n");
		sb.append("edit the 'lbconfig.props' file at:\n");
		sb.append(this.systemVariables.getConfigFileLocation());
		sb.append("\n");
		sb.append("=============================================================");
		
		return sb.toString();
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return this.decoratoredDataSource.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		return decoratoredDataSource.getConnection(username, password);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return this.decoratoredDataSource.getLogWriter();
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return this.decoratoredDataSource.getLoginTimeout();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		this.decoratoredDataSource.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		this.decoratoredDataSource.setLoginTimeout(seconds);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.decoratoredDataSource.isWrapperFor(iface);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return this.decoratoredDataSource.unwrap(iface);
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setLogger(LgLoggerIF logger) {
		this.logger = logger;
	}

	public LgLoggerIF getLogger() {
		return logger;
	}
}