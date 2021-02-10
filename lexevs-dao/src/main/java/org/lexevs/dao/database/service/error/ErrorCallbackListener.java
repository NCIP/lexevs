
package org.lexevs.dao.database.service.error;

/**
 * The listener interface for receiving errorCallback events.
 * The class that is interested in processing a errorCallback
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addErrorCallbackListener<code> method. When
 * the errorCallback event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ErrorCallbackEvent
 */
public interface ErrorCallbackListener {

	/**
	 * On database error.
	 * 
	 * @param databaseError the database error
	 */
	public void onDatabaseError(DatabaseError databaseError);
}