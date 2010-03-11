/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.dao.database.sqlimplementedmethods.entity;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.sqlimplementedmethods.AbstraceSqlImplementedMethodsDao;
import org.lexevs.dao.database.sqlimplementedmethods.codingscheme.SQLInterfaceCodingSchemeDao;
import org.lexevs.dao.database.utility.DaoUtility;

/**
 * The Class SQLInterfaceEntityDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SQLInterfaceEntityDao extends AbstraceSqlImplementedMethodsDao implements EntityDao {
		
		/** The supported datebase version. */
		private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("1.8");

		/* (non-Javadoc)
		 * @see org.lexevs.dao.database.access.entity.EntityDao#getAllEntitiesOfCodingScheme(java.lang.String, int, int)
		 */
		public List<? extends Entity> getAllEntitiesOfCodingScheme(
				String codingSchemeId, int start, int pageSize) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see org.lexevs.dao.database.access.entity.EntityDao#getEntityByCodeAndNamespace(java.lang.String, java.lang.String, java.lang.String)
		 */
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

		/* (non-Javadoc)
		 * @see org.lexevs.dao.database.access.entity.EntityDao#getEntityId(java.lang.String, java.lang.String, java.lang.String)
		 */
		public String getEntityId(String codingSchemeId, String entityCode,
				String entityCodeNamespace) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see org.lexevs.dao.database.access.entity.EntityDao#insertBatchEntities(java.lang.String, java.util.List)
		 */
		public void insertBatchEntities(String codingSchemeId,
				List<? extends Entity> entities) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see org.lexevs.dao.database.access.entity.EntityDao#insertEntity(java.lang.String, org.LexGrid.concepts.Entity)
		 */
		public String insertEntity(String codingSchemeId, Entity entity) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see org.lexevs.dao.database.access.entity.EntityDao#insertHistoryEntity(java.lang.String, org.LexGrid.concepts.Entity)
		 */
		public String insertHistoryEntity(String codingSchemeId, Entity entity) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		/**
		 * Update entity.
		 * 
		 * @param codingSchemeName the coding scheme name
		 * @param version the version
		 * @param entity the entity
		 */
		public void updateEntity(String codingSchemeName, String version,
				Entity entity) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao#executeInTransaction(org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao.IndividualDaoCallback)
		 */
		public <T> T executeInTransaction(IndividualDaoCallback<T> callback) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}
		
		/* (non-Javadoc)
		 * @see org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions()
		 */
		@Override
		public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
			return DaoUtility.createList(LexGridSchemaVersion.class, supportedDatebaseVersion);
		}

		/* (non-Javadoc)
		 * @see org.lexevs.dao.database.access.entity.EntityDao#getEntityCount(java.lang.String)
		 */
		@Override
		public int getEntityCount(String codingSchemeId) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see org.lexevs.dao.database.access.entity.EntityDao#updateEntity(java.lang.String, org.LexGrid.concepts.Entity)
		 */
		@Override
		public void updateEntity(String codingSchemeId, Entity entity) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}
}
