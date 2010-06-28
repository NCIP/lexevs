package org.lexevs.dao.database.service.event.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.service.event.DatabaseServiceEventListener;

public class BaseListenerRegistry implements ListenerRegistry {
	
	private static List<DatabaseServiceEventListener> EMPTY_LISTENER_LIST = 
		Collections.unmodifiableList(new ArrayList<DatabaseServiceEventListener>());

	private Map<String,DatabaseServiceEventListener> databaseServiceEventListeners = 
		new HashMap<String,DatabaseServiceEventListener>();

	private boolean enableListeners = true;
	
	public void setDatabaseServiceEventListeners(
			List<DatabaseServiceEventListener> databaseServiceEventListeners) {
		databaseServiceEventListeners.clear();
		if(CollectionUtils.isNotEmpty(databaseServiceEventListeners)) {
			for(DatabaseServiceEventListener listener : databaseServiceEventListeners) {
				this.registerListener(listener);
			}
		}
	}

	@Override
	public DatabaseServiceEventListener getRegisteredListener(String listenerId) {
		return databaseServiceEventListeners.get(listenerId);
	}
	
	@Override
	public void registerListener(String listenerId, DatabaseServiceEventListener listener) {
		databaseServiceEventListeners.put(listenerId, listener);
	}

	@Override
	public String registerListener(DatabaseServiceEventListener listener) {
		String key = this.createListenerId();
		this.registerListener(key, listener);
		return key;
	}

	@Override
	public void unregisterListener(String listenerId) {
		databaseServiceEventListeners.remove(listenerId);
	}

	@Override
	public Collection<DatabaseServiceEventListener> getRegisteredListeners() {
		if(enableListeners) {
			return databaseServiceEventListeners.values();
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
	
	protected String createListenerId() {
		return UUID.randomUUID().toString();
	}
}
