package org.lexevs.dao.database.ibatis.mybatis.association.batch;

import static org.lexevs.dao.database.ibatis.mybatis.association.batch.InsertAssociationTargetDynamicBatch.*;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.executor.BatchResult;
import org.lexevs.dao.database.ibatis.association.parameter.InsertOrUpdateAssociationTargetBean;
import org.lexevs.dao.database.ibatis.mybatis.association.batch.InsertAssociationTargetDynamicBatch.EntityAssnsToEntity;
import org.mybatis.dynamic.sql.insert.render.GeneralInsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertSelectStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface AssociationTargetMapper extends CommonInsertMapper<InsertOrUpdateAssociationTargetBean>{

   //@InsertProvider(type=SqlProviderAdapter.class, method="insert")
	//public int insert(InsertStatementProvider<InsertOrUpdateAssociationTargetBean> record);
    

	default List<BatchResult> flush() {
		// TODO Auto-generated method stub
		return null;
	}


	default int generalInsert(GeneralInsertStatementProvider arg0) {
		// TODO Auto-generated method stub
		return 0;
	}


	default int insert(InsertOrUpdateAssociationTargetBean record) {
	      return MyBatis3Utils.insert(this::insert, record, entityAssnsToEntity, c ->
          c.map(entityAssnsGuid).toProperty("uid")
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
          );
	}


	default int insertMultiple(MultiRowInsertStatementProvider<InsertOrUpdateAssociationTargetBean> arg0) {
		// TODO Auto-generated method stub
		return 0;
	}


	default int insertSelect(InsertSelectStatementProvider arg0) {
		// TODO Auto-generated method stub
		return 0;
	}



}
