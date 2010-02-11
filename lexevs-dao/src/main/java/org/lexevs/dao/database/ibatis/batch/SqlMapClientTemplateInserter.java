package org.lexevs.dao.database.ibatis.batch;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class SqlMapClientTemplateInserter implements IbatisInserter{

	private SqlMapClientTemplate sqlMapClientTemplate;
	
	public SqlMapClientTemplateInserter(SqlMapClientTemplate sqlMapClientTemplate){
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}
	
	public void insert(String sql, Object parameter) {
		sqlMapClientTemplate.insert(sql, parameter);
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}

}
