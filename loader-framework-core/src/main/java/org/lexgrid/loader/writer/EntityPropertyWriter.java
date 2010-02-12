package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.access.property.batch.PropertyBatchInsertItem;
import org.lexevs.dao.database.service.DatabaseService.DaoCallback;
import org.lexgrid.loader.wrappers.ParentIdHolder;

public class EntityPropertyWriter extends AbstractParentIdHolderWriter<Property>{

	@Override
	public void doWrite(final String codingSchemeId,
			List<ParentIdHolder<Property>> items) {
		final List<PropertyBatchInsertItem> batchList = new ArrayList<PropertyBatchInsertItem>();
		
		for(ParentIdHolder<Property> holder : items){
			batchList.add(new PropertyBatchInsertItem(holder.getParentId(), holder.getItem()));
		}
		this.getDatabaseServiceManager().
			getPropertyService().executeInDaoLayer(new DaoCallback(){

				public Object execute(DaoManager daoManager) {
					daoManager.getPropertyDao(null, null).
						insertBatchProperties(codingSchemeId, PropertyType.ENTITY, batchList);
					return null;
				}	
			});
	}
}
