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
import org.lexevs.dao.database.service.event.registry.ListenerRegistry;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The Class NotificationAdminOperationImpl.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NotificationAdminOperationImpl implements NotificationAdminOperation {

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.admin.NotificationAdminOperation#registerForNotification(org.lexevs.dao.database.service.event.DatabaseServiceEventListener)
	 */
	@Override
	public String registerForNotification(DatabaseServiceEventListener listener) {
		return this.getListenerRegistry().registerListener(listener);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.admin.NotificationAdminOperation#updateNotificationRegistration(java.lang.String, org.lexevs.dao.database.service.event.DatabaseServiceEventListener)
	 */
	@Override
	public void updateNotificationRegistration(String listenerId,
			DatabaseServiceEventListener listener) throws NotificationNotRegisteredException {
		verifyListener(listenerId);
		this.getListenerRegistry().registerListener(listenerId, listener);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.admin.NotificationAdminOperation#updateNotificationRegistrationStatus(java.lang.String, org.lexevs.cts2.admin.NotificationAdminOperation.NotificationStatus)
	 */
	@Override
	public void updateNotificationRegistrationStatus(
			String listenerId, NotificationStatus status) throws NotificationNotRegisteredException {
		verifyListener(listenerId);
		
		switch (status) {
			case REMOVE: {
				this.getListenerRegistry().unregisterListener(listenerId);
				break;
			}
			case REINSTATE: {
				this.getListenerRegistry().getRegisteredListener(listenerId).setActive(true);
				break;
			}
			case SUSPEND: {
				this.getListenerRegistry().getRegisteredListener(listenerId).setActive(false);
				break;
			}
		}
	}

	/**
	 * Gets the listener registry.
	 * 
	 * @return the listener registry
	 */
	protected ListenerRegistry getListenerRegistry() {
		return LexEvsServiceLocator.getInstance().
			getDatabaseServiceManager().getListenerRegistry();
	}

	private void verifyListener(String listenerId)
		throws NotificationNotRegisteredException {
		if(this.getListenerRegistry().getRegisteredListener(listenerId) == null) {
			throw new NotificationNotRegisteredException(listenerId);
		}
	}

}