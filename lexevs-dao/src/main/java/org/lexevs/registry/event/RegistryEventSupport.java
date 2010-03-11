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
