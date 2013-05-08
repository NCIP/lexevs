/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexevs.cts2.admin;

import org.lexevs.cts2.exception.admin.NotificationNotRegisteredException;
import org.lexevs.dao.database.service.event.DatabaseServiceEventListener;

/**
 * The Interface NotificationAdminOperation controls the registration and maintenence of Content
 * Notification Listeners.
 */
public interface NotificationAdminOperation {
	
	/**
	 * The Enum NotificationStatus.
	 * 
	 * SUSPEND: Notifications will cease being processed until further notice.
	 * REINSTATE: Notifications will resume processing.
	 * REMOVE: The Notification will be removed, and cannot be REINSTATE'd.
	 */
	public enum NotificationStatus {	
			SUSPEND, 
			REINSTATE,
			REMOVE}
	
	/**
	 * Register the given Notification Listener in the system.
	 * 
	 * @param listener 
	 * 		The Notification Listener
	 * 
	 * @return 
	 * 		The Notification Identifier: The unique identifier of the particular Notification
	 * 		in the system.
	 */
	public String registerForNotification(DatabaseServiceEventListener listener);
	
	/**
	 * Replaces a Notification Listener with an Updated Notification Listener
	 * 
	 * @param notificationId 
	 * 		The Notification Identifier of the Notification to be updated.
	 * @param listener the listener
	 */
	public void updateNotificationRegistration(String notificationId, DatabaseServiceEventListener listener)
		throws NotificationNotRegisteredException;
	
	/**
	 * Update the Notification Registration Status of a Notification Listener.
	 * 
	 * @param status 
	 * 		The new Notification Registration Status
	 * @param notificationId
	 * 		The Notification Identifier of the Notification to be updated.
	 */
	public void updateNotificationRegistrationStatus(String notificationId, NotificationStatus status)
		throws NotificationNotRegisteredException;
}