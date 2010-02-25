package org.lexevs.registry.event;

import java.util.ArrayList;
import java.util.List;

import org.lexevs.registry.event.events.TagUpdateEvent;
import org.lexevs.registry.model.RegistryEntry;

public class RegistryEventSupport {
	
	private List<RegistryListener> registryListeners = new ArrayList<RegistryListener>();
	
	public void fireTagUpdateEvent(String previousTag, RegistryEntry entry) {
		for(RegistryListener listener : registryListeners) {
			listener.tagUpdated(new TagUpdateEvent(previousTag, entry));
		}
	}
	
	public void addRegistryListener(RegistryListener listener) {
		this.registryListeners.add(listener);
	}

	public List<RegistryListener> getRegistryListeners() {
		return registryListeners;
	}

	public void setRegistryListeners(List<RegistryListener> registryListeners) {
		this.registryListeners = registryListeners;
	}
}
