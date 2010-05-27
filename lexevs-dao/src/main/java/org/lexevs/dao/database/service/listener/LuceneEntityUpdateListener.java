package org.lexevs.dao.database.service.listener;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;
import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;

public class LuceneEntityUpdateListener extends DefaultServiceEventListener {

	@Override
	public boolean onEntityUpdate(EntityUpdateEvent event) {
		IndexServiceManager indexServiceManager = LexEvsServiceLocator.getInstance().getIndexServiceManager();
		EntityIndexService entityIndexService = indexServiceManager.getEntityIndexService();
		
		EntityService entityService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getEntityService();
		
		Entity updatedEntity = entityService.getEntity(
				event.getCodingSchemeUri(),
				event.getCodingSchemeVersion(),
				event.getOriginalEntity().getEntityCode(),
				event.getOriginalEntity().getEntityCodeNamespace());

		entityIndexService.updateIndexForEntity(
				event.getCodingSchemeUri(), 
				event.getCodingSchemeVersion(), 
				updatedEntity);
		
		return true;
	}

	@Override
	public boolean onPostEntityInsert(EntityInsertOrRemoveEvent entityInsertEvent) {
		IndexServiceManager indexServiceManager = LexEvsServiceLocator.getInstance().getIndexServiceManager();
		EntityIndexService entityIndexService = indexServiceManager.getEntityIndexService();
		
		Entity entity = entityInsertEvent.getEntity();
		
		entityIndexService.addEntityToIndex(entityInsertEvent.getCodingSchemeUri(), entityInsertEvent.getVersion(), entity);
		
		return true;
	}
	
	
}
