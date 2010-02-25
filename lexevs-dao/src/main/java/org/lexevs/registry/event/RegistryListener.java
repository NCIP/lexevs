package org.lexevs.registry.event;

import org.lexevs.registry.event.events.TagUpdateEvent;

public interface RegistryListener {

	public void tagUpdated(TagUpdateEvent event);
}
