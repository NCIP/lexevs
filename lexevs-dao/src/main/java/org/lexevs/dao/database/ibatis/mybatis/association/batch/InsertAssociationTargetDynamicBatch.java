package org.lexevs.dao.database.ibatis.mybatis.association.batch;

import java.util.Date;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;


public final class InsertAssociationTargetDynamicBatch {
	
    public static final EntityAssnsToEntity entityAssnsToEntity = new EntityAssnsToEntity();
    public static final SqlColumn<String> entityAssnsGuid = entityAssnsToEntity.entityAssnsGuid;
    public static final SqlColumn<String> associationPredicateGuid = entityAssnsToEntity.associationPredicateGuid;
    public static final SqlColumn<String> sourceEntityCode = entityAssnsToEntity.sourceEntityCode;
    public static final SqlColumn<String> sourceEntityCodeNamespace = entityAssnsToEntity.sourceEntityCodeNamespace;
    public static final SqlColumn<String> targetEntityCode = entityAssnsToEntity.targetEntityCode;
    public static final SqlColumn<String> targetEntityCodeNamespace = entityAssnsToEntity.targetEntityCodeNamespace;
    public static final SqlColumn<String> associationInstanceId = entityAssnsToEntity.associationInstanceId;
    public static final SqlColumn<Boolean> isDefining = entityAssnsToEntity.isDefining;
    public static final SqlColumn<Boolean> isInferred = entityAssnsToEntity.isInferred;
    public static final SqlColumn<Boolean> isActive = entityAssnsToEntity.isActive;
    public static final SqlColumn<String> owner = entityAssnsToEntity.owner;
    public static final SqlColumn<String> status = entityAssnsToEntity.status;
    public static final SqlColumn<Date> effectiveDate = entityAssnsToEntity.effectiveDate;
    public static final SqlColumn<Date> expirationDate = entityAssnsToEntity.expirationDate;
    public static final SqlColumn<String> entryStateGuid = entityAssnsToEntity.entryStateGuid;

    public static final class EntityAssnsToEntity extends SqlTable {
        public final SqlColumn<String> entityAssnsGuid = column("entityAssnsGuid");
        public final SqlColumn<String> associationPredicateGuid = column("associationPredicateGuid");
        public final SqlColumn<String> sourceEntityCode = column("sourceEntityCode");
        public final SqlColumn<String> sourceEntityCodeNamespace = column("sourceEntityCodeNamespace");
        public final SqlColumn<String> targetEntityCode = column("targetEntityCode");
        public final SqlColumn<String> targetEntityCodeNamespace = column("targetEntityCodeNamespace");
        public final SqlColumn<String> associationInstanceId = column("associationInstanceId");
        public final SqlColumn<Boolean> isDefining = column("isDefining");
        public final SqlColumn<Boolean> isInferred = column("isInferred");
        public final SqlColumn<Boolean> isActive = column("isActive");
        public final SqlColumn<String> owner = column("owner");
        public final SqlColumn<String> status = column("status");
        public final SqlColumn<Date> effectiveDate = column("effectiveDate");
        public final SqlColumn<Date> expirationDate = column("expirationDate");
        public final SqlColumn<String> entryStateGuid = column("entryStateGuid");

        public EntityAssnsToEntity() {
            super(BatchAssociationPrefixedTableNameSupplier::getResolvedStaticPrefix);
        }
  
    
    }
	
	
	
//    INSERT INTO ${prefix}entityAssnsToEntity (
//    entityAssnsGuid,
//  associationPredicateGuid,
//  sourceEntityCode,
//  sourceEntityCodeNamespace,
//  targetEntityCode,
//  targetEntityCodeNamespace,
//  associationInstanceId,
//  isDefining,
//  isInferred,
//  isActive,
//  owner,
//  status,
//  effectiveDate,
//  expirationDate,
//  entryStateGuid)
//VALUES (#{uid},
//#{associationPredicateUId},
//#{sourceEntityCode},
//#{sourceEntityCodeNamespace},
//#{targetEntityCode},
//#{targetEntityCodeNamespace},
//#{associationInstanceId,jdbcType=VARCHAR},
//#{isDefining},
//#{isInferred},
//#{isActive},
//#{owner,jdbcType=VARCHAR},
//#{status,jdbcType=VARCHAR},
//#{effectiveDate,jdbcType=TIMESTAMP},
//#{expirationDate,jdbcType=TIMESTAMP},
//#{entryStateUId})

}
