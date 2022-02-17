
package org.lexgrid.loader.writer;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.relations.AssociationSource;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.association.batch.AssociationSourceBatchInsertItem;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexgrid.loader.wrappers.CodingSchemeUriVersionPair;
import org.lexgrid.loader.wrappers.ParentIdHolder;

public class AssociationSourceWriter extends AbstractParentIdHolderWriter<AssociationSource>{

	@Override
	public void doWrite(final CodingSchemeUriVersionPair codingSchemeId,
			List<ParentIdHolder<AssociationSource>> items) {
		
		final List<AssociationSourceBatchInsertItem> batch = 
			new ArrayList<AssociationSourceBatchInsertItem>();
		
		for(ParentIdHolder<AssociationSource> holder : items){
			batch.add(new AssociationSourceBatchInsertItem(
					holder.getParentId(), holder.getItem()));
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
				daoManager.getAssociationDao(
						codingSchemeId.getUri(), 
						codingSchemeId.getVersion())
							.insertBatchAssociationSources(codingSchemeIdInDb, batch);
				return null;
			}
		});	
	}
}