package org.lexevs.dao.database.ibatis.mybatis.association.batch;

import static org.mybatis.dynamic.sql.insert.BatchInsertDSL.insert;
import static org.lexevs.dao.database.ibatis.mybatis.association.batch.InsertAssociationTargetDynamicBatch.*;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.ibatis.association.parameter.InsertOrUpdateAssociationTargetBean;


import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.mybatis.dynamic.sql.insert.render.BatchInsert;
import org.mybatis.dynamic.sql.render.RenderingStrategies;

public class MybatisAssociationBatchInsertDao extends AbstractIbatisDao  {
	
	private SqlSessionFactory sqlSessionFactory;

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertMybatisBatchAssociationTarget(List<InsertOrUpdateAssociationTargetBean> list) {
		
		  try(SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
		        AssociationTargetMapper mapper = session.getMapper(AssociationTargetMapper.class);


		        BatchInsert<InsertOrUpdateAssociationTargetBean> batchInsert = insert(list)
		                .into(entityAssnsToEntity)
		                .map(entityAssnsGuid).toProperty("uid")
		                .map(associationPredicateGuid).toProperty("associationPredicateUId")
		                .map(sourceEntityCode).toProperty("sourceEntityCode")
		                .map(sourceEntityCodeNamespace).toProperty("sourceEntityCodeNamespace")
		                .map(targetEntityCode).toProperty("targetEntityCode")
		                .map(targetEntityCodeNamespace).toProperty("targetEntityCodeNamespace")
		                .map(associationInstanceId).toProperty("associationInstanceId")
		                .map(isDefining).toProperty("isDefining")
		                .map(isInferred).toProperty("isInferred")
		                .map(isActive).toProperty("isActive")
		                .map(owner).toProperty("owner")
		                .map(status).toProperty("status")
		                .map(effectiveDate).toProperty("effectiveDate")
		                .map(expirationDate).toProperty("expirationDate")
		                .map(entryStateGuid).toProperty("entryStateUId")
		                .build()
		                .render(RenderingStrategies.MYBATIS3);

		        batchInsert.insertStatements().forEach(x -> mapper.insert(x));
		        session.commit();
		        session.close();
		  }
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

}
