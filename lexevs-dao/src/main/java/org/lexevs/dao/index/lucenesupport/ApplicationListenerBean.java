package org.lexevs.dao.index.lucenesupport;

import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;

public class ApplicationListenerBean implements ApplicationListener<ContextRefreshedEvent> {
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		System.out.println("*** ContextRefreshedEvent");
	}

	public void onApplicationEvent(ContextStartedEvent contextStartedEvent) {
		
		System.out.println("*** ContextStartedEvent");
		LexEvsServiceLocator testLocator = LexEvsServiceLocator.getInstance();
		
		System.out.println("***  got service locator");
		
	}

}
