package org.lexevs.registry.event.events;

import org.lexevs.registry.model.RegistryEntry;

public class TagUpdateEvent {

	private String previousTag;
	private RegistryEntry registryEntry;
	
	
	public TagUpdateEvent(String previousTag, RegistryEntry registryEntry) {
		super();
		this.previousTag = previousTag;
		this.registryEntry = registryEntry;
	}
	
	public String getPreviousTag() {
		return previousTag;
	}
	public void setPreviousTag(String previousTag) {
		this.previousTag = previousTag;
	}
	public RegistryEntry getRegistryEntry() {
		return registryEntry;
	}
	public void setRegistryEntry(RegistryEntry registryEntry) {
		this.registryEntry = registryEntry;
	}
	
	
}
