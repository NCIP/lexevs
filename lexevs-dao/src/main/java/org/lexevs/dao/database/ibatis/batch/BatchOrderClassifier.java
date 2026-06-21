
package org.lexevs.dao.database.ibatis.batch;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lexevs.dao.database.ibatis.association.IbatisAssociationDao;
import org.lexevs.dao.database.ibatis.association.IbatisAssociationDataDao;
import org.lexevs.dao.database.ibatis.association.IbatisAssociationTargetDao;
import org.lexevs.dao.database.ibatis.entity.IbatisEntityDao;
import org.lexevs.dao.database.ibatis.ncihistory.IbatisNciHistoryDao;
import org.lexevs.dao.database.ibatis.property.IbatisPropertyDao;
import org.lexevs.dao.database.ibatis.versions.IbatisVersionsDao;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.classify.Classifier;

public class BatchOrderClassifier implements Classifier<String,Integer>{
	
	private static int GROUP_ONE = 1;
	private static int GROUP_TWO = 2;
	private static int GROUP_THREE = 3;
	private static int GROUP_FOUR = 4;
	
	private Map<Integer,List<String>> orderedGroups = new LinkedHashMap<Integer,List<String>>();
	
	public BatchOrderClassifier() {
		mapGroups();
	}
	
	protected void mapGroups() {
		orderedGroups.put(GROUP_ONE, DaoUtility.createNonTypedList(
				
				IbatisAssociationDao.INSERT_ASSOCIATIONENTITY_SQL,
				IbatisEntityDao.INSERT_ENTITY_SQL
				
		));
		
		orderedGroups.put(GROUP_TWO, DaoUtility.createNonTypedList(
				
				IbatisPropertyDao.INSERT_PROPERTY_SQL,
				IbatisEntityDao.INSERT_ENTITY_TYPE_SQL,
				IbatisAssociationTargetDao.INSERT_ENTITY_ASSN_ENTITY_SQL,
				IbatisAssociationDataDao.INSERT_ENTITY_ASSN_DATA_SQL
				
		));
		
		orderedGroups.put(GROUP_THREE, DaoUtility.createNonTypedList(
				
				IbatisPropertyDao.INSERT_PROPERTY_QUALIFIER_SQL,
				IbatisPropertyDao.INSERT_PROPERTY_SOURCE_SQL,
				IbatisPropertyDao.INSERT_PROPERTY_USAGECONTEXT_SQL,
				IbatisPropertyDao.INSERT_PROPERTYLINK_SQL,
				IbatisVersionsDao.INSERT_ENTRY_STATE_SQL,
				IbatisAssociationDao.INSERT_ASSOCIATION_QUAL_OR_CONTEXT_SQL,
				IbatisAssociationDao.INSERT_TRANSITIVE_CLOSURE_SQL
				
		));
		
		orderedGroups.put(GROUP_FOUR, DaoUtility.createNonTypedList(
				
				IbatisNciHistoryDao.INSERT_NCI_CHANGEEVENT_SQL
				
		));
	}

	@Override
	public Integer classify(String sql) {
		for(Integer key : this.orderedGroups.keySet()) {
			List<String> registerdSqlStatements = this.orderedGroups.get(key);
			if(registerdSqlStatements.contains(sql)) {
				return key;
			}
		}

		throw new RuntimeException(sql + " is not Batch Insertable.");
	
	}

	public List<Integer> getOrderedGroups() {
		return new ArrayList<Integer>(orderedGroups.keySet());
	}
}