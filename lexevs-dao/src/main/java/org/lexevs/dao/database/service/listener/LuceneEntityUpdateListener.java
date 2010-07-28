package org.lexevs.dao.database.service.listener;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

public class LuceneEntityUpdateListener extends DefaultServiceEventListener {

	@Override
	public boolean onEntityUpdate(EntityUpdateEvent event) {
		IndexServiceManager indexServiceManager = LexEvsServiceLocator.getInstance().getIndexServiceManager();
		EntityIndexService entityIndexService = indexServiceManager.getEntityIndexService();
		
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(event.getCodingSchemeUri());
		ref.setCodingSchemeVersion(event.getCodingSchemeVersion());

		if (entityIndexService.doesIndexExist(ref)) {
			EntityService entityService = LexEvsServiceLocator.getInstance()
					.getDatabaseServiceManager().getEntityService();
			
			Entity updatedEntity = entityService.getEntity(event
					.getCodingSchemeUri(), event.getCodingSchemeVersion(),
					event.getOriginalEntity().getEntityCode(), event
							.getOriginalEntity().getEntityCodeNamespace());

			entityIndexService.updateIndexForEntity(event.getCodingSchemeUri(),
					event.getCodingSchemeVersion(), updatedEntity);
		}
		
		return true;
	}
}
