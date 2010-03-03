package org.lexevs.dao.database.sqlimplementedmethods.entity;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.sqlimplementedmethods.AbstraceSqlImplementedMethodsDao;
import org.lexevs.dao.database.sqlimplementedmethods.codingscheme.SQLInterfaceCodingSchemeDao;
import org.lexevs.dao.database.utility.DaoUtility;

public class SQLInterfaceEntityDao extends AbstraceSqlImplementedMethodsDao implements EntityDao {
		
		private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("1.8");

		public List<? extends Entity> getAllEntitiesOfCodingScheme(
				String codingSchemeId, int start, int pageSize) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		public Entity getEntityByCodeAndNamespace(String codingSchemeId,
				String entityCode, String entityCodeNamespace) {
			try {
			AbsoluteCodingSchemeVersionReference reference = 
				SQLInterfaceCodingSchemeDao.resolveCodingSchemeKey(codingSchemeId);
			
			String codingSchemeName = 
				this.getResourceManager().getInternalCodingSchemeNameForUserCodingSchemeName(
					reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());
			
			
			return this.getSqlImplementedMethodsDao().
					buildCodedEntry(codingSchemeName, 
							reference.getCodingSchemeVersion(), 
							entityCode, 
							entityCodeNamespace, null, null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public String getEntityId(String codingSchemeId, String entityCode,
				String entityCodeNamespace) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		public void insertBatchEntities(String codingSchemeId,
				List<? extends Entity> entities) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		public String insertEntity(String codingSchemeId, Entity entity) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		public String insertHistoryEntity(String codingSchemeId, Entity entity) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		public void updateEntity(String codingSchemeName, String version,
				Entity entity) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		public <T> T executeInTransaction(IndividualDaoCallback<T> callback) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}
		
		@Override
		public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
			return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
		}

		@Override
		public int getEntityCount(String codingSchemeId) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		@Override
		public void updateEntity(String codingSchemeId, Entity entity) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}
}
