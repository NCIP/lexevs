package org.lexevs.dao.index.lucenesupport;

import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class LazyLoadMetaData implements ApplicationListener<ContextRefreshedEvent> {
	
	private LexEvsServiceLocator locator;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		System.out.println("*** ContextRefreshedEvent");
		
		LexEvsServiceLocator locator = LexEvsServiceLocator.getInstance();

		System.out.println("***  got service locator");
	}

	public LexEvsServiceLocator getLocator() {
		return locator;
	}

	public void setLocator(LexEvsServiceLocator locator) {
		this.locator = locator;
	}
}
