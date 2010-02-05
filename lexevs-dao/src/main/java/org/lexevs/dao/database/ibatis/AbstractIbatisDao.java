package org.lexevs.dao.database.ibatis;

import org.lexevs.dao.database.access.AbstractBaseDao;
import org.lexevs.dao.database.key.Java5UUIDKeyGenerator;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public abstract class AbstractIbatisDao extends AbstractBaseDao {

	private SqlMapClientTemplate sqlMapClientTemplate;

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}
	

}
