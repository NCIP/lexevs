package org.lexevs.cts2.admin;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.cts2.admin.NotificationAdminOperation.NotificationStatus;
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
	public void testRemoveNotification() {
		String id = notificationAdminOperationImpl.registerForNotification(new DefaultServiceEventListener());
		
		notificationAdminOperationImpl.updateNotificationRegistrationStatus(id, NotificationStatus.REMOVE);
		
		assertNull(listenerRegistry.getRegisteredListener(id));
		
		assertEquals(0,listenerRegistry.getRegisteredListeners().size());
	}
	
	@Test
	public void testSuspendNotification() {
		String id = notificationAdminOperationImpl.registerForNotification(new DefaultServiceEventListener());
		
		notificationAdminOperationImpl.updateNotificationRegistrationStatus(id, NotificationStatus.SUSPEND);
		
		assertEquals(false, listenerRegistry.getRegisteredListener(id).isActive());
		
		assertEquals(1,listenerRegistry.getRegisteredListeners().size());
	}
	
	@Test
	public void testReinstateNotification() {
		String id = notificationAdminOperationImpl.registerForNotification(new DefaultServiceEventListener());
		
		notificationAdminOperationImpl.updateNotificationRegistrationStatus(id, NotificationStatus.SUSPEND);
		
		assertEquals(false, listenerRegistry.getRegisteredListener(id).isActive());
		
		notificationAdminOperationImpl.updateNotificationRegistrationStatus(id, NotificationStatus.REINSTATE);
		
		assertEquals(true, listenerRegistry.getRegisteredListener(id).isActive());
		
		assertEquals(1,listenerRegistry.getRegisteredListeners().size());
	}
	
	@Test
	public void testUpdateNotification() {
		DefaultServiceEventListener listener1 = new DefaultServiceEventListener();
		DefaultServiceEventListener listener2 = new DefaultServiceEventListener();
		String id = notificationAdminOperationImpl.registerForNotification(listener1);
		
		assertEquals(listener1, listenerRegistry.getRegisteredListener(id));
		
		notificationAdminOperationImpl.updateNotificationRegistration(id, listener2);
		
		assertEquals(listener2, listenerRegistry.getRegisteredListener(id));
	}

}
