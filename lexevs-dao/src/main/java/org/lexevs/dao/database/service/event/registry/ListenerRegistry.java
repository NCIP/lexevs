package org.lexevs.dao.database.service.event.registry;

import java.util.Collection;

import org.lexevs.dao.database.service.event.DatabaseServiceEventListener;

public interface ListenerRegistry {

	public String registerListener(DatabaseServiceEventListener listener);
	
	public void unregisterListener(String listenerId);
	
	public Collection<DatabaseServiceEventListener> getRegisteredListeners();
	
	public DatabaseServiceEventListener getRegisteredListener(String listenerId);
	
	public void registerListener(String listenerId, DatabaseServiceEventListener listener);
}
