
package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.relations.AssociationQualification;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.AssociationDao;
import org.lexevs.dao.database.access.association.batch.AssociationQualifierBatchInsertItem;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.logging.LoggerFactory;
import org.lexgrid.loader.wrappers.CodingSchemeUriVersionPair;
import org.lexgrid.loader.wrappers.ParentIdHolder;

public class AssociationQualifierWriter extends AbstractParentIdHolderWriter<AssociationQualification> {
	
	static final private LgLoggerIF logger = LoggerFactory.getLogger();

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
			HashMap<String, String> map = null;
			try {
			 map = (HashMap<String, String>) assocDao.getInstanceToGuidCache(codingSchemeIdInDb);
			} catch(OutOfMemoryError e) {
				String message = "Likely GC OutOfMemoery error.  Increase memory heap. Known cause: ";
				logger.error(message + e.getMessage());
				throw new RuntimeException(message + e.getMessage());
			}
			assocDao.insertBatchAssociationQualifiers(codingSchemeIdInDb, batch, map);
			return null;
	}
	});			
	}

}