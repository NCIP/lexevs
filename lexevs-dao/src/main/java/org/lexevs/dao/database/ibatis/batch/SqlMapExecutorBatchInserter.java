package org.lexevs.dao.database.ibatis.batch;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.SqlMapExecutor;

public class SqlMapExecutorBatchInserter implements IbatisBatchInserter {
	
	private SqlMapExecutor sqlMapExecutor;
	
	public SqlMapExecutorBatchInserter(SqlMapExecutor sqlMapExecutor){
		this.sqlMapExecutor = sqlMapExecutor;
	}

	public void executeBatch() {
		try {
			sqlMapExecutor.executeBatch();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	public void startBatch() {
		try {
			sqlMapExecutor.startBatch();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	public void insert(String sql, Object parameter) {
		try {
			sqlMapExecutor.insert(sql, parameter);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public SqlMapExecutor getSqlMapExecutor() {
		return sqlMapExecutor;
	}

	public void setSqlMapExecutor(SqlMapExecutor sqlMapExecutor) {
		this.sqlMapExecutor = sqlMapExecutor;
	}
	
	
}
