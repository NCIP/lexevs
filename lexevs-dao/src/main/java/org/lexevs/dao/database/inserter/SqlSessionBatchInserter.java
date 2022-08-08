package org.lexevs.dao.database.inserter;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class SqlSessionBatchInserter implements MyBatisBatchInserter {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Override
	public void insert(String sql, Object parameter) {
		sqlSessionTemplate.insert(sql, parameter);

	}

}
