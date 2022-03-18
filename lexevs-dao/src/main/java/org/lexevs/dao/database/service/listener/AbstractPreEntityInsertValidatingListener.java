
package org.lexevs.dao.database.service.listener;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.event.entity.EntityBatchInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;

/**
 * The listener interface for receiving abstractPreEntityInsertValidating events.
 * The class that is interested in processing a abstractPreEntityInsertValidating
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addAbstractPreEntityInsertValidatingListener<code> method. When
 * the abstractPreEntityInsertValidating event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see AbstractPreEntityInsertValidatingEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractPreEntityInsertValidatingListener extends DefaultServiceEventListener{

	/**
	 * Do validate.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * @param entity the entity
	 * 
	 * @return true, if successful
	 */
	protected abstract boolean doValidate(String uri, String version, Entity entity);

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onPreBatchEntityInsert(org.lexevs.dao.database.service.event.entity.EntityBatchInsertEvent)
	 */
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

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.DefaultServiceEventListener#onPreEntityInsert(org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent)
	 */
	@Override
	public boolean onPreEntityInsert(EntityInsertOrRemoveEvent event) {
		if(event == null || event.getEntity() == null) {return true;}
	
		return this.doValidate(
				event.getCodingSchemeUri(),
				event.getVersion(),
				event.getEntity());
	}
}