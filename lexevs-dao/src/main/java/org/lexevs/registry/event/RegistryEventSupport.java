
package org.lexevs.registry.event;

import java.util.ArrayList;
import java.util.List;

import org.lexevs.registry.event.events.TagUpdateEvent;
import org.lexevs.registry.model.RegistryEntry;

/**
 * The Class RegistryEventSupport.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RegistryEventSupport {
	
	/** The registry listeners. */
	private List<RegistryListener> registryListeners = new ArrayList<RegistryListener>();
	
	/**
	 * Fire tag update event.
	 * 
	 * @param previousTag the previous tag
	 * @param entry the entry
	 */
	public void fireTagUpdateEvent(String previousTag, RegistryEntry entry) {
		for(RegistryListener listener : registryListeners) {
			listener.tagUpdated(new TagUpdateEvent(previousTag, entry));
		}
	}
	
	/**
	 * Adds the registry listener.
	 * 
	 * @param listener the listener
	 */
	public void addRegistryListener(RegistryListener listener) {
		this.registryListeners.add(listener);
	}

	/**
	 * Gets the registry listeners.
	 * 
	 * @return the registry listeners
	 */
	public List<RegistryListener> getRegistryListeners() {
		return registryListeners;
	}

	/**
	 * Sets the registry listeners.
	 * 
	 * @param registryListeners the new registry listeners
	 */
	public void setRegistryListeners(List<RegistryListener> registryListeners) {
		this.registryListeners = registryListeners;
	}
}