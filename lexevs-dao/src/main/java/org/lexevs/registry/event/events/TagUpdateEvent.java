
package org.lexevs.registry.event.events;

import org.lexevs.registry.model.RegistryEntry;

/**
 * The Class TagUpdateEvent.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class TagUpdateEvent {

	/** The previous tag. */
	private String previousTag;
	
	/** The registry entry. */
	private RegistryEntry registryEntry;
	
	
	/**
	 * Instantiates a new tag update event.
	 * 
	 * @param previousTag the previous tag
	 * @param registryEntry the registry entry
	 */
	public TagUpdateEvent(String previousTag, RegistryEntry registryEntry) {
		super();
		this.previousTag = previousTag;
		this.registryEntry = registryEntry;
	}
	
	/**
	 * Gets the previous tag.
	 * 
	 * @return the previous tag
	 */
	public String getPreviousTag() {
		return previousTag;
	}
	
	/**
	 * Sets the previous tag.
	 * 
	 * @param previousTag the new previous tag
	 */
	public void setPreviousTag(String previousTag) {
		this.previousTag = previousTag;
	}
	
	/**
	 * Gets the registry entry.
	 * 
	 * @return the registry entry
	 */
	public RegistryEntry getRegistryEntry() {
		return registryEntry;
	}
	
	/**
	 * Sets the registry entry.
	 * 
	 * @param registryEntry the new registry entry
	 */
	public void setRegistryEntry(RegistryEntry registryEntry) {
		this.registryEntry = registryEntry;
	}
	
	
}