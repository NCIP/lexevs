
package org.lexevs.dao.database.service.event.registry;

import java.util.Collection;

import org.lexevs.dao.database.service.event.DatabaseServiceEventListener;

/**
 * The Interface ListenerRegistry.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface ListenerRegistry {

	/**
	 * Register listener.
	 * 
	 * @param listener the listener
	 * 
	 * @return the string
	 */
	public String registerListener(DatabaseServiceEventListener listener);
	
	/**
	 * Unregister listener.
	 * 
	 * @param listenerId the listener id
	 */
	public void unregisterListener(String listenerId);
	
	/**
	 * Gets the registered listeners.
	 * 
	 * @return the registered listeners
	 */
	public Collection<DatabaseServiceEventListener> getRegisteredListeners();
	
	/**
	 * Gets the registered listener.
	 * 
	 * @param listenerId the listener id
	 * 
	 * @return the registered listener
	 */
	public DatabaseServiceEventListener getRegisteredListener(String listenerId);
	
	/**
	 * Register listener.
	 * 
	 * @param listenerId the listener id
	 * @param listener the listener
	 */
	public void registerListener(String listenerId, DatabaseServiceEventListener listener);
}