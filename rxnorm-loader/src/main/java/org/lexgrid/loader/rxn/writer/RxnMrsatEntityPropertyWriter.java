package org.lexgrid.loader.rxn.writer;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.access.property.batch.PropertyBatchInsertItem;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexgrid.loader.wrappers.CodingSchemeUriVersionPair;
import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.lexgrid.loader.writer.AbstractParentIdHolderWriter;

public class RxnMrsatEntityPropertyWriter extends AbstractParentIdHolderWriter<Property> {
	
		public void doWrite(final CodingSchemeUriVersionPair codingSchemeId,
				List<ParentIdHolder<Property>> items) {
			final List<PropertyBatchInsertItem> batchList = new ArrayList<PropertyBatchInsertItem>();
			
			for(ParentIdHolder<Property> holder : items){
				batchList.add(new PropertyBatchInsertItem(holder.getParentId(), holder.getItem()));
			}
			this.getDatabaseServiceManager().getDaoCallbackService().
				executeInDaoLayer(new DaoCallback<Object>(){

					public Object execute(DaoManager daoManager) {
						String codingSchemeIdInDb = daoManager.getCodingSchemeDao(
								codingSchemeId.getUri(), 
								codingSchemeId.getVersion()).
								getCodingSchemeUIdByUriAndVersion(
										codingSchemeId.getUri(), 
										codingSchemeId.getVersion());
						
						daoManager.getPropertyDao(
								codingSchemeId.getUri(), 
								codingSchemeId.getVersion()).
							insertBatchProperties(codingSchemeIdInDb, PropertyType.ENTITY, batchList);
						
						return null;
					}	
				});
		}

		public List<ParentIdHolder<Property>> processItems(List<ParentIdHolder<Property>> items){
			for(ParentIdHolder<Property> holder: items){
				Property prop = holder.getItem();
				
			}
		 return null;
		}
}
