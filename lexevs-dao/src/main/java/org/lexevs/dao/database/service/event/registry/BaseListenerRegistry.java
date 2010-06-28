package org.lexevs.dao.database.service.event.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lexevs.dao.database.service.event.DatabaseServiceEventListener;

public class BaseListenerRegistry implements ListenerRegistry {
	
	private static List<DatabaseServiceEventListener> EMPTY_LISTENER_LIST = 
		Collections.unmodifiableList(new ArrayList<DatabaseServiceEventListener>());

	private List<DatabaseServiceEventListener> databaseServiceEventListeners = new ArrayList<DatabaseServiceEventListener>();

	private boolean enableListeners = true;
	
	public void setDatabaseServiceEventListeners(
			List<DatabaseServiceEventListener> databaseServiceEventListeners) {
		this.databaseServiceEventListeners = databaseServiceEventListeners;
	}

	@Override
	public void registerListener(DatabaseServiceEventListener listener) {
		databaseServiceEventListeners.add(listener);
	}

	@Override
	public void unregisterListener(DatabaseServiceEventListener listener) {
		databaseServiceEventListeners.remove(listener);
	}

	@Override
	public List<DatabaseServiceEventListener> getRegisteredListeners() {
		if(enableListeners) {
			return databaseServiceEventListeners;
		} else {
			return EMPTY_LISTENER_LIST;
		}
	}

	public boolean isEnableListeners() {
		return enableListeners;
	}

	public void setEnableListeners(boolean enableListeners) {
		this.enableListeners = enableListeners;
	}
}
