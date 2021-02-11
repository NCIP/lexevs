
package org.lexevs.system.event;

import java.util.ArrayList;
import java.util.List;

import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

/**
 * The Class DatabaseServiceEventSupport.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SystemEventSupport {

	/** The database service event listeners. */
	private List<SystemEventListener> systemEventListeners = new ArrayList<SystemEventListener>();

	/**
	 * Fire coding scheme insert event.
	 * 
	 * @param codingScheme the coding scheme
	 * 
	 * @throws CodingSchemeAlreadyLoadedException the coding scheme already loaded exception
	 */
	protected void fireRemoveCodingSchemeResourceFromSystemEvent(String uri, String version){
		if(systemEventListeners != null){
			for(SystemEventListener listener : this.systemEventListeners){
				listener.onRemoveCodingSchemeResourceFromSystemEvent(uri, version);
			}
		}
	}

	public List<SystemEventListener> getSystemEventListeners() {
		return systemEventListeners;
	}

	public void setSystemEventListeners(
			List<SystemEventListener> systemEventListeners) {
		this.systemEventListeners = systemEventListeners;
	}
}