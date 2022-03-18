
package org.lexevs.system.event;

/**
 * The listener interface for receiving databaseServiceEvent events.
 * The class that is interested in processing a databaseServiceEvent
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDatabaseServiceEventListener<code> method. When
 * the databaseServiceEvent event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see DatabaseServiceEventEvent
 */
public interface SystemEventListener {

	public void onRemoveCodingSchemeResourceFromSystemEvent(String uri, String version);

}