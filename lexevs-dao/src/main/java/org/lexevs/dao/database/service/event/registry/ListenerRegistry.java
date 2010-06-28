package org.lexevs.dao.database.service.event.registry;

import java.util.List;

import org.lexevs.dao.database.service.event.DatabaseServiceEventListener;

public interface ListenerRegistry {

	public void registerListener(DatabaseServiceEventListener listener);
	
	public void unregisterListener(DatabaseServiceEventListener listener);
	
	public List<DatabaseServiceEventListener> getRegisteredListeners();
}
