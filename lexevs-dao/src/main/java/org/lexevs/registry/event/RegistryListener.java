
package org.lexevs.registry.event;

import org.lexevs.registry.event.events.TagUpdateEvent;

/**
 * The listener interface for receiving registry events.
 * The class that is interested in processing a registry
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addRegistryListener<code> method. When
 * the registry event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see RegistryEvent
 */
public interface RegistryListener {

	/**
	 * Invoked when tag update occurs.
	 * 
	 * @param event the event
	 */
	public void tagUpdated(TagUpdateEvent event);
}