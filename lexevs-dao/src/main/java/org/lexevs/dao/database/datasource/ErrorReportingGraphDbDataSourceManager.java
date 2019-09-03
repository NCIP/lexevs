package org.lexevs.dao.database.datasource;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;

public class ErrorReportingGraphDbDataSourceManager implements InitializingBean {
	
	ConcurrentHashMap<String, String> dbCache = new ConcurrentHashMap<String, String>();
	
	

	@Override
	public void afterPropertiesSet() throws Exception {
//		Assert.notNull(this.decoratoredDataSource);
		
//		try {
//			connection = this.decoratoredDataSource.getConnection();
//
//			JdbcUtils.closeConnection(connection);
//		} catch (Exception e) {
//			JdbcUtils.closeConnection(connection);
//			logger.fatal(
//					this.printError(e));
//			System.exit(1);
//		} finally {
//			JdbcUtils.closeConnection(connection);
//		}
	}

	
}
