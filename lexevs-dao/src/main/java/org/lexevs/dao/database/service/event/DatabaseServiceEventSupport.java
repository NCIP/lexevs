
package org.lexevs.dao.database.service.event;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.event.association.AssociationBatchInsertEvent;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeInsertErrorEvent;
import org.lexevs.dao.database.service.event.codingscheme.CodingSchemeUpdateEvent;
import org.lexevs.dao.database.service.event.codingscheme.PostCodingSchemeInsertEvent;
import org.lexevs.dao.database.service.event.codingscheme.PreCodingSchemeInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityBatchInsertEvent;
import org.lexevs.dao.database.service.event.entity.EntityInsertOrRemoveEvent;
import org.lexevs.dao.database.service.event.entity.EntityUpdateEvent;
import org.lexevs.dao.database.service.event.property.PropertyUpdateEvent;
import org.lexevs.dao.database.service.event.registry.ListenerRegistry;
import org.lexevs.dao.database.service.event.revision.EntityReviseEvent;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

/**
 * The Class DatabaseServiceEventSupport.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DatabaseServiceEventSupport {

	/** The listener registry. */
	private ListenerRegistry listenerRegistry;

	
	/**
	 * Fire coding scheme insert error event.
	 * 
	 * @param scheme the scheme
	 * @param exception the exception
	 * 
	 * @throws T the T
	 */
	protected <T extends Exception> void fireCodingSchemeInsertErrorEvent(CodingScheme scheme, T exception) throws T {
		for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
			listener.onCodingSchemeInsertError(
					new CodingSchemeInsertErrorEvent<T>(
							scheme,
							exception));
		}
		throw exception;
	}
	
	/**
	 * Fire coding scheme update event.
	 * 
	 * @param revisionId the revision id
	 * @param entryStateId the entry state id
	 * @param originalCodingScheme the original coding scheme
	 * @param updatedCodingScheme the updated coding scheme
	 */
	protected void fireCodingSchemeUpdateEvent(
			String revisionId,
			String entryStateId,
			CodingScheme originalCodingScheme,
			CodingScheme updatedCodingScheme){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onCodingSchemeUpdate(
						new CodingSchemeUpdateEvent(
								revisionId,
								entryStateId,
								originalCodingScheme,
								updatedCodingScheme));
			}
	}
	
	/**
	 * Fire coding scheme insert event.
	 * 
	 * @param codingScheme the coding scheme
	 * 
	 * @throws CodingSchemeAlreadyLoadedException the coding scheme already loaded exception
	 */
	protected void firePreCodingSchemeInsertEvent(
			CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException{
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPreCodingSchemeInsert(
						new PreCodingSchemeInsertEvent(
								codingScheme));
			}
	}
	
	/**
	 * Fire post coding scheme insert event.
	 * 
	 * @param codingScheme the coding scheme
	 * 
	 * @throws CodingSchemeAlreadyLoadedException the coding scheme already loaded exception
	 */
	protected void firePostCodingSchemeInsertEvent(
			CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException{
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPostCodingSchemeInsert(
						new PostCodingSchemeInsertEvent(
								codingScheme));
		}
	}
	
	/**
	 * Fire pre entity insert event.
	 * 
	 * @param entityInsertEvent the entity insert event
	 */
	protected void firePreEntityInsertEvent(EntityInsertOrRemoveEvent entityInsertEvent){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPreEntityInsert(entityInsertEvent);
			}
	}
	
	/**
	 * Fire post entity insert event.
	 * 
	 * @param entityInsertEvent the entity insert event
	 */
	protected void firePostEntityInsertEvent(EntityInsertOrRemoveEvent entityInsertEvent){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPostEntityInsert(entityInsertEvent);
			}
	}
	
	/**
	 * Fire pre batch entity insert event.
	 * 
	 * @param entityInsertEvent the entity insert event
	 */
	protected void firePreBatchEntityInsertEvent(EntityBatchInsertEvent entityInsertEvent){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPreBatchEntityInsert(entityInsertEvent);
			}
	}
	
	/**
	 * Fire post batch entity insert event.
	 * 
	 * @param entityInsertEvent the entity insert event
	 */
	protected void firePostBatchEntityInsertEvent(EntityBatchInsertEvent entityInsertEvent){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPostBatchEntityInsert(entityInsertEvent);
			}
	}
	
	/**
	 * Fire pre batch association insert event.
	 * 
	 * @param assocInsertEvent the assoc insert event
	 */
	protected void firePreBatchAssociationInsertEvent(AssociationBatchInsertEvent assocInsertEvent){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPreBatchAssociationInsert(assocInsertEvent);
			}
	}

	/**
	 * Fire post property insert event.
	 * 
	 * @param propertyInsertEvent the property insert event
	 */
	protected void firePostPropertyInsertEvent(PropertyUpdateEvent propertyInsertEvent){
		for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
			listener.onPostPropertyInsert(propertyInsertEvent);
		}
	}
	
	/**
	 * Fire property update event.
	 * 
	 * @param event the event
	 */
	protected void firePropertyUpdateEvent(
			PropertyUpdateEvent event) {
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPropertyUpdate(event);
			}
	}
	
	/**
	 * Fire post property remove event.
	 * 
	 * @param propertyRemoveEvent the property remove event
	 */
	protected void firePostPropertyRemoveEvent(PropertyUpdateEvent propertyRemoveEvent){
		for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
			listener.onPostPropertyRemove(propertyRemoveEvent);
		}
	}

	/**
	 * Fire entity update event.
	 * 
	 * @param entityUpdateEvent the entity update event
	 */
	protected void fireEntityUpdateEvent(EntityUpdateEvent entityUpdateEvent) {
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onEntityUpdate(entityUpdateEvent);
			}
	}
	
	/**
	 * Fire pre entity remove event.
	 * 
	 * @param entityRemoveEvent the entity remove event
	 */
	protected void firePreEntityRemoveEvent(EntityInsertOrRemoveEvent entityRemoveEvent){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPreEntityRemove(entityRemoveEvent);
			}
	}
	
	/**
	 * Fire post entity remove event.
	 * 
	 * @param entityRemoveEvent the entity remove event
	 */
	protected void firePostEntityRemoveEvent(EntityInsertOrRemoveEvent entityRemoveEvent){
			for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
				listener.onPostEntityRemove(entityRemoveEvent);
			}
	}
	
	/**
	 * Fire entity revise event.
	 * 
	 * @param entityReviseEvent the entity revise event
	 */
	protected void fireEntityReviseEvent(EntityReviseEvent entityReviseEvent){
		for(DatabaseServiceEventListener listener : this.listenerRegistry.getRegisteredListeners()){
			listener.onEntityReviseEvent(entityReviseEvent);
		}
}

	/**
	 * Sets the listener registry.
	 * 
	 * @param listenerRegistry the new listener registry
	 */
	public void setListenerRegistry(ListenerRegistry listenerRegistry) {
		this.listenerRegistry = listenerRegistry;
	}

	/**
	 * Gets the listener registry.
	 * 
	 * @return the listener registry
	 */
	public ListenerRegistry getListenerRegistry() {
		return listenerRegistry;
	}
}