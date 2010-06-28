package org.lexevs.dao.database.service.event.registry;

import java.util.ArrayList;
import java.util.List;

import org.lexevs.dao.database.service.event.DatabaseServiceEventListener;

public class BaseListenerRegistry implements ListenerRegistry {
	
	private List<DatabaseServiceEventListener> databaseServiceEventListeners = new ArrayList<DatabaseServiceEventListener>();

	public void setDatabaseServiceEventListeners(
			List<DatabaseServiceEventListener> databaseServiceEventListeners) {
		this.databaseServiceEventListeners = databaseServiceEventListeners;
	}

	public List<DatabaseServiceEventListener> getDatabaseServiceEventListeners() {
		return databaseServiceEventListeners;
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
		return databaseServiceEventListeners;
	}
}
