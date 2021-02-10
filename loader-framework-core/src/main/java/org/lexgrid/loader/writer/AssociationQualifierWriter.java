
package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.association.batch.AssociationQualifierBatchInsertItem;
import org.lexevs.dao.database.access.association.batch.AssociationSourceBatchInsertItem;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexgrid.loader.wrappers.CodingSchemeUriVersionPair;
import org.lexgrid.loader.wrappers.ParentIdHolder;


public class AssociationQualifierWriter extends AbstractParentIdHolderWriter<AssociationQualification> {

	@Override
	public void doWrite(CodingSchemeUriVersionPair codingSchemeId, List<ParentIdHolder<AssociationQualification>> items) {
		final List<AssociationQualifierBatchInsertItem> batch = 
			new ArrayList<AssociationQualifierBatchInsertItem>();
		
		for(ParentIdHolder<AssociationQualification> holder : items){
			batch.add(new AssociationQualifierBatchInsertItem(
					holder.getParentId(), holder.getItem()));
		}
			this.getDatabaseServiceManager().getDaoCallbackService().
		executeInDaoLayer(new DaoCallback<Object>(){

		public Object execute(DaoManager daoManager) {
			AssociationDao assocDao = daoManager.getAssociationDao(
					codingSchemeId.getUri(), 
					codingSchemeId.getVersion());
			String codingSchemeIdInDb = daoManager.getCodingSchemeDao(
					codingSchemeId.getUri(), 
					codingSchemeId.getVersion()).
					getCodingSchemeUIdByUriAndVersion(
							codingSchemeId.getUri(), 
							codingSchemeId.getVersion());
			HashMap<String, String> map = (HashMap<String, String>) assocDao.getInstanceToGuidCache(codingSchemeIdInDb);
			assocDao.insertBatchAssociationQualifiers(codingSchemeIdInDb, batch, map);
			return null;
	}
	});			
	}

}