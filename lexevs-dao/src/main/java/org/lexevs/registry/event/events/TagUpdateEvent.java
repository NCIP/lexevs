/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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
