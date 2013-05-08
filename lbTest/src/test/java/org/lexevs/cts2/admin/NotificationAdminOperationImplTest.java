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

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.cts2.admin.NotificationAdminOperation.NotificationStatus;
import org.lexevs.cts2.exception.admin.NotificationNotRegisteredException;
import org.lexevs.dao.database.service.event.registry.BaseListenerRegistry;
import org.lexevs.dao.database.service.listener.DefaultServiceEventListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDao-test.xml"})
public class NotificationAdminOperationImplTest {
	
	private NotificationAdminOperationImpl notificationAdminOperationImpl;
	
	@Resource
	private BaseListenerRegistry listenerRegistry;
	
	@Before
	public void setUp() {
		listenerRegistry.setDatabaseServiceEventListeners(null);
		notificationAdminOperationImpl = new NotificationAdminOperationImpl();
	}
	
	@Test
	public void testRegisterNotification() {
		String id = notificationAdminOperationImpl.registerForNotification(new DefaultServiceEventListener());
		
		assertNotNull(id);
		
		assertNotNull(listenerRegistry.getRegisteredListener(id));
		
		assertEquals(1,listenerRegistry.getRegisteredListeners().size());
	}
	
	@Test
	public void testRemoveNotification() throws NotificationNotRegisteredException {
		String id = notificationAdminOperationImpl.registerForNotification(new DefaultServiceEventListener());
		
		notificationAdminOperationImpl.updateNotificationRegistrationStatus(id, NotificationStatus.REMOVE);
		
		assertNull(listenerRegistry.getRegisteredListener(id));
		
		assertEquals(0,listenerRegistry.getRegisteredListeners().size());
	}
	
	@Test(expected=NotificationNotRegisteredException.class)
	public void testRemoveNotificationException() throws NotificationNotRegisteredException {	
		notificationAdminOperationImpl.updateNotificationRegistrationStatus("BOGUS_ID", NotificationStatus.REMOVE);
	}
	
	@Test
	public void testSuspendNotification() throws NotificationNotRegisteredException {
		String id = notificationAdminOperationImpl.registerForNotification(new DefaultServiceEventListener());
		
		notificationAdminOperationImpl.updateNotificationRegistrationStatus(id, NotificationStatus.SUSPEND);
		
		assertEquals(false, listenerRegistry.getRegisteredListener(id).isActive());
		
		assertEquals(1,listenerRegistry.getRegisteredListeners().size());
	}
	
	@Test
	public void testReinstateNotification() throws NotificationNotRegisteredException {
		String id = notificationAdminOperationImpl.registerForNotification(new DefaultServiceEventListener());
		
		notificationAdminOperationImpl.updateNotificationRegistrationStatus(id, NotificationStatus.SUSPEND);
		
		assertEquals(false, listenerRegistry.getRegisteredListener(id).isActive());
		
		notificationAdminOperationImpl.updateNotificationRegistrationStatus(id, NotificationStatus.REINSTATE);
		
		assertEquals(true, listenerRegistry.getRegisteredListener(id).isActive());
		
		assertEquals(1,listenerRegistry.getRegisteredListeners().size());
	}
	
	@Test
	public void testUpdateNotification() throws NotificationNotRegisteredException {
		DefaultServiceEventListener listener1 = new DefaultServiceEventListener();
		DefaultServiceEventListener listener2 = new DefaultServiceEventListener();
		String id = notificationAdminOperationImpl.registerForNotification(listener1);
		
		assertEquals(listener1, listenerRegistry.getRegisteredListener(id));
		
		notificationAdminOperationImpl.updateNotificationRegistration(id, listener2);
		
		assertEquals(listener2, listenerRegistry.getRegisteredListener(id));
	}
	
	@Test(expected=NotificationNotRegisteredException.class)
	public void testUpdateNotificationException() throws NotificationNotRegisteredException {	
		notificationAdminOperationImpl.updateNotificationRegistration("BOGUS_ID", new DefaultServiceEventListener());
	}

}