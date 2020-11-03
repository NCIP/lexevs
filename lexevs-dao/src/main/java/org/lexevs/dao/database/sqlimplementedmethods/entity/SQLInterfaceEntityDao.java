/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.sqlimplementedmethods.AbstraceSqlImplementedMethodsDao;
import org.lexevs.dao.database.sqlimplementedmethods.codingscheme.SQLInterfaceCodingSchemeDao;

/**
 * The Class SQLInterfaceEntityDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SQLInterfaceEntityDao extends AbstraceSqlImplementedMethodsDao implements EntityDao {
		
		/** The supported datebase versions. */
		private LexGridSchemaVersion supportedDatebaseVersionNMinus2 = LexGridSchemaVersion.parseStringToVersion("1.7");
		private LexGridSchemaVersion supportedDatebaseVersionNMinus1 = LexGridSchemaVersion.parseStringToVersion("1.8");

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
		public String getEntityUId(String codingSchemeId, String entityCode,
				String entityCodeNamespace) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see org.lexevs.dao.database.access.entity.EntityDao#insertBatchEntities(java.lang.String, java.util.List)
		 */
		public void insertBatchEntities(String codingSchemeId,
				List<? extends Entity> entities, boolean cascade) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see org.lexevs.dao.database.access.entity.EntityDao#insertEntity(java.lang.String, org.LexGrid.concepts.Entity)
		 */
		public String insertEntity(String codingSchemeId, 
				Entity entity, boolean cascade) {
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
			List<LexGridSchemaVersion> supportedDBVersions = new ArrayList<LexGridSchemaVersion>();
			supportedDBVersions.add(supportedDatebaseVersionNMinus2);
			supportedDBVersions.add(supportedDatebaseVersionNMinus1);
			return supportedDBVersions;
//			return DaoUtility.createNonTypedList(supportedDatebaseVersionN);
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
		public String updateEntity(String codingSchemeUId, String entityUId, Entity entity) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		@Override
		public String insertHistoryEntity(String codingSchemeId,
				String entityId, Entity entity) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		@Override
		public Entity getHistoryEntityByRevision(String codingSchemeId,
				String entityId, String revisionId) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}
		
		@Override
		public Entity getEntityByUId(String codingSchemeId, String entityId) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		@Override
		public void updateEntity(String codingSchemeId, AssociationEntity entity) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		@Override
		public ResolvedConceptReference getResolvedCodedNodeReferenceByCodeAndNamespace(
				String codingSchemeId, String entityCode,
				String entityCodeNamespace) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		@Override
		public String updateEntityVersionableAttrib(String codingSchemeUId,
				String entityUId, Entity entity) {
			throw new UnsupportedOperationException();
			
		}

		@Override
		public void removeEntityByUId(String codingSchemeUId, String entityUId) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getLatestRevision(String csUId, String entityUId) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean entityInUse(String codingSchemeUId, String entityCode,
				String entityCodeNamespace) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getEntryStateUId(String codingSchemeUId, String entityUId) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void updateEntryStateUId(String codingSchemeUId,
				String entityUId, String entryStateUId) {
			throw new UnsupportedOperationException();
			
		}

		@Override
		public boolean entryStateExists(String codingSchemeUId, String entryStateUId) {
			throw new UnsupportedOperationException();
		}

		@Override
		public AssociationEntity getAssociationEntityByCodeAndNamespace(
				String codingSchemeUId, String entityCode,
				String entityCodeNamespace) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		@Override
		public List<Entity> getEntities(String codingSchemeId,
				List<String> entityUids) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		@Override
		public List<Entity> getEntities(String codingSchemeId,
				List<String> propertyNames, List<String> propertyTypes,
				List<String> entityUids) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		@Override
		public Entity getEntityByCodeAndNamespace(String codingSchemeUId,
				String entityCode, String entityCodeNamespace,
				List<String> propertyNames, List<String> propertyTypes) {
			try {
			AbsoluteCodingSchemeVersionReference reference = 
				SQLInterfaceCodingSchemeDao.resolveCodingSchemeKey(codingSchemeUId);
			
			String codingSchemeName = 
				this.getResourceManager().getInternalCodingSchemeNameForUserCodingSchemeName(
					reference.getCodingSchemeURN(), reference.getCodingSchemeVersion());
			
				return this.getSqlImplementedMethodsDao().
					buildCodedEntry(
							codingSchemeName, 
							reference.getCodingSchemeVersion(), 
							entityCode, 
							entityCodeNamespace, 
							toLocalNameList(propertyNames), 
							toPropertyType(propertyTypes));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public EntityDescription getEntityDescription(String codingSchemeUid,
				String entityCode, String entityCodeNamespace) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		@Override
		public Entity getEntityByUId(String codingSchemeUId, String entityUId,
				List<String> propertyNames, List<String> propertyTypes) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		@Override
		public Map<String, Entity> getEntitiesWithUidMap(String codingSchemeId,
				List<String> propertyNames, List<String> propertyTypes,
				List<String> entityUids) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		@Override
		public List<String> getDistinctEntityNamespacesFromCode(
				String codingSchemeUId, String entityCode) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

		private LocalNameList toLocalNameList(List<String> list) {
			if(CollectionUtils.isEmpty(list)) {
				return null;
			}
			LocalNameList returnList = new LocalNameList();
			
			for(String item : list) {
				returnList.addEntry(item);
			}
			
			return returnList;
		}
		
		private org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[] toPropertyType(List<String> list) {
			if(CollectionUtils.isEmpty(list)) {
				return null;
			}
			org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[] returnList = 
				new org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[list.size()];
			
			for(int i=0;i<list.size();i++){
				returnList[i] = mapPropertyType(list.get(i));
			}
			
			return returnList;
		}
		
		 private org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType mapPropertyType(String propertyType){
		        if (propertyType.equals(SQLTableConstants.TBLCOLVAL_COMMENT)) {
		            return org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType.COMMENT;
		        } else if (propertyType.equals(SQLTableConstants.TBLCOLVAL_DEFINITION)) {
		            return org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType.DEFINITION;
		        } else if (propertyType.equals(SQLTableConstants.TBLCOLVAL_PROPERTY)) {
		            return org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType.GENERIC;
		        } else if (propertyType.equals(SQLTableConstants.TBLCOLVAL_PRESENTATION)) {
		            return org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType.PRESENTATION;
		        } else {
		            throw new RuntimeException("Unexpected PropertyType");
		        }
		    }

		@Override
		public String getEntityDescriptionAsString(String codingSchemeUid, String entityCode,
				String entityCodeNamespace) {
			// TODO Auto-generated method stub (IMPLEMENT!)
			throw new UnsupportedOperationException();
		}

}