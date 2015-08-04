package org.lexevs.dao.indexer.utility;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.dao.index.lucenesupport.LazyLoadMetaData;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.test.context.ContextConfiguration;

public class LazyLoadMetaDataTest {
	
	LazyLoadMetaData applicationListener;
	LexEvsServiceLocator locator;
	
	@BeforeClass
	public static void setUp() throws Exception {
	Field field = LexEvsServiceLocator.class.getDeclaredField("CONTEXT_FILE");
		
		field.setAccessible(true);
		
		field.set(null, "lexevsDao-testLLMD.xml");
		
		LexEvsServiceLocator.getInstance();
	}

	@Test
	public void test() {
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
