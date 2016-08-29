package org.lexevs.dao.indexer.utility;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexevs.dao.index.lucenesupport.LazyLoadMetaData;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDao.xml"})
public class LazyLoadMetaDataTest {
	
	LazyLoadMetaData applicationListener;
	LexEvsServiceLocator locator;
	
	@Autowired
	ApplicationContext context;
	
	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void test() {
		applicationListener = (LazyLoadMetaData)context.getBean("eventListenerBean");
		try {
			applicationListener.lazyLoadMetadata();
		} catch (LBParameterException | IOException e) {
			// TODO Auto-generated catch block
			fail();
		}
	
	}

	public LazyLoadMetaData getApplicationListener() {
		return applicationListener;
	}

	public void setApplicationListener(LazyLoadMetaData applicationListener) {
		this.applicationListener = applicationListener;
	}

	public LexEvsServiceLocator getLocator() {
		return locator;
	}

	public void setLocator(LexEvsServiceLocator locator) {
		this.locator = locator;
	}
	
	

}
