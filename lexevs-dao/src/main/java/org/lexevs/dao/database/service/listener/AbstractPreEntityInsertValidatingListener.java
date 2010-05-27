package org.lexevs.dao.database.service.listener;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.event.entity.EntityBatchInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;


public abstract class AbstractPreEntityInsertValidatingListener extends DefaultServiceEventListener{

	protected abstract boolean doValidate(String uri, String version, Entity entity);

	@Override
	public boolean onPreBatchEntityInsert(EntityBatchInsertEvent event) {
		if(event == null || event.getEntities() == null) {return true;}
		
		for(Entity entity : event.getEntities()) {
			this.doValidate(
					event.getCodingSchemeUri(),
					event.getVersion(),
					entity);
		}
		
		return true;
	}

	@Override
	public boolean onPreEntityInsert(EntityInsertOrRemoveEvent event) {
		if(event == null || event.getEntity() == null) {return true;}
	
		return this.doValidate(
				event.getCodingSchemeUri(),
				event.getVersion(),
				event.getEntity());
	}
}
