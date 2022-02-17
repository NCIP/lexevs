
package org.lexevs.dao.database.service.event;

import org.lexevs.dao.database.service.event.association.AssociationBatchInsertEvent;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeInsertErrorEvent;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeUpdateEvent;
import org.lexevs.dao.database.service.event.codingscheme.PostCodingSchemeInsertEvent;
import org.lexevs.dao.database.service.event.codingscheme.PreCodingSchemeInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityBatchInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;
import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;
import org.lexevs.dao.database.service.event.property.PropertyUpdateEvent;
import org.lexevs.dao.database.service.event.revision.EntityReviseEvent;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

/**
 * The listener interface for receiving databaseServiceEvent events.
 * The class that is interested in processing a databaseServiceEvent
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDatabaseServiceEventListener<code> method. When
 * the databaseServiceEvent event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see DatabaseServiceEventEvent
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface DatabaseServiceEventListener {
	
	/**
	 * Checks if is active.
	 * 
	 * @return true, if is active
	 */
	public boolean isActive();
	
	/**
	 * Sets the active.
	 * 
	 * @param isActive the new active
	 */
	public void setActive(boolean isActive);

	/**
	 * On coding scheme update.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 */
	public boolean onCodingSchemeUpdate(CodingSchemeUpdateEvent event);
	
	/**
	 * On coding scheme insert.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 * 
	 * @throws CodingSchemeAlreadyLoadedException the coding scheme already loaded exception
	 */
	public boolean onPreCodingSchemeInsert(PreCodingSchemeInsertEvent event) throws CodingSchemeAlreadyLoadedException;
	
	/**
	 * On post coding scheme insert.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 */
	public boolean onPostCodingSchemeInsert(PostCodingSchemeInsertEvent event);
	
	/**
	 * On entity update.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 */
	public boolean onEntityUpdate(EntityUpdateEvent event);

	/**
	 * On post property insert.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 */
	public boolean onPostPropertyInsert(PropertyUpdateEvent event);
	
	/**
	 * On property update.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 */
	public boolean onPropertyUpdate(PropertyUpdateEvent event);
	
	/**
	 * On post property remove.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 */
	public boolean onPostPropertyRemove(PropertyUpdateEvent event);
	
	/**
	 * On pre entity insert.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 */
	public boolean onPreEntityInsert(EntityInsertOrRemoveEvent event);
	
	/**
	 * On post entity insert.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 */
	public boolean onPostEntityInsert(EntityInsertOrRemoveEvent event);
	
	/**
	 * On pre batch entity insert.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 */
	public boolean onPreBatchEntityInsert(EntityBatchInsertEvent event);
	
	/**
	 * On post batch entity insert.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 */
	public boolean onPostBatchEntityInsert(EntityBatchInsertEvent event);

	/**
	 * On pre entity remove.
	 * 
	 * @param entityRemoveEvent the entity remove event
	 * 
	 * @return true, if successful
	 */
	public boolean onPreEntityRemove(EntityInsertOrRemoveEvent entityRemoveEvent);
	
	/**
	 * On post entity remove.
	 * 
	 * @param entityRemoveEvent the entity remove event
	 * 
	 * @return true, if successful
	 */
	public boolean onPostEntityRemove(EntityInsertOrRemoveEvent entityRemoveEvent);
	
	/**
	 * On entity revise event.
	 * 
	 * @param reviseEvent the revise event
	 * 
	 * @return true, if successful
	 */
	public boolean onEntityReviseEvent(EntityReviseEvent reviseEvent);

	/**
	 * On pre batch association insert.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 */
	public boolean onPreBatchAssociationInsert(AssociationBatchInsertEvent event);

	/**
	 * On pre association insert.
	 * 
	 * @param event the event
	 * 
	 * @return true, if successful
	 */
	public boolean onPreAssociationInsert(AssociationBatchInsertEvent event);

	/**
	 * On coding scheme insert error.
	 * 
	 * @param codingSchemeInsertErrorEvent the coding scheme insert error event
	 */
	public  <T extends Exception> void onCodingSchemeInsertError(
			CodingSchemeInsertErrorEvent<T> codingSchemeInsertErrorEvent);
}